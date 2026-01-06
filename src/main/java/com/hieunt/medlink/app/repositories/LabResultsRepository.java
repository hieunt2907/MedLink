package com.hieunt.medlink.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hieunt.medlink.app.entities.LabResultsEntity;

public interface LabResultsRepository extends BaseRepository<LabResultsEntity, Long> {

    Page<LabResultsEntity> findByLabOrderId(Long labOrderId, Pageable pageable);

    Page<LabResultsEntity> findByPerformedBy(Long performedBy, Pageable pageable);
}
