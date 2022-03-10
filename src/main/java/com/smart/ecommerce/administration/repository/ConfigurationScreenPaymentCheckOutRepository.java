package com.smart.ecommerce.administration.repository;

import java.util.List;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenPaymentLinkDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.PaymentCheckOutParametersDTO;

public interface ConfigurationScreenPaymentCheckOutRepository {

  List<ConfigurationScreenPaymentLinkDto> getConfigurationLinkPayScreenByUser(Integer userId);
  //List<ConfigurationScreenDto> getConfigurationLinkPayScreenByUser(Integer userId)
  List<PaymentCheckOutParametersDTO> getMsiByIdUser(InfoTokenDto dto);
  Boolean hasMsiConfigured (PaymentCheckOutParametersDTO dto);
  List<PaymentCheckOutParametersDTO> getMsiByClientProductId(PaymentCheckOutParametersDTO dto);
   
}
