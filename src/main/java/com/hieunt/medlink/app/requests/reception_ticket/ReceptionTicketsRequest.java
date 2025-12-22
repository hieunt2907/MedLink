package com.hieunt.medlink.app.requests.reception_ticket;

import java.time.OffsetDateTime;

import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.AppointmentPriority;
import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.QueueStatus;
import com.hieunt.medlink.app.entities.ReceptionTicketsEntity.VisitPayerType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionTicketsRequest {
    private Long hospitalId;
    private Long patientId;
    private Long specialtyId;
    private Long doctorId;
    private Long roomId;
    private Integer queueNumber;
    private OffsetDateTime estimatedTime;
    private VisitPayerType payerType;
    private QueueStatus Status;
    private String reason;
    private AppointmentPriority priority;
    private String notes;
    
}
