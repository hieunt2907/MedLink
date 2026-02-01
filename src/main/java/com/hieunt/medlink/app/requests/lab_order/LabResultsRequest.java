package com.hieunt.medlink.app.requests.lab_order;

import java.time.OffsetDateTime;

import com.hieunt.medlink.app.entities.LabResultsEntity.LabResultStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultsRequest {

    private Long labOrderId;

    private String testName;

    private String resultValue;

    private String referenceRange;

    private String unit;

    private LabResultStatus status;

    private String notes;

    private Long performedBy;

    private OffsetDateTime performedAt;
}
