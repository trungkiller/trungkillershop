package com.shop.trungkillershop.model.user;

import lombok.Data;

@Data
public class RegisterRequest {
    String username;
    String password;
    String email;
    String phoneNumber;
    String fullName;
    String address;
    String role;
    String confirmPassword;
    String userType;
}
