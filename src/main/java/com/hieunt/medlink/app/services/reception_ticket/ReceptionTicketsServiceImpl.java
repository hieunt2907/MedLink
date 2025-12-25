package com.hieunt.medlink.app.services.reception_ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hieunt.medlink.app.repositories.ReceptionsTicketsRepository;
import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.reception_ticket.ReceptionTicketsResponse;
import com.hieunt.medlink.app.services.kafka.ReceptionTicketProducerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionTicketsServiceImpl implements ReceptionTicketsService {

    private final ReceptionTicketProducerService producerService;

    private final ReceptionsTicketsRepository receptionsTicketsRepository;

    @Override
    public BaseResponse<ReceptionTicketsResponse> createReceptionTicket(ReceptionTicketsRequest request) {


        // Gửi request vào Kafka để xử lý bất đồng bộ
        // Kafka consumer sẽ kiểm tra thông tin và lưu vào database
        producerService.sendReceptionTicket(request);

        // Trả về response ngay lập tức cho client
        // Lưu ý: Ở đây chúng ta trả về một response tạm thời
        // Trong thực tế, bạn có thể muốn implement một cơ chế callback hoặc polling
        // để client biết khi nào ticket được tạo thành công

        ReceptionTicketsResponse response = receptionsTicketsRepository.getReceptionTicketByPatientId(request.getPatientId());

        return new BaseResponse<>("Creating reception ticket successfully", response);
    }

    @Override
    public BaseResponse<Page<ReceptionTicketsResponse>> filterReceptionTickets(Long hospitalId, Long roomId, Long specialtyId,
            Long doctorId, String keyword, Pageable pageable) {
            Page<ReceptionTicketsResponse> response = receptionsTicketsRepository.filterReceptionTickets(hospitalId, roomId, specialtyId, doctorId, keyword, pageable);
            return new BaseResponse<>("Filter reception tickets successfully", response);
    }

    @Override
    public BaseResponse<Page<ReceptionTicketsResponse>> filterReceptionTicketMe(Long patientId, String keyword, Pageable pageable) {
            Page<ReceptionTicketsResponse> response = receptionsTicketsRepository.filterReceptionTicketMe(patientId, keyword, pageable);
            return new BaseResponse<>("Filter reception tickets successfully", response);
    }

    @Override
    public BaseResponse<ReceptionTicketsResponse> getReceptionTicket(Long id) {
        ReceptionTicketsResponse response = receptionsTicketsRepository.getReceptionTicketByPatientId(id);
        return new BaseResponse<>("Get reception ticket successfully", response);
    }

    @Override
    public BaseResponse<ReceptionTicketsResponse> updateReceptionTicket(Long id, ReceptionTicketsRequest request) {
        ReceptionTicketsResponse response = receptionsTicketsRepository.getReceptionTicketByPatientId(id);
        return new BaseResponse<>("Update reception ticket successfully", response);
    }

    @Override
    public BaseResponse<ReceptionTicketsResponse> deleteReceptionTicket(Long id) {
        ReceptionTicketsResponse response = receptionsTicketsRepository.getReceptionTicketByPatientId(id);
        return new BaseResponse<>("Delete reception ticket successfully", response);
    }
}
