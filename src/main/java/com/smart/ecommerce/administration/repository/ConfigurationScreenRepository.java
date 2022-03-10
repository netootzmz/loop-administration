package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenDto;

import java.util.List;

public interface ConfigurationScreenRepository {


List<ConfigurationScreenDto> getConfigurationLinkPayScreenByUser (Integer userId);


}
