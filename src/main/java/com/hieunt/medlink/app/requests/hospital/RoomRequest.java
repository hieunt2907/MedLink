package com.hieunt.medlink.app.requests.hospital;

import com.hieunt.medlink.app.entities.RoomEntity.RoomStatus;
import com.hieunt.medlink.app.entities.RoomEntity.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private RoomType roomType;
    private Long specialtyId;
    private Integer capacity;
    private RoomStatus status;
}
