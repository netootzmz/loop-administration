package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

@Data
public class ClientConfigurationDto {

  private String client_id;
  private Integer service_id;
  private Integer clientConfigId;
  private Integer serviceCharactId;
  private Integer providerCharactId;
  private Integer type_notification_id;

}
