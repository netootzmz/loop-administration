package com.smart.ecommerce.administration.model.dto;

import org.apache.commons.codec.binary.Base64;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TemplateClientDTO {
  private Integer templateClientId;
  private Integer typeOwnerId;
  private String  templateStr;
  private Base64  template;
  private String  description;
  private String  clientId;
  private String  cve;
  private Integer statusId;
  private Integer userId;
}

