package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.requests.hospital.SpecialtyRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecialtiesService {
    BaseResponse<SpecialtiesEntity> createSpecialty(SpecialtyRequest specialty);

    BaseResponse<SpecialtiesEntity> updateSpecialty(Long id, SpecialtyRequest specialty);

    BaseResponse<SpecialtiesEntity> getSpecialtyById(Long id);

    BaseResponse<SpecialtiesEntity> deleteSpecialty(Long id);

    BaseResponse<Page<SpecialtiesEntity>> filterSpecialties(Long hospitalId, Pageable pageable);
}
