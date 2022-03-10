package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.administration.model.UserGenericDto;
import com.smart.ecommerce.administration.model.dto.BitacoraPasswordDTO;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.repository.*;
import com.smart.ecommerce.administration.request.MailNotificationRequest;
import com.smart.ecommerce.administration.service.RestorePasswordService;
import com.smart.ecommerce.administration.util.ConvertDates;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.Utils;
import com.smart.ecommerce.entity.checkout.SystemConfig;
import com.smart.ecommerce.entity.configuration.BitacoraPassword;
import com.smart.ecommerce.entity.configuration.CoreTemplateClient;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.CoreRestorePassword;
import com.smart.ecommerce.entity.core.User;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service("RestorePasswordService")
public class RestorePasswordServiceImpl implements RestorePasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ErrorCodeRepository codeRepository;

    @Autowired
    RestorePasswordRepository passwordRepository;

    @Autowired
    RestorePasswordRepositoryJPA restorePasswordRepositoryJPA;

    @Autowired
    SystemConfigRepository configRepository;

    @Autowired
    PasswordExpiredRepository passwordExpiredRepository;

    @Autowired
    BitacoraPasswordRepository bitacoraPasswordRepository;

    @Autowired
    CoreTemplateClientRepository coreTemplateClientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder1;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public GenericResponse requestRestorePass(String email, int typeContact, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreRestorePassword restorePassword = new CoreRestorePassword();
        boolean save = false;
        CoreErrorCodeDto errorItem = null;


        if (email == null || typeContact == 0) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        User user = userRepository.getUserByEmail(email);
        info(idOperation, "Solicitud de restablecimiento de contraseña:: username " + user.getUser_name() + " " + typeContact);

        try {
            if (null != user) {

                if (user.getStatus_id() == 1) {

                    List<CoreRestorePassword> restorePasswordList = restorePasswordRepositoryJPA.getCoreRestorePasswordByUserID(user.getUser_id());

                    for (CoreRestorePassword coreRestorePassword : restorePasswordList) {

                        coreRestorePassword.setStatus(2);
                        restorePasswordRepositoryJPA.saveAndFlush(coreRestorePassword);


                    }


                    String codeVerification = Utils.generateCodeVerfication();
                    restorePassword = setProperties(codeVerification, user.getUser_id());
                    save = passwordRepository.saveRestorePassword(restorePassword, idOperation);

                    if (save) {

                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

                        headers.setContentType(MediaType.APPLICATION_JSON);

                        Map<String, Object> parametersMap = new HashMap<>();

                        Map<String, Object> parametersMaps = new HashMap<>();

                        String userName = user.getUser_name();
                        String name = user.getName()+'_'+user.getLast_name1();
                        String clientId = user.getClient_id();

                        String linkPasswordRestore = configRepository.getSystemConfigByName("URL_PATH_RESTORE_PASS").getValue() + codeVerification + '/' + userName + '/' + name;



                        CoreTemplateClient coreTemplateClient = coreTemplateClientRepository.getCoreTemplateClientByClientAndType(clientId,configRepository.getSystemConfigByName("CVE_MAIL_RECUPERACION_CONTRASEÑA").getValue());

                        String template ;
                        String template_cve ;
                        if(coreTemplateClient != null  ){

                            template = String.valueOf(coreTemplateClient.getTemplateClientId());
                            template_cve = coreTemplateClient.getCve();
                        }else{
                            template = "6";
                            template_cve = null;
                        }


                        parametersMap.put("email-provider", "5");
                        parametersMap.put("email-template", template);
                        parametersMap.put("template_cve", template_cve);
                        parametersMap.put("clientId", clientId);
                        parametersMap.put("email-addressee", user.getMail());
                        parametersMap.put("email-subject", "notice");
                        parametersMap.put("email-message", "Hi!., welcome to SMART!..");

                        parametersMaps.put("user", userName);
                        parametersMaps.put("ligaPago", linkPasswordRestore);

                        parametersMap.put("email-template-params", parametersMaps);

                        parametersMap.put("transaction-ticket", "XXX1234567543");


                        ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);

                        if (response.getStatusCode() == HttpStatus.OK) {
                            System.out.println("Request Successful");

                            info(idOperation, "Generación de código de verificación exitoso");
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
                        } else {
                            info(idOperation, "Error al enviar correo");
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
                        }


                    } else {

                        info(idOperation, "Error al almacenar el código de verificación");
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
                        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

                    }

                } else {

                    info(idOperation, String.format("El usuario:  %s no se encuentra activo.", user.getUser_name()));
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_INACTIVO);
                    return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

                }

            } else {

                info(idOperation, String.format("El nombre de usuario:  %s no se encuentra registrado en base de datos.", user.getUser_name()));
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_CORREO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

            }
        } catch (NullPointerException e) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }
    }

    @Override
    public GenericResponse requestRestorePassUserReg(String email, int typeContact, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreRestorePassword restorePassword = new CoreRestorePassword();
        boolean save = false;
        CoreErrorCodeDto errorItem = null;


        if (email == null || typeContact == 0) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        User user = userRepository.getUserByEmail(email);
        info(idOperation, "Solicitud de restablecimiento de contraseña:: username " + user.getUser_name() + " " + typeContact);

        try {
            if (null != user) {

                if (user.getStatus_id() == 1) {

                    List<CoreRestorePassword> restorePasswordList = restorePasswordRepositoryJPA.getCoreRestorePasswordByUserID(user.getUser_id());

                    for (CoreRestorePassword coreRestorePassword : restorePasswordList) {

                        coreRestorePassword.setStatus(2);
                        restorePasswordRepositoryJPA.saveAndFlush(coreRestorePassword);


                    }


                    String codeVerification = Utils.generateCodeVerfication();
                    restorePassword = setProperties(codeVerification, user.getUser_id());
                    save = passwordRepository.saveRestorePassword(restorePassword, idOperation);

                    if (save) {

                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

                        headers.setContentType(MediaType.APPLICATION_JSON);

                        Map<String, Object> parametersMap = new HashMap<>();

                        Map<String, Object> parametersMaps = new HashMap<>();

                        String userName = user.getUser_name();
                        String name = user.getName()+'_'+user.getLast_name1();

                        String clientId = user.getClient_id();


                        String linkPasswordRestore = configRepository.getSystemConfigByName("URL_PATH_RESTORE_PASS").getValue() + codeVerification + '/' + userName + '/' + name;

                        CoreTemplateClient coreTemplateClient = coreTemplateClientRepository.getCoreTemplateClientByClientAndType(clientId,configRepository.getSystemConfigByName("CVE_MAIL_RECUPERACION_CONTRASEÑA").getValue());

                        String template ;
                        String template_cve ;
                        if(coreTemplateClient != null  ){

                            template = String.valueOf(coreTemplateClient.getTemplateClientId());
                            template_cve = coreTemplateClient.getCve();
                        }else{
                            template = "36";
                            template_cve = null;
                        }


                        parametersMap.put("email-provider", "5");
                        parametersMap.put("email-template", template);
                        parametersMap.put("template_cve", template_cve);
                        parametersMap.put("email-addressee", user.getMail());
                        parametersMap.put("email-subject", "notice");
                        parametersMap.put("clientId", clientId);
                        parametersMap.put("email-message", "Hi!., welcome to SMART!..");

                        parametersMaps.put("user", userName);
                        parametersMaps.put("ligaPago", linkPasswordRestore);

                        parametersMap.put("email-template-params", parametersMaps);

                        parametersMap.put("transaction-ticket", "XXX1234567543");


                        ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);

                        if (response.getStatusCode() == HttpStatus.OK) {
                            System.out.println("Request Successful");

                            info(idOperation, "Generación de código de verificación exitoso");
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
                        } else {
                            info(idOperation, "Error al enviar correo");
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
                        }


                    } else {

                        info(idOperation, "Error al almacenar el código de verificación");
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
                        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

                    }

                } else {

                    info(idOperation, String.format("El usuario:  %s no se encuentra activo.", user.getUser_name()));
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_INACTIVO);
                    return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

                }

            } else {

                info(idOperation, String.format("El nombre de usuario:  %s no se encuentra registrado en base de datos.", user.getUser_name()));
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_CORREO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

            }
        } catch (NullPointerException e) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }
    }


    @Override
    public GenericResponse changePassword(String email, String password, String newPassword, String newPasswordConfirm, String userLog, Integer languageId) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);

        info(idOperation, "Restablecimiento de nueva contraseña::");
        CoreErrorCodeDto errorItem = null;

        if (password == null || newPassword == null || newPasswordConfirm == null || email == null) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }


        User user = userRepository.getUserByEmail(email);

        if (user.getUser_id() != null) {
//
            if (1 != user.getStatus_id()) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            if (Utils.isPassValid(newPassword) == false) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_EL_PASSWORD_NO_ES_VALIDO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }
            if (!newPassword.equals(newPasswordConfirm)) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_NO_COINCIDE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            if (newPassword.equals(password)) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_NUEVO_PASSWORD_ACTUAL);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }


            if (!encoder.matches(password, user.getPassword())) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_ACTUAL_NO_COINCIDE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }


            List<BitacoraPassword> bitacoraPasswordList = bitacoraPasswordRepository.getListPasswordByUser(user.getUser_id());


            for (BitacoraPassword bitacoraPassword : bitacoraPasswordList) {

                if (encoder.matches(newPassword, bitacoraPassword.getPassword())) {

                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_PREVIO);
                    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());


                }

            }


            user.setPassword(encoder.encode(newPassword));
            user.setDate_change(new Date());
            user.setUser_change(userLog);
            try {
                User save = userRepository.save(user);


                if (save != null) {

                    for (BitacoraPassword bitacoraPassword : bitacoraPasswordList) {

                        if ( 1 == bitacoraPassword.getStatus()) {

                            bitacoraPassword.setStatus(2);

                            bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);

                        }

                    }

                    BitacoraPassword bitacoraPassword = new BitacoraPassword();
                    bitacoraPassword.setPassword(encoder.encode(newPassword));
                    bitacoraPassword.setDateRegister(new Date());
                    bitacoraPassword.setStatus(1);
                    bitacoraPassword.setUserId(user.getUser_id());
                    bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);

                    Map <String,Object> daypassflagObject = new HashMap<>();

                    String  passDate= new SimpleDateFormat("yyyy-MM-dd").format(bitacoraPassword.getDateRegister());
                    String  actualDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                    LocalDate dateBefore = LocalDate.parse(passDate);
                    LocalDate dateAfter = LocalDate.parse(actualDate);

                    long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);

                    if (noOfDaysBetween >= 60) {
                       daypassflagObject.put("daypassflag ",3);
                    } else if (noOfDaysBetween <= 59) {

                        if (noOfDaysBetween >= 50) {
                            daypassflagObject.put("daypassflag ",2);

                        } else {

                            daypassflagObject.put("daypassflag ",1);

                        }

                    }


                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    CoreTemplateClient coreTemplateClient = coreTemplateClientRepository.getCoreTemplateClientByClientAndType(user.getClient_id(),configRepository.getSystemConfigByName("CVE_MAIL_CAMBIO_CONTRASEÑA_EXITOSA").getValue());

                    String template ;
                    String template_cve ;
                    if(coreTemplateClient != null  ){

                        template = String.valueOf(coreTemplateClient.getTemplateClientId());
                        template_cve = coreTemplateClient.getCve();
                    }else{
                        template = "5";
                        template_cve = null;
                    }



                    String msgHeader = "Cambio de contraseña";

                    String msgBody = "El cambio de tu contraseña ha quedado completado.";

                    String clientId = user.getClient_id();

                    Map<String, Object> parametersMap = new HashMap<>();

                    Map<String, Object> parametersMaps = new HashMap<>();

                    parametersMap.put("email-provider", "5");
                    parametersMap.put("email-template", template);
                    parametersMap.put("template_cve", template_cve);
                    parametersMap.put("email-addressee", user.getMail());
                    parametersMap.put("clientId", clientId);
                    parametersMap.put("email-subject", "notice");
                    parametersMap.put("email-message", "Hi!., welcome to SMART!..");

                    parametersMaps.put("user", user.getUser_name());
                    parametersMaps.put("msgHeader", msgHeader);
                    parametersMaps.put("msgBody", msgBody);
                    parametersMap.put("email-template-params", parametersMaps);

                    parametersMap.put("transaction-ticket", "XXX1234567543");

                    ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);


                    if (response.getStatusCode() == HttpStatus.OK) {
                        System.out.println("Request Successful");


                        info(idOperation, "Restablecimiento de contraseña correcto");
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(),daypassflagObject);
                    } else {
                        System.out.println("Request Failed");
                        info(idOperation, String.format("Surgio un error al enviar el correo.", user.getUser_name()));
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_EXISTE_EN_BD);
                        return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                    }

                }


            } catch (DataIntegrityViolationException e) {
                System.out.println("causa especifica" + e.getMostSpecificCause());
                if (e.getMostSpecificCause().toString().equals("java.sql.SQLIntegrityConstraintViolationException: Column 'client_id' cannot be null")) {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                    return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                }

            }


        } else {

            info(idOperation, String.format("El nombre de usuario:  %s no se encuentra registrado en base de datos.", user.getUser_name()));
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_EXISTE_EN_BD);
            return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

        }


        return null;


    }


    @Override
    public GenericResponse codeVerification(String username, String codeVerification, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        SystemConfig systemConfig = configRepository.getSystemConfigByName("TIME_CODE_VERIFICATION");
        CoreRestorePassword restorePassword = new CoreRestorePassword();
        Date dateRequest = new Date();
        Map<String, Object> information = new HashMap<>();
        CoreErrorCodeDto errorItem = null;

        info(idOperation, "Verificación de código::");

        if (username == null || codeVerification == null) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        User user = userRepository.getUser(username);
        if (user.getUser_id() != null) {

            restorePassword = passwordRepository.getByUserIdVerficationCode(user.getUser_id(), codeVerification);

            if (restorePassword.getRestore_pass_id() != null) {

                if (restorePassword.getStatus() == 1) {

                    Date dateRestore = ConvertDates.addHours(restorePassword.getDate_generation_code(), Integer.parseInt(systemConfig.getValue()));
                    info(idOperation, String.format("fecha de la petición:  %s fecha de generación del código más minutos vigencia: %s", dateRequest, dateRestore));

                    if (dateRestore.after(dateRequest)) {
                        if (restorePassword.getVarificaction_code().equals(codeVerification)) {

                            information.put("restorePassId", restorePassword.getRestore_pass_id());
                            info(idOperation, "Código de verificación exitoso");
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), information);

                        } else {

                            info(idOperation, String.format("El código de verificación ingresado: %s no coincide con el código de Base de datos:  %s", codeVerification, restorePassword.getVarificaction_code()));
                            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_ERRONEO);
                            return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), information);

                        }

                    } else {

                        restorePassword.setStatus(0);
                        passwordRepository.saveRestorePassword(restorePassword, idOperation);
                        info(idOperation, "Código de verificación vencido por tiempo");
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_VENCIDO);
                        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

                    }

                } else {

                    info(idOperation, "El estatus del restablecimiento es: " + restorePassword.getStatus());
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_VENCIDO);
                    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

                }

            } else {

                info(idOperation, String.format("No existe el código de verificación para el usuario %s: ", username));
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_ERRONEO);
                return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), codeVerification));

            }

        } else {

            info(idOperation, String.format("El nombre de usuario:  %s no se encuentra registrado en base de datos.", username));
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
            return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), username));

        }
    }

    @Override
    public GenericResponse newPassword(String username, String newPassword, String newPasswordConfirm, String codeVerification, String userLog, Integer languageId) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);

        info(idOperation, "Restablecimiento de nueva contraseña::");
        CoreErrorCodeDto errorItem = null;

        if (username == null || newPassword == null) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }


        User user = userRepository.getUser(username);

        if (user.getUser_id() != null) {

            if (1 != user.getStatus_id()) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            if (Utils.isPassValid(newPassword) == false) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_EL_PASSWORD_NO_ES_VALIDO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }
            if (!newPassword.equals(newPasswordConfirm)) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_NO_COINCIDE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            GenericResponse codeValidationResponse = this.codeVerification(user.getUser_name(), codeVerification, languageId);
            Map<String, Object> restorePasswordMap = null;
            if (!ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE.equals(codeValidationResponse.getCodeStatus())) {

                errorItem = ErrorCode.getError(error, codeValidationResponse.getCodeStatus());
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());


            } else {

                restorePasswordMap = codeValidationResponse.getInformation();
            }


            List<BitacoraPassword> bitacoraPasswordList = bitacoraPasswordRepository.getListPasswordByUser(user.getUser_id());


            for (BitacoraPassword bitacoraPassword : bitacoraPasswordList) {

                if (encoder.matches(newPassword, bitacoraPassword.getPassword())) {

                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PASSWORD_PREVIO);
                    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());


                }

            }


            user.setPassword(encoder.encode(newPassword));
            user.setDate_change(new Date());
            user.setUser_change(userLog);
            try {
                User save = userRepository.save(user);
                CoreRestorePassword restorePassword = null;
                restorePassword = passwordRepository.getById((Integer) restorePasswordMap.get("restorePassId"));

                if (restorePassword.getRestore_pass_id() != null) {

                    if (restorePassword.getStatus() == 1) {

                        if (save != null) {

                            for (BitacoraPassword bitacoraPassword : bitacoraPasswordList) {

                                if ( 1 == bitacoraPassword.getStatus()) {

                                    bitacoraPassword.setStatus(2);

                                    bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);

                                }

                            }


                            BitacoraPassword bitacoraPassword = new BitacoraPassword();
                            bitacoraPassword.setPassword(encoder.encode(newPassword));
                            bitacoraPassword.setDateRegister(new Date());
                            bitacoraPassword.setStatus(1);
                            bitacoraPassword.setUserId(user.getUser_id());
                            bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);

                            Map <String,Object> daypassflagObject = new HashMap<>();

                            String  passDate= new SimpleDateFormat("yyyy-MM-dd").format(bitacoraPassword.getDateRegister());
                            String  actualDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                            LocalDate dateBefore = LocalDate.parse(passDate);
                            LocalDate dateAfter = LocalDate.parse(actualDate);

                            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);

                            if (noOfDaysBetween >= 60) {
                                daypassflagObject.put("daypassflag ",3);
                            } else if (noOfDaysBetween <= 59) {

                                if (noOfDaysBetween >= 50) {
                                    daypassflagObject.put("daypassflag ",2);

                                } else {

                                    daypassflagObject.put("daypassflag ",1);

                                }

                            }


                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

                            headers.setContentType(MediaType.APPLICATION_JSON);

                            Map<String, Object> parametersMap = new HashMap<>();

                            Map<String, Object> parametersMaps = new HashMap<>();
                            String clientId = user.getClient_id();

                            String msgHeader = "Restauración de contraseña";

                            String msgBody = "La restauración de tu contraseña ha quedado completada.";

                            CoreTemplateClient coreTemplateClient = coreTemplateClientRepository.getCoreTemplateClientByClientAndType(clientId,configRepository.getSystemConfigByName("CVE_MAIL_CAMBIO_CONTRASEÑA_EXITOSA").getValue());

                            String template ;
                            String template_cve ;
                            if(coreTemplateClient != null  ){

                                template = String.valueOf(coreTemplateClient.getTemplateClientId());
                                template_cve = coreTemplateClient.getCve();
                            }else{
                                template = "5";
                                template_cve = null;
                            }

                            parametersMap.put("email-provider", "5");
                            parametersMap.put("email-template", template);
                            parametersMap.put("email-addressee", user.getMail());
                            parametersMaps.put("msgHeader", msgHeader);
                            parametersMap.put("template_cve", template_cve);
                            parametersMap.put("clientId", clientId);
                            parametersMaps.put("msgBody", msgBody);
                            parametersMap.put("email-subject", "notice");
                            parametersMap.put("email-message", "Hi!., welcome to SMART!..");

                            parametersMaps.put("user", user.getUser_name());

                            parametersMap.put("email-template-params", parametersMaps);

                            parametersMap.put("transaction-ticket", "XXX1234567543");

                            ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);

                            if (response.getStatusCode() == HttpStatus.OK) {
                                System.out.println("Request Successful");

                                restorePassword = passwordRepository.getById((Integer) restorePasswordMap.get("restorePassId"));

                                restorePassword.setStatus(2);
                                restorePassword.setDate_restore_pass(new Timestamp(new Date().getTime()));
                                passwordRepository.saveRestorePassword(restorePassword, idOperation);

                                info(idOperation, "Restablecimiento de contraseña correcto");
                                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(),daypassflagObject);
                            } else {
                                System.out.println("Request Failed");
                                info(idOperation, String.format("Surgio un error al enviar el correo.", user.getUser_name()));
                                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_EXISTE_EN_BD);
                                return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                            }


                        }
                    } else {
                        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_VENCIDO);
                        return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                    }
                } else {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CODIGO_VERIFICACION_ERRONEO);
                    return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                }

            } catch (DataIntegrityViolationException e) {
                System.out.println("causa especifica" + e.getMostSpecificCause());
                if (e.getMostSpecificCause().toString().equals("java.sql.SQLIntegrityConstraintViolationException: Column 'client_id' cannot be null")) {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                    return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));
                }

            }


        } else {

            info(idOperation, String.format("El nombre de usuario:  %s no se encuentra registrado en base de datos.", user.getUser_name()));
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_EXISTE_EN_BD);
            return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), user.getUser_name()));

        }


        return null;
    }


    public static void info(String msg, String idOperation) {
        Console.writeln(Console.Level.INFO, idOperation, msg);
    }

    public CoreRestorePassword setProperties(String codeVerification, Integer userId) {
        CoreRestorePassword restorePassword = new CoreRestorePassword();

        restorePassword.setUser_id(userId);
        restorePassword.setVarificaction_code(codeVerification);
        restorePassword.setDate_generation_code(new Timestamp(new Date().getTime()));
        restorePassword.setStatus(1);

        return restorePassword;
    }


    @Override
    public UserGenericDto requestRestorePassByUsername(String email, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        UserGenericDto user = new UserGenericDto();
        info(idOperation, "Solicitud de mail y phone del usuario::");
        CoreErrorCodeDto errorItem = null;

        if (email == null) {

            info(idOperation, "Parámetros incompletos");

            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);

            user.setMail(null);
            user.setPhone(null);
            user.setMensaje(errorItem.getMessage());
            user.setCodeStatus(errorItem.getCode());

            return user;
        }


        User userReq = userRepository.getUserByEmail(email);
        try {

            if (userReq.getUser_name() != null) {

                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);

                user.setMail(Utils.encryptSha256(userReq.getMail()));
                user.setPhone(Utils.encryptSha256(userReq.getPhone()));
                user.setMensaje(errorItem.getMessage());
                user.setCodeStatus(errorItem.getCode());

                return user;


            } else {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                user.setMail(null);
                user.setPhone(null);
                user.setMensaje(errorItem.getMessage());
                user.setCodeStatus(errorItem.getCode());

                return user;
            }

        } catch (NullPointerException e) {
            e.getMessage();
        }
        return user;
    }

    @Override
    public GenericResponse getContacts(String username) {

        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(new Integer(1));
        List<Map> listContacts = new ArrayList<>();
        CoreErrorCodeDto errorItem;

        if (username == null || username == null) {

            info(idOperation, "Parámetros incompletos");
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        } else {

            Map<String, Object> information = new HashMap<>();
            Map<String, Object> contacts = userRepository.getContactsByUserName(username);

            if (null == contacts || contacts.isEmpty()) {
                info(idOperation, "No existe en BD");
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            String mailUser = String.valueOf(contacts.get("mailUser"));
            String phoneUser = String.valueOf(contacts.get("phoneUser"));


            if (!mailUser.equals("") && mailUser.length() > 5) {
                mailUser = Utils.maskString(mailUser, 2, mailUser.length() - 4, '*');
                Map<String, Object> outPut1 = new HashMap<>();
                outPut1.put("typeContact", 1);//TYPECONTACT 1 MAIL
                outPut1.put("contact", mailUser);
                listContacts.add(outPut1);
            }

            if (!phoneUser.equals("") && mailUser.length() > 9) {
                phoneUser = Utils.maskString(phoneUser, 2, phoneUser.length() - 2, '*');
                Map<String, Object> outPut2 = new HashMap<>();
                outPut2.put("typeContact", 2);//TYPECONTACT 2 SMS
                outPut2.put("contact", phoneUser);
                listContacts.add(outPut2);
            }

            information.put("contacts", listContacts);
            return new GenericResponse(ResponseType.ACEPT, information);

        }


    }


}
