package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/patients/allergies")
@RequiredArgsConstructor
public class SuperAdminPatientAllergyController {
    private final PatientAllergiesService patientAllergiesService;

    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> filterPatientAllergies(
            @PathVariable Long profileId, @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterAllergies(profileId,
                pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> deletePatientAllergy(@PathVariable Long id) {
        BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.deletePatientAllergy(id);
        return ResponseEntity.ok(response);
    }
}
