package com.shop.trungkillershop.model.baseresponse;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

@Data
public class BaseResponse<T> {
    String repsonseCode;
    String responseMessage;
    T data;
}
