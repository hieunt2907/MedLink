package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.errors.ResourceNotFoundException;
import com.hieunt.medlink.app.mappers.SpecialtiesMapper;
import com.hieunt.medlink.app.repositories.SpecialtiesRepository;
import com.hieunt.medlink.app.requests.hospital.SpecialtyRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtiesServiceImpl implements SpecialtiesService {
    private final SpecialtiesRepository specialtiesRepository;
    private final SpecialtiesMapper specialtiesMapper;

    @Override
    public BaseResponse<SpecialtiesEntity> createSpecialty(SpecialtyRequest specialty) {
        SpecialtiesEntity specialtiesEntity = specialtiesMapper.toEntity(specialty);
        if (specialtiesEntity != null) {
            specialtiesRepository.save(specialtiesEntity);
        }

        return new BaseResponse<>("creating specialty successfully", specialtiesEntity);
    }

    @Override
    public BaseResponse<SpecialtiesEntity> updateSpecialty(Long id, SpecialtyRequest specialty) {
        SpecialtiesEntity specialtiesEntity = getSpecialtyById(id).getData();

        if (specialtiesEntity != null) {
            specialtiesMapper.toEntity(specialty, specialtiesEntity);
            specialtiesRepository.save(specialtiesEntity);
            return new BaseResponse<>("updating specialty successfully", specialtiesEntity);
        }

        throw new ResourceNotFoundException("Specialty not found");
    }

    @Override
    public BaseResponse<SpecialtiesEntity> getSpecialtyById(Long id) {
        SpecialtiesEntity specialtiesEntity = specialtiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        return new BaseResponse<>("getting specialty successfully", specialtiesEntity);
    }

    @Override
    public BaseResponse<SpecialtiesEntity> deleteSpecialty(Long id) {
        SpecialtiesEntity specialtiesEntity = getSpecialtyById(id).getData();

        if (specialtiesEntity != null) {
            specialtiesRepository.delete(specialtiesEntity);
            return new BaseResponse<>("deleting specialty successfully", specialtiesEntity);
        }

        throw new ResourceNotFoundException("Specialty not found");
    }

    @Override
    public BaseResponse<Page<SpecialtiesEntity>> filterSpecialties(Long hospitalId, Pageable pageable) {
        Page<SpecialtiesEntity> specialtiesEntities = specialtiesRepository.filterSpecialties(hospitalId, pageable);
        return new BaseResponse<>("filtering specialties successfully", specialtiesEntities);
    }
}
