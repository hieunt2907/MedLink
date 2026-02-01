package com.hieunt.medlink.app.requests.lab_order;

import java.time.OffsetDateTime;

import com.hieunt.medlink.app.entities.LabOrdersEntity.LabOrderStatus;
import com.hieunt.medlink.app.entities.LabOrdersEntity.LabOrderUrgency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabOrdersRequest {
    private Long medicalRecordId;
    private Long doctorId;
    private Long specialtyId;
    private Long roomId;
    private Long labDoctorId;
    private String orderNumber;
    private String testType;
    private String instructions;
    private LabOrderStatus status;
    private LabOrderUrgency urgency;
    private OffsetDateTime orderedAt;
    private OffsetDateTime completedAt;
}
