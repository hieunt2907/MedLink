package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.requests.lab_order.LabOrdersRequest;
import org.springframework.stereotype.Component;

@Component
public class LabOrdersMapper {

    public LabOrdersEntity toEntity(LabOrdersRequest request) {
        if (request == null) {
            return null;
        }

        LabOrdersEntity entity = new LabOrdersEntity();
        entity.setMedicalRecordId(request.getMedicalRecordId());
        entity.setDoctorId(request.getDoctorId());
        entity.setSpecialtyId(request.getSpecialtyId());
        entity.setRoomId(request.getRoomId());
        entity.setLabDoctorId(request.getLabDoctorId());
        entity.setOrderNumber(request.getOrderNumber());
        entity.setTestType(request.getTestType());
        entity.setInstructions(request.getInstructions());
        entity.setStatus(request.getStatus());
        entity.setUrgency(request.getUrgency());
        entity.setOrderedAt(request.getOrderedAt());
        entity.setCompletedAt(request.getCompletedAt());
        return entity;
    }

    public LabOrdersEntity toEntity(LabOrdersRequest request, LabOrdersEntity entity) {
        if (request == null) {
            return entity;
        }



        if (request.getDoctorId() != null) {
            entity.setDoctorId(request.getDoctorId());
        }

        if (request.getSpecialtyId() != null) {
            entity.setSpecialtyId(request.getSpecialtyId());
        }

        if (request.getRoomId() != null) {
            entity.setRoomId(request.getRoomId());
        }

        if (request.getLabDoctorId() != null) {
            entity.setLabDoctorId(request.getLabDoctorId());
        }

        if (request.getOrderNumber() != null) {
            entity.setOrderNumber(request.getOrderNumber());
        }

        if (request.getTestType() != null) {
            entity.setTestType(request.getTestType());
        }

        if (request.getInstructions() != null) {
            entity.setInstructions(request.getInstructions());
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if (request.getUrgency() != null) {
            entity.setUrgency(request.getUrgency());
        }

        if (request.getOrderedAt() != null) {
            entity.setOrderedAt(request.getOrderedAt());
        }

        if (request.getCompletedAt() != null) {
            entity.setCompletedAt(request.getCompletedAt());
        }

        return entity;
    }
}
