package com.hieunt.medlink.app.mappers;


import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.requests.patient.PatientProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class PatientProfileMapper {
    public PatientProfilesEntity toEntity(PatientProfileRequest patient) {
        if (patient == null) {
            return null;
        }

        PatientProfilesEntity patientProfilesEntity = new PatientProfilesEntity();
        patientProfilesEntity.setPatientCode(patient.getPatientCode());
        patientProfilesEntity.setEmergencyContactName(patient.getEmergencyContactName());
        patientProfilesEntity.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        patientProfilesEntity.setBloodType(patient.getBloodType());
        return patientProfilesEntity;
    }

    public PatientProfilesEntity toEntity(PatientProfileRequest patient, PatientProfilesEntity patientProfilesEntity) {
        if (patient == null) {
            return patientProfilesEntity;
        }

        if (patient.getPatientCode() != null) {
            patientProfilesEntity.setPatientCode(patient.getPatientCode());
        }

        if (patient.getEmergencyContactName() != null) {
            patientProfilesEntity.setEmergencyContactName(patient.getEmergencyContactName());
        }

        if (patient.getEmergencyContactPhone() != null) {
            patientProfilesEntity.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        }

        if (patient.getBloodType() != null) {
            patientProfilesEntity.setBloodType(patient.getBloodType());
        }

        return patientProfilesEntity;
    }
}
