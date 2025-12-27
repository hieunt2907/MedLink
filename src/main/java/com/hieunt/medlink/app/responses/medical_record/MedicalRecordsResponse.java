package com.hieunt.medlink.app.responses.medical_record;

import java.time.OffsetDateTime;

public interface MedicalRecordsResponse {
    Long getId();

    String getRecordNumber();

    Long getQueueNumber();

    String getPatientName();

    String getDoctorName();

    String getHospitalName();

    String getSpecialtyName();

    String getChiefComplaint();

    String getDiagnosis();

    String getStatus();

    OffsetDateTime getCreatedAt();

    OffsetDateTime getUpdatedAt();
}
