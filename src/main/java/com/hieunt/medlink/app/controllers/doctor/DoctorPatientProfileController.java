package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor/patients/profile")
@RequiredArgsConstructor
public class DoctorPatientProfileController {
    private final PatientProfileService patientProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> getPatientProfile(@PathVariable Long id) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.getPatientProfile(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientProfilesEntity>>> filterPatientProfiles(@RequestParam(required = false) String keyword,@PageableDefault(size = 20) Pageable pageable) {
        try {
            BaseResponse<Page<PatientProfilesEntity>> response = patientProfileService.filterPatientProfiles(keyword, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
