package com.smart.ecommerce.administration.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailPaymentBodyDTO {
  
  @JsonProperty(value = "client_id", required = false)
  @ApiModelProperty(name = "client_id", required = false)
  private String clientId;
  
  @JsonProperty(value = "user_id", required = false)
  @ApiModelProperty(name = "user_id", required = false)
  private Integer userId;
  
  @JsonProperty(value = "cve", required = false)
  @ApiModelProperty(name = "cve", required = false)
  private String cve;
  
  @JsonProperty(value = "message_header", required = false)
  @ApiModelProperty(name = "message_header", required = false)
  private String messageHeader;
  
  @JsonProperty(value = "line_one_title", required = false)
  @ApiModelProperty(name = "line_one_title", required = false)
  private String lineOneTitle;
  
  @JsonProperty(value = "line_two_title", required = false)
  @ApiModelProperty(name = "line_two_title", required = false)
  private String lineTwoTitle;
  
  @JsonProperty(value = "message_body", required = false)
  @ApiModelProperty(name = "message_body", required = false)
  private String messageBody;
  
  @JsonProperty(value = "message_footer", required = false)
  @ApiModelProperty(name = "message_footer", required = false)
  private String messageFooter;
  
  @JsonProperty(value = "phone_footer", required = false)
  @ApiModelProperty(name = "phone_footer", required = false)
  private String phoneFooter;
  
  @JsonProperty(value = "mail_footer", required = false)
  @ApiModelProperty(name = "mail_footer", required = false)
  private String mailFooter;
  
  @JsonProperty(value = "disclaimer_footer", required = false)
  @ApiModelProperty(name = "disclaimer_footer", required = false)
  private String disclaimerFooter;
  
}