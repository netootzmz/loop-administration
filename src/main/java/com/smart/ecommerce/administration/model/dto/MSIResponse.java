package com.smart.ecommerce.administration.model.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MSIResponse {

  private Integer msiId;

  private String msiName;

  private BigDecimal amountPucharse;

  private Integer monthsAvailable;

}
