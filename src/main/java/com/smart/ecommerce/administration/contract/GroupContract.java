package com.smart.ecommerce.administration.contract;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.GroupDTO;
import com.smart.ecommerce.administration.model.dto.GroupGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestGroupDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "group")
public interface GroupContract {
	
	@ApiOperation(value = "Método para dar de alta nuevos grupos", notes = "")
	public GenericResponse createGroup(HttpServletRequest request, @RequestBody GroupGenericDTO groupDto) throws SQLIntegrityConstraintViolationException;
	
	@ApiOperation(value = "Método para obtener la informacion de un grupo", notes = "Solo regresa grupos con estatus activo e inactivos (1 o 2)")
	public GenericResponse getGroup(HttpServletRequest request, @PathVariable String groupId);
	
	@ApiOperation(value = "Método para actualizar los datos de un grupo", notes = "Solo permite actualizar calve, nombre y estatus (0 o 1)")
	public GenericResponse updateGroup(HttpServletRequest request, @PathVariable String groupId, @RequestBody RequestGroupDTO groupDto);
	
	@ApiOperation(value = "Método para dar de baja grupos", notes = "Eliminación lógica (se cambia el estatus a 2)")
	public GenericResponse deleteGroup(HttpServletRequest request, @PathVariable String groupId);

}
