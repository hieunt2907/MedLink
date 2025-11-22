package com.hieunt.medlink.app.requests.patient;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAllergiesRequest {
    private String allergyName;
    private PatientAllergiesEntity.Severity severity;
    private String notes;
}
