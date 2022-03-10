package com.smart.ecommerce.administration.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.UserDto;
import com.smart.ecommerce.administration.model.UserPtlSrvDto;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.UsersSearchDTO;
import com.smart.ecommerce.administration.repository.*;
import com.smart.ecommerce.administration.repository.Custom.FailedLoginAttemptRepositoryCustom;
import com.smart.ecommerce.administration.request.MailNotificationRequest;
import com.smart.ecommerce.administration.request.SmsNotificationRequest;
import com.smart.ecommerce.administration.service.UserService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.Utils;
import com.smart.ecommerce.entity.checkout.Status;
import com.smart.ecommerce.entity.checkout.SystemConfig;
import com.smart.ecommerce.entity.configuration.BitacoraPassword;
import com.smart.ecommerce.entity.core.*;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserPrtlSrvRepository repositoryPrtlSrv;

    @Autowired
    private FailedLoginAttemptRepository failedLoginAttemptRepository;

    @Autowired
    private FailedLoginAttemptRepositoryCustom failedLoginAttemptRepositoryCustom;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ErrorCodeRepository codeRepository;

    @Autowired
    SystemConfigRepository configRepository;

    @Autowired
    PasswordExpiredRepository passwordExpiredRepository;

    @Autowired
    BitacoraPasswordRepository bitacoraPasswordRepository;


    @Override
    public GenericResponse failedLoginAttempt(Integer userId, Integer languageId) {
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        FailedLoginAttempt failedLoginAttempt = new FailedLoginAttempt();
        SystemConfig minutes = configRepository.getSystemConfigByName("FAIL_LOGIN_ATTEMPTS_MINUTES");
        CoreErrorCodeDto errorItem = new CoreErrorCodeDto();
        List<FailedLoginAttempt> list = failedLoginAttemptRepositoryCustom.getFailedLoginAttemptsById(userId, Integer.parseInt(minutes.getValue()));
        if (list.size() == 0) {
            Date date = new Date();
            failedLoginAttempt.setAttempt_num(1);
            failedLoginAttempt.setUserId(userId);
            failedLoginAttempt.setCreated_at(date);
            try {
                failedLoginAttemptRepository.save(failedLoginAttempt);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_CREDENCIALES_INVALIDAS);
            } catch (DataIntegrityViolationException e) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
            }
        } else {
            SystemConfig maxAttempts = configRepository.getSystemConfigByName("FAIL_LOGIN_ATTEMPTS_MAX");
            failedLoginAttempt = list.get(list.size() - 1);
            if (failedLoginAttempt.getAttempt_num() < Integer.parseInt(maxAttempts.getValue())) {
                failedLoginAttempt.setAttempt_num(failedLoginAttempt.getAttempt_num() + 1);
                failedLoginAttemptRepository.save(failedLoginAttempt);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_VUELVE_A_INTENTARLO);
            } else {
                User user = repository.getUserStatusBlocked(failedLoginAttempt.getUserId());
                user.setUser_id(user.getUser_id());
                user.setStatus_id(2);
                repository.save(user);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_BLOQUEADO);
            }

        }
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse newUpdateUser(UserDto dto, String name, Integer languageId) {
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        String idOperation = SystemLog.newTxnIdOperation();
        User entity = new User();
        User savedUser;
        boolean edit = false;
        info(name + String.format(" a entrando a administracioncion [%s de Usurario]", dto.getUser_id() != null ?
                "Edicion" : "Alata"), idOperation);

        try {
            isValidData(dto, idOperation, error);

            if (dto.getUser_id() == null || dto.getUser_id() == 0) {
                int exist = repository.getUserByUsernamer(dto.getUser_name(), dto.getMail());
                if (exist > 0) {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                    throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario"), idOperation);
                }
                entity.setStatus_id(Status.ACTIVE);
            } else {
                dto.setUser_change(name);
                dto.setDate_change(new Date());
                edit = true;
            }

            Role role = roleRepository.getRoleById(dto.getRoleId());
            if (role == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "Rol"), idOperation);
            }

            dto.setDate_admission(new Date());
            dto.setDate_admission(!edit ? new Date() : dto.getDate_admission());
            dto.setUser_admission(!edit ? name : dto.getUser_admission());

            Map<String, Object> parametersMaps = new HashMap<>();
            parametersMaps.put("codigoAutorizacion", dto.getUser_name());
            parametersMaps.put("numeroReferencia", dto.getPassword());

            dto.setPassword(Utils.encryptSha256(dto.getPassword()));

            BeanUtils.copyProperties(entity, dto);

            if (dto.getPhone() != null) {
                if (Utils.validatePhoneNumber(dto.getPhone())) {
                    entity.setPhone(dto.getPhone());
                } else {
                    entity.setPhone("");
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_VALIDO);
                    throw new GeneralException(idOperation, errorItem.getCode(),
                            String.format(errorItem.getMessage(), "Phone"));
                }
            }

            entity.setRole_id(role.getRoleId());
            entity.setClient_id(dto.getClientId());
            entity.setGroup_id(dto.getGroupId());
            entity.setStatus_id(dto.getStatus_id());
            entity.setLanguage_id(dto.getLanguage_id());
            savedUser = repository.save(entity);

            if (!edit && savedUser != null) {
                BitacoraPassword bitacoraPassword = new BitacoraPassword();
                bitacoraPassword.setPassword(Utils.encryptSha256(dto.getPassword()));
                bitacoraPassword.setDateRegister(new Date());
                bitacoraPassword.setStatus(1);
                bitacoraPassword.setUserId(savedUser.getUser_id());
                bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);
            }

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> parametersMap = new HashMap<>();


            parametersMap.put("email-provider", "4");
            parametersMap.put("email-template", "7");
            parametersMap.put("email-addressee", dto.getMail());
            parametersMap.put("email-subject", "notice");
            parametersMap.put("email-message", "Hi!., welcome to SMART!..");

            parametersMap.put("email-template-params", parametersMaps);

            parametersMap.put("transaction-ticket", "XXX1234567543");

            ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);


            RestTemplate restTemplateSms = new RestTemplate();
            SystemConfig systemConfigSms = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS_SMS");


            Map<String, Object> parametersMapSms = new HashMap<>();


            parametersMapSms.put("sms-provider", "4");
            parametersMapSms.put("sms-phoneNumber", dto.getPhone());
            System.out.println(dto.getPhone());
            parametersMapSms.put("sms-message", "Se ha dado de alta un nuevo usuario con este numero");
            parametersMapSms.put("transaction-ticket", "XXX1234567543");

            ResponseEntity<SmsNotificationRequest> responseSms = restTemplateSms.postForEntity(systemConfigSms.getValue(), parametersMapSms, SmsNotificationRequest.class);


            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            info(errorItem.getMessage(), idOperation);

        } catch (GeneralException ge) {
            return new GenericResponse(ge.getErrorCode(), ge.getMessage());
        } catch (Exception ex) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
            Console.logException(ResponseType.NOT_AVAILABLE.getMessage(), ex, idOperation);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse deleteUser(Integer id, String name, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            User user = new User();
            user.setUser_id(id);

            user = repository.getUser(id);
            user.setStatus_id(2);
            repository.save(user);
            info(name + " a eliminado al usuario con id: " + id, idOperation);

        } catch (Exception ex) {

            info(name + " intento eliminar al usuario con id: " + id, idOperation);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_AL_ELIMINAR);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse listUsers(Integer languageId) {
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        Map mp = new HashMap();
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        mp.put("list", repository.usersAll());
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }

    @Override
    public GenericResponse getUserIncludingBlocked(String user, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        UserDto userDto = new UserDto();
        User userEntity = repository.getUserStatusBlocked(user);
        ObjectMapper oMapper = new ObjectMapper();


        Map<String, Object> response;

        try {
            BeanUtils.copyProperties(userDto, userEntity);
            if (userDto.getUser_id() == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), null);
            }
            if (userDto.getUser_id() != null && userDto.getStatus_id() == 2) {
                response = oMapper.convertValue(userDto, Map.class);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_BLOQUEADO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), response);
            }
        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        response = oMapper.convertValue(userDto, Map.class);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), response);

    }

    @Override
    public GenericResponse getUserIncludingBlocked(Integer userId, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        Map mp = new HashMap();

        UserDto userDto = new UserDto();
        User userEntity = repository.getUserStatusBlocked(userId);
        try {
            BeanUtils.copyProperties(userDto, userEntity);
            if (userDto.getUser_id() == null) {
                //mp.put("list", userDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), null);
            }
            if (userDto.getUser_id() != null && userDto.getStatus_id() == 2) {
                mp.put("list", userDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_BLOQUEADO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
            }

        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        mp.put("list", userDto);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }

    @Override
    public GenericResponse getByEmail(String email, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        UserDto userDto = new UserDto();
        User userEntity = repository.getUserByEmail(email);
        ObjectMapper oMapper = new ObjectMapper();


        Map<String, Object> response;

        try {
            BeanUtils.copyProperties(userDto, userEntity);
            if (userDto.getUser_id() == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), null);
            }
            if (userDto.getUser_id() != null && userDto.getStatus_id() == 2) {
                response = oMapper.convertValue(userDto, Map.class);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_BLOQUEADO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), response);
            }
        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        response = oMapper.convertValue(userDto, Map.class);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), response);



    }


    @Override
    public GenericResponse getUser(Integer userId, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        Map mp = new HashMap();

        UserDto userDto = new UserDto();
        User userEntity = repository.getUser(userId);
        //repository.existsById(userId.longValue());
        try {
            BeanUtils.copyProperties(userDto, userEntity);
            if (userDto.getUser_id() == null) {
                userDto.setStatus_id(2);
                mp.put("list", userDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), null);
            }
        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        mp.put("list", userDto);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);

    }

    @Override
    public GenericResponse getUser(String user, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        UserDto userDto = new UserDto();
        User userEntity = repository.getUser(user);

        try {
            BeanUtils.copyProperties(userDto, userEntity);
        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        ObjectMapper oMapper = new ObjectMapper();


        Map<String, Object> response = oMapper.convertValue(userDto, Map.class);


        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), response);

    }

    @Override
    public GenericResponse getRoles() {
        Map mp = new HashMap();
        mp.put("listRoles", repository.getRoles());
        return new GenericResponse(ResponseType.ACEPT, mp);
    }

    @Override
    public GenericResponse getLanguages() {
        Map mp = new HashMap();
        mp.put("listLanguages", repository.getLanguages());
        return new GenericResponse(ResponseType.ACEPT, mp);
    }

    @Override
    public GenericResponse getUserStatus() {
        Map mp = new HashMap();
        mp.put("listUserStatus", repository.getUserStatus());
        return new GenericResponse(ResponseType.ACEPT, mp);
    }

    private void isValidData(UserDto dto, String idOperation, List<CoreErrorCode> error) throws GeneralException {
        CoreErrorCodeDto errorItem = null;

        if (dto.getRoleId() == null || dto.getRoleId() == null || dto.getUser_name() == null || dto.getPassword() == null || dto.getMail() == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            //return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), (Map<String, Object>) idOperation);
            throw new GeneralException(errorItem.getCode(), errorItem.getMessage(), idOperation);
        }

        if (Utils.isPassValid(dto.getPassword()) == false) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_EL_PASSWORD_NO_ES_VALIDO);
            throw new GeneralException(errorItem.getCode(), errorItem.getMessage(), idOperation);
        }
        if (dto.getUser_id() == null || dto.getUser_id() == 0) {
            int exist = repository.validUserNameRegistration(dto.getUser_name());
            if (exist > 0) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(),
                        "usuario(" + dto.getUser_name() + ")"), idOperation);
            }

            if (!Utils.isMailValid(dto.getMail())) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_VALIDO);
                throw new GeneralException(idOperation, errorItem.getCode(), String.format(errorItem.getMessage(), "Email"));
            }
            exist = repository.validEmailRegistration(dto.getMail());

            if (exist > 0) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(idOperation, errorItem.getCode(), String.format(errorItem.getMessage(), "Email"));
            }

        }


    }

    private static GenericResponse response(ResponseType type) {
        return new GenericResponse(type);
    }

    private static void info(String msg, String idOperacion) {
        Console.writeln(Console.Level.INFO, idOperacion, msg);
    }

    public GenericResponse getUsersBySearchFiltered(UsersSearchDTO dto, InfoTokenDto infoTokenDto) {
        List<CoreErrorCode> error = codeRepository.getAll(infoTokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;
        Map mp = new HashMap();
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        User usuarioLogeado = repository.getUserByUId(infoTokenDto.getUser_by_register());
        List<Map<String, Object>> listUsersFiltered = repository.getUsersBySearchFiltered( usuarioLogeado.getClient_id(), dto.getStringToSearch(), dto.getHierarchyId(), dto.getProfileId(), dto.getStatusId());
        mp.put("list", listUsersFiltered);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }

    @Override
    public GenericResponse getListHierarchies() {
        Map mp = new HashMap();
        mp.put("listHierarchies", repository.getHierarchies());
        return new GenericResponse(ResponseType.ACEPT, mp);
    }

    @Override
    public GenericResponse getListStatus() {
        Map mp = new HashMap();
        mp.put("listStatus", repository.getStatus());
        return new GenericResponse(ResponseType.ACEPT, mp);
    }

    @Override
    public GenericResponse newUpdateUserPtlSrv(UserPtlSrvDto dto, String name, Integer languageId) {
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        String idOperation = SystemLog.newTxnIdOperation();
        UserPtlSrv entity = new UserPtlSrv();
        UserPtlSrv savedUser;
        dto.setLanguage_id(languageId); // se establece por default
        dto.setUser_name(dto.getMail());
//        el lenguaje con que se raliza la solicitud de tabla core_language, ya que actualmente(21/07/2021 - EP06 Configuraciones del
//        Sistema US09 Usuarios) no se tiene un campo en el formulario que almacene el lenguaje
        boolean edit = false;
        info(name + String.format(" a entrando a administracioncion [%s de Usurario]", dto.getUser_id() != null ?
                "Edicion" : "Alata"), idOperation);

        try {
            isValidDataPtlSrv(dto, idOperation, error);

            if (dto.getUser_id() == null || dto.getUser_id() == 0) {
                int exist = repository.getUserByUsernamer(dto.getUser_name(), dto.getMail());
                if (exist > 0) {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                    throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario"), idOperation);
                }
                entity.setStatus_id(Status.ACTIVE);
            } else {
                dto.setUser_change(name);
                dto.setDate_change(new Date());
                edit = true;
                UserPtlSrv userEntity = repositoryPrtlSrv.getUserPrtlSrvStatusBlocked(dto.getUser_id());
                if( userEntity.getUser_id() != null && userEntity.getStatus_id() == 4 ) {
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                    throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
                }
            }

            Role role = roleRepository.getRoleById(dto.getRoleId());
            if (role == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "Rol"), idOperation);
            }

            dto.setDate_admission(new Date());
            dto.setDate_admission(!edit ? new Date() : dto.getDate_admission());
            dto.setUser_admission(!edit ? name : dto.getUser_admission());

            Map<String, Object> parametersMaps = new HashMap<>();
            parametersMaps.put("codigoAutorizacion", dto.getUser_name());
            parametersMaps.put("numeroReferencia", dto.getPassword());

            dto.setPassword(Utils.encryptSha256(dto.getPassword()));

            BeanUtils.copyProperties(entity, dto);

            if (dto.getPhone() != null && dto.getPhone() != "") {
                if (Utils.validatePhoneNumber(dto.getPhone())) {
                    entity.setPhone(dto.getPhone());
                } else {
                    entity.setPhone("");
                    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_VALIDO);
                    throw new GeneralException(idOperation, errorItem.getCode(),
                            String.format(errorItem.getMessage(), "Phone"));
                }
            }

            entity.setRole_id(role.getRoleId());
            entity.setClient_id(dto.getClientId());
            entity.setGroup_id(dto.getGroupId());
            entity.setStatus_id(dto.getStatus_id());
            entity.setLanguage_id(dto.getLanguage_id());
            savedUser = repositoryPrtlSrv.save(entity);

            if (!edit && savedUser != null) {
                BitacoraPassword bitacoraPassword = new BitacoraPassword();
                bitacoraPassword.setPassword(Utils.encryptSha256(dto.getPassword()));
                bitacoraPassword.setDateRegister(new Date());
                bitacoraPassword.setStatus(1);
                bitacoraPassword.setUserId(savedUser.getUser_id());
                bitacoraPasswordRepository.saveAndFlush(bitacoraPassword);
            }

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            SystemConfig systemConfig = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS");

            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> parametersMap = new HashMap<>();


            parametersMap.put("email-provider", "4");
            parametersMap.put("email-template", "7");
            parametersMap.put("email-addressee", dto.getMail());
            parametersMap.put("email-subject", "notice");
            parametersMap.put("email-message", "Hi!., welcome to SMART!..");

            parametersMap.put("email-template-params", parametersMaps);

            parametersMap.put("transaction-ticket", "XXX1234567543");

//            ResponseEntity<MailNotificationRequest> response = restTemplate.postForEntity(systemConfig.getValue(), parametersMap, MailNotificationRequest.class);


            RestTemplate restTemplateSms = new RestTemplate();
            SystemConfig systemConfigSms = configRepository.getSystemConfigByName("URL_SEND_NOTIFICATIONS_SMS");


            Map<String, Object> parametersMapSms = new HashMap<>();


            parametersMapSms.put("sms-provider", "4");
            parametersMapSms.put("sms-phoneNumber", dto.getPhone());
            System.out.println(dto.getPhone());
            parametersMapSms.put("sms-message", "Se ha dado de alta un nuevo usuario con este numero");
            parametersMapSms.put("transaction-ticket", "XXX1234567543");

//            ResponseEntity<SmsNotificationRequest> responseSms = restTemplateSms.postForEntity(systemConfigSms.getValue(), parametersMapSms, SmsNotificationRequest.class);


            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            info(errorItem.getMessage(), idOperation);

        } catch (GeneralException ge) {
            return new GenericResponse(ge.getErrorCode(), ge.getMessage());
        } catch (Exception ex) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
            Console.logException(ResponseType.NOT_AVAILABLE.getMessage(), ex, idOperation);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    private void isValidDataPtlSrv(UserPtlSrvDto dto, String idOperation, List<CoreErrorCode> error) throws GeneralException {
        CoreErrorCodeDto errorItem = null;

        if (dto.getRoleId() == null || dto.getRoleId() == 0 || dto.getUser_name() == null ||  dto.getUser_name() == "" ||
                dto.getPassword() == null || dto.getMail() == null || dto.getMail() == "" ||
                dto.getName() == null || dto.getLast_name1() == null || dto.getName() == "" || dto.getLast_name1() == "" ) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            throw new GeneralException(errorItem.getCode(), errorItem.getMessage(), idOperation);
        }
//        Comentado debido a que para el 21/07/2021 la historia de usuario EP06 Configuraciones del Sistema US09 Usuarios
//        no cuenta con descripción para trabajar el almacenamiento de password
//        if (Utils.isPassValid(dto.getPassword()) == false) {
//            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_EL_PASSWORD_NO_ES_VALIDO);
//            throw new GeneralException(errorItem.getCode(), errorItem.getMessage(), idOperation);
//        }

        if (dto.getUser_id() == null || dto.getUser_id() == 0) {
            int exist = repository.validUserNameRegistration(dto.getUser_name());
            if (exist > 0) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(),
                        "usuario(" + dto.getUser_name() + ")"), idOperation);
            }

            if (!Utils.isMailValid(dto.getMail())) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_VALIDO);
                throw new GeneralException(idOperation, errorItem.getCode(), String.format(errorItem.getMessage(), "Email"));
            }
            exist = repository.validEmailRegistration(dto.getMail());

            if (exist > 0) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                throw new GeneralException(idOperation, errorItem.getCode(), String.format(errorItem.getMessage(), "Email"));
            }

        }


    }

    @Override
    public GenericResponse deleteUserPrtlSrv(Integer id, String name, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            UserPtlSrv userEntity = repositoryPrtlSrv.getUserPrtlSrvStatusBlocked(id);
            if( userEntity.getUser_id() != null && userEntity.getStatus_id() == 4 ) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
            }

            User user = new User();
            user.setUser_id(id);

            user = repository.getUser(id);
            user.setStatus_id(4);   // Establecer status baja(id=4) Tabla status al 21/07/2021
            repository.save(user);
            info(name + " a eliminado al usuario con id: " + id, idOperation);

        } catch (Exception ex) {

            info(name + " intento eliminar al usuario con id: " + id, idOperation);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_AL_ELIMINAR);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse blockUserPrtlSrv(Integer id, String name, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            UserPtlSrv userEntity = repositoryPrtlSrv.getUserPrtlSrvStatusBlocked(id);
            if( userEntity.getUser_id() != null && userEntity.getStatus_id() == 4 ) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
            }

            User user = new User();
            user.setUser_id(id);

            user = repository.getUser(id);
            user.setStatus_id(3);       // Establecer status bloqueado(id=3) Tabla status al 21/07/2021
            repository.save(user);
            info(name + " a eliminado al usuario con id: " + id, idOperation);

        } catch (Exception ex) {

            info(name + " intento eliminar al usuario con id: " + id, idOperation);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_AL_ELIMINAR);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse getUserIncludingBlockedPrtlSrv(Integer userId, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        Map mp = new HashMap();

        UserPtlSrvDto userDto = new UserPtlSrvDto();
        UserPtlSrv userEntity = repositoryPrtlSrv.getUserPrtlSrvStatusBlocked(userId);
        try {
            BeanUtils.copyProperties(userDto, userEntity);
            userDto.setRoleId(userEntity.getRole_id());
            userDto.setGroupId(userEntity.getGroup_id());
            userDto.setClientId(userEntity.getClient_id());
            if (userDto.getUser_id() == null) {
                //mp.put("list", userDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), null);
            }
            if (userDto.getUser_id() != null && userDto.getStatus_id() == 2) {
                mp.put("list", userDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
            }

        } catch (IllegalAccessException illegalAccessException) {
            Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
        }
        mp.put("list", userDto);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }

    @Override
    public GenericResponse unblockUserPrtlSrv(Integer id, String name, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            UserPtlSrv userEntity = repositoryPrtlSrv.getUserPrtlSrvStatusBlocked(id);

            if( userEntity.getUser_id() == null ) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
            }

            if( userEntity.getUser_id() != null && userEntity.getStatus_id() == 4 ) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_INACTIVO);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
            }

            if( userEntity.getUser_id() != null && userEntity.getStatus_id() != 3 ) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_NO_VALIDO);
                throw new GeneralException(errorItem.getCode(), String.format(errorItem.getMessage(), "usuario " ), idOperation);
            }

            User user = new User();
            user.setUser_id(id);

            user = repository.getUser(id);
            user.setStatus_id(1);       // Establecer status bloqueado(id=3) Tabla status al 21/07/2021
            repository.save(user);
            info(name + " a eliminado al usuario con id: " + id, idOperation);

        } catch (Exception ex) {

            info(name + " intento eliminar al usuario con id: " + id, idOperation);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_AL_ELIMINAR);
            Console.logException(errorItem.getMessage(), ex, idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }
}
