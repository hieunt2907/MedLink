package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.requests.hospital.SpecialtyRequest;
import org.springframework.stereotype.Component;

@Component
public class SpecialtiesMapper {
    public SpecialtiesEntity toEntity(SpecialtyRequest request) {
        if (request == null) {
            return null;
        }

        SpecialtiesEntity entity = new SpecialtiesEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setHospitalId(request.getHospitalId());

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        return entity;
    }

    public SpecialtiesEntity toEntity(SpecialtyRequest request, SpecialtiesEntity entity) {
        if (request == null) {
            return entity;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getHospitalId() != null) {
            entity.setHospitalId(request.getHospitalId());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        return entity;
    }
}
