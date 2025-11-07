package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import com.hieunt.medlink.app.requests.patient.PatientChronicDiseaseRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientChronicDiseasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/patients/chronic-diseases")
@RequiredArgsConstructor
public class PatientChronicDiseasesController {

    private final PatientChronicDiseasesService chronicDiseasesService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> createChronicDisease(
            @RequestBody PatientChronicDiseaseRequest request) {
        try {
            BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.createChronicDisease(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientChronicDiseasesEntity>>> getMyChronicDiseases(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            BaseResponse<Page<PatientChronicDiseasesEntity>> response = chronicDiseasesService.filterMyChronicDiseases(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> updateChronicDisease(
            @PathVariable Long id,
            @RequestBody PatientChronicDiseaseRequest request) {
        try {
            BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.updateChronicDisease(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> deleteChronicDisease(@PathVariable Long id) {
        try {
            BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.deleteChronicDisease(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
