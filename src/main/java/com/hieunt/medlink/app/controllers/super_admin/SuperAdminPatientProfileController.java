package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.patient.PatientProfileResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
import com.hieunt.medlink.app.services.patient.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/patients/profile")
@RequiredArgsConstructor
public class SuperAdminPatientProfileController {
    private final PatientProfileService patientProfileService;
    private final PatientAllergiesService patientAllergiesService;

    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> getPatientProfile(@PathVariable Long profileId) {
        BaseResponse<PatientProfilesEntity> response = patientProfileService.getPatientProfile(profileId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientProfileResponse>>> filterPatientProfiles(
            @RequestParam(required = false) String keyword, Pageable pageable) {
        BaseResponse<Page<PatientProfileResponse>> response = patientProfileService.filterPatientProfiles(keyword,
                pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> deletePatientProfile(@PathVariable Long id) {
        BaseResponse<PatientProfilesEntity> response = patientProfileService.deletePatientProfile(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{profileId}/allergies")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> filterPatientAllergies(
            @PathVariable Long profileId, Pageable pageable) {
        BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterAllergies(profileId,
                pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{profileId}/allergies/{allergyId}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> deletePatientAllergy(@PathVariable Long profileId,
            @PathVariable Long allergyId) {
        BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.deletePatientAllergy(allergyId);
        return ResponseEntity.ok(response);
    }
}
