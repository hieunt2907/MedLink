package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientAllergiesService {
    BaseResponse<PatientAllergiesEntity> createPatientAllergy(PatientAllergiesRequest request);
    BaseResponse<PatientAllergiesEntity> updatePatientAllergy(Long id, PatientAllergiesRequest request);
    BaseResponse<PatientAllergiesEntity> deletePatientAllergy(Long id);
    BaseResponse<Page<PatientAllergiesEntity>> filterMyAllergies(Pageable pageable);
    BaseResponse<Page<PatientAllergiesEntity>> filterAllergies(Long patientProfileId, Pageable pageable);
    BaseResponse<PatientAllergiesEntity> getPatientAllergy(Long id);
}
