package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.SpecialtiesEntity;
import com.hieunt.medlink.app.mappers.SpecialtiesMapper;
import com.hieunt.medlink.app.repositories.SpecialtiesRepository;
import com.hieunt.medlink.app.requests.hospital.SpecialtyRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtiesServiceImpl implements SpecialtiesService{
    private final SpecialtiesRepository specialtiesRepository;
    private final SpecialtiesMapper specialtiesMapper;

    @Override
    public BaseResponse<SpecialtiesEntity> createSpecialty(SpecialtyRequest specialty) {
        try {
            SpecialtiesEntity specialtiesEntity = specialtiesMapper.toEntity(specialty);
            specialtiesRepository.save(specialtiesEntity);
            return new BaseResponse<>("creating specialty successfully", specialtiesEntity);
        } catch (Exception e) {
            throw new RuntimeException("error creating specialty: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<SpecialtiesEntity> updateSpecialty(Long id, SpecialtyRequest specialty) {
        try {
            SpecialtiesEntity specialtiesEntity = getSpecialtyById(id).getData();
            specialtiesMapper.toEntity(specialty, specialtiesEntity);
            specialtiesRepository.save(specialtiesEntity);
            return new BaseResponse<>("updating specialty successfully", specialtiesEntity);
        } catch (Exception e) {
            throw new RuntimeException("error updating specialty: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<SpecialtiesEntity> getSpecialtyById(Long id) {
        try {
            SpecialtiesEntity specialtiesEntity = specialtiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Specialty with id " + id + " not found"));
            return new BaseResponse<>("getting specialty successfully", specialtiesEntity);
        } catch (Exception e) {
            throw new RuntimeException("error getting specialty: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<SpecialtiesEntity> deleteSpecialty(Long id) {
        try {
            SpecialtiesEntity specialtiesEntity = getSpecialtyById(id).getData();
            specialtiesRepository.delete(specialtiesEntity);
            return new BaseResponse<>("deleting specialty successfully", specialtiesEntity);
        } catch (Exception e) {
            throw new RuntimeException("error deleting specialty: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<SpecialtiesEntity> filterSpecialties(String keyword, Pageable pageable) {
        return null;
    }
}
