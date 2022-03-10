/**
 * 
 */
package com.smart.ecommerce.administration.service;

import java.util.List;

import com.smart.ecommerce.administration.model.dto.GrantedDTO;
import com.smart.ecommerce.administration.model.dto.ResponseActioRoleDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

/**
 * @author Eduardo Valeriano
 *
 */
public interface GrantedService {
	GenericResponse getAccessGrantedByIdRole(int roleId, Integer languageId )throws Exception;

	GenericResponse getAccessGranted(GrantedDTO request, Integer languageId )throws Exception ;

	GenericResponse deleteAccessGranted(GrantedDTO request, Integer languageId )throws Exception;

	

}
