package com.hieunt.medlink.app.services.lab_order;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.requests.lab_order.LabOrdersRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.lab_order.LabOrdersResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabOrdersService {

    BaseResponse<LabOrdersEntity> createLabOrder(LabOrdersRequest request);

    BaseResponse<LabOrdersEntity> updateLabOrder(Long id, LabOrdersRequest request);

    BaseResponse<LabOrdersEntity> getLabOrderById(Long id);

    BaseResponse<LabOrdersEntity> getLabOrderByOrderNumber(String orderNumber);

    BaseResponse<LabOrdersEntity> deleteLabOrder(Long id);

    BaseResponse<Page<LabOrdersEntity>> getMyDoctorLabOrders(Pageable pageable);

    BaseResponse<Page<LabOrdersResponse>> filterLabOrders(
            Long medicalRecordId,
            Long doctorId,
            Long labDoctorId,
            Long specialtyId,
            String status,
            String urgency,
            String keyword,
            Pageable pageable);
}
