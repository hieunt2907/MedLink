package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient_profile.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/profile")
@RequiredArgsConstructor
public class PatientProfileController {
    private final PatientProfileService patientProfileService;

    @PostMapping
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> createPatientProfile(PatientProfileRequest request ) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.createPatientProfile(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> getMe() {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.getMe();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> updateMe(@RequestParam Long userId, @RequestBody PatientProfileRequest request ) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.updateMe(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
