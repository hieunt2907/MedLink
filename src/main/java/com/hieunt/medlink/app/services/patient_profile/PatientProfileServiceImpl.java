package com.hieunt.medlink.app.services.patient_profile;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.PatientProfileMapper;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService{
    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<PatientProfilesEntity> createPatientProfile(PatientProfileRequest patient) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            if (patientProfileRepository.existsByPatientCode(patient.getPatientCode())) {
                throw new RuntimeException("patient profile with code already exists");
            }

            PatientProfilesEntity patientProfile = patientProfileMapper.toEntity(patient);
            patientProfile.setUserId(user.getId());
            patientProfileRepository.save(patientProfile);
            return new BaseResponse<>("creating patient profile successfully", patientProfile);
        } catch (Exception e) {
            throw new RuntimeException("error creating patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> getPatientProfile(Long id) {
        try {
            PatientProfilesEntity patient = patientProfileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("patient profile not found"));
            if (patient == null) {
                throw new RuntimeException("patient profile not found");
            }
            return new BaseResponse<>("getting patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error getting patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> getMe() {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patient = patientProfileRepository.findByUserId(user.getId());
            return new BaseResponse<>("getting patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error getting patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> updateMe(Long userId, PatientProfileRequest request) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patient = patientProfileRepository.findByUserId(user.getId());
            patientProfileMapper.toEntity(request, patient);
            patientProfileRepository.save(patient);
            return new BaseResponse<>("updating patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error updating patient profile: " + e.getMessage(), e);
        }
    }
}
