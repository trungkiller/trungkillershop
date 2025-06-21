package com.shop.trungkillershop.config.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonJdbcTempConfig {
    private final JdbcTemplate jdbcTemplate;

    public CommonJdbcTempConfig(@Qualifier("commonJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> callProcedure(
            String packageName,
            String procedureName,
            SqlParameter[] parameters,
            Map<String, Object> paramValues
    ) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName(packageName)
                    .withProcedureName(procedureName)
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(parameters);

            return jdbcCall.execute(paramValues);
        } catch (Exception e) {
            throw new RuntimeException("Error calling procedure " + procedureName + ": " + e.getMessage(), e);
        }
    }
}
