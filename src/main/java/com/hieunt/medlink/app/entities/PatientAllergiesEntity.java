package com.hieunt.medlink.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient_allergies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAllergiesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_profile_id")
    private Long patientProfileId;

    @Column(name = "allergy_name")
    private String allergyName;

    @Column(name = "severity")
    private Severity severity;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum Severity {
        mild, moderate, severe
    }
}
