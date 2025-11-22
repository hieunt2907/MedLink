package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientProfileRepository extends BaseRepository<PatientProfilesEntity, Long> {
    boolean existsByPatientCode(String patientCode);

    PatientProfilesEntity findByUserId(Long userId);

    @Query(value = """
                select *
                from patient_profiles pp
                inner join users u on pp.user_id = u.id
                where pp.patient_code ilike concat('%', :keyword, '%')
                    or u.full_name ilike concat('%', :keyword, '%')
                    or u.username ilike concat('%', :keyword, '%')
                    or u.email ilike concat('%', :keyword, '%')
                    or u.phone ilike concat('%', :keyword, '%')
                    or pp.emergency_contact_name ilike concat('%', :keyword, '%')
                    or pp.emergency_contact_name ilike concat('%', :keyword, '%')
            """, nativeQuery = true)
    Page<PatientProfilesEntity> filterPatientProfiles(@Param("keyword") String keyword, Pageable pageable);
}
