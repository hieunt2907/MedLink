package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.reception_ticket.ReceptionTicketsResponse;
import com.hieunt.medlink.app.services.reception_ticket.ReceptionTicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/reception-tickets")
@RequiredArgsConstructor
public class PatientReceptionTicketsController {
    private final ReceptionTicketsService receptionTicketsService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> createReceptionTicket(
            @RequestBody ReceptionTicketsRequest request) {
        return ResponseEntity.ok(receptionTicketsService.createReceptionTicket(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> getReceptionTicket(@PathVariable Long id) {
        return ResponseEntity.ok(receptionTicketsService.getReceptionTicket(id));
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<Page<ReceptionTicketsResponse>>> getMyReceptionTickets(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<ReceptionTicketsResponse>> receptionTickets = receptionTicketsService
                .filterReceptionTicketMe(patientId, keyword, pageable);
        return ResponseEntity.ok(receptionTickets);
    }
}
