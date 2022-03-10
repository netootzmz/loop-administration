package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.ActionDto;
import com.smart.ecommerce.administration.model.UserDto;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.repository.ActionRepository;
import com.smart.ecommerce.administration.repository.ActionRepositoryCustom;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.service.ActionService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.Action;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.User;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.apache.commons.beanutils.BeanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smart.ecommerce.administration.service.impl.RoleServiceImpl.info;

@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    private ActionRepository repository;

    @Autowired
    ActionRepositoryCustom repositoryCustom;

    @Autowired
    private ErrorCodeRepository codeRepository;

    @Override
    public GenericResponse newEdit(ActionDto dto, String user, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            info(idOperation, "Entrando a administración de acciones");

            Action entity = repository.getByName(dto.getName());

            if (dto.getName() == null || dto.getDescription() == null || dto.getModuleId() == null || dto.getPortalId() == null || dto.getOrder() == null
                    || dto.getUrl() == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            if (entity.getActionId() != null && dto.getActionId() == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
                return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), "Acción"));
            }

            if (dto.getActionId() != null) {
                dto.setUserChange(user);
//                dto.setDate_change(new Date());
//                entity.setDate_change(new Date());
            } else {
                dto.setUserAdmission(user);
//                dto.setDate_admission(new Date());
//                entity.setDate_admission(new Date());
                dto.setStatus(1);
            }

            ModelMapper modelMapper = new ModelMapper();
            entity = modelMapper.map(dto, Action.class);
            if (dto.getActionId() != null) {
                entity.setDateChange(new Date());
            } else {
                entity.setDateAdmission(new Date());
            }

            repositoryCustom.save(entity);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        } catch (Exception ex) {
            Console.logException("Error al procesar datos de la acción", ex, idOperation);
        }
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse delete(Integer id, Integer status, String user, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            info(idOperation, "Entrando a administración de acciones");

            if (id == null || status == null) {
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
            }

            Action entity = repository.getById(id);
            entity.setStatus(status);
            entity.setDateChange(new Date());
            entity.setUserAdmission(user);

            repository.save(entity, idOperation);

            info(idOperation, String.format("%s a %s la acción %s", user, (status == 0 ? "Desactivado" : "Eliminado"), entity.getDescription()));
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
            Console.logException("Error al Activar/Desactivar acción", ex, idOperation);
        }
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse getAll(Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {
            Map mp = new HashMap();
            List<Action> list = repository.getAll();

            if (list.size() > 0) {
                mp.put("list", list);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                return new GenericResponse(errorItem.getCode(), ErrorCode.getError(error, "00").getMessage(), mp);
            }

        } catch (Exception ex) {
            Console.logException("Error al consultar acciones", ex, idOperation);
        }
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }

    @Override
    public GenericResponse getById(Integer id, Integer languageId) {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        try {

            Map mp = new HashMap();
            ActionDto actionDto = new ActionDto();
            Action action = repository.getById(id);

            try {
                BeanUtils.copyProperties(actionDto, action);
            } catch (IllegalAccessException illegalAccessException) {
                Console.writeln(Console.Level.INFO, idOperation, illegalAccessException.getMessage());
            } catch (InvocationTargetException invocationTargetException) {
                Console.writeln(Console.Level.INFO, idOperation, invocationTargetException.getMessage());
            }

            if (action.getModuleId() != null) {
                mp.put("action", actionDto);
                errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
                return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
            }

        } catch (Exception ex) {
            Console.logException("Error al consultar acciones por id", ex, idOperation);
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
        }
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }
}
