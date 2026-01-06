package com.hieunt.medlink.app.entities;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "lab_results")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lab_order_id", nullable = false)
    private Long labOrderId;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "result_value")
    private String resultValue;

    @Column(name = "reference_range")
    private String referenceRange;

    @Column(name = "unit")
    private String unit;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private LabResultStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "performed_by")
    private Long performedBy;

    @CreationTimestamp
    @Column(name = "performed_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime performedAt;

    public enum LabResultStatus {
        normal, abnormal, critical
    }

}
