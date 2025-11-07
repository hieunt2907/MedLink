package com.hieunt.medlink.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "patient_chronic_diseases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientChronicDiseasesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_profile_id")
    private Long patientProfileId;

    @Column(name = "disease_name", nullable = false)
    private String diseaseName;

    @Column(name = "diagnosed_date", columnDefinition = "DATE")
    private LocalDate diagnosisDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
