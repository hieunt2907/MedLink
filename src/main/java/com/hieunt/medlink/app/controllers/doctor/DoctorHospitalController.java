package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.HospitalService;
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
@RequestMapping("/api/v1/doctor/hospital")
@RequiredArgsConstructor
public class DoctorHospitalController {
    private final HospitalService hospitalService;

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<HospitalEntity>>> filterHospitals(@RequestParam(required = false) String keyword, @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<HospitalEntity>> response = hospitalService.filterHospitals(keyword, pageable);
        return ResponseEntity.ok(response);
    }

}
