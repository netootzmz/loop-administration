package com.smart.ecommerce.administration.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.ecommerce.administration.model.dto.GroupDTO;
import com.smart.ecommerce.administration.model.dto.GroupGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestGroupDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.GroupRepository;
import com.smart.ecommerce.administration.service.GroupService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.entity.admin.Group;
import com.smart.ecommerce.entity.core.CoreErrorCode;

@Service
public class GroupServiceImpl implements GroupService {
	
	@Autowired
	private ErrorCodeRepository codeDao;
	@Autowired
	private GroupRepository groupDao;

	@Override
	@Transactional(readOnly = false)
	public GenericResponse createGroup(HttpServletRequest request, GroupGenericDTO groupDto, String idOperation) throws SQLIntegrityConstraintViolationException{
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (groupDto.getClientId() == null || groupDto.getGroupLevelId() == null || groupDto.getCve() == null || groupDto.getName() == null || groupDto.getStatusId() == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		if (!(groupDto.getStatusId() == 0 || groupDto.getStatusId() == 1)) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
			return response;
		}
		if (groupDao.findByGroupIdAndClientIdAndStatusIn(groupDto.getCve()).size() > 0) {
			response.setCodeStatus("02");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "02").getMessage(), "grupo"));
			return response;
		}
		try {
			Group group = new Group();
			String userId = InfoToken.getAditionalInfoToken(request, "usrId", idOperation);
			group.setGroupId(groupDto.getCve());
			group.setCreatedAt(new Date());
			group.setClientId(groupDto.getClientId());
			group.setCve(groupDto.getCve());
			group.setName(groupDto.getName());
			if (groupDto.getParentGroupId() != null) {
				group.setParentGroupId(groupDto.getParentGroupId());
			}
			group.setStatusId(groupDto.getStatusId());
			group.setUserByRegister(Integer.parseInt(userId));
			group.setGroupLevelId(groupDto.getGroupLevelId());
			groupDao.save(group);
			response.setCodeStatus("00");
			response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		} catch (DataIntegrityViolationException e) {
			e.getMessage();
		}
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public GenericResponse getGroup(HttpServletRequest request, String groupId, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (groupId == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		Group group = groupDao.findByGroupId(groupId);
		if (group == null || group.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "grupo"));
			return response;
		}
		GroupDTO responseGroupDto = new GroupDTO(group.getGroupId(), group.getClientId(), group.getParentGroupId(), group.getGroupLevelId(), group.getCve(), group.getName(), group.getStatusId());
		Map<String, Object> information = new HashMap<>();
		information.put("results", responseGroupDto);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		response.setInformation(information);
		return response;
	}

	@Override
	@Transactional(readOnly = false)
	public GenericResponse updateGroup(HttpServletRequest request, String groupId, RequestGroupDTO groupDto,
			String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (groupId == null) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "groupId"));
			return response;
		}
		if (groupDto.getStatusId() != null && (!(groupDto.getStatusId() == 0 || groupDto.getStatusId() == 1))) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
			return response;
		}
		if (groupDto.getClientId() == null && groupDto.getParentGroupId() == null && groupDto.getGroupLevelId() == null && groupDto.getCve() == null && groupDto.getName() == null && groupDto.getStatusId() == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		Group actualGroup = groupDao.findByGroupId(groupId);
		if (actualGroup == null || actualGroup.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "grupo"));
			return response;
		}
		if (groupDto.getClientId() != null) {
			actualGroup.setClientId(groupDto.getClientId());
		}
		if (groupDto.getParentGroupId() != null) {
			actualGroup.setParentGroupId(groupDto.getParentGroupId());
		}
		if (groupDto.getGroupLevelId() != null) {
			actualGroup.setGroupLevelId(groupDto.getGroupLevelId());
		}
		if (groupDto.getCve() != null) {
			actualGroup.setCve(groupDto.getCve());
		}
		if (groupDto.getName() != null) {
			actualGroup.setName(groupDto.getName());
		}
		if (groupDto.getStatusId() != null) {
			actualGroup.setStatusId(groupDto.getStatusId());
		}
		groupDao.save(actualGroup);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		return response;
	}

	@Override
	public GenericResponse deleteGroup(HttpServletRequest request, String groupId, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (groupId == null) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "groupId"));
			return response;
		}
		Group group = groupDao.findByGroupId(groupId);
		if (group == null || group.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "grupo"));
			return response;
		}
		group.setStatusId(2);
		groupDao.save(group);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		return response;
	}

}
