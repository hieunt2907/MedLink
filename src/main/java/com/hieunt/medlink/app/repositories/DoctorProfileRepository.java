package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.responses.doctor.DoctorProfileResponse;

public interface DoctorProfileRepository extends BaseRepository<DoctorProfileEntity, Long> {
    boolean existsByUserId(Long userId);

    DoctorProfileEntity findByUserId(Long userId);

    @Query(value = """
            select
                dp.id,
                dp.user_id as userId,
                u.full_name as fullName,
                s.name as specialtyName,
                h.name as hospitalName,
                s.id as specialtyId,
                h.id as hospitalId,
                dp.experience_years,
                dp.qualifications
            from doctor_profiles dp
            inner join users u on dp.user_id = u.id
            inner join user_roles ur on u.id = ur.user_id
            inner join specialties s on dp.specialty_id = s.id
            inner join hospitals h on ur.hospital_id = h.id
            where u.full_name ilike concat('%', :keyword, '%')
            """, nativeQuery = true)
    Page<DoctorProfileResponse> filterDoctorProfiles(@Param("keyword") String keyword, Pageable pageable);
}
