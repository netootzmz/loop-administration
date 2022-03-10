package com.smart.ecommerce.administration.controller;


import com.smart.ecommerce.administration.contract.MailConfigurationContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailConfigurationDTO;
import com.smart.ecommerce.administration.service.MailConfigurationService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/MailConfigurationController")
public class MailConfigurationController implements MailConfigurationContract {

    @Autowired
    MailConfigurationService mailConfigurationService;

    @Override
    @PostMapping(value = "/addMailConfiguration", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse addMailConfiguration(HttpServletRequest request, @RequestBody MailConfigurationDTO mailConfigurationDTO) {

        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);

        return mailConfigurationService.addMailConfiguration(infoTokenDto, mailConfigurationDTO);

    }


    @Override
    @PostMapping(value = "/updateMailConfiguration/{mailConfigurationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse updateMailConfiguration(HttpServletRequest request, @PathVariable Integer mailConfigurationId, @RequestBody MailConfigurationDTO mailConfigurationDTO) {

        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);

        return mailConfigurationService.updateMailConfiguration(infoTokenDto, mailConfigurationId, mailConfigurationDTO);

    }


    @Override
    @PostMapping(value = "/getMailConfiguration/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse getMailConfiguration(HttpServletRequest request, @PathVariable String clientId ) {

        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);

        return mailConfigurationService.getMailConfiguration(infoTokenDto, clientId);

    }


    @Override
    @PostMapping(value = "/deleteMailConfiguration/{mailConfigurationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse deleteMailConfiguration(HttpServletRequest request, @PathVariable Integer mailConfigurationId ) {

        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);

        return mailConfigurationService.deleteMailConfiguration(infoTokenDto, mailConfigurationId);

    }


}
