package com.hieunt.medlink.app.responses.doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public interface DoctorRoomAssignmentResponse {
    Long getId();

    Long getDoctorProfileId();

    Long getHospitalId();

    Long getRoomId();

    String getRoomNumber();

    Long getSpecialtyId();

    Boolean getIsPrimary();

    LocalTime getShiftStart();

    LocalTime getShiftEnd();

    LocalDate getStartDate();

    LocalDate getEndDate();

    String getNotes();

    OffsetDateTime getCreatedAt();

    OffsetDateTime getUpdatedAt();
}
