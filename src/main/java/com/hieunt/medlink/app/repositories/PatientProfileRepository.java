package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.PatientProfilesEntity;

public interface PatientProfileRepository extends BaseRepository<PatientProfilesEntity, Long> {
    boolean existsByPatientCode(String patientCode);
    PatientProfilesEntity findByUserId(Long userId);

}
