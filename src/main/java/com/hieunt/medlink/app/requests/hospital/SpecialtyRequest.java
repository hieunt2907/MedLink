package com.hieunt.medlink.app.requests.hospital;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyRequest {
    private String name;
    private Long hospitalId;
    private String description;
    private SpecialtiesEntity.SpecialtyStatus status;
}
