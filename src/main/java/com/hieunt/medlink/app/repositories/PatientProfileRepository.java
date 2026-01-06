package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;
import com.hieunt.medlink.app.responses.patient.PatientProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientProfileRepository extends BaseRepository<PatientProfilesEntity, Long> {
    boolean existsByPatientCode(String patientCode);

    PatientProfilesEntity findByUserId(Long userId);

    @Query(value = """
                select pp.id,
                pp.patient_code,
                pp.emergency_contact_name,
                pp.emergency_contact_phone,
                pp.blood_type,
                pp.user_id as userId,
                u.full_name as fullName,
                u.email as email,
                u.phone as phone,
                u.gender as gender,
                u.date_of_birth as dateOfBirth,
                u.address as address
                from patient_profiles pp
                inner join users u on pp.user_id = u.id
                where pp.patient_code ilike concat('%', :keyword, '%')
                    or u.full_name ilike concat('%', :keyword, '%')
                    or u.username ilike concat('%', :keyword, '%')
                    or u.email ilike concat('%', :keyword, '%')
                    or u.phone ilike concat('%', :keyword, '%')
                    or pp.emergency_contact_name ilike concat('%', :keyword, '%')
                    or pp.emergency_contact_phone ilike concat('%', :keyword, '%')
            """, nativeQuery = true)
    Page<PatientProfileResponse> filterPatientProfiles(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
                select pp.id,
                pp.patient_code,
                pp.emergency_contact_name,
                pp.emergency_contact_phone,
                pp.blood_type,
                pp.user_id as userId,
                u.full_name as fullName,
                u.email as email,
                u.phone as phone,
                u.gender as gender,
                u.date_of_birth as dateOfBirth,
                u.address as address
                from patient_profiles pp
                inner join users u on pp.user_id = u.id
                where pp.user_id = :userId
            """, nativeQuery = true)
    PatientProfileResponse filterPatientProfileMe(@Param("userId") Long userId);
}
