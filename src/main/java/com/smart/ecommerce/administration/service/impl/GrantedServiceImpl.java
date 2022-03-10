/**
 *
 */
package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.administration.model.dto.GrantedDTO;
import com.smart.ecommerce.administration.model.dto.ResponseActioRoleDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.GenericGrantedAccessRepository;
import com.smart.ecommerce.administration.repository.GrantedRepository;
import com.smart.ecommerce.administration.service.GrantedService;
import com.smart.ecommerce.administration.util.ConvertDates;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GeneralConstant;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Eduardo Valeriano
 *
 */
@Service
public class GrantedServiceImpl implements GrantedService {


    @Resource
    GenericGrantedAccessRepository grantedAccessRepository;
    @Autowired
    private ErrorCodeRepository codeRepository;

    @Autowired
    GrantedRepository grantedRepository;


    @Override
    public GenericResponse getAccessGrantedByIdRole(int roleId, Integer languageId) throws GeneralException {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;
        Map<String, Object> informationResponse = new HashMap<>();

        if (roleId <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
        }
        List<Map<String, Object>> lst = new ArrayList<>();
        List<ResponseActioRoleDTO> lstResponse = new ArrayList<ResponseActioRoleDTO>();

        try {
            lst = grantedAccessRepository.getListRole(roleId, GeneralConstant.active);
            for (Map<String, Object> map : lst) {
                ResponseActioRoleDTO objActionRole = new ResponseActioRoleDTO();
                objActionRole.setRoleid(map.get("roleId").toString());
                objActionRole.setDescription(map.get("roleDescription").toString());
                objActionRole.setPortalId(map.get("portalId").toString());
                objActionRole.setPortalName(map.get("portalName").toString());
                objActionRole.setModuleNam(map.get("moduleName").toString());
                objActionRole.setModuleId(map.get("moduleId").toString());
                objActionRole.setActionId(map.get("actionId").toString());
                objActionRole.setActionName(map.get("actionName").toString());
                objActionRole.setActuinUrl(map.get("actionUrl").toString());
                lstResponse.add(objActionRole);
            }
            informationResponse.put("list", lstResponse);
        } catch (Exception ex) {
            Console.logException("Error al realizar la consulta", ex, idOperation);
        }
        if (lst == null || lst.size() <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_EXIST, idOperation);
        }
        errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
        return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//		return lstResponse;
    }


    @Override
    public GenericResponse getAccessGranted(GrantedDTO request, Integer languageId) throws GeneralException {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        if (request.getActionId() <= 0 || String.valueOf(request.getPortalId()) == null ||
                request.getModuleId() <= 0 || request.getActionId() <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
        }
        List<Map<String, Object>> lst = new ArrayList<>();
        List<Map<String, Object>> lstModule = new ArrayList<>();
        List<Map<String, Object>> lstPortal = new ArrayList<>();
        List<Map<String, Object>> lstAction = new ArrayList<>();

        lstModule = grantedAccessRepository.getModule(request.getModuleId());
        lstPortal = grantedAccessRepository.getPortal(request.getPortalId());
        lstAction = grantedAccessRepository.getAction(request.getActionId());
        if (lstModule == null || lstModule.size() <= 0 ||
                lstPortal == null || lstPortal.size() <= 0 || lstAction == null || lstAction.size() <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_EXIST, idOperation);
        }

        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        String fechaFormateada = ConvertDates.getDateDDMMYYYY(date);
        String response = "";
        try {
            response = grantedAccessRepository.insertRecord(request.getRoleId(), request.getPortalId(), request.getModuleId(), request.getActionId(), 1, fechaFormateada);
//			
        } catch (Exception ex) {
            Console.logException("Error al realizar la consulta", ex, idOperation);
        }
        if (response == null || response.equalsIgnoreCase("")) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_EXIST, idOperation);
        }

        return new GenericResponse(ResponseType.ACEPT);
    }


    @Override
    public GenericResponse deleteAccessGranted(GrantedDTO request, Integer languageId) throws Exception {
        String idOperation = SystemLog.newTxnIdOperation();
        List<CoreErrorCode> error = codeRepository.getAll(languageId);
        CoreErrorCodeDto errorItem = null;

        if (request.getActionId() <= 0 || String.valueOf(request.getPortalId()) == null
                || request.getModuleId() <= 0 || request.getActionId() <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
        }
        List<Map<String, Object>> lstRoleAction = new ArrayList<>();
        lstRoleAction = grantedAccessRepository.getRoleAction(request.getRoleId(), request.getActionId(),
                request.getPortalId(), request.getModuleId());
        if (lstRoleAction == null || lstRoleAction.size() <= 0) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_EXIST, idOperation);
        }
        String response = "";

        try {
            response = grantedAccessRepository.deleteRoleAction(request.getRoleId(), request.getPortalId(),
                    request.getModuleId(), request.getActionId());

        } catch (Exception ex) {
            ex.printStackTrace();
            Console.logException("Error al realizar la consulta", ex, idOperation);
        }

        if (response == null || response.equalsIgnoreCase("")) {
            errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
            return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_EXIST, idOperation);
        }

        return new GenericResponse(ResponseType.ACEPT);
    }

}
