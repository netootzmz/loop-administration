package com.smart.ecommerce.administration.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.contract.GroupContract;
import com.smart.ecommerce.administration.model.dto.GroupDTO;
import com.smart.ecommerce.administration.model.dto.GroupGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestGroupDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.service.GroupService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.logging.SystemLog;

@RestController
@RequestMapping("/groups")
public class GroupController implements GroupContract {
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private ErrorCodeRepository codeDao;
	
	@Override
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse createGroup(HttpServletRequest request, GroupGenericDTO groupDto) throws SQLIntegrityConstraintViolationException {
		GenericResponse response = new GenericResponse();
		String idOperation = SystemLog.newTxnIdOperation();
		List<CoreErrorCode> listCodes = codeDao.getAll(1);
		try {
			return groupService.createGroup(request, groupDto, idOperation);
		} catch (DataIntegrityViolationException e) {
			response.setCodeStatus("61");
			response.setMessage(ErrorCode.getError(listCodes, "61").getMessage());
		}
		return response;
		
	}

	@Override
	@PostMapping(value = "/get/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse getGroup(HttpServletRequest request, String groupId) {
		String idOperation = SystemLog.newTxnIdOperation();
		return groupService.getGroup(request, groupId, idOperation);
	}

	@Override
	@PostMapping(value = "/update/{groupId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse updateGroup(HttpServletRequest request, String groupId, RequestGroupDTO groupDto) {
		String idOperation = SystemLog.newTxnIdOperation();
		return groupService.updateGroup(request, groupId, groupDto, idOperation);
	}

	@Override
	@PostMapping(value = "/delete/{groupId}")
	public GenericResponse deleteGroup(HttpServletRequest request, String groupId) {
		String idOperation = SystemLog.newTxnIdOperation();
		return groupService.deleteGroup(request, groupId, idOperation);
	}

}
