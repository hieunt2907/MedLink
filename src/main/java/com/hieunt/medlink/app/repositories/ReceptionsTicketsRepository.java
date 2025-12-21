package com.hieunt.medlink.app.repositories;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.ReceptionTicketsEntity;
import com.hieunt.medlink.app.responses.reception_ticket.ReceptionTicketsResponse;

public interface ReceptionsTicketsRepository extends BaseRepository<ReceptionTicketsEntity, Long> {

        /**
         * Lấy queue_number lớn nhất trong ngày cho phòng khám + chuyên khoa + bệnh viện
         * Queue number sẽ tăng theo cùng phòng, chuyên khoa, bệnh viện
         * Sử dụng để tự động tạo queue_number mới (max + 1), hoặc reset về 1 nếu là
         * ngày mới
         * 
         * @param hospitalId  ID của bệnh viện
         * @param roomId      ID của phòng khám
         * @param specialtyId ID của chuyên khoa
         * @param currentDate Ngày hiện tại
         * @return queue_number lớn nhất, hoặc null nếu chưa có bản ghi nào trong ngày
         */
        @Query(value = "SELECT MAX(queue_number) " +
                        "FROM reception_tickets " +
                        "WHERE hospital_id = :hospitalId " +
                        "AND room_id = :roomId " +
                        "AND specialty_id = :specialtyId " +
                        "AND DATE(created_at) = DATE(:currentDate)", nativeQuery = true)
        Integer findMaxQueueNumberInDay(
                        @Param("hospitalId") Long hospitalId,
                        @Param("roomId") Long roomId,
                        @Param("specialtyId") Long specialtyId,
                        @Param("currentDate") OffsetDateTime currentDate);

        /**
         * Kiểm tra xem bệnh nhân đã có phiếu khám trong ngày chưa (tránh trùng lặp)
         * Kiểm tra theo hospital + patient + room + specialty
         * 
         * @param hospitalId  ID của bệnh viện
         * @param patientId   ID của bệnh nhân
         * @param roomId      ID của phòng khám
         * @param specialtyId ID của chuyên khoa
         * @param currentDate Ngày hiện tại
         * @return 1 nếu đã tồn tại, 0 nếu chưa
         */
        @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
                        "FROM reception_tickets " +
                        "WHERE hospital_id = :hospitalId " +
                        "AND patient_id = :patientId " +
                        "AND room_id = :roomId " +
                        "AND specialty_id = :specialtyId " +
                        "AND DATE(created_at) = DATE(:currentDate)", nativeQuery = true)
        Integer existsPatientTicketInDay(
                        @Param("hospitalId") Long hospitalId,
                        @Param("patientId") Long patientId,
                        @Param("roomId") Long roomId,
                        @Param("specialtyId") Long specialtyId,
                        @Param("currentDate") OffsetDateTime currentDate);

        /**
         * Filter reception tickets theo nhiều tiêu chí
         * Hỗ trợ tìm kiếm theo: hospital, room, specialty, status, priority, và tên
         * bệnh nhân
         * 
         * @param hospitalId  ID của bệnh viện (bắt buộc)
         * @param roomId      ID của phòng khám (optional, null = tất cả)
         * @param specialtyId ID của chuyên khoa (optional, null = tất cả)
         * @param status      Trạng thái (optional, null = tất cả)
         * @param priority    Độ ưu tiên (optional, null = tất cả)
         * @param keyword     Từ khóa tìm kiếm tên bệnh nhân (optional, null/empty = tất
         *                    cả)
         * @param pageable    Phân trang
         * @return Page of ReceptionTicketsResponse
         */
        @Query(value = """
                        select
                            rt.id,
                            h.name as hospitalName,
                            s.name as specialtyName,
                            us.full_name as patientName,
                            ud.full_name as doctorName,
                            r.room_number as roomNumber,
                            rt.queue_number as queueNumber,
                            rt.estimated_time as estimatedTime,
                            rt.actual_start_time as actualStartTime,
                            rt.actual_end_time as actualEndTime,
                            rt.payer_type as payerType,
                            rt.status as status,
                            rt.reason as reason,
                            rt.priority as priority,
                            rt.notes as notes,
                            rt.created_at as createdAt
                        from reception_tickets rt
                        inner  join specialties s on rt.specialty_id = s.id
                        inner join hospitals h on rt.hospital_id  = h.id
                        inner join users us on rt.patient_id = us.id
                        inner join users ud on rt.doctor_id = ud.id
                        inner join rooms r on rt.room_id = r.id
                        where rt.patient_id = :patientId
                        and (COALESCE(:keyword, '') = '' OR us.full_name ILIKE CONCAT('%', :keyword, '%'))
                        order by
                            rt.created_at desc,
                            rt.queue_number asc
                        """, nativeQuery = true)
        Page<ReceptionTicketsResponse> filterReceptionTicketMe(
                        @Param("patientId") Long patientId,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        @Query(value = """
                        select
                            rt.id as id,
                            h.name as hospitalName,
                            us.full_name as patientName,
                            s.name as specialtyName,
                            ud.full_name as doctorName,
                            r.room_number as roomNumber,
                            rt.queue_number as queueNumber,
                            rt.estimated_time as estimatedTime,
                            rt.actual_start_time as actualStartTime,
                            rt.actual_end_time as actualEndTime,
                            rt.payer_type as payerType,
                            rt.status as status,
                            rt.reason as reason,
                            rt.priority as priority,
                            rt.notes as notes,
                            rt.created_at as createdAt
                        from reception_tickets rt
                        inner join hospitals h on rt.hospital_id = h.id
                        inner join specialties s on rt.specialty_id = s.id
                        inner join users ud on rt.doctor_id = ud.id
                        inner join users us on rt.patient_id = us.id
                        inner join rooms r on rt.room_id = r.id
                        where
                            (:hospitalId IS NULL OR rt.hospital_id = :hospitalId)
                            or (:roomId IS NULL OR rt.room_id = :roomId)
                            or (:specialtyId IS NULL OR rt.specialty_id = :specialtyId)
                            or (:doctorId IS NULL OR rt.doctor_id = :doctorId)
                            and (COALESCE(:keyword, '') = '' OR us.full_name ilike concat('%', :keyword, '%'))
                        order by
                            rt.created_at desc,
                            rt.queue_number asc
                        """, nativeQuery = true)
        Page<ReceptionTicketsResponse> filterReceptionTickets(
                        @Param("hospitalId") Long hospitalId,
                        @Param("roomId") Long roomId,
                        @Param("specialtyId") Long specialtyId,
                        @Param("doctorId") Long doctorId,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        @Query(value = """
                        select
                            rt.id as id,
                            h.name as hospitalName,
                            us.full_name as patientName,
                            s.name as specialtyName,
                            ud.full_name as doctorName,
                            r.room_number as roomNumber,
                            rt.queue_number as queueNumber,
                            rt.estimated_time as estimatedTime,
                            rt.actual_start_time as actualStartTime,
                            rt.actual_end_time as actualEndTime,
                            rt.payer_type as payerType,
                            rt.status as status,
                            rt.reason as reason,
                            rt.priority as priority,
                            rt.notes as notes,
                            rt.created_at as createdAt
                        from reception_tickets rt
                        inner join hospitals h on rt.hospital_id = h.id
                        inner join specialties s on rt.specialty_id = s.id
                        inner join users ud on rt.doctor_id = ud.id
                        inner join users us on rt.patient_id = us.id
                        inner join rooms r on rt.room_id = r.id
                        where rt.patient_id = :patientId
                        order by rt.created_at desc
                        limit 1
                        """, nativeQuery = true)
        ReceptionTicketsResponse getReceptionTicketByPatientId(@Param("patientId") Long patientId);
}
