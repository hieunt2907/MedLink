package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.doctor.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient/doctors/profile")
@RequiredArgsConstructor
public class PatientDoctorProfileController {
    private final DoctorProfileService doctorProfileService;

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
