package com.hieunt.medlink.app.services.doctor;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;
import com.hieunt.medlink.app.mappers.DoctorRoomAssignmentsMapper;
import com.hieunt.medlink.app.repositories.DoctorRoomAssignmentsRepository;
import com.hieunt.medlink.app.requests.doctor.DoctorRoomAssignmentsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.doctor.DoctorRoomAssignmentResponse;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorRoomAssignmentsServiceImpl implements DoctorRoomAssignmentsService {
    private final DoctorRoomAssignmentsRepository doctorRoomAssignmentsRepository;
    private final DoctorRoomAssignmentsMapper doctorRoomAssignmentsMapper;
    private final DoctorProfileService doctorProfileService;

    @Override
    public BaseResponse<DoctorRoomAssignmentsEntity> createDoctorRoomAssignment(DoctorRoomAssignmentsRequest request) {
        DoctorRoomAssignmentsEntity entity = doctorRoomAssignmentsMapper.toEntity(request);
        DoctorProfileEntity doctorProfileEntity = doctorProfileService
                .getDoctorProfileById(request.getDoctorProfileId()).getData();
        if (doctorProfileEntity == null) {
            throw new ResourceNotFoundException("DoctorProfile", "id", request.getDoctorProfileId());
        }
        if (entity != null) {
            doctorRoomAssignmentsRepository.save(entity);
        }
        return new BaseResponse<>("creating doctor room assignment successfully", entity);
    }

    @Override
    public BaseResponse<DoctorRoomAssignmentsEntity> updateDoctorRoomAssignment(Long id,
            DoctorRoomAssignmentsRequest request) {
        DoctorRoomAssignmentsEntity entity = getDoctorRoomAssignmentById(id).getData();

        if (entity != null) {
            doctorRoomAssignmentsMapper.toEntity(request, entity);
            doctorRoomAssignmentsRepository.save(entity);
            return new BaseResponse<>("updating doctor room assignment successfully", entity);
        }

        throw new ResourceNotFoundException("DoctorRoomAssignment", "id", id);
    }

    @Override
    public BaseResponse<DoctorRoomAssignmentsEntity> getDoctorRoomAssignmentById(Long id) {
        DoctorRoomAssignmentsEntity entity = doctorRoomAssignmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorRoomAssignment", "id", id));
        return new BaseResponse<>("getting doctor room assignment successfully", entity);
    }

    @Override
    public BaseResponse<DoctorRoomAssignmentsEntity> deleteDoctorRoomAssignment(Long id) {
        DoctorRoomAssignmentsEntity entity = getDoctorRoomAssignmentById(id).getData();

        if (entity != null) {
            doctorRoomAssignmentsRepository.delete(entity);
            return new BaseResponse<>("deleting doctor room assignment successfully", entity);
        }

        throw new ResourceNotFoundException("DoctorRoomAssignment", "id", id);
    }

    @Override
    public BaseResponse<Page<DoctorRoomAssignmentResponse>> filterDoctorRoomAssignments(Long doctorProfileId,
            String keyword, Pageable pageable) {
        Page<DoctorRoomAssignmentResponse> assignments = doctorRoomAssignmentsRepository
                .filterDoctorRoomAssignments(doctorProfileId, keyword, pageable);
        return new BaseResponse<>("filtering doctor room assignments successfully", assignments);
    }
}
