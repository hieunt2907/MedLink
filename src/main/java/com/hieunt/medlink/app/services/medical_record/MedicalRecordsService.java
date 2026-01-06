package com.hieunt.medlink.app.services.medical_record;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.requests.medical_record.MedicalRecordsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.medical_record.MedicalRecordsResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordsService {

    BaseResponse<MedicalRecordsEntity> createMedicalRecord(MedicalRecordsRequest request);

    BaseResponse<MedicalRecordsEntity> updateMedicalRecord(Long id, MedicalRecordsRequest request);

    BaseResponse<MedicalRecordsEntity> getMedicalRecordById(Long id);

    BaseResponse<MedicalRecordsEntity> getMedicalRecordByReceptionTicketId(Long receptionTicketId);

    BaseResponse<MedicalRecordsEntity> deleteMedicalRecord(Long id);

    BaseResponse<Page<MedicalRecordsEntity>> getMyMedicalRecords(Pageable pageable);

    BaseResponse<Page<MedicalRecordsEntity>> getMyDoctorMedicalRecords(Pageable pageable);

    BaseResponse<Page<MedicalRecordsResponse>> filterMedicalRecords(
            Long hospitalId,
            Long patientId,
            Long doctorId,
            String status,
            String keyword,
            Pageable pageable);
}
