package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.MedicalRecordsEntity;
import com.hieunt.medlink.app.responses.medical_record.MedicalRecordsResponse;

public interface MedicalRecordsRepository extends BaseRepository<MedicalRecordsEntity, Long> {

    /**
     * Tìm medical record theo reception ticket ID
     * 
     * @param receptionTicketId ID của phiếu khám
     * @return MedicalRecordsEntity hoặc null nếu không tìm thấy
     */
    MedicalRecordsEntity findByReceptionTicketId(Long receptionTicketId);

    /**
     * Kiểm tra xem reception ticket đã có medical record chưa
     * 
     * @param receptionTicketId ID của phiếu khám
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByReceptionTicketId(Long receptionTicketId);

    /**
     * Lấy danh sách medical records theo patient ID
     * 
     * @param patientId ID của bệnh nhân
     * @param pageable  Phân trang
     * @return Page of MedicalRecordsEntity
     */
    Page<MedicalRecordsEntity> findByPatientId(Long patientId, Pageable pageable);

    /**
     * Lấy danh sách medical records theo doctor ID
     * 
     * @param doctorId ID của bác sĩ
     * @param pageable Phân trang
     * @return Page of MedicalRecordsEntity
     */
    Page<MedicalRecordsEntity> findByDoctorId(Long doctorId, Pageable pageable);

    /**
     * Filter medical records với nhiều tiêu chí
     * 
     * @param hospitalId ID của bệnh viện (optional)
     * @param patientId  ID của bệnh nhân (optional)
     * @param doctorId   ID của bác sĩ (optional)
     * @param status     Trạng thái (optional)
     * @param keyword    Từ khóa tìm kiếm (tên bệnh nhân, số hồ sơ) (optional)
     * @param pageable   Phân trang
     * @return Page of MedicalRecordsResponse
     */
    @Query(value = """
            select
                mr.id,
                mr.record_number as recordNumber,
                rt.queue_number as queueNumber,
                up.full_name as patientName,
                ud.full_name as doctorName,
                h.name as hospitalName,
                s.name as specialtyName,
                mr.chief_complaint as chiefComplaint,
                mr.diagnosis,
                mr.status,
                mr.created_at as createdAt,
                mr.updated_at as updatedAt
            from medical_records mr
            inner join users up on mr.patient_id = up.id
            inner join users ud on mr.doctor_id = ud.id
            inner join hospitals h on mr.hospital_id = h.id
            inner join reception_tickets rt on mr.reception_ticket_id = rt.id
            inner join specialties s on rt.specialty_id = s.id
            where
                (:hospitalId IS NULL OR mr.hospital_id = :hospitalId)
                and (:patientId IS NULL OR mr.patient_id = :patientId)
                and (:doctorId IS NULL OR mr.doctor_id = :doctorId)
                and (:status IS NULL OR mr.status = CAST(:status AS medical_record_status))
                and (COALESCE(:keyword, '') = ''
                    OR up.full_name ILIKE CONCAT('%', :keyword, '%')
                    OR mr.record_number ILIKE CONCAT('%', :keyword, '%'))
            order by mr.created_at desc
            """, nativeQuery = true)
    Page<MedicalRecordsResponse> filterMedicalRecords(
            @Param("hospitalId") Long hospitalId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable);
}
