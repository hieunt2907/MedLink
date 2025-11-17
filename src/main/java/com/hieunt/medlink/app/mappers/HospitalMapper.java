package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.requests.hospital.HospitalRequest;
import org.springframework.stereotype.Component;

@Component
public class HospitalMapper {

    public HospitalEntity toEntity(HospitalRequest request) {
        if (request == null) return null;

        HospitalEntity entity = new HospitalEntity();
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        return entity;
    }

    public HospitalEntity toEntity(HospitalRequest request, HospitalEntity entity) {
        if (request == null) return entity;

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getAddress() != null) {
            entity.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            entity.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        return entity;
    }
}
