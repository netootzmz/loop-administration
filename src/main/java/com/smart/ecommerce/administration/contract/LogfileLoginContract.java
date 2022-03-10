package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

public interface LogfileLoginContract {
    @ApiOperation(value = "Servivio  para dar de alta un nuevo registro en  la bitacora de login ", notes = "")
    public GenericResponse createLogFileLogin(HttpServletRequest request);

}
