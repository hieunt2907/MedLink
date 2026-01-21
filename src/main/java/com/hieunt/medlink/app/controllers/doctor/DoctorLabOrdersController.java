package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.requests.lab_order.LabOrdersRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.lab_order.LabOrdersResponse;
import com.hieunt.medlink.app.services.lab_order.LabOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor/lab-orders")
@RequiredArgsConstructor
public class DoctorLabOrdersController {
    private final LabOrdersService labOrdersService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> createLabOrder(
            @RequestBody LabOrdersRequest request) {
        return ResponseEntity.ok(labOrdersService.createLabOrder(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> updateLabOrder(
            @PathVariable Long id,
            @RequestBody LabOrdersRequest request) {
        return ResponseEntity.ok(labOrdersService.updateLabOrder(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> getLabOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(labOrdersService.getLabOrderById(id));
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> getLabOrderByOrderNumber(
            @PathVariable String orderNumber) {
        return ResponseEntity.ok(labOrdersService.getLabOrderByOrderNumber(orderNumber));
    }


    @GetMapping("/my-orders")
    public ResponseEntity<BaseResponse<Page<LabOrdersEntity>>> getMyDoctorLabOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(labOrdersService.getMyDoctorLabOrders(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<LabOrdersResponse>>> filterLabOrders(
            @RequestParam(required = false) Long medicalRecordId,
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<LabOrdersResponse>> response = labOrdersService.filterLabOrders(
                medicalRecordId, null, null, specialtyId, status, urgency, keyword, pageable);
        return ResponseEntity.ok(response);
    }
}
