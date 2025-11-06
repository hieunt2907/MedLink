package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.PatientAllergiesMapper;
import com.hieunt.medlink.app.repositories.PatientAllergiesRepository;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
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
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());
            if (patientProfile == null) {
                throw new RuntimeException("patient profile not found for the current user");
            }

            PatientAllergiesEntity allergy = patientAllergiesMapper.toEntity(request);
            allergy.setPatientProfileId(patientProfile.getId());
            patientAllergiesRepository.save(allergy);
            return new BaseResponse<>("creating patient allergy successfully", allergy);
        } catch (Exception e) {
            throw new RuntimeException("error creating patient allergy: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> updatePatientAllergy(Long id, PatientAllergiesRequest request) {
        try {
            PatientAllergiesEntity allergies = getPatientAllergy(id).getData();
            if (allergies == null) {
                throw new RuntimeException("patient allergy not found for the current user");
            }

            patientAllergiesMapper.toEntity(request, allergies);
            patientAllergiesRepository.save(allergies);
            return new BaseResponse<>("updating patient allergy successfully", allergies);
        } catch (Exception e) {
            throw new RuntimeException("error updating patient allergy: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<PatientAllergiesEntity>> filterMyAllergies(Pageable pageable) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());
            if (patientProfile == null) {
                throw new RuntimeException("patient profile not found for the current user");
            }
            Page<PatientAllergiesEntity> allergies = patientAllergiesRepository.filterAllergies(patientProfile.getId(), pageable);
            return new BaseResponse<>("filtering patient allergies successfully", allergies);

        } catch (Exception e) {
            throw new RuntimeException("error filtering patient allergies: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> deletePatientAllergy(Long id) {
        try {
            PatientAllergiesEntity allergy = patientAllergiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("patient allergy not found"));
            patientAllergiesRepository.delete(allergy);
            return new BaseResponse<>("deleting patient allergy successfully", allergy);
        } catch (Exception e) {
            throw new RuntimeException("error deleting patient allergy: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<PatientAllergiesEntity>> filterAllergies(Long patientProfileId, Pageable pageable) {
        try {
            Page<PatientAllergiesEntity> allergies = patientAllergiesRepository.filterAllergies(patientProfileId, pageable);
            return new BaseResponse<>("filtering patient allergies successfully", allergies);
        } catch (Exception e) {
            throw new RuntimeException("error filtering patient allergies: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientAllergiesEntity> getPatientAllergy(Long id) {
        try {
            PatientAllergiesEntity allergy = patientAllergiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("patient allergy not found"));
            return new BaseResponse<>("getting patient allergy successfully", allergy);
        } catch (Exception e) {
            throw new RuntimeException("error getting patient allergy: " + e.getMessage(), e);
        }
    }
}
