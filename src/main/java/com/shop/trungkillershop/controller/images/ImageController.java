package com.shop.trungkillershop.controller.images;

import com.shop.trungkillershop.config.security.CustomUserDetails;
import com.shop.trungkillershop.service.ProductService;
import com.shop.trungkillershop.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product/{id}/upload")
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            File uploadDir = new File(Constant.UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(Constant.UPLOAD_DIR + fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            Map<String, Object> uploadImg = productService.uploadImage(String.valueOf(id), fileName, userDetails.getUsername());

            if (uploadImg == null || uploadImg.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
            } else if (uploadImg.get("ecode") != null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(uploadImg.get("emsg"));
            }

            // Trả về URL truy cập
            String fileUrl = "/images/" + fileName;
            return ResponseEntity.ok().body(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}
