package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.LoginRequest;
import com.countryedu.empapi.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
