package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.requests.hospital.HospitalRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super_admin/hospitals")
@RequiredArgsConstructor
public class SuperAdminHospitalController {
    private final HospitalService hospitalService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<HospitalEntity>> createHospital(HospitalRequest request) {
        BaseResponse<HospitalEntity> response = hospitalService.createHospital(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<HospitalEntity>> updateHospital(@PathVariable Long id, HospitalRequest request) {
        BaseResponse<HospitalEntity> response = hospitalService.updateHospital(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<HospitalEntity>> deleteHospital(@PathVariable Long id) {
        BaseResponse<HospitalEntity> response = hospitalService.deleteHospital(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<HospitalEntity>>> filterHospitals(@RequestParam(required = false) String keyword, Pageable pageable) {
        BaseResponse<Page<HospitalEntity>> response = hospitalService.filterHospitals(keyword, pageable);
        return ResponseEntity.ok(response);
    }
}
