package com.hieunt.medlink.app.services.reception_ticket;

import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.reception_ticket.ReceptionTicketsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceptionTicketsService {
    BaseResponse<ReceptionTicketsResponse> createReceptionTicket(ReceptionTicketsRequest request);
    BaseResponse<Page<ReceptionTicketsResponse>> filterReceptionTickets(Long hospitalId, Long roomId, Long specialtyId, Long doctorId,
            String keyword, Pageable pageable);
    BaseResponse<Page<ReceptionTicketsResponse>> filterReceptionTicketMe(Long patientId, String keyword, Pageable pageable);
    BaseResponse<ReceptionTicketsResponse> getReceptionTicket(Long id);
    BaseResponse<ReceptionTicketsResponse> updateReceptionTicket(Long id, ReceptionTicketsRequest request);
    BaseResponse<ReceptionTicketsResponse> deleteReceptionTicket(Long id);
}
