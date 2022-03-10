package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.model.dto.LabelSystemDTO;
import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface CoreLanguageContract {

    @ApiOperation(value = "Método para obtener Corelanguage ", notes = "")
    public GenericResponse getAllCoreLanguage();

}
