package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.PatientProfileMapper;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            validatePatientProfile(patient);

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
            if (patient == null) {
                throw new RuntimeException("patient profile not found for the current user");
            }
            return new BaseResponse<>("getting patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error getting patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> updateMe(PatientProfileRequest request) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patient = patientProfileRepository.findByUserId(user.getId());
            patientProfileMapper.toEntity(request, patient);
            patient.setPatientCode(patient.getPatientCode());
            patientProfileRepository.save(patient);
            return new BaseResponse<>("updating patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error updating patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> updatePatientProfile(Long id, PatientProfileRequest request) {
        try {
            PatientProfilesEntity patient = getPatientProfile(id).getData();
            validatePatientProfile(request);
            patientProfileMapper.toEntity(request, patient);
            patientProfileRepository.save(patient);
            return new BaseResponse<>("updating patient profile successfully", patient);
        } catch (Exception e) {
            throw new RuntimeException("error updating patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientProfilesEntity> deletePatientProfile(Long id) {
        try {
            PatientProfilesEntity patient = getPatientProfile(id).getData();
            patientProfileRepository.delete(patient);
            return new BaseResponse<>("deleting patient profile successfully", null);
        } catch (Exception e) {
            throw new RuntimeException("error deleting patient profile: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<PatientProfilesEntity>> filterPatientProfiles(String keyword, Pageable pageable) {
        try {
            Page<PatientProfilesEntity> patients = patientProfileRepository.filterPatientProfiles(keyword, pageable);
            return new BaseResponse<>("filtering patient profiles successfully", patients);
        } catch (Exception e) {
            throw new RuntimeException("error filtering patient profiles: " + e.getMessage(), e);
        }
    }

    private void validatePatientProfile(PatientProfileRequest patient) {
        if (patientProfileRepository.existsByPatientCode(patient.getPatientCode())) {
            throw new RuntimeException("patient code already exists");
        }
    }
}
