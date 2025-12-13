package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.doctor.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/doctors/profile")
@RequiredArgsConstructor
public class SuperAdminDoctorProfileController {
    private final DoctorProfileService doctorProfileService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> createDoctorProfile(
            @RequestBody DoctorProfileRequest request) {
        return ResponseEntity.ok(doctorProfileService.createDoctorProfile(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> updateDoctorProfile(@PathVariable Long id,
            @RequestBody DoctorProfileRequest request) {
        return ResponseEntity.ok(doctorProfileService.updateDoctorProfile(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> deleteDoctorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(doctorProfileService.deleteDoctorProfile(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorProfileEntity>> getDoctorProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorProfileService.getDoctorProfileById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<DoctorProfileEntity>>> filterDoctorProfiles(
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<DoctorProfileEntity>> doctorProfiles = doctorProfileService.filterDoctorProfiles(specialtyId,
                keyword, pageable);
        return ResponseEntity.ok(doctorProfiles);
    }
}
