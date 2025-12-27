package com.hieunt.medlink.app.services.medical_record;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.MedicalRecordsMapper;
import com.hieunt.medlink.app.repositories.MedicalRecordsRepository;
import com.hieunt.medlink.app.requests.medical_record.MedicalRecordsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.medical_record.MedicalRecordsResponse;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordsServiceImpl implements MedicalRecordsService {
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final MedicalRecordsMapper medicalRecordsMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<MedicalRecordsEntity> createMedicalRecord(MedicalRecordsRequest request) {
        // Kiểm tra xem reception ticket đã có medical record chưa
        if (request.getReceptionTicketId() != null
                && medicalRecordsRepository.existsByReceptionTicketId(request.getReceptionTicketId())) {
            throw new IllegalArgumentException(
                    "Medical record already exists for reception ticket ID: " + request.getReceptionTicketId());
        }

        MedicalRecordsEntity entity = medicalRecordsMapper.toEntity(request);
        if (entity != null) {
            medicalRecordsRepository.save(entity);
        }
        return new BaseResponse<>("Creating medical record successfully", entity);
    }

    @Override
    public BaseResponse<MedicalRecordsEntity> updateMedicalRecord(Long id, MedicalRecordsRequest request) {
        MedicalRecordsEntity entity = getMedicalRecordById(id).getData();

        if (entity != null) {
            medicalRecordsMapper.toEntity(request, entity);
            medicalRecordsRepository.save(entity);
            return new BaseResponse<>("Updating medical record successfully", entity);
        }

        throw new ResourceNotFoundException("Medical record not found with id: " + id);
    }

    @Override
    public BaseResponse<MedicalRecordsEntity> getMedicalRecordById(Long id) {
        MedicalRecordsEntity entity = medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord", "id", id));
        return new BaseResponse<>("Getting medical record successfully", entity);
    }

    @Override
    public BaseResponse<MedicalRecordsEntity> getMedicalRecordByReceptionTicketId(Long receptionTicketId) {
        MedicalRecordsEntity entity = medicalRecordsRepository.findByReceptionTicketId(receptionTicketId);

        if (entity == null) {
            throw new ResourceNotFoundException(
                    "Medical record not found for reception ticket ID: " + receptionTicketId);
        }

        return new BaseResponse<>("Getting medical record successfully", entity);
    }

    @Override
    public BaseResponse<MedicalRecordsEntity> deleteMedicalRecord(Long id) {
        MedicalRecordsEntity entity = getMedicalRecordById(id).getData();

        if (entity != null) {
            medicalRecordsRepository.delete(entity);
            return new BaseResponse<>("Deleting medical record successfully", entity);
        }

        throw new ResourceNotFoundException("Medical record not found with id: " + id);
    }

    @Override
    public BaseResponse<Page<MedicalRecordsEntity>> getMyMedicalRecords(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        Page<MedicalRecordsEntity> medicalRecords = medicalRecordsRepository.findByPatientId(user.getId(), pageable);
        return new BaseResponse<>("Getting my medical records successfully", medicalRecords);
    }

    @Override
    public BaseResponse<Page<MedicalRecordsEntity>> getMyDoctorMedicalRecords(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        Page<MedicalRecordsEntity> medicalRecords = medicalRecordsRepository.findByDoctorId(user.getId(), pageable);
        return new BaseResponse<>("Getting doctor's medical records successfully", medicalRecords);
    }

    @Override
    public BaseResponse<Page<MedicalRecordsResponse>> filterMedicalRecords(
            Long hospitalId,
            Long patientId,
            Long doctorId,
            String status,
            String keyword,
            Pageable pageable) {
        Page<MedicalRecordsResponse> medicalRecords = medicalRecordsRepository.filterMedicalRecords(
                hospitalId,
                patientId,
                doctorId,
                status,
                keyword,
                pageable);
        return new BaseResponse<>("Filtering medical records successfully", medicalRecords);
    }
}
