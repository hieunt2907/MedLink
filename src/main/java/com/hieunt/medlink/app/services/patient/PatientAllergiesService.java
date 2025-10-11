package com.hieunt.medlink.app.services.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

public interface PatientAllergiesService {
    BaseResponse<PatientAllergiesEntity> createPatientAllergy(PatientAllergiesRequest request);
}
