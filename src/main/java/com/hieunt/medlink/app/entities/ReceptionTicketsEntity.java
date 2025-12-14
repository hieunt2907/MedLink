package com.hieunt.medlink.app.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reception_tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionTicketsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "queue_number", nullable = false)
    private Integer queueNumber;

    @Column(name = "estimated_time", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime estimatedTime;

    @Column(name = "actual_start_time", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime actualStartTime;

    @Column(name = "actual_end_time", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime actualEndTime;

    @Column(name = "visit_payer_type")
    private VisitPayerType visitPayerType;

    @Column(name = "status")
    private QueueStatus Status;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "priority")
    private AppointmentPriority priority;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    public enum VisitPayerType {
        self_pay,
        insurance
    }

    public enum QueueStatus {
        waiting,
        called,
        in_progress,
        completed,
        skipped
    }

    public enum AppointmentPriority {
        normal,
        urgent,
        emergency
    }
}
