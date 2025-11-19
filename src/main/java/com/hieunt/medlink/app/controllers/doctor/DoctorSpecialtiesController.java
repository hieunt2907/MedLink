package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.SpecialtiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/hospital/specialties")
@RequiredArgsConstructor
public class DoctorSpecialtiesController {
    private final SpecialtiesService specialtiesService;

    @GetMapping("/{hospitalId}")
    public ResponseEntity<BaseResponse<Page<SpecialtiesEntity>>> filterSpecialties(@PathVariable Long hospitalId, @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<SpecialtiesEntity>> specialties = specialtiesService.filterSpecialties(hospitalId, pageable);
        return ResponseEntity.ok(specialties);
    }
}


