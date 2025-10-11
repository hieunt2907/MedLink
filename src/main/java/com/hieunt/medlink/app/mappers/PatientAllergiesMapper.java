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
}
