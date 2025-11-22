package com.hieunt.medlink.app.requests.hospital;

import com.hieunt.medlink.app.entities.HospitalEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRequest {
    private String name;
    private String address;
    private String phone;
    private String email;
    private HospitalEntity.HospitalStatus status;
}
