package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.LogfileLoginContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.LogfileLoginDTO;
import com.smart.ecommerce.administration.service.LogfileLoginService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/logfile-login-controller")
public class LogfileLoginController implements LogfileLoginContract {
    @Autowired
    private LogfileLoginService logfileLoginService;

    @Override
    @PostMapping(value = "/createLogFileLogin", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse createLogFileLogin(HttpServletRequest request) {
        InfoTokenDto tokenDto = InfoToken.getInfoToken(request);

        return logfileLoginService.createLogFileLogin(request, tokenDto);
    }
}
