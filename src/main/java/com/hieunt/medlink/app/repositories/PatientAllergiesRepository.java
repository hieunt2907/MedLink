package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientAllergiesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientAllergiesRepository extends BaseRepository<PatientAllergiesEntity, Long>{
    PatientAllergiesEntity findByPatientProfileId(Long patientProfileId);

    @Query(value = """
        select 
            pa.id             as id,
            pa.patient_profile_id as patientProfileId,
            pa.allergy_name   as allergyName,
            pa.severity       as severity,
            pa.notes          as notes
        from patient_allergies pa
        where pa.patient_profile_id = :id
    """, nativeQuery = true)
    Page<PatientAllergiesEntity> filterAllergies(@Param("id") Long patientProfileId, Pageable pageable);
}
