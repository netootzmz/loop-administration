package com.smart.ecommerce.administration.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamGetMailPaymentDTO {
  @JsonProperty(value = "client_id", required = false)
  @ApiModelProperty(name = "client_id", required = false)
  private String clientId;
  
  @JsonProperty(value = "user_id", required = false)
  @ApiModelProperty(name = "user_id", required = false)
  private Integer userId;
  
  @JsonProperty(value = "cve", required = false)
  @ApiModelProperty(name = "cve", required = false)
  private String cve;
  
}
