package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.patient.PatientProfileResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientProfileService {
    BaseResponse<PatientProfilesEntity> createPatientProfile(PatientProfileRequest request);

    BaseResponse<PatientProfilesEntity> updatePatientProfile(Long id, PatientProfileRequest request);

    BaseResponse<PatientProfilesEntity> deletePatientProfile(Long id);

    BaseResponse<PatientProfilesEntity> getPatientProfile(Long id);

    BaseResponse<PatientProfileResponse> getMe();

    BaseResponse<PatientProfilesEntity> updateMe(PatientProfileRequest request);

    BaseResponse<Page<PatientProfileResponse>> filterPatientProfiles(String keyword, Pageable pageable);

}
