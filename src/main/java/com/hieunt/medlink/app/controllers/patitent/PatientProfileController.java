package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.patient.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/patients/profile")
@RequiredArgsConstructor
public class PatientProfileController {
    private final PatientProfileService patientProfileService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> createPatientProfile(PatientProfileRequest request ) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.createPatientProfile(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> getMe() {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.getMe();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> updateMe(@RequestBody PatientProfileRequest request ) {
        try {
            BaseResponse<PatientProfilesEntity> response = patientProfileService.updateMe(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
