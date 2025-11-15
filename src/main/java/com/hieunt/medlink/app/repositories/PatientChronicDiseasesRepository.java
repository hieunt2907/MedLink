package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientChronicDiseasesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientChronicDiseasesRepository extends BaseRepository<PatientChronicDiseasesEntity, Long> {

    @Query(value = """
                select
                    pcd.id               as id,
                    pcd.patient_profile_id as patientProfileId,
                    pcd.disease_name     as diseaseName,
                    pcd.diagnosed_date   as diagnosisDate,
                    pcd.notes            as notes
                from patient_chronic_diseases pcd
                where pcd.patient_profile_id = :id
            """, nativeQuery = true)
    Page<PatientChronicDiseasesEntity> filterChronicDiseases(@Param("id") Long patientProfileId, Pageable pageable);
}
