package com.hieunt.medlink.app.services.patient_profile;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

public interface PatientProfileService {
    BaseResponse<PatientProfilesEntity> createPatientProfile(PatientProfileRequest request);
    BaseResponse<PatientProfilesEntity> getPatientProfile(Long id);
    BaseResponse<PatientProfilesEntity> getMe();
    BaseResponse<PatientProfilesEntity> updateMe(Long userId, PatientProfileRequest request);

}
