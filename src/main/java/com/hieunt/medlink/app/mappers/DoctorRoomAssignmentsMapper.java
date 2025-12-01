package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.DoctorRoomAssignmentsEntity;
import com.hieunt.medlink.app.requests.doctor.DoctorRoomAssignmentsRequest;
import org.springframework.stereotype.Component;

@Component
public class DoctorRoomAssignmentsMapper {

    public DoctorRoomAssignmentsEntity toEntity(DoctorRoomAssignmentsRequest request) {
        if (request == null)
            return null;

        DoctorRoomAssignmentsEntity entity = new DoctorRoomAssignmentsEntity();
        entity.setDoctorProfileId(request.getDoctorProfileId());
        entity.setHospitalId(request.getHospitalId());
        entity.setRoomId(request.getRoomId());
        entity.setSpecialtyId(request.getSpecialtyId());
        entity.setIsPrimary(request.getIsPrimary());
        entity.setDaysOfWeek(request.getDaysOfWeek());
        entity.setShiftStart(request.getShiftStart());
        entity.setShiftEnd(request.getShiftEnd());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setNote(request.getNote());
        return entity;
    }

    public DoctorRoomAssignmentsEntity toEntity(DoctorRoomAssignmentsRequest request,
            DoctorRoomAssignmentsEntity entity) {
        if (request == null)
            return entity;

        if (request.getDoctorProfileId() != null) {
            entity.setDoctorProfileId(request.getDoctorProfileId());
        }
        if (request.getHospitalId() != null) {
            entity.setHospitalId(request.getHospitalId());
        }
        if (request.getRoomId() != null) {
            entity.setRoomId(request.getRoomId());
        }
        if (request.getSpecialtyId() != null) {
            entity.setSpecialtyId(request.getSpecialtyId());
        }
        if (request.getIsPrimary() != null) {
            entity.setIsPrimary(request.getIsPrimary());
        }
        if (request.getDaysOfWeek() != null) {
            entity.setDaysOfWeek(request.getDaysOfWeek());
        }
        if (request.getShiftStart() != null) {
            entity.setShiftStart(request.getShiftStart());
        }
        if (request.getShiftEnd() != null) {
            entity.setShiftEnd(request.getShiftEnd());
        }
        if (request.getStartDate() != null) {
            entity.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            entity.setEndDate(request.getEndDate());
        }
        if (request.getNote() != null) {
            entity.setNote(request.getNote());
        }
        return entity;
    }
}
