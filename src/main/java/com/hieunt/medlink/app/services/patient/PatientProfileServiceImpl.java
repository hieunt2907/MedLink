package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.PatientProfileMapper;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.error.DuplicateResourceException;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {
    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<PatientProfilesEntity> createPatientProfile(PatientProfileRequest patient) {
        UserEntity user = getCurrentUser.getCurrentUser();
        validatePatientCodeNotExists(patient.getPatientCode());

        PatientProfilesEntity patientProfile = patientProfileMapper.toEntity(patient);
        if (patientProfile != null) {
            patientProfile.setUserId(user.getId());
            patientProfileRepository.save(patientProfile);
            return new BaseResponse<>("creating patient profile successfully", patientProfile);
        }

        throw new ResourceNotFoundException("Patient profile entity is null");
    }

    @Override
    public BaseResponse<PatientProfilesEntity> getPatientProfile(Long id) {
        PatientProfilesEntity patient = patientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        return new BaseResponse<>("getting patient profile successfully", patient);
    }

    @Override
    public BaseResponse<PatientProfilesEntity> getMe() {
        UserEntity user = getCurrentUser.getCurrentUser();
        PatientProfilesEntity patient = patientProfileRepository.findByUserId(user.getId());

        if (patient == null) {
            throw new ResourceNotFoundException("Patient profile not found for the current user");
        }

        return new BaseResponse<>("getting patient profile successfully", patient);
    }

    @Override
    public BaseResponse<PatientProfilesEntity> updateMe(PatientProfileRequest request) {
        UserEntity user = getCurrentUser.getCurrentUser();
        PatientProfilesEntity patient = patientProfileRepository.findByUserId(user.getId());

        if (patient == null) {
            throw new ResourceNotFoundException("Patient profile not found for current user");
        }

        patientProfileMapper.toEntity(request, patient);
        patient.setPatientCode(patient.getPatientCode());
        patientProfileRepository.save(patient);

        return new BaseResponse<>("updating patient profile successfully", patient);
    }

    @Override
    public BaseResponse<PatientProfilesEntity> updatePatientProfile(Long id, PatientProfileRequest request) {
        PatientProfilesEntity patient = getPatientProfile(id).getData();

        if (patient == null) {
            throw new ResourceNotFoundException("Patient Profile not found");
        }

        // Kiểm tra duplicate patient code nếu thay đổi
        if (!patient.getPatientCode().equals(request.getPatientCode())) {
            validatePatientCodeNotExists(request.getPatientCode());
        }

        patientProfileMapper.toEntity(request, patient);
        patientProfileRepository.save(patient);

        return new BaseResponse<>("updating patient profile successfully", patient);
    }

    @Override
    public BaseResponse<PatientProfilesEntity> deletePatientProfile(Long id) {
        PatientProfilesEntity patient = getPatientProfile(id).getData();

        if (patient == null) {
            throw new ResourceNotFoundException("Patient Profile not found");
        }

        patientProfileRepository.delete(patient);
        return new BaseResponse<>("deleting patient profile successfully", null);
    }

    @Override
    public BaseResponse<Page<PatientProfilesEntity>> filterPatientProfiles(String keyword, Pageable pageable) {
        Page<PatientProfilesEntity> patients = patientProfileRepository.filterPatientProfiles(keyword, pageable);
        return new BaseResponse<>("filtering patient profiles successfully", patients);
    }

    /**
     * Validate patient code không tồn tại
     * Throw DuplicateResourceException nếu đã tồn tại
     */
    private void validatePatientCodeNotExists(String patientCode) {
        if (patientProfileRepository.existsByPatientCode(patientCode)) {
            throw new DuplicateResourceException("Patient Profile", "patient code", patientCode);
        }
    }
}
