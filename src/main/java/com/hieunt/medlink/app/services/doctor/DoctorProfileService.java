package com.hieunt.medlink.app.services.doctor;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorProfileService {
    BaseResponse<DoctorProfileEntity> createDoctorProfile(DoctorProfileRequest request);

    BaseResponse<DoctorProfileEntity> updateDoctorProfile(Long id, DoctorProfileRequest request);

    BaseResponse<DoctorProfileEntity> getDoctorProfileById(Long id);

    BaseResponse<DoctorProfileEntity> getMe();

    BaseResponse<DoctorProfileEntity> updateMe(DoctorProfileRequest request);

    BaseResponse<DoctorProfileEntity> deleteDoctorProfile(Long id);

    BaseResponse<Page<DoctorProfileEntity>> filterDoctorProfiles(Long specialtyId, String keyword, Pageable pageable);
}
