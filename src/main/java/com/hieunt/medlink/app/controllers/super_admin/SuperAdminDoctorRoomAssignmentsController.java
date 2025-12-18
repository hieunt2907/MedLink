package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;

import com.hieunt.medlink.app.requests.doctor.DoctorRoomAssignmentsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.doctor.DoctorRoomAssignmentResponse;
import com.hieunt.medlink.app.services.doctor.DoctorRoomAssignmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/doctors/room-assignments")
@RequiredArgsConstructor
public class SuperAdminDoctorRoomAssignmentsController {
    private final DoctorRoomAssignmentsService doctorRoomAssignmentsService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<DoctorRoomAssignmentsEntity>> createDoctorRoomAssignment(
            @RequestBody DoctorRoomAssignmentsRequest request) {
        return ResponseEntity.ok(doctorRoomAssignmentsService.createDoctorRoomAssignment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorRoomAssignmentsEntity>> updateDoctorRoomAssignment(
            @PathVariable Long id,
            @RequestBody DoctorRoomAssignmentsRequest request) {
        return ResponseEntity.ok(doctorRoomAssignmentsService.updateDoctorRoomAssignment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorRoomAssignmentsEntity>> deleteDoctorRoomAssignment(
            @PathVariable Long id) {
        return ResponseEntity.ok(doctorRoomAssignmentsService.deleteDoctorRoomAssignment(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DoctorRoomAssignmentsEntity>> getDoctorRoomAssignmentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(doctorRoomAssignmentsService.getDoctorRoomAssignmentById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<DoctorRoomAssignmentResponse>>> filterDoctorRoomAssignments(
            @RequestParam(required = false) Long doctorProfileId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<DoctorRoomAssignmentResponse>> assignments = doctorRoomAssignmentsService
                .filterDoctorRoomAssignments(doctorProfileId, keyword, pageable);
        return ResponseEntity.ok(assignments);
    }
}
