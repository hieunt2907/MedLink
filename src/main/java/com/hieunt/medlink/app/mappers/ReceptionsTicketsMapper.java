package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.ReceptionTicketsEntity;
import com.hieunt.medlink.app.requests.reception_ticket.ReceptionTicketsRequest;
import org.springframework.stereotype.Component;

@Component
public class ReceptionsTicketsMapper {

    public ReceptionTicketsEntity toEntity(ReceptionTicketsRequest request) {
        if (request == null)
            return null;

        ReceptionTicketsEntity entity = new ReceptionTicketsEntity();
        entity.setHospitalId(request.getHospitalId());
        entity.setPatientId(request.getPatientId());
        entity.setSpecialtyId(request.getSpecialtyId());
        entity.setDoctorId(request.getDoctorId());
        entity.setRoomId(request.getRoomId());
        entity.setEstimatedTime(request.getEstimatedTime());
        entity.setPayerType(request.getPayerType());
        entity.setStatus(request.getStatus());
        entity.setReason(request.getReason());
        entity.setPriority(request.getPriority());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public ReceptionTicketsEntity toEntity(ReceptionTicketsRequest request, ReceptionTicketsEntity entity) {
        if (request == null)
            return entity;

        if (request.getHospitalId() != null) {
            entity.setHospitalId(request.getHospitalId());
        }
        if (request.getPatientId() != null) {
            entity.setPatientId(request.getPatientId());
        }
        if (request.getSpecialtyId() != null) {
            entity.setSpecialtyId(request.getSpecialtyId());
        }
        if (request.getDoctorId() != null) {
            entity.setDoctorId(request.getDoctorId());
        }
        if (request.getRoomId() != null) {
            entity.setRoomId(request.getRoomId());
        }
        if (request.getEstimatedTime() != null) {
            entity.setEstimatedTime(request.getEstimatedTime());
        }
        if (request.getPayerType() != null) {
            entity.setPayerType(request.getPayerType());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        if (request.getReason() != null) {
            entity.setReason(request.getReason());
        }
        if (request.getPriority() != null) {
            entity.setPriority(request.getPriority());
        }
        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }
        return entity;
    }
}
