package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.requests.medical_record.MedicalRecordsRequest;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordsMapper {

    public MedicalRecordsEntity toEntity(MedicalRecordsRequest request) {
        if (request == null) {
            return null;
        }

        MedicalRecordsEntity entity = new MedicalRecordsEntity();
        entity.setReceptionTicketId(request.getReceptionTicketId());
        entity.setPatientId(request.getPatientId());
        entity.setDoctorId(request.getDoctorId());
        entity.setHospitalId(request.getHospitalId());
        entity.setRecordNumber(request.getRecordNumber());
        entity.setChiefComplaint(request.getChiefComplaint());
        entity.setHistoryOfPresentIllness(request.getHistoryOfPresentIllness());
        entity.setPhysicalExamination(request.getPhysicalExamination());
        entity.setAssessment(request.getAssessment());
        entity.setDiagnosis(request.getDiagnosis());
        entity.setPlan(request.getPlan());
        entity.setVitalSign(request.getVitalSign());
        entity.setStatus(request.getStatus());
        return entity;
    }

    public MedicalRecordsEntity toEntity(MedicalRecordsRequest request, MedicalRecordsEntity entity) {
        if (request == null) {
            return entity;
        }

        if (request.getReceptionTicketId() != null) {
            entity.setReceptionTicketId(request.getReceptionTicketId());
        }

        if (request.getPatientId() != null) {
            entity.setPatientId(request.getPatientId());
        }

        if (request.getDoctorId() != null) {
            entity.setDoctorId(request.getDoctorId());
        }

        if (request.getHospitalId() != null) {
            entity.setHospitalId(request.getHospitalId());
        }

        if (request.getRecordNumber() != null) {
            entity.setRecordNumber(request.getRecordNumber());
        }

        if (request.getChiefComplaint() != null) {
            entity.setChiefComplaint(request.getChiefComplaint());
        }

        if (request.getHistoryOfPresentIllness() != null) {
            entity.setHistoryOfPresentIllness(request.getHistoryOfPresentIllness());
        }

        if (request.getPhysicalExamination() != null) {
            entity.setPhysicalExamination(request.getPhysicalExamination());
        }

        if (request.getAssessment() != null) {
            entity.setAssessment(request.getAssessment());
        }

        if (request.getDiagnosis() != null) {
            entity.setDiagnosis(request.getDiagnosis());
        }

        if (request.getPlan() != null) {
            entity.setPlan(request.getPlan());
        }

        if (request.getVitalSign() != null) {
            entity.setVitalSign(request.getVitalSign());
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        return entity;
    }
}
