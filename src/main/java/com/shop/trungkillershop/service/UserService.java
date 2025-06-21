package com.shop.trungkillershop.service;

import com.shop.trungkillershop.model.user.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface UserService {
    Map<String, Object> createUser(RegisterRequest registerRequest);
    Map<String, Object> login(String username);
}
