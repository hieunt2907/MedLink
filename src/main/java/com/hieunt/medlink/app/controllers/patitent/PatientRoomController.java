package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.RoomEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.hospital.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient/hospital/rooms")
@RequiredArgsConstructor
public class PatientRoomController {
    private final RoomService roomService;

    @GetMapping("/{specialtyId}")
    public ResponseEntity<BaseResponse<Page<RoomEntity>>> filterRooms(@PathVariable Long specialtyId,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<RoomEntity>> rooms = roomService.filterRooms(specialtyId, pageable);
        return ResponseEntity.ok(rooms);
    }
}
