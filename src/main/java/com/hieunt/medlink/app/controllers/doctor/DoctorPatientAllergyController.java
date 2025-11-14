package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/patients/allergies")
@RequiredArgsConstructor
public class DoctorPatientAllergyController {
    private final PatientAllergiesService patientAllergiesService;

    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> filterPatientAllergies(@PathVariable Long profileId, @PageableDefault(size = 20) Pageable pageable) {
        try {
            BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterAllergies(profileId, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
