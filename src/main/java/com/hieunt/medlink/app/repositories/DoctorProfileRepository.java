package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;

public interface DoctorProfileRepository extends BaseRepository<DoctorProfileEntity, Long> {
    boolean existsByUserId(Long userId);

    DoctorProfileEntity findByUserId(Long userId);

    @Query(value = """
            select
                dp.id          as id,
                dp.user_id     as userId,
                dp.specialty_id as specialtyId,
                dp.qualifications as qualifications,
                dp.experience_year as experienceYear
            from doctor_profiles dp
            inner join users u on dp.user_id = u.id
            where u.fullname ilike concat('%', :keyword, '%')
            and dp.specialty_id = :specialtyId
            """, nativeQuery = true)
    Page<DoctorProfileEntity> filterDoctorProfiles(@Param("specialtyId") Long specialtyId,
            @Param("keyword") String keyword, Pageable pageable);
}
