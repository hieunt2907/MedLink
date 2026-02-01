package com.hieunt.medlink.app.services.doctor;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.errors.ResourceNotFoundException;
import com.hieunt.medlink.app.mappers.DoctorProfileMapper;
import com.hieunt.medlink.app.repositories.DoctorProfileRepository;
import com.hieunt.medlink.app.requests.doctor.DoctorProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.doctor.DoctorProfileResponse;
import com.hieunt.medlink.app.utils.GetCurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<DoctorProfileEntity> createDoctorProfile(DoctorProfileRequest request) {
        DoctorProfileEntity doctorProfileEntity = doctorProfileMapper.toEntity(request);
        if (doctorProfileEntity != null) {
            doctorProfileRepository.save(doctorProfileEntity);
        }
        return new BaseResponse<>("creating doctor profile successfully", doctorProfileEntity);
    }

    @Override
    public BaseResponse<DoctorProfileEntity> updateDoctorProfile(Long id, DoctorProfileRequest request) {
        DoctorProfileEntity doctorProfileEntity = getDoctorProfileById(id).getData();

        if (doctorProfileEntity != null) {
            doctorProfileMapper.toEntity(request, doctorProfileEntity);
            doctorProfileRepository.save(doctorProfileEntity);
            return new BaseResponse<>("updating doctor profile successfully", doctorProfileEntity);
        }

        throw new ResourceNotFoundException("Doctor profile not found for current user");
    }

    @Override
    public BaseResponse<DoctorProfileEntity> getDoctorProfileById(Long id) {
        DoctorProfileEntity doctorProfileEntity = doctorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorProfile", "id", id));
        return new BaseResponse<>("getting doctor profile successfully", doctorProfileEntity);
    }

    @Override
    public BaseResponse<DoctorProfileEntity> getMe() {
        UserEntity user = getCurrentUser.getCurrentUser();
        DoctorProfileEntity doctorProfileEntity = doctorProfileRepository.findByUserId(user.getId());

        if (doctorProfileEntity == null) {
            throw new ResourceNotFoundException("Doctor profile not found for current user");
        }
        return new BaseResponse<>("getting doctor profile successfully", doctorProfileEntity);
    }

    @Override
    public BaseResponse<DoctorProfileEntity> updateMe(DoctorProfileRequest request) {
        UserEntity user = getCurrentUser.getCurrentUser();
        DoctorProfileEntity doctorProfileEntity = doctorProfileRepository.findByUserId(user.getId());

        if (doctorProfileEntity == null) {
            throw new ResourceNotFoundException("Doctor profile not found for current user");
        }
        doctorProfileMapper.toEntity(request, doctorProfileEntity);
        doctorProfileRepository.save(doctorProfileEntity);
        return new BaseResponse<>("updating doctor profile successfully", doctorProfileEntity);
    }

    @Override
    public BaseResponse<DoctorProfileEntity> deleteDoctorProfile(Long id) {
        DoctorProfileEntity doctorProfileEntity = getDoctorProfileById(id).getData();

        if (doctorProfileEntity != null) {
            doctorProfileRepository.delete(doctorProfileEntity);
            return new BaseResponse<>("deleting doctor profile successfully", doctorProfileEntity);
        }

        throw new ResourceNotFoundException("Doctor profile not found for current user");
    }

    @Override
    public BaseResponse<Page<DoctorProfileResponse>> filterDoctorProfiles(String keyword,
            Pageable pageable) {
        Page<DoctorProfileResponse> doctorProfiles = doctorProfileRepository.filterDoctorProfiles(keyword,
                pageable);
        return new BaseResponse<>("filtering doctor profiles successfully", doctorProfiles);
    }
}
