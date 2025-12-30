package com.hieunt.medlink.app.responses.lab_order;

import java.time.OffsetDateTime;

public interface LabOrdersResponse {
    Long getId();

    String getOrderNumber();

    String getTestType();

    String getPatientName();

    String getDoctorName();

    String getLabDoctorName();

    String getSpecialtyName();

    String getRoomNumber();

    String getStatus();

    String getUrgency();

    OffsetDateTime getOrderedAt();

    OffsetDateTime getCompletedAt();
}
