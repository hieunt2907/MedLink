package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.medical_record.MedicalRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/medical-records")
@RequiredArgsConstructor
public class PatientMedicalRecordsController {
    private final MedicalRecordsService medicalRecordsService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> getMedicalRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordsService.getMedicalRecordById(id));
    }

    @GetMapping("/reception-ticket/{receptionTicketId}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> getMedicalRecordByReceptionTicketId(
            @PathVariable Long receptionTicketId) {
        return ResponseEntity.ok(medicalRecordsService.getMedicalRecordByReceptionTicketId(receptionTicketId));
    }

    @GetMapping("/my-records")
    public ResponseEntity<BaseResponse<Page<MedicalRecordsEntity>>> getMyMedicalRecords(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(medicalRecordsService.getMyMedicalRecords(pageable));
    }
}
