package com.hieunt.medlink.app.services.kafka;

import java.time.OffsetDateTime;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hieunt.medlink.app.entities.ReceptionTicketsEntity;
import com.hieunt.medlink.app.mappers.ReceptionsTicketsMapper;
import com.hieunt.medlink.app.repositories.ReceptionsTicketsRepository;
import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;

@Service
@RequiredArgsConstructor
public class ReceptionTicketConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ReceptionTicketConsumerService.class);

    private final ReceptionsTicketsRepository receptionsTicketsRepository;
    private final ReceptionsTicketsMapper receptionsTicketsMapper;

    /**
     * Kafka consumer để nhận và xử lý reception ticket requests
     * Kiểm tra thông tin trước khi lưu vào database
     * 
     * @param request ReceptionTicketsRequest từ Kafka
     */
    @KafkaListener(topics = "${kafka.topic.reception-ticket}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeReceptionTicket(ReceptionTicketsRequest request) {
        logger.info("Received reception ticket from Kafka for patient: {}", request.getPatientId());

        try {
            // Bước 0: Tạo timestamp hiện tại để sử dụng nhất quán trong toàn bộ logic
            OffsetDateTime currentDate = OffsetDateTime.now();

            // Bước 1: Kiểm tra xem bệnh nhân đã có phiếu khám trong ngày chưa
            Integer exists = receptionsTicketsRepository.existsPatientTicketInDay(
                    request.getHospitalId(),
                    request.getPatientId(),
                    request.getRoomId(),
                    request.getSpecialtyId(),
                    currentDate);

            if (exists != null && exists == 1) {
                logger.warn("Patient {} already has a reception ticket today for hospital: {}, room: {}, specialty: {}",
                        request.getPatientId(), request.getHospitalId(),
                        request.getRoomId(), request.getSpecialtyId());
                // Có thể throw exception hoặc xử lý theo logic nghiệp vụ
                // Ở đây tôi sẽ không lưu và return
                return;
            }

            // Bước 2: Lấy queue number lớn nhất trong ngày và tăng lên 1
            Integer maxQueueNumber = receptionsTicketsRepository.findMaxQueueNumberInDay(
                    request.getHospitalId(),
                    request.getRoomId(),
                    request.getSpecialtyId(),
                    currentDate);

            // Nếu chưa có queue number nào trong ngày, bắt đầu từ 1
            int newQueueNumber = (maxQueueNumber != null) ? maxQueueNumber + 1 : 1;

            logger.info("Calculated queue number: {} (max was: {})", newQueueNumber, maxQueueNumber);

            // Bước 3: Tạo entity và lưu vào database
            ReceptionTicketsEntity entity = receptionsTicketsMapper.toEntity(request);
            entity.setQueueNumber(newQueueNumber);
            entity.setCreatedAt(currentDate); // Set created_at manually để đảm bảo nhất quán với query

            ReceptionTicketsEntity savedEntity = receptionsTicketsRepository.save(entity);

            logger.info("Successfully saved reception ticket to database. ID: {}, Queue Number: {}, Created At: {}",
                    savedEntity.getId(), savedEntity.getQueueNumber(), savedEntity.getCreatedAt());

        } catch (Exception e) {
            logger.error("Error processing reception ticket for patient: {}",
                    request.getPatientId(), e);
        }
    }
}
