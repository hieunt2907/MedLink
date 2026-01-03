package com.hieunt.medlink.app.services.lab_order;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.LabOrdersMapper;
import com.hieunt.medlink.app.repositories.LabOrdersRepository;
import com.hieunt.medlink.app.requests.lab_order.LabOrdersRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.lab_order.LabOrdersResponse;
import com.hieunt.medlink.pkg.error.ResourceNotFoundException;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabOrdersServiceImpl implements LabOrdersService {
    private final LabOrdersRepository labOrdersRepository;
    private final LabOrdersMapper labOrdersMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<LabOrdersEntity> createLabOrder(LabOrdersRequest request) {
        if (request.getOrderNumber() != null
                && labOrdersRepository.existsByOrderNumber(request.getOrderNumber())) {
            throw new IllegalArgumentException(
                    "Lab order already exists with order number: " + request.getOrderNumber());
        }

        LabOrdersEntity entity = labOrdersMapper.toEntity(request);
        if (entity != null) {
            labOrdersRepository.save(entity);
        }
        return new BaseResponse<>("Creating lab order successfully", entity);
    }

    @Override
    public BaseResponse<LabOrdersEntity> updateLabOrder(Long id, LabOrdersRequest request) {
        LabOrdersEntity entity = getLabOrderById(id).getData();

        if (entity != null) {
            labOrdersMapper.toEntity(request, entity);
            labOrdersRepository.save(entity);
            return new BaseResponse<>("Updating lab order successfully", entity);
        }

        throw new ResourceNotFoundException("Lab order not found with id: " + id);
    }

    @Override
    public BaseResponse<LabOrdersEntity> getLabOrderById(Long id) {
        LabOrdersEntity entity = labOrdersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrder", "id", id));
        return new BaseResponse<>("Getting lab order successfully", entity);
    }

    @Override
    public BaseResponse<LabOrdersEntity> getLabOrderByOrderNumber(String orderNumber) {
        LabOrdersEntity entity = labOrdersRepository.findByOrderNumber(orderNumber);

        if (entity == null) {
            throw new ResourceNotFoundException(
                    "Lab order not found with order number: " + orderNumber);
        }

        return new BaseResponse<>("Getting lab order successfully", entity);
    }

    @Override
    public BaseResponse<LabOrdersEntity> deleteLabOrder(Long id) {
        LabOrdersEntity entity = getLabOrderById(id).getData();

        if (entity != null) {
            labOrdersRepository.delete(entity);
            return new BaseResponse<>("Deleting lab order successfully", entity);
        }

        throw new ResourceNotFoundException("Lab order not found with id: " + id);
    }

    @Override
    public BaseResponse<Page<LabOrdersEntity>> getLabOrdersByMedicalRecordId(Long medicalRecordId, Pageable pageable) {
        Page<LabOrdersEntity> labOrders = labOrdersRepository.findByMedicalRecordId(medicalRecordId, pageable);
        return new BaseResponse<>("Getting lab orders by medical record successfully", labOrders);
    }

    @Override
    public BaseResponse<Page<LabOrdersEntity>> getMyDoctorLabOrders(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        Page<LabOrdersEntity> labOrders = labOrdersRepository.findByDoctorId(user.getId(), pageable);
        return new BaseResponse<>("Getting doctor's lab orders successfully", labOrders);
    }

    @Override
    public BaseResponse<Page<LabOrdersEntity>> getMyLabDoctorLabOrders(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        Page<LabOrdersEntity> labOrders = labOrdersRepository.findByLabDoctorId(user.getId(), pageable);
        return new BaseResponse<>("Getting lab doctor's lab orders successfully", labOrders);
    }

    @Override
    public BaseResponse<Page<LabOrdersResponse>> filterLabOrders(
            Long medicalRecordId,
            Long doctorId,
            Long labDoctorId,
            Long specialtyId,
            String status,
            String urgency,
            String keyword,
            Pageable pageable) {
        Page<LabOrdersResponse> labOrders = labOrdersRepository.filterLabOrders(
                medicalRecordId,
                doctorId,
                labDoctorId,
                specialtyId,
                status,
                urgency,
                keyword,
                pageable);
        return new BaseResponse<>("Filtering lab orders successfully", labOrders);
    }
}
