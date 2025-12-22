package com.hieunt.medlink.app.requests.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileRequest {
    private Long userId;
    private Long specialtyId;
    private String qualifications;
    private Integer experienceYears;
}
