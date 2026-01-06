package com.hieunt.medlink.app.responses.doctor;

public interface DoctorProfileResponse {
    Long getId();

    Long getUserId();

    String getFullName();

    String getSpecialtyName();

    String getHospitalName();

    Integer getExperienceYears();

    String getQualifications();

    Long getSpecialtyId();

    Long getHospitalId();
}
