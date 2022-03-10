package com.smart.ecommerce.administration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.contract.ConfigurationScreenPaymentCheckOutContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.service.ConfigurationScreenPaymentCheckOutService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
//import com.smart.ecommerce.logging.SystemLog

@RestController
@RequestMapping("/ConfigurationScreenPaymentCheckOut")
public class ConfigurationScreenPaymentCheckOutController implements ConfigurationScreenPaymentCheckOutContract {
  
  @Autowired private ConfigurationScreenPaymentCheckOutService cspcService;

  @Override
  @PostMapping(value = "/getConfigurationLinkPayScreenByUser", produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse getConfigurationLinkPayScreenByUser(HttpServletRequest request) {
    InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);
    //String idOperation = SystemLog.newTxnIdOperation() 
    return cspcService.getConfigurationLinkPayScreenByUser( infoTokenDto);
  }

}
