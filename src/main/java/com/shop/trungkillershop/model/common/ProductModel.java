package com.shop.trungkillershop.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class ProductModel {
    String productId;
    String description;
    String name;

    @JsonIgnore
    String ecode;

    @JsonIgnore
    String emsg;
}
