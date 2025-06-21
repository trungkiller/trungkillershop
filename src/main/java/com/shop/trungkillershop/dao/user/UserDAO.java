package com.shop.trungkillershop.dao.user;

import com.shop.trungkillershop.config.common.CommonJdbcTempConfig;
import com.shop.trungkillershop.dao.common.ProductDAO;
import com.shop.trungkillershop.model.user.RegisterRequest;
import com.shop.trungkillershop.service.UserService;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDAO implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    @Autowired
    CommonJdbcTempConfig commonJdbcTemplate;

    @Override
    public Map<String, Object> createUser(RegisterRequest registerRequest) {
        Map<String, Object> params = new HashMap<>();

        try {
            String hashed = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(12));

            params.put("p_user_name", registerRequest.getUsername());
            params.put("p_password", hashed);
            params.put("p_email", registerRequest.getEmail());
            params.put("p_phone", registerRequest.getPhoneNumber());
            params.put("p_full_name", registerRequest.getFullName());
            params.put("p_role", registerRequest.getRole());
            params.put("p_user_type", registerRequest.getUserType());

            SqlParameter[] paramDefs = new SqlParameter[] {
                    new SqlParameter("p_user_name", Types.VARCHAR),
                    new SqlParameter("p_password", Types.VARCHAR),
                    new SqlParameter("p_email", Types.VARCHAR),
                    new SqlParameter("p_phone", Types.VARCHAR),
                    new SqlParameter("p_full_name", Types.VARCHAR),
                    new SqlParameter("p_role", Types.VARCHAR),
                    new SqlParameter("p_user_type", Types.VARCHAR),
                    new SqlOutParameter("data", OracleTypes.CURSOR, new ColumnMapRowMapper())
            };

            Map<String, Object> result = commonJdbcTemplate.callProcedure(
                    "pkg_users",
                    "create_user",
                    paramDefs,
                    params
            );

            List<Map<String, Object>> users = (List<Map<String, Object>>) result.get("data");

            logger.info("func: {} response: {}", "getAllProducts", users);

            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> login(String username) {
        Map<String, Object> params = new HashMap<>();

        try {

            params.put("p_user_name", username);

            SqlParameter[] paramDefs = new SqlParameter[] {
                    new SqlParameter("p_user_name", Types.VARCHAR),
                    new SqlOutParameter("data", OracleTypes.CURSOR, new ColumnMapRowMapper())
            };

            Map<String, Object> result = commonJdbcTemplate.callProcedure(
                    "pkg_users",
                    "login",
                    paramDefs,
                    params
            );

            List<Map<String, Object>> users = (List<Map<String, Object>>) result.get("data");

            logger.info("func: {} response: {}", "login", users);

            if (users.isEmpty()) {
                return null;
            }

            Map<String, Object> user = users.get(0);
//            String hashed = (String) user.get("password");
//            boolean matched = BCrypt.checkpw(registerRequest.getPassword(), hashed);
//
//            if (!matched) {
//                return null; // Password does not match
//            }

            return user; // Return user details if password matches

        } catch (Exception e) {
            logger.error("Error login: {}", e.getMessage());
            return null;
        }
    }
}
