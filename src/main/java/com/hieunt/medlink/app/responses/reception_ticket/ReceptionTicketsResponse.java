package com.hieunt.medlink.app.responses.reception_ticket;

import java.time.Instant;

import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.AppointmentPriority;
import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.QueueStatus;
import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.VisitPayerType;

public interface ReceptionTicketsResponse {
    Long getId();
    String getHospitalName();
    String getPatientName();
    String getSpecialtyName();
    String getDoctorName();
    String getRoomNumber();
    Integer getQueueNumber();
    Instant getEstimatedTime();
    Instant getActualStartTime();
    Instant getActualEndTime();
    VisitPayerType getPayerType();
    QueueStatus getStatus();
    String getReason();
    AppointmentPriority getPriority();
    String getNotes();
    Instant getCreatedAt();
    
}
