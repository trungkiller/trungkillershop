package com.shop.trungkillershop.dao.common;

import com.shop.trungkillershop.config.common.CommonJdbcTempConfig;
import com.shop.trungkillershop.controller.common.ProductCommonController;
import com.shop.trungkillershop.model.common.ProductModel;
import com.shop.trungkillershop.service.ProductService;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductDAO implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    @Autowired
    CommonJdbcTempConfig commonJdbcTemplate;

    @Override
    public List<Map<String, Object>> getAllProducts(String name) {
        Map<String, Object> params = new HashMap<>();

        params.put("p_name", name);

        SqlParameter[] paramDefs = new SqlParameter[] {
                new SqlParameter("p_name", Types.VARCHAR),
                new SqlOutParameter("data", OracleTypes.CURSOR, new ColumnMapRowMapper())
        };

        Map<String, Object> result = commonJdbcTemplate.callProcedure(
                "pkg_products",
                "get_all_products",
                paramDefs,
                params
        );

        List<Map<String, Object>> products = (List<Map<String, Object>>) result.get("data");

        logger.info("func: {} response: {}", "getAllProducts", products);

        return products;
    }
}