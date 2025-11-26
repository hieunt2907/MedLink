package com.hieunt.medlink.app.services.hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hieunt.medlink.app.entities.RoomEntity;
import com.hieunt.medlink.app.requests.hospital.RoomRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

public interface RoomService {
    BaseResponse<RoomEntity> createRoom(Long hospitalId, RoomRequest request);

    BaseResponse<RoomEntity> updateRoom(Long id, RoomRequest request);

    BaseResponse<RoomEntity> deleteRoom(Long id);

    BaseResponse<RoomEntity> getRoom(Long id);

    BaseResponse<Page<RoomEntity>> filterRooms(Long hospitalId, Pageable pageable);
}
