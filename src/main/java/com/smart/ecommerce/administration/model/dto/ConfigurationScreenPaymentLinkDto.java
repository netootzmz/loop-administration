package com.smart.ecommerce.administration.model.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConfigurationScreenPaymentLinkDto {

  private String apiKey;

  private Integer validityDefault;

  private Integer editValidity;

  private Integer paymentScreenBehaviorId;

  private String sendWay;

  private Integer numbersPatterns;
  
  private Integer cardHolderDataRequest;
  
  private Integer validityValue;
  
  private Integer validityUnity;
  
  private Integer allowEdition;

  private Integer allowMsi;

  private Integer haveMsi;

  private List<MSIResponse> msi;
}
