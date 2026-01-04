package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.LabResultsEntity;
import com.hieunt.medlink.app.requests.lab_order.LabResultsRequest;
import org.springframework.stereotype.Component;

@Component
public class LabResultsMapper {

    public LabResultsEntity toEntity(LabResultsRequest request) {
        if (request == null) {
            return null;
        }

        LabResultsEntity entity = new LabResultsEntity();
        entity.setLabOrderId(request.getLabOrderId());
        entity.setTestName(request.getTestName());
        entity.setResultValue(request.getResultValue());
        entity.setReferenceRange(request.getReferenceRange());
        entity.setUnit(request.getUnit());
        entity.setStatus(request.getStatus());
        entity.setNotes(request.getNotes());
        entity.setPerformedBy(request.getPerformedBy());
        return entity;
    }

    public LabResultsEntity toEntity(LabResultsRequest request, LabResultsEntity entity) {
        if (request == null) {
            return entity;
        }

        if (request.getLabOrderId() != null) {
            entity.setLabOrderId(request.getLabOrderId());
        }

        if (request.getTestName() != null) {
            entity.setTestName(request.getTestName());
        }

        if (request.getResultValue() != null) {
            entity.setResultValue(request.getResultValue());
        }

        if (request.getReferenceRange() != null) {
            entity.setReferenceRange(request.getReferenceRange());
        }

        if (request.getUnit() != null) {
            entity.setUnit(request.getUnit());
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }

        if (request.getPerformedBy() != null) {
            entity.setPerformedBy(request.getPerformedBy());
        }


        return entity;
    }
}
