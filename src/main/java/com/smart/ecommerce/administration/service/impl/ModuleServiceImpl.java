package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.ModuleDto;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.ModuleRepository;
import com.smart.ecommerce.administration.service.ModuleService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.Module;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smart.ecommerce.administration.service.impl.RoleServiceImpl.info;

@Service
public class ModuleServiceImpl implements ModuleService {

  @Autowired
  private ModuleRepository repository;

  @Autowired
  private ErrorCodeRepository codeRepository;

  @Override
  public GenericResponse newEdit(ModuleDto dto, String user, Integer languageId) {
    List<CoreErrorCode> error = codeRepository.getAll(languageId);
    String idOperation = SystemLog.newTxnIdOperation();
    CoreErrorCodeDto errorItem = null;

    try {
      info(idOperation, "Entrando a administración de módulo");

      if (dto.getName() == null || dto.getPortal_id() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Module entity = repository.findModuleByName(dto.getName());

      if (entity.getModule_id() != null && dto.getModule_id() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
        return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), "Módulo"));
      }

      if (dto.getModule_id() != null) {
        dto.setUser_change(user);
        dto.setDate_change(new Date());
      } else {
        dto.setUser_admission(user);
        dto.setDate_admission(new Date());
        dto.setStatus(1);
      }

      ModelMapper modelMapper = new ModelMapper();
      entity = modelMapper.map(dto, Module.class);

      repository.save(entity, idOperation);
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

    } catch (Exception ex) {
      Console.logException("Error al procesar datos de módulo", ex, idOperation);
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

      info(idOperation, "Entrando a administración de módulo");

      if (id == null || status == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Module entityValidate = repository.validationModuleById(id);

      if (entityValidate.getPortal_id() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_IMPOSIBLE_REALIZAR_EDICION);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Module entity = repository.findModuleById(id);
      entity.setStatus(status);
      entity.setUser_change(user);
      entity.setDate_change(new Timestamp(new Date().getTime()));

      repository.save(entity, idOperation);

      info(idOperation, String.format("%s a %s el módulo %s", user, (status == 0 ? "Desactivado" : "Eliminado"), entity.getDescription()));
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

    } catch (Exception ex) {
      Console.logException("Error al registrar módulo", ex, idOperation);
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
      List<Module> list = repository.findModuleAll();

      if (list.size() > 0) {
        mp.put("list", list);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
      }

    } catch (Exception ex) {
      Console.logException("Error al consultar módulos", ex, idOperation);
    }
    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
  }

  @Override
  public GenericResponse getById(Integer id, Integer languageId) {
    List<CoreErrorCode> error = codeRepository.getAll(languageId);
    String idOperation = SystemLog.newTxnIdOperation();
    CoreErrorCodeDto errorItem = null;

    try {
      Map mp = new HashMap();
      Module module = repository.findModuleById(id);

      if (module.getModule_id() != null) {
        mp.put("module", module);
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
      }

    } catch (Exception ex) {
      Console.logException("Error al consultar módulos por id", ex, idOperation);
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
    }
    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
  }

}
