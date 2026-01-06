package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;
import com.hieunt.medlink.app.responses.doctor.DoctorRoomAssignmentResponse;

public interface DoctorRoomAssignmentsRepository extends BaseRepository<DoctorRoomAssignmentsEntity, Long> {

        @Query(value = """
                        select dra.id,
                        dra.doctor_profile_id as doctorProfileId,
                        dra.hospital_id as hospitalId,
                        dra.room_id as roomId,
                        r.room_number as roomNumber,
                        dra.specialty_id as specialtyId,
                        dra.is_primary as isPrimary,
                        dra.shift_start as shiftStart,
                        dra.shift_end as shiftEnd,
                        dra.start_date as startDate,
                        dra.end_date as endDate,
                        dra.notes,
                        dra.created_at as createdAt,
                        dra.updated_at as updatedAt
                        from doctor_room_assignments dra
                        inner join specialties s on dra.specialty_id = s.id
                        inner join rooms r on dra.room_id = r.id
                        where
                        (:doctorProfileId is null or dra.doctor_profile_id = :doctorProfileId)
                        """, nativeQuery = true)
        Page<DoctorRoomAssignmentResponse> filterDoctorRoomAssignments(@Param("doctorProfileId") Long doctorProfileId,
                        @Param("keyword") String keyword, Pageable pageable);
}
