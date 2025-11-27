package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.DoctorProfileEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class DoctorProfileMapper {

    public DoctorProfileEntity toEntity(DoctorProfileRequest request) {
        if (request == null)
            return null;

        DoctorProfileEntity entity = new DoctorProfileEntity();
        entity.setUserId(request.getUserId());
        entity.setSpecialtyId(request.getSpecialtyId());
        entity.setQualifications(request.getQualifications());
        entity.setExperienceYear(request.getExperienceYear());
        return entity;
    }

    public DoctorProfileEntity toEntity(DoctorProfileRequest request, DoctorProfileEntity entity) {
        if (request == null)
            return entity;

        if (request.getUserId() != null) {
            entity.setUserId(request.getUserId());
        }
        if (request.getSpecialtyId() != null) {
            entity.setSpecialtyId(request.getSpecialtyId());
        }
        if (request.getQualifications() != null) {
            entity.setQualifications(request.getQualifications());
        }
        if (request.getExperienceYear() != null) {
            entity.setExperienceYear(request.getExperienceYear());
        }
        return entity;
    }
}
