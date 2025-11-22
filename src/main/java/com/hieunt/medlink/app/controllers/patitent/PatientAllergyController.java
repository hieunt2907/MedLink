package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
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
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> createPatientAllergy(@RequestBody PatientAllergiesRequest request) {
        BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.createPatientAllergy(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> getMyAllergies(@PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterMyAllergies(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> updatePatientAllergy(@PathVariable Long id, @RequestBody PatientAllergiesRequest request) {
        BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.updatePatientAllergy(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> deletePatientAllergy(@PathVariable Long id) {
        BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.deletePatientAllergy(id);
        return ResponseEntity.ok(response);
    }
}
