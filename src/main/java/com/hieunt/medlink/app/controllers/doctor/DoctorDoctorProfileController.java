package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.doctor.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/doctors/profile")
@RequiredArgsConstructor
public class DoctorDoctorProfileController {
    private final DoctorProfileService doctorProfileService;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> getMyProfile() {
        return ResponseEntity.ok(doctorProfileService.getMe());
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<DoctorProfileEntity>>> filterDoctorProfiles(
            @RequestParam(required = true) Long hospitalId,
            @RequestParam(required = true) Long specialtyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<DoctorProfileEntity>> doctorProfiles = doctorProfileService.filterDoctorProfiles(hospitalId, specialtyId, keyword, pageable);
        return ResponseEntity.ok(doctorProfiles);
    }

    @PutMapping("/me")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> updateMyProfile(@RequestBody DoctorProfileRequest request) {
        return ResponseEntity.ok(doctorProfileService.updateMe(request));
    }
}
