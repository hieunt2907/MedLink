package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.mappers.HospitalMapper;
import com.hieunt.medlink.app.repositories.HospitalRepository;
import com.hieunt.medlink.app.requests.hospital.HospitalRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.pkg.error.DuplicateResourceException;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
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
        validateHospitalNotExists(request.getName());

        HospitalEntity hospital = hospitalMapper.toEntity(request);
        if (hospital != null) {
            hospitalRepository.save(hospital);
        }

        return new BaseResponse<>("creating hospital successfully", hospital);
    }

    @Override
    public BaseResponse<HospitalEntity> updateHospital(Long id, HospitalRequest request) {
        HospitalEntity hospital = getHospital(id).getData();

        // Kiểm tra duplicate name nếu name thay đổi
        if (!hospital.getName().equals(request.getName())) {
            validateHospitalNotExists(request.getName());
        }

        hospitalMapper.toEntity(request, hospital);
        if (hospital != null) {
            hospitalRepository.save(hospital);
        }

        return new BaseResponse<>("updating hospital successfully", hospital);
    }

    @Override
    public BaseResponse<HospitalEntity> deleteHospital(Long id) {
        HospitalEntity hospital = getHospital(id).getData();
        if (hospital != null) {
            hospitalRepository.delete(hospital);
        }

        return new BaseResponse<>("deleting hospital successfully", hospital);
    }

    @Override
    public BaseResponse<HospitalEntity> getHospital(Long id) {
        HospitalEntity hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        return new BaseResponse<>("getting hospital successfully", hospital);
    }

    @Override
    public BaseResponse<Page<HospitalEntity>> filterHospitals(String keyword, Pageable pageable) {
        Page<HospitalEntity> hospitals = hospitalRepository.filterHospitals(keyword, pageable);
        return new BaseResponse<>("filtering hospitals successfully", hospitals);
    }

    /**
     * Validate hospital không tồn tại với name đã cho
     * Throw DuplicateResourceException nếu đã tồn tại
     */
    private void validateHospitalNotExists(String name) {
        if (hospitalRepository.existsByName(name)) {
            throw new DuplicateResourceException("Hospital", "name", name);
        }
    }
}
