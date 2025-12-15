package com.hieunt.medlink.app.entities;

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
@Table(name = "doctor_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "qualifications", columnDefinition = "TEXT")
    private String qualifications;

    @Column(name = "experience_years")
    private Integer experienceYears;
}
