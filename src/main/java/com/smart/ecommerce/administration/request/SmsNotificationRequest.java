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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author jefloresg
 * @version 1.0
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsNotificationRequest implements Serializable {

  @JsonProperty(value = "sms-provider", required = true)
  @NotNull(message = "sms-provider should not be null")
  @ApiModelProperty(name = "sms-provider", required = true, position = 0,
    value = "Represents the SMS provider type.", example = "3")
  private Integer provider;

  @JsonProperty(value = "sms-phoneNumber", required = true)
  @NotNull(message = "sms-phoneNumber should not be null")
  @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{4}$",
    message = "The phone number must be ten digits")
  @ApiModelProperty(name = "sms-phoneNumber", required = true, position = 1,
    value = "Represents the ten-digit phone number", example = "1234567890")
  private String phoneNumber;

  @JsonProperty(value = "sms-message", required = true)
  @NotNull(message = "sms-message should not be null")
  @ApiModelProperty(name = "sms-message", required = true, position = 2,
    value = "Represents the sms message.", example = "SMS message")
  private String message;

  @JsonProperty(value = "transaction-ticket", required = true)
  @NotNull(message = "transaction-ticket should not be null")
  @ApiModelProperty(name = "transaction-ticket", required = true, position = 3,
    value = "Represents the payment transaction ticket.",
    example = "SMART-123456ASDFGH-1234")
  private String transactionTicket;

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
