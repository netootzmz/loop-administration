package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface TemplateGeneratorService {

    GenericResponse newTemplate ();
    GenericResponse generatedTemplate(InfoTokenDto infoToken,ParamGetMailPaymentDTO dto);


}
