package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface SpecialtiesService {
    BaseResponse<SpecialtiesEntity> createSpecialty(SpecialtiesEntity specialty);
    BaseResponse<SpecialtiesEntity> updateSpecialty(Long id, SpecialtiesEntity specialty);
    BaseResponse<SpecialtiesEntity> getSpecialtyById(Long id);
    BaseResponse<SpecialtiesEntity> deleteSpecialty(Long id);
    BaseResponse<SpecialtiesEntity> filterSpecialties(String keyword, Pageable pageable);
}
