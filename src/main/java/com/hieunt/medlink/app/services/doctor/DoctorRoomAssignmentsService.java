package com.hieunt.medlink.app.services.doctor;

import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorRoomAssignmentsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hieunt.medlink.app.responses.doctor.DoctorRoomAssignmentResponse;

public interface DoctorRoomAssignmentsService {
    BaseResponse<DoctorRoomAssignmentsEntity> createDoctorRoomAssignment(DoctorRoomAssignmentsRequest request);

    BaseResponse<DoctorRoomAssignmentsEntity> updateDoctorRoomAssignment(Long id, DoctorRoomAssignmentsRequest request);

    BaseResponse<DoctorRoomAssignmentsEntity> getDoctorRoomAssignmentById(Long id);

    BaseResponse<DoctorRoomAssignmentsEntity> deleteDoctorRoomAssignment(Long id);

    BaseResponse<Page<DoctorRoomAssignmentResponse>> filterDoctorRoomAssignments(Long doctorProfileId, String keyword,
            Pageable pageable);
}
