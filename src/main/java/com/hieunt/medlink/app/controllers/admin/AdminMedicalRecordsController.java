package com.hieunt.medlink.app.controllers.admin;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.requests.medical_record.MedicalRecordsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.medical_record.MedicalRecordsResponse;
import com.hieunt.medlink.app.services.medical_record.MedicalRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/medical-records")
@RequiredArgsConstructor
public class AdminMedicalRecordsController {
    private final MedicalRecordsService medicalRecordsService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> createMedicalRecord(
            @RequestBody MedicalRecordsRequest request) {
        return ResponseEntity.ok(medicalRecordsService.createMedicalRecord(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecordsRequest request) {
        return ResponseEntity.ok(medicalRecordsService.updateMedicalRecord(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> getMedicalRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordsService.getMedicalRecordById(id));
    }

    @GetMapping("/reception-ticket/{receptionTicketId}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> getMedicalRecordByReceptionTicketId(
            @PathVariable Long receptionTicketId) {
        return ResponseEntity.ok(medicalRecordsService.getMedicalRecordByReceptionTicketId(receptionTicketId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<MedicalRecordsEntity>> deleteMedicalRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordsService.deleteMedicalRecord(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<MedicalRecordsResponse>>> filterMedicalRecords(
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<MedicalRecordsResponse>> response = medicalRecordsService.filterMedicalRecords(
                hospitalId, patientId, doctorId, status, keyword, pageable);
        return ResponseEntity.ok(response);
    }
}
