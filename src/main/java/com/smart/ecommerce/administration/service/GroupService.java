package com.smart.ecommerce.administration.service;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.GroupDTO;
import com.smart.ecommerce.administration.model.dto.GroupGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestGroupDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface GroupService {
	
	public GenericResponse createGroup(HttpServletRequest request, @RequestBody GroupGenericDTO groupDto, String idOperation) throws SQLIntegrityConstraintViolationException;
	
	public GenericResponse getGroup(HttpServletRequest request, @PathVariable String groupId, String idOperation);
	
	public GenericResponse updateGroup(HttpServletRequest request, @PathVariable String groupId, @RequestBody RequestGroupDTO groupDto, String idOperation);
	
	public GenericResponse deleteGroup(HttpServletRequest request, @PathVariable String groupId, String idOperation);

}
