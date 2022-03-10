package com.smart.ecommerce.administration.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.ClientDTO;
import com.smart.ecommerce.administration.model.dto.ClientGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestClientDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface ClientService {
	
	public GenericResponse createClient(HttpServletRequest request, ClientGenericDTO clientDto, String idOperation);
	
	public GenericResponse getClient(HttpServletRequest request, String clientId, String idOperation);
	
	public GenericResponse updateClient(HttpServletRequest request, @PathVariable String clientId, @RequestBody RequestClientDTO clientDto, String idOperation);
	
	public GenericResponse deleteClient(HttpServletRequest request, @PathVariable String clientId, String idOperation);

}
