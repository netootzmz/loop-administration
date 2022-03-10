package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

@Data
public class ServiceCharcteristicDto {

  private Integer service_characteristic_id;
  private Integer service_id;
  private Integer characteristic_id;
  private Integer status_id;
}
