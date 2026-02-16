package com.hieunt.medlink.app.services.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReceptionTicketProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ReceptionTicketProducerService.class);

    @Value("${kafka.topic.reception-ticket}")
    private String topicName;

    private final KafkaTemplate<String, ReceptionTicketsRequest> kafkaTemplate;

    /**
     * Gửi reception ticket request vào Kafka topic
     * 
     * @param request ReceptionTicketsRequest cần xử lý
     * @return CompletableFuture để theo dõi kết quả gửi message
     */
    public CompletableFuture<SendResult<String, ReceptionTicketsRequest>> sendReceptionTicket(
            ReceptionTicketsRequest request) {

        logger.info("Sending reception ticket to Kafka topic: {} for patient: {}",
                topicName, request.getPatientId());

        @SuppressWarnings("null")
        CompletableFuture<SendResult<String, ReceptionTicketsRequest>> future = kafkaTemplate.send(topicName,
                String.valueOf(request.getPatientId()), request);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Successfully sent reception ticket to Kafka. Offset: {}, Partition: {}",
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
            } else {
                logger.error("Failed to send reception ticket to Kafka for patient: {}",
                        request.getPatientId(), ex);
            }
        });

        return future;
    }
}
