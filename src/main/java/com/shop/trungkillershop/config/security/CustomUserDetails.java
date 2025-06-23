package com.shop.trungkillershop.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // Import lớp User của Spring Security

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails extends User {

    private String role;     // Thêm thuộc tính role
    private String userType; // Thêm thuộc tính userType
    private String email; // Thêm thuộc tính userType
    private String fullName; // Thêm thuộc tính userType
    // Bạn có thể thêm các trường thông tin khác mà bạn muốn đưa vào JWT và UserDetails

    /**
     * Constructor cho CustomUserDetails.
     *
     * @param username Tên người dùng.
     * @param password Mật khẩu đã được mã hóa.
     * @param role Vai trò của người dùng (ví dụ: "ADMIN", "USER").
     * @param userType Loại người dùng (ví dụ: "INDIVIDUAL", "BUSINESS").
     */
    public CustomUserDetails(String username, String password, String role, String userType, String email, String fullName) {
        // Gọi constructor của lớp cha (org.springframework.security.core.userdetails.User)
        // Spring Security yêu cầu GrantedAuthority phải có tiền tố "ROLE_" để nhận diện vai trò.
        super(username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)));
        this.role = role;
        this.userType = userType;
        this.email = email;
        this.fullName = fullName;
    }

    // --- Getters cho các thuộc tính tùy chỉnh ---
    public String getRole() {
        return role;
    }

    public String getUserType() {
        return userType;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }

    // --- Các phương thức của UserDetails (được kế thừa từ lớp User) ---
    // Bạn không cần phải ghi đè (override) hầu hết các phương thức này
    // trừ khi bạn có yêu cầu đặc biệt về tài khoản không hết hạn, không bị khóa, v.v.

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     // Nếu bạn có nhiều vai trò hoặc quyền hạn phức tạp, bạn có thể ghi đè phương thức này
    //     // để trả về một tập hợp các GrantedAuthority.
    //     // Ví dụ: return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role), new SimpleGrantedAuthority("PERMISSION_X"));
    //     return super.getAuthorities(); // Mặc định đã được xử lý trong constructor của User
    // }

    // @Override
    // public boolean isAccountNonExpired() {
    //     return true; // Mặc định là true, thay đổi nếu bạn có logic hết hạn tài khoản
    // }

    // @Override
    // public boolean isAccountNonLocked() {
    //     return true; // Mặc định là true, thay đổi nếu bạn có logic khóa tài khoản
    // }

    // @Override
    // public boolean isCredentialsNonExpired() {
    //     return true; // Mặc định là true, thay đổi nếu bạn có logic hết hạn thông tin xác thực
    // }

    // @Override
    // public boolean isEnabled() {
    //     return true; // Mặc định là true, thay đổi nếu bạn có logic kích hoạt/vô hiệu hóa tài khoản
    // }
}
