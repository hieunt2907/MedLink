package com.hieunt.medlink.app.controllers.admin;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientChronicDiseasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/patients/chronic-diseases")
@RequiredArgsConstructor
public class AdminPatientChronicDiseasesController {

    private final PatientChronicDiseasesService chronicDiseasesService;

    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse<Page<PatientChronicDiseasesEntity>>> filterPatientChronicDiseases(
            @PathVariable Long profileId,
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            BaseResponse<Page<PatientChronicDiseasesEntity>> response =
                    chronicDiseasesService.filterChronicDiseases(profileId, pageable);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientChronicDiseasesEntity>> deletePatientChronicDisease(@PathVariable Long id) {
        try {
            BaseResponse<PatientChronicDiseasesEntity> response =
                    chronicDiseasesService.deleteChronicDisease(id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
