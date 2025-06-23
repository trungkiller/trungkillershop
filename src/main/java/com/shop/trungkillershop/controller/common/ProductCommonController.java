package com.shop.trungkillershop.controller.common;

import com.shop.trungkillershop.model.baseresponse.BaseResponse;
import com.shop.trungkillershop.service.ProductService;
import com.shop.trungkillershop.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class ProductCommonController {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommonController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public BaseResponse<List<Map<String, Object>>> createProduct() {
        logger.info("api: {} ", "createProduct");
        BaseResponse<List<Map<String, Object>>> response = new BaseResponse<>();
        List<Map<String, Object>> data = productService.getAllProducts();
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

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(Constant.UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
