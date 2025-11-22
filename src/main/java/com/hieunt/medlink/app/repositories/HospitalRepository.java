package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.HospitalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HospitalRepository extends BaseRepository<HospitalEntity, Long> {
    boolean existsByName(String name);

    @Query(value = """
                select
                    h.id          as id,
                    h.name        as name,
                    h.address     as address,
                    h.phone_number as phoneNumber,
                    h.email       as email,
                    h.description as description
                from hospitals h
                where
                    h.name ilike concat('%', :keyword, '%')
                    or h.address ilike concat('%', :keyword, '%')
                    or h.phone ilike concat('%', :keyword, '%')
                    or h.email ilike concat('%', :keyword, '%')
                    or h.website ilike concat('%', :keyword, '%')
                    and h.status = 'active'
            """, nativeQuery = true)
    Page<HospitalEntity> filterHospitals(String keyword, Pageable pageable);
}
