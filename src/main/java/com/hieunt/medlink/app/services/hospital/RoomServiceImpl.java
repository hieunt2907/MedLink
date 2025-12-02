package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.mappers.RoomMapper;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hieunt.medlink.app.entities.RoomEntity;
import com.hieunt.medlink.app.repositories.RoomRepository;
import com.hieunt.medlink.app.requests.hospital.RoomRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public BaseResponse<RoomEntity> createRoom(Long hospitalId, RoomRequest request) {
        RoomEntity roomEntity = roomMapper.toEntity(request);
        if (roomEntity != null) {
            roomRepository.save(roomEntity);
        }
        return new BaseResponse<>("creating room successfully", roomEntity);
    }

    @Override
    public BaseResponse<RoomEntity> deleteRoom(Long id) {
        RoomEntity roomEntity = getRoom(id).getData();
        if (roomEntity != null) {
            roomRepository.delete(roomEntity);
            return new BaseResponse<>("deleting room successfully", roomEntity);
        }
        throw new ResourceNotFoundException("Room not found");
    }

    @Override
    public BaseResponse<Page<RoomEntity>> filterRooms(Long hospitalId, Pageable pageable) {
        Page<RoomEntity> rooms = roomRepository.filterRooms(hospitalId, pageable);
        return new BaseResponse<>("filtering rooms successfully", rooms);
    }

    @Override
    public BaseResponse<RoomEntity> getRoom(Long id) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return new BaseResponse<>("getting room successfully", roomEntity);
    }

    @Override
    public BaseResponse<RoomEntity> updateRoom(Long id, RoomRequest request) {
        RoomEntity roomEntity = getRoom(id).getData();
        if (roomEntity != null) {
            roomMapper.toEntity(request, roomEntity);
            roomRepository.save(roomEntity);
            return new BaseResponse<>("updating room successfully", roomEntity);
        }
        throw new ResourceNotFoundException("Room not found");
    }

}
