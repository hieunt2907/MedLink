package com.hieunt.medlink.app.entities;

import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medical_record_id", nullable = false)
    private Long medicalRecordId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "specialty_id", nullable = false)
    private Long specialtyId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "lab_doctor_id")
    private Long labDoctorId;

    @Column(name = "order_number", length = 20, nullable = false)
    private String orderNumber;

    @Column(name = "test_type", length = 100, nullable = false)
    private String testType;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private LabOrderStatus status;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "urgency")
    private LabOrderUrgency urgency;

    @Column(name = "ordered_at")
    private OffsetDateTime orderedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;


    public enum LabOrderStatus {
        ordered, collected, processing, completed, cancelled
    }

    public enum LabOrderUrgency {
        routine, urgent, stat
    }


}
