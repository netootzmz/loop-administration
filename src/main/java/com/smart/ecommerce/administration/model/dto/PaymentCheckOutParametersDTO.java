package com.smart.ecommerce.administration.model.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentCheckOutParametersDTO {

  private Integer blockingBehaviorId;

  private String blockingBehaviorName;

  private Integer haveMsi;

  private Integer allowMsi;

  private Integer msiId;

  private String msiName;

  private Integer monthsAvailable;

  private BigDecimal amountPucharse;

  private Integer validityValue;

  private Integer haveValidityDefault;

  private Integer cardHolderDataRequest;

  private Integer allowEdition;

  private Integer validityUnity;

  private Integer configClientProductId;
  
}
