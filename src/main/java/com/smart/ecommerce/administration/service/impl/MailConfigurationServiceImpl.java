package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailConfigurationDTO;
import com.smart.ecommerce.administration.repository.CommonRepository;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.MailConfigurationRepository;
import com.smart.ecommerce.administration.service.MailConfigurationService;
import com.smart.ecommerce.administration.util.DecrypDatatest;
import com.smart.ecommerce.administration.util.EncriptDataTest;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.configuration.MailConfiguration;
import com.smart.ecommerce.entity.configuration.SecurityMailCatalog;
import com.smart.ecommerce.entity.configuration.ServerMailCatalog;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.logging.SystemLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("mailConfigurationService")
public class MailConfigurationServiceImpl implements MailConfigurationService {

    private static final Logger log = LogManager.getLogger(MailConfigurationServiceImpl.class);


    @Autowired
    MailConfigurationRepository mailConfigurationRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    private ErrorCodeRepository codeRepository;


    @Override
    public GenericResponse addMailConfiguration(InfoTokenDto infoTokenDto, MailConfigurationDTO mailConfigurationDTO) {

        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(infoTokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;
        Map<String, Object> informationResponse = new HashMap<>();

        SecurityMailCatalog securityMailCatalog = commonRepository.get(SecurityMailCatalog.class, mailConfigurationDTO.getSecurityMailId());



        if (mailConfigurationDTO.getClientId() == null ||
                mailConfigurationDTO.getServerMail() == null ||
                mailConfigurationDTO.getPortServer() == null ||
                mailConfigurationDTO.getPasswordMail() == null ||
                mailConfigurationDTO.getUserMail() == null ) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }

        MailConfiguration mailConfigurationUser = mailConfigurationRepository.getMailConfigurationByClientId(mailConfigurationDTO.getClientId());

        if (mailConfigurationUser != null ) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }


        try {

            Date fechaActual = new Date();
            DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss z");
            String dateFormatter = dateFormat.format(fechaActual);
            String fechaToHexa = EncriptDataTest.toHexadecimal(dateFormatter);
            Key key = EncriptDataTest.generaLLaveByString32char(fechaToHexa.substring(0, 32));
            String datosEncriptados = EncriptDataTest.encripta(key, mailConfigurationDTO.getPasswordMail());



            MailConfiguration mailConfiguration = new MailConfiguration();

            mailConfiguration.setClientId(mailConfigurationDTO.getClientId());
            mailConfiguration.setServerMail(mailConfigurationDTO.getServerMail());
            mailConfiguration.setPortServer(mailConfigurationDTO.getPortServer());
            mailConfiguration.setSecurityMailId(mailConfigurationDTO.getSecurityMailId());
            mailConfiguration.setUserMail(mailConfigurationDTO.getUserMail());
            mailConfiguration.setPasswordMail(datosEncriptados);
            mailConfiguration.setStatusId(1);
            mailConfiguration.setUser_by_register(infoTokenDto.getUser_by_register());
            mailConfiguration.setCreatedAt(fechaActual);


            mailConfigurationRepository.saveAndFlush(mailConfiguration);


            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }


    }

    @Override
    public GenericResponse updateMailConfiguration(InfoTokenDto infoTokenDto, Integer mailConfigurationId, MailConfigurationDTO mailConfigurationDTO) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(infoTokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;
        Map<String, Object> informationResponse = new HashMap<>();

        SecurityMailCatalog securityMailCatalog = commonRepository.get(SecurityMailCatalog.class, mailConfigurationDTO.getSecurityMailId());

        if (securityMailCatalog == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }


        if (mailConfigurationId == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }

        try {

            Date fechaActual = new Date();
            DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss z");
            String dateFormatter = dateFormat.format(fechaActual);
            String fechaToHexa = EncriptDataTest.toHexadecimal(dateFormatter);
            log.info("************** " + fechaToHexa.length());
            Key key = EncriptDataTest.generaLLaveByString32char(fechaToHexa.substring(0, 32));
            String datosEncriptados = EncriptDataTest.encripta(key, mailConfigurationDTO.getPasswordMail());

            String datosCrudos = DecrypDatatest.desencriptaCOnLLave(key, datosEncriptados);
            log.info("datos Desencriptados " + datosCrudos);


            MailConfiguration mailConfiguration = mailConfigurationRepository.findById(mailConfigurationId).get();

            mailConfiguration.setClientId(mailConfigurationDTO.getClientId());
            mailConfiguration.setServerMail(mailConfigurationDTO.getServerMail());
            mailConfiguration.setPortServer(mailConfigurationDTO.getPortServer());
            mailConfiguration.setSecurityMailId(mailConfigurationDTO.getSecurityMailId());
            mailConfiguration.setUserMail(mailConfigurationDTO.getUserMail());
            mailConfiguration.setPasswordMail(datosEncriptados);
            mailConfiguration.setStatusId(1);
            mailConfiguration.setUser_by_register(infoTokenDto.getUser_by_register());
            mailConfiguration.setCreatedAt(fechaActual);
            mailConfiguration.setUpdateDate(fechaActual);


            mailConfigurationRepository.saveAndFlush(mailConfiguration);


            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

    }

    @Override
    public GenericResponse getMailConfiguration(InfoTokenDto infoTokenDto, String clientId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(infoTokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;
        Map<String, Object> informationResponse = new HashMap<>();


        if (clientId == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }

        MailConfiguration mailConfiguration = mailConfigurationRepository.getMailConfigurationByClientId(clientId);


        if(mailConfiguration == null){

            errorItem = ErrorCode.getError(error, ErrorCode.ERORR_CODE_CONFIGURACION_MAIL_INEXISTENTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        }

        informationResponse.put("configurationMail",mailConfiguration);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(),informationResponse);


    }

    @Override
    public GenericResponse deleteMailConfiguration(InfoTokenDto infoTokenDto, Integer mailConfigurationId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(infoTokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;
        Map<String, Object> informationResponse = new HashMap<>();


        if (mailConfigurationId == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }

        MailConfiguration mailConfiguration = mailConfigurationRepository.findById(mailConfigurationId).get();

        if (mailConfiguration == null) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }

        mailConfiguration.setStatusId(2);

        mailConfigurationRepository.saveAndFlush(mailConfiguration);



        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());



    }
}
