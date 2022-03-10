package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

@Data
public class ConfigurationScreenDto {
    private String apiKey;
    private Integer validityDefault;
    private Integer editValidity;
    private Integer paymentScreenBehaviorId;
    private String sendWay;
    private Integer numbersPatterns;
}
