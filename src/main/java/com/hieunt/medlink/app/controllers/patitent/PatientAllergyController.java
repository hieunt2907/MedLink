package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
import com.hieunt.medlink.app.services.patient.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/patients/allergies")
@RequiredArgsConstructor
public class PatientAllergyController {
    private final PatientAllergiesService patientAllergiesService;


    @PostMapping("/")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> createPatientAllergy(@RequestBody PatientAllergiesRequest request ) {
        try {
            BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.createPatientAllergy(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> getMyAllergies(@PageableDefault(size = 20)Pageable pageable) {
        try {
            BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterMyAllergies(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> updatePatientAllergy(@PathVariable Long id, @RequestBody PatientAllergiesRequest request ) {
        try {
            BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.updatePatientAllergy(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> deletePatientAllergy(@PathVariable Long id) {
        try {
            BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.deletePatientAllergy(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
