package com.hieunt.medlink.app.controllers.admin;

import com.hieunt.medlink.app.entities.RoomEntity;
import com.hieunt.medlink.app.requests.hospital.RoomRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/hospital/rooms")
@RequiredArgsConstructor
public class AdminRoomController {
    private final RoomService roomService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<RoomEntity>> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(null, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<RoomEntity>> updateRoom(@PathVariable Long id,
            @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<RoomEntity>> deleteRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RoomEntity>> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @GetMapping("/{specialtyId}")
    public ResponseEntity<BaseResponse<Page<RoomEntity>>> filterRooms(@PathVariable Long specialtyId,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<RoomEntity>> rooms = roomService.filterRooms(specialtyId, pageable);
        return ResponseEntity.ok(rooms);
    }
}
