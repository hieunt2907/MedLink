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
}
