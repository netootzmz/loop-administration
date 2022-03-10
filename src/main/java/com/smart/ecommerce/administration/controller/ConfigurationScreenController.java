package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.ConfigurationScreenContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.service.ConfigurationScreenService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.logging.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ConfigurationScreenController")
public class ConfigurationScreenController implements ConfigurationScreenContract {


    @Autowired
    ConfigurationScreenService configurationScreenService;

    @Override
    @PostMapping(value = "/getConfigurationLinkPayScreenByUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse getConfigurationLinkPayScreenByUser(HttpServletRequest request) {
        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);
        String idOperation = SystemLog.newTxnIdOperation();

        return configurationScreenService.getConfigurationLinkPayScreenByUser( infoTokenDto);
    }








}
