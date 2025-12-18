package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.patient.PatientProfileResponse;
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
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> createPatientProfile(
            @RequestBody PatientProfileRequest request) {
        BaseResponse<PatientProfilesEntity> response = patientProfileService.createPatientProfile(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<PatientProfileResponse>> getMe() {
        BaseResponse<PatientProfileResponse> response = patientProfileService.getMe();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/")
    public ResponseEntity<BaseResponse<PatientProfilesEntity>> updateMe(@RequestBody PatientProfileRequest request) {
        BaseResponse<PatientProfilesEntity> response = patientProfileService.updateMe(request);
        return ResponseEntity.ok(response);
    }
}
