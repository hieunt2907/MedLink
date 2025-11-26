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
                r.id          as id,
                r.room_number as roomNumber,
                r.room_type   as roomType,
                r.specialty_id as specialtyId,
                r.capacity    as capacity,
                r.status      as status
            from rooms r
            where r.specialty_id = :specialtyId
            """, nativeQuery = true)
    Page<RoomEntity> filterRooms(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
