package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends BaseRepository<RoomEntity, Long> {
    boolean existsByRoomNumber(String roomNumber);

    @Query(value = """
            select
                r.id,
                r.room_number,
                r.hospital_id,
                r.room_type,
                r.specialty_id,
                r.capacity,
                r.status
            from rooms r
            where r.specialty_id = :specialtyId
            """, nativeQuery = true)
    Page<RoomEntity> filterRooms(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
