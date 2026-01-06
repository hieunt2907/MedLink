package com.hieunt.medlink.app.controllers.doctor;

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
@RequestMapping("/api/v1/doctor/reception-tickets")
@RequiredArgsConstructor
public class DoctorReceptionTicketsController {
    private final ReceptionTicketsService receptionTicketsService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReceptionTicketsResponse>> getReceptionTicket(@PathVariable Long id) {
        return ResponseEntity.ok(receptionTicketsService.getReceptionTicket(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<ReceptionTicketsResponse>>> filterReceptionTickets(
            @RequestParam(required = true) Long hospitalId,
            @RequestParam(required = true) Long roomId,
            @RequestParam(required = true) Long specialtyId,
            @RequestParam(required = true) Long doctorId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<ReceptionTicketsResponse>> receptionTickets = receptionTicketsService
                .filterReceptionTickets(hospitalId, roomId, specialtyId, doctorId, keyword, pageable);
        return ResponseEntity.ok(receptionTickets);
    }
}
