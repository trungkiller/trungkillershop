package com.shop.trungkillershop.service;

import com.shop.trungkillershop.model.common.ProductModel;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Map<String, Object>> getAllProducts(String name);
}
