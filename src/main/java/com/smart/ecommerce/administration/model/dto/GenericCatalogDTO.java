package com.smart.ecommerce.administration.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericCatalogDTO {
  
  @JsonProperty(value = "id", required = false)
  @ApiModelProperty(name = "id", required = false)
  private Integer id;
  
  @JsonProperty(value = "cve", required = false)
  @ApiModelProperty(name = "cve", required = false)
  private String cve;
  
  @JsonProperty(value = "name", required = false)
  @ApiModelProperty(name = "name", required = false)
  private String name;
  
  @JsonProperty(value = "description", required = false)
  @ApiModelProperty(name = "description", required = false)
  private String description;
  
}
