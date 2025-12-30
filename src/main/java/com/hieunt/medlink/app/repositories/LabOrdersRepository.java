package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.responses.lab_order.LabOrdersResponse;

public interface LabOrdersRepository extends BaseRepository<LabOrdersEntity, Long> {

    LabOrdersEntity findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    Page<LabOrdersEntity> findByMedicalRecordId(Long medicalRecordId, Pageable pageable);

    Page<LabOrdersEntity> findByDoctorId(Long doctorId, Pageable pageable);

    Page<LabOrdersEntity> findByLabDoctorId(Long labDoctorId, Pageable pageable);

    @Query(value = """
            select
                lo.id,
                lo.order_number as orderNumber,
                lo.test_type as testType,
                up.full_name as patientName,
                ud.full_name as doctorName,
                ul.full_name as labDoctorName,
                s.name as specialtyName,
                r.room_number as roomNumber,
                lo.status,
                lo.urgency,
                lo.ordered_at as orderedAt,
                lo.completed_at as completedAt
            from lab_orders lo
            inner join medical_records mr on lo.medical_record_id = mr.id
            inner join users up on mr.patient_id = up.id
            inner join users ud on lo.doctor_id = ud.id
            left join users ul on lo.lab_doctor_id = ul.id
            inner join specialties s on lo.specialty_id = s.id
            left join rooms r on lo.room_id = r.id
            where
                (:medicalRecordId IS NULL OR lo.medical_record_id = :medicalRecordId)
                and (:doctorId IS NULL OR lo.doctor_id = :doctorId)
                and (:labDoctorId IS NULL OR lo.lab_doctor_id = :labDoctorId)
                and (:specialtyId IS NULL OR lo.specialty_id = :specialtyId)
                and (:status IS NULL OR lo.status = CAST(:status AS lab_order_status))
                and (:urgency IS NULL OR lo.urgency = CAST(:urgency AS lab_order_urgency))
                and (COALESCE(:keyword, '') = ''
                    OR up.full_name ILIKE CONCAT('%', :keyword, '%')
                    OR lo.order_number ILIKE CONCAT('%', :keyword, '%')
                    OR lo.test_type ILIKE CONCAT('%', :keyword, '%'))
            order by lo.ordered_at desc
            """, nativeQuery = true)
    Page<LabOrdersResponse> filterLabOrders(
            @Param("medicalRecordId") Long medicalRecordId,
            @Param("doctorId") Long doctorId,
            @Param("labDoctorId") Long labDoctorId,
            @Param("specialtyId") Long specialtyId,
            @Param("status") String status,
            @Param("urgency") String urgency,
            @Param("keyword") String keyword,
            Pageable pageable);
}
