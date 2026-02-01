package com.hieunt.medlink.app.controllers.technician;

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
@RequestMapping("/api/v1/technician/reception-tickets")
@RequiredArgsConstructor
public class TechnicianReceptionTicketsController {
    private final ReceptionTicketsService receptionTicketsService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> createReceptionTicket(
            @RequestBody ReceptionTicketsRequest request) {
        return ResponseEntity.ok(receptionTicketsService.createReceptionTicket(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> updateReceptionTicket(
            @PathVariable Long id,
            @RequestBody ReceptionTicketsRequest request) {
        return ResponseEntity.ok(receptionTicketsService.updateReceptionTicket(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> getReceptionTicket(@PathVariable Long id) {
        return ResponseEntity.ok(receptionTicketsService.getReceptionTicket(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<ReceptionTicketsResponse>>> filterReceptionTickets(
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<ReceptionTicketsResponse>> receptionTickets = receptionTicketsService
                .filterReceptionTickets(hospitalId, roomId, specialtyId, doctorId, keyword, pageable);
        return ResponseEntity.ok(receptionTickets);
    }
}
