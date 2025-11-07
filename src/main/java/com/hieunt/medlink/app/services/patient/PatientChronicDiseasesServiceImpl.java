package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.PatientChronicDiseasesMapper;
import com.hieunt.medlink.app.repositories.PatientChronicDiseasesRepository;
import com.hieunt.medlink.app.repositories.PatientProfileRepository;
import com.hieunt.medlink.app.requests.patient.PatientChronicDiseaseRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientChronicDiseasesServiceImpl implements PatientChronicDiseasesService {

    private final PatientChronicDiseasesRepository chronicDiseasesRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final GetCurrentUser getCurrentUser;
    private final PatientChronicDiseasesMapper mapper;

    @Override
    public BaseResponse<PatientChronicDiseasesEntity> createChronicDisease(PatientChronicDiseaseRequest request) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());
            if (patientProfile == null)
                throw new RuntimeException("patient profile not found for the current user");

            PatientChronicDiseasesEntity entity = mapper.toEntity(request);
            entity.setPatientProfileId(patientProfile.getId());
            chronicDiseasesRepository.save(entity);

            return new BaseResponse<>("creating chronic disease successfully", entity);
        } catch (Exception e) {
            throw new RuntimeException("error creating chronic disease: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientChronicDiseasesEntity> updateChronicDisease(Long id, PatientChronicDiseaseRequest request) {
        try {
            PatientChronicDiseasesEntity entity = getChronicDisease(id).getData();
            mapper.toEntity(request, entity);
            chronicDiseasesRepository.save(entity);

            return new BaseResponse<>("updating chronic disease successfully", entity);
        } catch (Exception e) {
            throw new RuntimeException("error updating chronic disease: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientChronicDiseasesEntity> deleteChronicDisease(Long id) {
        try {
            PatientChronicDiseasesEntity entity = chronicDiseasesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("chronic disease not found"));
            chronicDiseasesRepository.delete(entity);

            return new BaseResponse<>("deleting chronic disease successfully", entity);
        } catch (Exception e) {
            throw new RuntimeException("error deleting chronic disease: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<PatientChronicDiseasesEntity>> filterMyChronicDiseases(Pageable pageable) {
        try {
            UserEntity user = getCurrentUser.getCurrentUser();
            PatientProfilesEntity patientProfile = patientProfileRepository.findByUserId(user.getId());
            if (patientProfile == null)
                throw new RuntimeException("patient profile not found for the current user");

            Page<PatientChronicDiseasesEntity> result =
                    chronicDiseasesRepository.filterChronicDiseases(patientProfile.getId(), pageable);

            return new BaseResponse<>("filtering chronic diseases successfully", result);

        } catch (Exception e) {
            throw new RuntimeException("error filtering chronic diseases: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<PatientChronicDiseasesEntity>> filterChronicDiseases(Long patientProfileId, Pageable pageable) {
        try {
            Page<PatientChronicDiseasesEntity> result =
                    chronicDiseasesRepository.filterChronicDiseases(patientProfileId, pageable);

            return new BaseResponse<>("filtering chronic diseases successfully", result);
        } catch (Exception e) {
            throw new RuntimeException("error filtering chronic diseases: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<PatientChronicDiseasesEntity> getChronicDisease(Long id) {
        try {
            PatientChronicDiseasesEntity entity = chronicDiseasesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("chronic disease not found"));

            return new BaseResponse<>("getting chronic disease successfully", entity);
        } catch (Exception e) {
            throw new RuntimeException("error getting chronic disease: " + e.getMessage(), e);
        }
    }
}
