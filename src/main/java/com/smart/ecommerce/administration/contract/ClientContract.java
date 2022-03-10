package com.smart.ecommerce.administration.contract;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.ClientDTO;
import com.smart.ecommerce.administration.model.dto.ClientGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestClientDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "client")
public interface ClientContract {
	
	@ApiOperation(value = "Método para dar de alta nuevos clientes", notes = "")
	public GenericResponse createClient(HttpServletRequest request, @RequestBody ClientGenericDTO clientDto);
	
	@ApiOperation(value = "Método para obtener la informacion de un cliente", notes = "Solo regresa clientes con estatus activo e inactivos (1 o 2)")
	public GenericResponse getClient(HttpServletRequest request, @PathVariable String clientId);
	
	@ApiOperation(value = "Método para actualizar los datos de un cliente", notes = "Solo permite actualizar calve, nombre y estatus (0 o 1)")
	public GenericResponse updateClient(HttpServletRequest request, @PathVariable String clientId, @RequestBody RequestClientDTO clientDto);
	
	@ApiOperation(value = "Método para dar de baja clientes", notes = "Eliminación lógica (se cambia el estatus a 2)")
	public GenericResponse deleteClient(HttpServletRequest request, @PathVariable String clientId);

}
