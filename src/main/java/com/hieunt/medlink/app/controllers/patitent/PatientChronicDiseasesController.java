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
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> createChronicDisease(@RequestBody PatientChronicDiseaseRequest request) {
        BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.createChronicDisease(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientChronicDiseasesEntity>>> getMyChronicDiseases(@PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<PatientChronicDiseasesEntity>> response = chronicDiseasesService.filterMyChronicDiseases(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> updateChronicDisease(@PathVariable Long id, @RequestBody PatientChronicDiseaseRequest request) {
        BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.updateChronicDisease(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> deleteChronicDisease(@PathVariable Long id) {
        BaseResponse<PatientChronicDiseasesEntity> response = chronicDiseasesService.deleteChronicDisease(id);
        return ResponseEntity.ok(response);
    }
}
