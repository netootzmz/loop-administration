package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.TemplateGeneratorContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.RestorePassword;
import com.smart.ecommerce.administration.service.TemplateGeneratorService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/TemplateGeneratorController")
public class TemplateGeneratorController implements TemplateGeneratorContract {

    @Autowired
    TemplateGeneratorService templateGeneratorService;


    @PostMapping(value = "/newTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Generación de template.")
    public GenericResponse newTemplate() {



        return templateGeneratorService.newTemplate();
    }


}
