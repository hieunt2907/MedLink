package com.hieunt.medlink.app.controllers.admin;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.requests.hospital.SpecialtyRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.SpecialtiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/hospital/specialties")
@RequiredArgsConstructor
public class AdminSpecialtiesController {
    private final SpecialtiesService specialtiesService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<SpecialtiesEntity>> createSpecialty(@RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(specialtiesService.createSpecialty(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<SpecialtiesEntity>> updateSpecialty(@PathVariable Long id, @RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(specialtiesService.updateSpecialty(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<SpecialtiesEntity>> deleteSpecialty(@PathVariable Long id) {
        return ResponseEntity.ok(specialtiesService.deleteSpecialty(id));
    }
}
