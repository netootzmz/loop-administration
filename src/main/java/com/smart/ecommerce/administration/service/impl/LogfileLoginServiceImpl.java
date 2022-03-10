package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.LogfileLoginDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.LogfileLoginRepository;
import com.smart.ecommerce.administration.repository.UserRepository;
import com.smart.ecommerce.administration.service.LogfileLoginService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.admin.LogfileLogin;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.User;
import com.smart.ecommerce.logging.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service("LogfileLoginService")
public class LogfileLoginServiceImpl implements LogfileLoginService {
    @Autowired
    LogfileLoginRepository logfileLoginRepository;


    @Autowired
    UserRepository userRepository;


    @Autowired
    ErrorCodeRepository codeRepository;

    @Override
    public GenericResponse createLogFileLogin(HttpServletRequest request, InfoTokenDto tokenDto) {

        String idOperation = SystemLog.newTxnIdOperation();
        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
        Map<String, Object> informationResponse = new HashMap<>();
        List<CoreErrorCode> error = codeRepository.getAll(tokenDto.getLanguage_id());
        CoreErrorCodeDto errorItem = null;

        User user = userRepository.getUserByUId(tokenDto.getUser_by_register());


        if(null == user  || String.valueOf(user.getUser_id()).isEmpty()) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_USUARIO_NO_EXISTE);
            informationResponse.put("idOperation", idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
        }

        LogfileLogin logfileLoginEntity = logfileLoginRepository.getLogfileLoginByUserId(tokenDto.getUser_by_register());

        if(null != logfileLoginEntity && !String.valueOf(logfileLoginEntity.getUserId()).isEmpty() ){

            logfileLoginEntity.setDateLogin(new Date());
            logfileLoginRepository.saveAndFlush(logfileLoginEntity);

            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            informationResponse.put("idOperation", idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);


        }else{

            LogfileLogin logfileLogin = new LogfileLogin();

            logfileLogin.setUserId(user.getUser_id());
            logfileLogin.setDateLogin(new Date());

            logfileLoginRepository.saveAndFlush(logfileLogin);


            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            informationResponse.put("idOperation", idOperation);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);



        }






    }
}
