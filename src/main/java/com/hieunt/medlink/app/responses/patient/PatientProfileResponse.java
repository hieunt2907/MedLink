package com.hieunt.medlink.app.responses.patient;

import java.util.List;

public interface PatientProfileResponse {
    Long getId();
    String getPatientCode();
    String getEmergencyContactName();
    String getEmergencyContactPhone();
    String getBloodType();
    List<PatientAllergiesResponse> getAllergies();
    List<PatientChronicDiseasesResponse> getDiseases();
}
