package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;

import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.doctor.DoctorRoomAssignmentResponse;
import com.hieunt.medlink.app.services.doctor.DoctorRoomAssignmentsService;
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
@RequestMapping("/api/v1/doctor/doctors/room-assignments")
@RequiredArgsConstructor
public class DoctorDoctorRoomAssignmentsController {
    private final DoctorRoomAssignmentsService doctorRoomAssignmentsService;

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
