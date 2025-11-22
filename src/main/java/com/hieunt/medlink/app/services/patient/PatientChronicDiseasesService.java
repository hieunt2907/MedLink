package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import com.hieunt.medlink.app.requests.patient.PatientChronicDiseaseRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientChronicDiseasesService {
    BaseResponse<PatientChronicDiseasesEntity> createChronicDisease(PatientChronicDiseaseRequest request);

    BaseResponse<PatientChronicDiseasesEntity> updateChronicDisease(Long id, PatientChronicDiseaseRequest request);

    BaseResponse<PatientChronicDiseasesEntity> deleteChronicDisease(Long id);

    BaseResponse<Page<PatientChronicDiseasesEntity>> filterMyChronicDiseases(Pageable pageable);

    BaseResponse<Page<PatientChronicDiseasesEntity>> filterChronicDiseases(Long patientProfileId, Pageable pageable);

    BaseResponse<PatientChronicDiseasesEntity> getChronicDisease(Long id);
}
