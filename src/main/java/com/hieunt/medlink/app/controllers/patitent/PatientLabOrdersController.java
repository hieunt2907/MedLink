package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.entities.LabOrdersEntity;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.services.lab_order.LabOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/lab-orders")
@RequiredArgsConstructor
public class PatientLabOrdersController {
    private final LabOrdersService labOrdersService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> getLabOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(labOrdersService.getLabOrderById(id));
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<BaseResponse<LabOrdersEntity>> getLabOrderByOrderNumber(
            @PathVariable String orderNumber) {
        return ResponseEntity.ok(labOrdersService.getLabOrderByOrderNumber(orderNumber));
    }

}
