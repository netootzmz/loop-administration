package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

public interface ConfigurationScreenContract {

    @ApiOperation(value = "Método para obtener datos del usuario ")
    public GenericResponse getConfigurationLinkPayScreenByUser(HttpServletRequest request);
}
