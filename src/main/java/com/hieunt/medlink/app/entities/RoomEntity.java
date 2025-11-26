package com.hieunt.medlink.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "status")
    private RoomStatus status;

    public enum RoomType {
        examination, laboratory, surgery, ward
    }

    public enum RoomStatus {
        available, occupied, maintenance
    }
}
