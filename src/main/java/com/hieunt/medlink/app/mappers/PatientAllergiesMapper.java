package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import com.hieunt.medlink.app.requests.patient.PatientAllergiesRequest;
import org.springframework.stereotype.Component;

@Component
public class PatientAllergiesMapper {
    public PatientAllergiesEntity toEntity(PatientAllergiesRequest request) {
        if (request == null) {
            return null;
        }

        PatientAllergiesEntity entity = new PatientAllergiesEntity();
        entity.setAllergyName(request.getAllergyName());
        entity.setSeverity(request.getSeverity());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public PatientAllergiesEntity toEntity(PatientAllergiesRequest request, PatientAllergiesEntity entity) {
        if (request == null) {
            return entity;
        }

        if (request.getAllergyName() != null) {
            entity.setAllergyName(request.getAllergyName());
        }

        if (request.getSeverity() != null) {
            entity.setSeverity(request.getSeverity());
        }

        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }

        return entity;
    }
}
