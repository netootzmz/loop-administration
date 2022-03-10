package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.PortalDto;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.PortalRepository;
import com.smart.ecommerce.administration.service.PortalService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.Portal;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PortalService")
public class PortalServiceImpl implements PortalService {

  @Autowired
  private PortalRepository repository;

  @Autowired
  private ErrorCodeRepository codeRepository;

  @Override
  public GenericResponse newEdit(PortalDto dto, String user, Integer languageId) {
    String idOperation = SystemLog.newTxnIdOperation();
    List<CoreErrorCode> error = codeRepository.getAll(languageId);
    CoreErrorCodeDto errorItem = null;

    try {

      info(idOperation, "Entrando a administración de portal");

      if (dto.getName() == null || dto.getUrl() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Portal entity = repository.findByName(dto.getName());

      if (entity.getPortal_id() != null && dto.getPortal_id() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_PREEXISTENTES);
        return new GenericResponse(errorItem.getCode(), String.format(errorItem.getMessage(), "Portal"));
      }

      if (dto.getPortal_id() != null) {
        dto.setUser_change(user);
        dto.setDate_change(new Timestamp(new Date().getTime()));
      } else {
        dto.setUser_admission(user);
        dto.setDate_admission(new Timestamp(new Date().getTime()));
        dto.setStatus(1);
      }

      BeanUtils.copyProperties(entity, dto);

      repository.savePortal(entity, idOperation);

      info(idOperation, String.format("%s a %s el portal %s", user, (dto.getPortal_id() != null ? "Editado" : "Agregado"), dto.getDescription()));
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

    } catch (Exception ex) {
      Console.logException("Error al registrar portal", ex, idOperation);
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

      info(idOperation, "Entrando a administración de portal");

      if (id == null || status == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Portal entityValidate = repository.validationPortalById(id);

      if (entityValidate.getPortal_id() == null) {
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_IMPOSIBLE_REALIZAR_EDICION);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
      }

      Portal entity = repository.portalById(id);
      entity.setStatus(status);
      entity.setUser_change(user);
      entity.setDate_change(new Date());

      repository.savePortal(entity, idOperation);

      info(idOperation, String.format("%s a %s el portal %s", user, (status == 0 ? "Desactivado" : "Eliminado"), entity.getDescription()));
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage());

    } catch (Exception ex) {
      Console.logException("Error al registrar portal", ex, idOperation);
    }
    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_ERROR_EN_EL_SISTEMA);
    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
  }

  @Override
  public GenericResponse all(Integer languageId) {
    Map mp = new HashMap();
    List<CoreErrorCode> error = codeRepository.getAll(languageId);
    List<Portal> list = repository.findPortalAll();
    CoreErrorCodeDto errorItem = null;

    if (list.size() > 0) {
      mp.put("list", list);
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }

    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//    return new GenericResponse(ResponseType.LISTEMPTY);
  }

  @Override
  public GenericResponse getPortal(Integer id, Integer languageId) {
    Map mp = new HashMap();
    List<CoreErrorCode> error = codeRepository.getAll(languageId);
    CoreErrorCodeDto errorItem = null;

    Portal portal = repository.portalById(id);

    if (portal.getPortal_id() != null) {
      mp.put("portal", portal);
      errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
      return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), mp);
    }
    errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
    return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
  }


  private static void info(String idOperacion, String msg) {
    Console.writeln(Console.Level.INFO, idOperacion, msg);
  }
}
