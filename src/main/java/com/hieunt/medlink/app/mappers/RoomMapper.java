package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.RoomEntity;
import com.hieunt.medlink.app.requests.hospital.RoomRequest;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomEntity toEntity(RoomRequest request) {
        if (request == null)
            return null;

        RoomEntity entity = new RoomEntity();
        entity.setRoomNumber(request.getRoomNumber());
        entity.setRoomType(request.getRoomType());
        entity.setSpecialtyId(request.getSpecialtyId());
        entity.setCapacity(request.getCapacity());
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        return entity;
    }

    public RoomEntity toEntity(RoomRequest request, RoomEntity entity) {
        if (request == null)
            return entity;

        if (request.getRoomNumber() != null) {
            entity.setRoomNumber(request.getRoomNumber());
        }
        if (request.getRoomType() != null) {
            entity.setRoomType(request.getRoomType());
        }
        if (request.getSpecialtyId() != null) {
            entity.setSpecialtyId(request.getSpecialtyId());
        }
        if (request.getCapacity() != null) {
            entity.setCapacity(request.getCapacity());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        return entity;
    }
}
