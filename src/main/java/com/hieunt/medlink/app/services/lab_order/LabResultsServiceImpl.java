package com.hieunt.medlink.app.services.lab_order;

import com.hieunt.medlink.app.entities.LabResultsEntity;
import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.errors.ResourceNotFoundException;
import com.hieunt.medlink.app.mappers.LabResultsMapper;
import com.hieunt.medlink.app.repositories.LabResultsRepository;
import com.hieunt.medlink.app.requests.lab_order.LabResultsRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.utils.GetCurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabResultsServiceImpl implements LabResultsService {
    private final LabResultsRepository labResultsRepository;
    private final LabResultsMapper labResultsMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public BaseResponse<LabResultsEntity> createLabResult(LabResultsRequest request) {
        LabResultsEntity entity = labResultsMapper.toEntity(request);

        if (entity != null) {
            // Set performedBy to current user if not provided
            if (entity.getPerformedBy() == null) {
                UserEntity currentUser = getCurrentUser.getCurrentUser();
                entity.setPerformedBy(currentUser.getId());
            }
            labResultsRepository.save(entity);
        }

        return new BaseResponse<>("Creating lab result successfully", entity);
    }

    @Override
    public BaseResponse<LabResultsEntity> updateLabResult(Long id, LabResultsRequest request) {
        LabResultsEntity entity = getLabResultById(id).getData();

        if (entity != null) {
            labResultsMapper.toEntity(request, entity);
            labResultsRepository.save(entity);
            return new BaseResponse<>("Updating lab result successfully", entity);
        }

        throw new ResourceNotFoundException("Lab result not found with id: " + id);
    }
    

    @Override
    public BaseResponse<LabResultsEntity> getLabResultById(Long id) {
        LabResultsEntity entity = labResultsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabResult", "id", id));
        return new BaseResponse<>("Getting lab result successfully", entity);
    }

    @Override
    public BaseResponse<LabResultsEntity> deleteLabResult(Long id) {
        LabResultsEntity entity = getLabResultById(id).getData();

        if (entity != null) {
            labResultsRepository.delete(entity);
            return new BaseResponse<>("Deleting lab result successfully", entity);
        }

        throw new ResourceNotFoundException("Lab result not found with id: " + id);
    }


    @Override
    public BaseResponse<Page<LabResultsEntity>> getMyLabResults(Pageable pageable) {
        UserEntity user = getCurrentUser.getCurrentUser();
        Page<LabResultsEntity> labResults = labResultsRepository.findByPerformedBy(user.getId(), pageable);
        return new BaseResponse<>("Getting my lab results successfully", labResults);
    }
}
