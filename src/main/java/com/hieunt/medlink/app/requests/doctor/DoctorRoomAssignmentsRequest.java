package com.hieunt.medlink.app.requests.doctor;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRoomAssignmentsRequest {
    private Long doctorProfileId;
    private Long hospitalId;
    private Long roomId;
    private Long specialtyId;
    private Boolean isPrimary;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
}
