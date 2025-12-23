package com.hieunt.medlink.app.responses.patient;


public interface PatientProfileResponse {
    Long getId();
    Long getUserId();
    String getPatientCode();
    String getFullName();
    String getDateOfBirth();
    String getGender();
    String getAddress(); 
    String getEmail();
    String getPhone();
    String getEmergencyContactName();
    String getEmergencyContactPhone();
    String getBloodType();
}
