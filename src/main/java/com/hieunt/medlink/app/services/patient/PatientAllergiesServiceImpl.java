package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.errors.ResourceNotFoundException;
import com.hieunt.medlink.app.mappers.PatientAllergiesMapper;
import com.hieunt.medlink.app.repositories.PatientAllergiesRepository;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.utils.GetCurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientAllergiesServiceImpl implements PatientAllergiesService {
    private final PatientAllergiesRepository patientAllergiesRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final GetCurrentUser getCurrentUser;
    private final PatientAllergiesMapper patientAllergiesMapper;

    @Override
    public BaseResponse<PatientAllergiesEntity> createPatientAllergy(PatientAllergiesRequest request) {
        UserEntity user = getCurrentUser.getCurrentUser();
        PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());

        if (patientProfile == null) {
            throw new ResourceNotFoundException("Patient profile not found for the current user");
        }

        PatientAllergiesEntity allergy = patientAllergiesMapper.toEntity(request);
        allergy.setPatientProfileId(patientProfile.getId());
        patientAllergiesRepository.save(allergy);

        return new BaseResponse<>("creating patient allergy successfully", allergy);
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> updatePatientAllergy(Long id, PatientAllergiesRequest request) {
        PatientAllergiesEntity allergies = getPatientAllergy(id).getData();

        if (allergies == null) {
            throw new ResourceNotFoundException("Patient Allergy not found");
        }

        patientAllergiesMapper.toEntity(request, allergies);
        patientAllergiesRepository.save(allergies);

        return new BaseResponse<>("updating patient allergy successfully", allergies);
    }

    @Override
    public BaseResponse<Page<PatientAllergiesEntity>> filterMyAllergies(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());

        if (patientProfile == null) {
            throw new ResourceNotFoundException("Patient profile not found for the current user");
        }

        Page<PatientAllergiesEntity> allergies = patientAllergiesRepository.filterAllergies(patientProfile.getId(),
                pageable);
        return new BaseResponse<>("filtering patient allergies successfully", allergies);
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> deletePatientAllergy(Long id) {
        PatientAllergiesEntity allergy = getPatientAllergy(id).getData();

        if (allergy != null) {
            patientAllergiesRepository.delete(allergy);
        }

        return new BaseResponse<>("deleting patient allergy successfully", allergy);
    }

    @Override
    public BaseResponse<Page<PatientAllergiesEntity>> filterAllergies(Long patientProfileId, Pageable pageable) {
        Page<PatientAllergiesEntity> allergies = patientAllergiesRepository.filterAllergies(patientProfileId, pageable);
        return new BaseResponse<>("filtering patient allergies successfully", allergies);
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> getPatientAllergy(Long id) {
        PatientAllergiesEntity allergy = patientAllergiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient Allergy not found"));

        return new BaseResponse<>("getting patient allergy successfully", allergy);
    }
}
