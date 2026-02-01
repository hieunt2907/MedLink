package com.hieunt.medlink.app.services.auth;

import com.hieunt.medlink.app.requests.auth.LoginRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.auth.LoginResponse;

public interface AuthService {
    BaseResponse<LoginResponse> login(LoginRequest request);
}
