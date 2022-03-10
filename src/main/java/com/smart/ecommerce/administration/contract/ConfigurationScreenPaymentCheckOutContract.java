package com.smart.ecommerce.administration.contract;

import javax.servlet.http.HttpServletRequest;

import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.ApiOperation;

public interface ConfigurationScreenPaymentCheckOutContract {

  @ApiOperation( value = "Método para obtener datos del usuario para la liga de pagos ")
  public GenericResponse getConfigurationLinkPayScreenByUser(HttpServletRequest request);
  
}
