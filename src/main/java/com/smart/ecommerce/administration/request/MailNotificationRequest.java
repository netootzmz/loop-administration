/*
 *  ---------------------------------------------------------------------------
 *  All rights reserved © 2020 Smart.
 *  This software contains information that is exclusive property of Smart,this
 *  information is considered confidential.
 *  It is strictly forbidden the copy or spreading of any part of this document
 *  in any format, whether mechanic or electronic.
 *  ---------------------------------------------------------------------------
 */

package com.smart.ecommerce.administration.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

/**
 * @author jefloresg
 * @version 1.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailNotificationRequest {

  @JsonProperty(value = "email-provider", required = true)
  @NotNull(message = "email-provider should not be null")
  @ApiModelProperty(name = "email-provider", required = true, position = 0,
    value = "Represents the email provider type.", example = "3")
  private Integer provider;

  @JsonProperty(value = "email-template", required = true)
  @PositiveOrZero(message = "email-template must be numeric")
  @ApiModelProperty(name = "email-template", required = true, position = 1,
    value = "Represents the identifier of the email message template",
    example = "0")
  private Integer templateId;

  @JsonProperty(value = "email-addressee", required = true)
  @Pattern(
    regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
    message = "The email address is invalid")
  @ApiModelProperty(name = "email-addressee", required = true, position = 2,
    value = "Represents the email address.", example = "example@anything.com")
  private String addressee;

  @JsonProperty(value = "email-subject", required = true)
  @NotNull(message = "email-subject should not be null")
  @ApiModelProperty(name = "email-subject", required = true, position = 3,
    value = "Represents the subject email.", example = "notice")
  private String subject;

  @JsonProperty(value = "email-message")
  @ApiModelProperty(name = "email-message", position = 4,
    value = "Representative of email message template.",
    example = "Hi!., welcome to SMART!..",
    notes = "The email message template should be Base64 encrypted.")
  private String message;

  @JsonProperty(value = "email-template-params")
  @ApiModelProperty(name = "email-template-params", position = 5,
    value = "Represents the parameters of the email message template.",
    example = " {\"name\": \"Jesús Flores\",\"liga\": "
      + "\"http:smart-payment/checkout/index2/CwfPKAtIWAqLyrTin0KlYqwGXMzYJVXX6uYjRy7nz1n62A9fN1SRe6e1e5ow\"}")
  private Map params;

  @JsonProperty(value = "transaction-ticket", required = true)
  @NotNull(message = "transaction-ticket should not be null")
  @ApiModelProperty(name = "transaction-ticket", required = true, position = 6,
    value = "Represents the payment transaction ticket.",
    example = "XXX1234567543")
  private String transactionTicket;

}
