package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.model.UserGenericDto;
import com.smart.ecommerce.administration.service.RestorePasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/getDatauserByUserName")
@Api("Servicio para obtener telefono y email por userName")
public class GetDataUserController {

    @Autowired
    RestorePasswordService passwordService;

    @PostMapping("/getDatauserByUserName")
    @ApiOperation("Solicitud de restablecimiento de contraseña por nombre de usuario.")
    public UserGenericDto getDatauserByUserName(HttpServletRequest request, @RequestParam String username) {
        return passwordService.requestRestorePassByUsername(username, 1);
    }

}
