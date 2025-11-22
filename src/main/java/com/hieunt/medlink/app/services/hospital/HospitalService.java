package com.hieunt.medlink.app.services.hospital;

import com.hieunt.medlink.app.entities.HospitalEntity;
import com.hieunt.medlink.app.requests.hospital.HospitalRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalService {
    BaseResponse<HospitalEntity> createHospital(HospitalRequest request);

    BaseResponse<HospitalEntity> updateHospital(Long id, HospitalRequest request);

    BaseResponse<HospitalEntity> deleteHospital(Long id);

    BaseResponse<HospitalEntity> getHospital(Long id);

    BaseResponse<Page<HospitalEntity>> filterHospitals(String keyword, Pageable pageable);
}
