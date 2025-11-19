package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialtiesRepository extends BaseRepository<SpecialtiesEntity, Long> {
    boolean existsByName(String name);

    @Query(value = """
            select
                s.id          as id,
                s.name        as name,
                s.description as description
            from specialties s
            where
                s.hospital_id = :hospitalId
                and s.status = 'active'
            """, nativeQuery = true)
    Page<SpecialtiesEntity> filterSpecialties(@Param("hospitalId") Long hospitalId, Pageable pageable);
}
