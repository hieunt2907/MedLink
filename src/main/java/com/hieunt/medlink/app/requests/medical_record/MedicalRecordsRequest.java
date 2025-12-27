package com.hieunt.medlink.app.requests.medical_record;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity.MedicalRecordStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordsRequest {
    private Long receptionTicketId;
    private Long patientId;
    private Long doctorId;
    private Long hospitalId;
    private String recordNumber;
    private String chiefComplaint;
    private String historyOfPresentIllness;
    private String physicalExamination;
    private String assessment;
    private String diagnosis;
    private String plan;
    private String vitalSign;
    private MedicalRecordStatus status;
}
