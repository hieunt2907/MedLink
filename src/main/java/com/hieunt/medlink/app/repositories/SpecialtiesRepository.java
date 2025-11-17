package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface SpecialtiesRepository extends BaseRepository<SpecialtiesEntity, Long> {
    boolean existsByName(String name);

    @Query(value = """
            select 
            """, nativeQuery = true)
    Page<SpecialtiesEntity> filterSpecialties(String keyword, Pageable pageable);
}
