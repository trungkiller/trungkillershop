package com.shop.trungkillershop.controller.user;

import com.shop.trungkillershop.config.security.CustomUserDetails;
import com.shop.trungkillershop.controller.common.ProductCommonController;
import com.shop.trungkillershop.dao.user.UserDAO;
import com.shop.trungkillershop.model.baseresponse.BaseResponse;
import com.shop.trungkillershop.model.user.RegisterRequest;
import com.shop.trungkillershop.service.UserService;
import com.shop.trungkillershop.utils.jwt.JwtUtil;
import com.shop.trungkillershop.utils.jwt.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommonController.class);

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailService userDetailsService;

    @PostMapping("/regist")
    public BaseResponse<Map<String, Object>> registUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("api: {} param: {}", "registUser", registerRequest);
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        Map<String, Object> data = userService.createUser(registerRequest);

        if (data == null) {
            response.setRepsonseCode("400");
            response.setResponseMessage("Regist user failed");
            response.setData(null);
        } else {
            response.setRepsonseCode(data.get("ecode").toString());
            response.setResponseMessage(data.get("emsg").toString());
            response.setData(null);
        }

        logger.info("api: {} response: {}", "registUser", response);

        return response;
    }

    @PostMapping("/login")
    public BaseResponse<Map<String, Object>> login(@RequestBody RegisterRequest authenticationRequest) {
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();

        try {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                );
            } catch (BadCredentialsException e) {
                logger.error("Login failed for user: {}", authenticationRequest.getUsername(), e);
                response.setRepsonseCode("401");
                response.setResponseMessage("Invalid username or password");
                return response;
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String accessToken = jwtUtil.generateToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            Map<String, Object> userInfo = new HashMap<>();

            userInfo.put("accessToken", accessToken);
            userInfo.put("refreshToken", refreshToken);

            if (userDetails instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                userInfo.put("role", customUserDetails.getRole());
                userInfo.put("userType", customUserDetails.getUserType());
            }

            response.setRepsonseCode("200");
            response.setResponseMessage("Login successful");
            response.setData(userInfo);

            return response;
        } catch (Exception e) {
            logger.error("Error logging in user: {}", authenticationRequest.getUsername(), e);
            response.setRepsonseCode("500");
            response.setResponseMessage("Internal server error");
            return response;
        }
    }

    @PostMapping("/refresh-token")
    public BaseResponse<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();

        try {
            if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
                throw new Exception("Refresh Token is missing or invalid");
            }

            String refreshToken = refreshTokenHeader.substring(7);
            String username = jwtUtil.extractUsername(refreshToken);

            if (username != null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(refreshToken, userDetails)) {
                    final String newAccessToken = jwtUtil.generateToken(userDetails);

                    Map<String, Object> userInfo = new HashMap<>();

                    userInfo.put("accessToken", newAccessToken);
                    userInfo.put("refreshToken", refreshToken);

                    if (userDetails instanceof CustomUserDetails) {
                        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                        userInfo.put("role", customUserDetails.getRole());
                        userInfo.put("userType", customUserDetails.getUserType());
                    }

                    response.setRepsonseCode("200");
                    response.setResponseMessage("Refresh token successful");
                    response.setData(userInfo);

                    return response;
                } else {
                    throw new Exception("Invalid Refresh Token (token validation failed)");
                }
            } else {
                throw new Exception("Invalid Refresh Token (username not found in token)");
            }
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage(), e);
            response.setRepsonseCode("500");
            response.setResponseMessage("Internal server error");
            return response;
        }
    }
}
