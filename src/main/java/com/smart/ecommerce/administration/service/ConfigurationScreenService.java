package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface ConfigurationScreenService {

    GenericResponse getConfigurationLinkPayScreenByUser(InfoTokenDto infoToken);


}
