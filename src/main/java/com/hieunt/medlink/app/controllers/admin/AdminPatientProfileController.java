package com.hieunt.medlink.app.controllers.admin;


import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientAllergiesService;
import com.hieunt.medlink.app.services.patient.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/patients")
@RequiredArgsConstructor
public class AdminPatientProfileController {
    private final PatientProfileService patientProfileService;
    private final PatientAllergiesService patientAllergiesService;

    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> getPatientProfile(@PathVariable Long profileId) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.getPatientProfile(profileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<PatientProfilesEntity>>> filterPatientProfiles(@RequestParam(required = false) String keyword, Pageable pageable) {
        try {
            BaseResponse<Page<PatientProfilesEntity>> response = patientProfileService.filterPatientProfiles(keyword, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> deletePatientProfile(@PathVariable Long id) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.deletePatientProfile(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("{profileId}/allergies")
    public ResponseEntity<BaseResponse<Page<PatientAllergiesEntity>>> filterPatientAllergies(@PathVariable Long profileId, Pageable pageable) {
        try {
            BaseResponse<Page<PatientAllergiesEntity>> response = patientAllergiesService.filterAllergies(profileId, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("{profileId}/allergies/{allergyId}")
    public ResponseEntity<BaseResponse<PatientAllergiesEntity>> deletePatientAllergy(@PathVariable Long profileId, @PathVariable Long allergyId) {
        try {
            BaseResponse<PatientAllergiesEntity> response = patientAllergiesService.deletePatientAllergy(allergyId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
