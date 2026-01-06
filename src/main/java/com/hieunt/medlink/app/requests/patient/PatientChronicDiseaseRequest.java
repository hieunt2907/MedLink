package com.hieunt.medlink.app.requests.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientChronicDiseaseRequest {
    private String diseaseName;
    private LocalDate diagnosedDate;
    private String notes;
}
