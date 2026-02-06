package com.hieunt.medlink.app.services.lab_order;

import com.hieunt.medlink.app.entities.LabResultsEntity;
import com.hieunt.medlink.app.requests.lab_order.LabResultsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabResultsService {

    BaseResponse<LabResultsEntity> createLabResult(LabResultsRequest request);

    BaseResponse<LabResultsEntity> updateLabResult(Long id, LabResultsRequest request);

    BaseResponse<LabResultsEntity> getLabResultById(Long id);

    BaseResponse<LabResultsEntity> deleteLabResult(Long id);

    BaseResponse<Page<LabResultsEntity>> getMyLabResults(Pageable pageable);
}
