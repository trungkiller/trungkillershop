package com.shop.trungkillershop.utils.jwt;

import com.shop.trungkillershop.config.security.CustomUserDetails;
import com.shop.trungkillershop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, Object> user = userService.login(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (user.get("ecode") != null) {
            throw new UsernameNotFoundException("Username invalid: " + username);
        }

        return new CustomUserDetails(user.get("username").toString(), user.get("password").toString(), user.get("roleId").toString(), user.get("userType").toString());
    }
}