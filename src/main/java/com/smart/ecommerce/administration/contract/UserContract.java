package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.model.UserDto;
import com.smart.ecommerce.administration.model.UserPtlSrvDto;
import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "user-system")
public interface UserContract {

    @ApiOperation("Servicio registro de usuario")
    @PostMapping("/saveUpdate")
    GenericResponse addUpdateUser(HttpServletRequest request, @RequestBody UserDto dto);

    @ApiOperation("Servicio actualizacion de usuario")
    @PostMapping("/delete")
    GenericResponse deleteUser(HttpServletRequest request, @RequestParam Integer id);

    @ApiOperation("Consulta de lista de usuarios")
    @PostMapping("/getAllUser")
    GenericResponse listUsers(HttpServletRequest request);

    @ApiOperation("Consulta por identificador de usuario")
    @PostMapping("/getByUserId")
    GenericResponse listUser(HttpServletRequest request, @RequestParam Integer userId);
    
    @ApiOperation("Consulta por identificador de usuario, incluye los usuarios bloqueados")
    @PostMapping("/getByUserIdIncludingBlocked")
    GenericResponse getUserByIdBlocked(HttpServletRequest request, @RequestParam Integer userId);

    @PostMapping("/getByUserName")
    @ApiOperation("Consulta por nombre de usuario")
    GenericResponse listUser(HttpServletRequest request, @RequestParam String user);
    
    @PostMapping("/getByUserNameIncludingBlocked")
    @ApiOperation("Consulta por nombre de usuario, incluye los usuarios bloqueados")
    GenericResponse getUserByNameBlocked(HttpServletRequest request, @RequestParam String user);


    @PostMapping("/getByEmail")
    @ApiOperation("Consulta por email de usuario, incluye los usuarios bloqueados")
    GenericResponse getByEmail(HttpServletRequest request, @RequestParam String email);

    
    @PostMapping("/failedLoginAttempt")
    @ApiOperation("Registra el numero de intentos de login fallidos")
    GenericResponse failedLoginAttempt(HttpServletRequest request, @RequestParam Integer userId);

    @ApiOperation("Servicio registro de usuario Portal de Servicios")
    @PostMapping("/saveUpdatePtlSrv")
    GenericResponse addUpdateUserPtlSrv(HttpServletRequest request, @RequestBody UserPtlSrvDto dto);

    @ApiOperation("Servicio eliminación de usuario")
    @PostMapping("/deletePrtlSrv")
    GenericResponse deleteUserPrtlSrv(HttpServletRequest request, @RequestParam Integer id);

    @ApiOperation("Servicio bloqueo de usuario")
    @PostMapping("/blockPrtlSrv")
    GenericResponse blockUserPrtlSrv(HttpServletRequest request, @RequestParam Integer id);

    @ApiOperation("Consulta por identificador de usuario, incluye los usuarios bloqueados")
    @PostMapping("/getByUserIdIncludingBlockedPrtlSrv")
    GenericResponse getUserByIdBlockedPrtlSrv(HttpServletRequest request, @RequestParam Integer userId);

    @ApiOperation("Servicio bloqueo de usuario")
    @PostMapping("/unblockPrtlSrv")
    GenericResponse unblockUserPrtlSrv(HttpServletRequest request, @RequestParam Integer id);
}
