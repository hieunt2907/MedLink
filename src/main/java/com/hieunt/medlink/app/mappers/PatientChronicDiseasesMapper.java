package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import com.hieunt.medlink.app.requests.patient.PatientChronicDiseaseRequest;
import org.springframework.stereotype.Component;

@Component
public class PatientChronicDiseasesMapper {

    public PatientChronicDiseasesEntity toEntity(PatientChronicDiseaseRequest request) {
        if (request == null) return null;

        PatientChronicDiseasesEntity entity = new PatientChronicDiseasesEntity();
        entity.setDiseaseName(request.getDiseaseName());
        entity.setDiagnosedDate(request.getDiagnosedDate());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public PatientChronicDiseasesEntity toEntity(PatientChronicDiseaseRequest request, PatientChronicDiseasesEntity entity) {
        if (request == null) return entity;

        if (request.getDiseaseName() != null) {
            entity.setDiseaseName(request.getDiseaseName());
        }
        if (request.getDiagnosedDate() != null) {
            entity.setDiagnosedDate(request.getDiagnosedDate());
        }
        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }
        return entity;
    }
}
