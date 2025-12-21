package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientChronicDiseasesRepository extends BaseRepository<PatientChronicDiseasesEntity, Long> {

    @Query(value = """
                select pcd.*
                from patient_chronic_diseases pcd
                where pcd.patient_profile_id = :id
            """, nativeQuery = true)
    Page<PatientChronicDiseasesEntity> filterChronicDiseases(@Param("id") Long patientProfileId, Pageable pageable);
}
