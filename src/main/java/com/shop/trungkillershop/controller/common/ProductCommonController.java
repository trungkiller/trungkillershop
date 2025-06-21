package com.shop.trungkillershop.controller.common;

import com.shop.trungkillershop.model.baseresponse.BaseResponse;
import com.shop.trungkillershop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductCommonController {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommonController.class);

    @Autowired
    ProductService productService;

    @PostMapping
    public BaseResponse<List<Map<String, Object>>> createProduct(@RequestParam String name) {
        logger.info("api: {} param: {}", "createProduct", name);
        BaseResponse<List<Map<String, Object>>> response = new BaseResponse<>();
        List<Map<String, Object>> data = productService.getAllProducts(name);
        if (data == null || data.isEmpty()) {
            response.setRepsonseCode("404");
            response.setResponseMessage("No products found");
        } else {
            if (data.get(0) != null && data.get(0).get("ecode")  != null) {
                response.setRepsonseCode(data.get(0).get("ecode").toString());
                response.setResponseMessage(data.get(0).get("emsg").toString());
                response.setData(null);
            } else {
                response.setRepsonseCode("200");
                response.setResponseMessage("Successfully");
                response.setData(data);
            }
        }

        logger.info("api: {} response: {}", "createProduct", response);

        return response;
    }
}
