package com.hieunt.medlink.app.responses.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;

public interface PatientAllergiesResponse {
    Long getId();
    String getAllergyName();
    PatientAllergiesEntity.Severity getSeverity();
    String getNotes();
}
