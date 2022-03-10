package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.model.dto.MailConfigurationDTO;
import com.smart.ecommerce.administration.util.GenericResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

public interface MailConfigurationContract {

    public GenericResponse addMailConfiguration(HttpServletRequest request , @RequestBody MailConfigurationDTO mailConfigurationDTO);

    public GenericResponse updateMailConfiguration(HttpServletRequest request , @PathVariable Integer mailConfigurationId, @RequestBody MailConfigurationDTO mailConfigurationDTO);

    public GenericResponse getMailConfiguration(HttpServletRequest request, @PathVariable String userId );

    public GenericResponse deleteMailConfiguration(HttpServletRequest request, @PathVariable Integer mailConfigurationId );
}
