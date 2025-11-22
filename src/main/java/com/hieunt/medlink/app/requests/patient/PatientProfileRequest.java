package com.hieunt.medlink.app.requests.patient;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientProfileRequest {
    private String patientCode;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private PatientProfilesEntity.BloodType bloodType;
}
