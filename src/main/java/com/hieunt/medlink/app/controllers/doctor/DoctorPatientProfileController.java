package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.patient.PatientProfileResponse;
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
        BaseResponse<PatientProfilesEntity> response = patientProfileService.getPatientProfile(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientProfileResponse>>> filterPatientProfiles(@RequestParam(required = false) String keyword, @PageableDefault(size = 20) Pageable pageable) {

        BaseResponse<Page<PatientProfileResponse>> response = patientProfileService.filterPatientProfiles(keyword, pageable);
        return ResponseEntity.ok(response);
    }
}
