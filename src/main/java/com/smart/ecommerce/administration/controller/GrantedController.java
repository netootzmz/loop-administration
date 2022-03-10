/**
 * 
 */
package com.smart.ecommerce.administration.controller;

import java.util.List;

import com.smart.ecommerce.administration.util.InfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.model.dto.GrantedDTO;
import com.smart.ecommerce.administration.model.dto.GrantedForRoleDTO;
import com.smart.ecommerce.administration.model.dto.ResponseActioRoleDTO;
import com.smart.ecommerce.administration.service.GrantedService;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eduardo Valeriano
 *
 */
@RestController
@RequestMapping("/manage-access")
public class GrantedController {
	@Autowired private GrantedService grantedService;
	
	@PostMapping("/getAllByRole")
	@ApiOperation("Servicio que regresa una lista de registros de la tabla role_action")
	public GenericResponse getAllRoleAction(HttpServletRequest request, @RequestBody GrantedForRoleDTO requestGrantedForRole)throws Exception{
		Integer languageId = InfoToken.getLanguageId(request);
		return  grantedService.getAccessGrantedByIdRole(requestGrantedForRole.getRoleId(), languageId );
	}
	
	
	@PostMapping("/add")
	@ApiOperation("Servicio que agrega un permisos a un id_role "
			+ "Datos necedarios: roleId,portalId,moduleId,actionId")
	public GenericResponse getAccessGranted(HttpServletRequest request, @RequestBody GrantedDTO requestGranted)throws Exception  {
		Integer languageId = InfoToken.getLanguageId(request);
		return grantedService.getAccessGranted(requestGranted, languageId);
	}
	
	@PostMapping("/delete")
	@ApiOperation("Servicio que elimina permisos a un id_role *Solo regresa registros activos "
			+ "Datos necedarios: roleId,portalId,moduleId,actionId")
	public GenericResponse deleteAccessGranted(HttpServletRequest request, @RequestBody GrantedDTO requestGranted )throws Exception {
		Integer languageId = InfoToken.getLanguageId(request);
		return grantedService.deleteAccessGranted(requestGranted, languageId );
	}
	
	
	
	

}
