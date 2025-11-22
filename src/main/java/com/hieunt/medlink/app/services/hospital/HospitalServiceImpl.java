package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.mappers.HospitalMapper;
import com.hieunt.medlink.app.repositories.HospitalRepository;
import com.hieunt.medlink.app.requests.hospital.HospitalRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    public BaseResponse<HospitalEntity> createHospital(HospitalRequest request) {
        try {
            validateHospital(request);
            HospitalEntity hospital = hospitalMapper.toEntity(request);
            hospitalRepository.save(hospital);
            return new BaseResponse<>("creating hospital successfully", hospital);
        } catch (Exception e) {
            throw new RuntimeException("error creating hospital: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<HospitalEntity> updateHospital(Long id, HospitalRequest request) {
        try {
            HospitalEntity hospital = getHospital(id).getData();
            validateHospital(request);
            hospitalMapper.toEntity(request, hospital);
            hospitalRepository.save(hospital);
            return new BaseResponse<>("updating hospital successfully", hospital);
        } catch (Exception e) {
            throw new RuntimeException("error updating hospital: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<HospitalEntity> deleteHospital(Long id) {
        try {
            HospitalEntity hospital = getHospital(id).getData();
            hospitalRepository.delete(hospital);
            return new BaseResponse<>("deleting hospital successfully", hospital);
        } catch (Exception e) {
            throw new RuntimeException("error deleting hospital: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<HospitalEntity> getHospital(Long id) {
        try {
            HospitalEntity hospital = hospitalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Hospital with id " + id + " not found"));
            return new BaseResponse<>("getting hospital successfully", hospital);
        } catch (Exception e) {
            throw new RuntimeException("error getting hospital: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<HospitalEntity>> filterHospitals(String keyword, Pageable pageable) {
        try {
            Page<HospitalEntity> hospitals = hospitalRepository.filterHospitals(keyword, pageable);
            return new BaseResponse<>("filtering hospitals successfully", hospitals);
        } catch (Exception e) {
            throw new RuntimeException("error filtering hospitals: " + e.getMessage(), e);
        }
    }

    private void validateHospital(HospitalRequest hospitalRequest) {
        if (hospitalRepository.existsByName(hospitalRequest.getName())) {
            throw new RuntimeException("Hospital with name " + hospitalRequest.getName() + " already exists");
        }
    }
}
