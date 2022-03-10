package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailConfigurationDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface MailConfigurationService {

    GenericResponse addMailConfiguration(InfoTokenDto infoTokenDto, MailConfigurationDTO mailConfigurationDTO);

    GenericResponse updateMailConfiguration(InfoTokenDto infoTokenDto, Integer mailConfigurationId, MailConfigurationDTO mailConfigurationDTO);

    GenericResponse getMailConfiguration(InfoTokenDto infoTokenDto, String clientId);
    GenericResponse deleteMailConfiguration(InfoTokenDto infoTokenDto, Integer mailConfigurationId);


}
