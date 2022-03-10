package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.model.dto.RestorePassword;
import com.smart.ecommerce.administration.model.dto.SetPasswordDTO;
import com.smart.ecommerce.administration.service.RestorePasswordService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.entity.core.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/restorePassword")
@Api("Servicios para restablecer la contraseña de un usuario")
public class RestorePasswordController {

  @Autowired
  RestorePasswordService passwordService;

  @PostMapping("/requestRestorePass")
  @ApiOperation("Solicitud de restablecimiento de contraseña.")
  public GenericResponse requestRestorePass(HttpServletRequest request, @RequestParam String email, @RequestParam int typeContact) {
    return passwordService.requestRestorePass(email, typeContact, new Integer(1));
  }

  @PostMapping("/requestRestorePassUserReg")
  @ApiOperation("Solicitud de restablecimiento de contraseña.")
  public GenericResponse requestRestorePassUserReg(HttpServletRequest request, @RequestParam String email, @RequestParam int typeContact) {
    return passwordService.requestRestorePassUserReg(email, typeContact, new Integer(1));
  }
  
  @PostMapping("/codeVerification")
  @ApiOperation("Validación del código de verificación obtenida del correo enviado al usuario.")
  public GenericResponse codeVerification(HttpServletRequest request, @RequestParam String username, @RequestParam String codeVerification) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Integer languageId = InfoToken.getLanguageId(request);
    return passwordService.codeVerification(username, codeVerification, languageId);
  }

  @PostMapping(value = "/newPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("Generación de nueva contraseña.")
  public GenericResponse newPassword(HttpServletRequest request, @RequestBody RestorePassword restorePassword) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Integer languageId = InfoToken.getLanguageId(request);
    
    String username = restorePassword.getUserName();
    String newPassword = restorePassword.getNewPassword();
    String newPasswordConfirm = restorePassword.getNewPasswordConfirm();
    String codeVerification = restorePassword.getCodeVerification();

    return passwordService.newPassword(username, newPassword, newPasswordConfirm,  codeVerification,auth.getName(), languageId);
  }


  @PostMapping(value = "/setPass", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("Cambio de contraseña de usuario logueado.")
  public GenericResponse changePassword(HttpServletRequest request, @RequestBody SetPasswordDTO setPasswordDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Integer languageId = InfoToken.getLanguageId(request);

    String mail = setPasswordDTO.getEmail();
    String password = setPasswordDTO.getPassword();
    String newPassword = setPasswordDTO.getNewPassword();
    String newPasswordConfirm = setPasswordDTO.getNewPasswordConfirm();

    return passwordService.changePassword(mail,password, newPassword, newPasswordConfirm,auth.getName(), languageId);
  }

  @PostMapping("/contact")
  @ApiOperation("Recupera los contactos del usuario.")
  public GenericResponse getContacts(@RequestParam String userName) {

    return passwordService.getContacts(userName);
  }

}
