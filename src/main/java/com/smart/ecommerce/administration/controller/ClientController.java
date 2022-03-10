package com.smart.ecommerce.administration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.contract.ClientContract;
import com.smart.ecommerce.administration.model.dto.ClientDTO;
import com.smart.ecommerce.administration.model.dto.ClientGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestClientDTO;
import com.smart.ecommerce.administration.service.ClientService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.logging.SystemLog;

@RestController
@RequestMapping("/clients")
public class ClientController implements ClientContract {
	
	@Autowired
	private ClientService clientService;

	@Override
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse createClient(HttpServletRequest request, @RequestBody ClientGenericDTO clientDto) {
		String idOperation = SystemLog.newTxnIdOperation();
		return clientService.createClient(request, clientDto, idOperation);
	}

	@Override
	@PostMapping(value = "/get/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse getClient(HttpServletRequest request, @PathVariable String clientId) {
		String idOperation = SystemLog.newTxnIdOperation();
		return clientService.getClient(request, clientId, idOperation);
	}

	@Override
	@PostMapping(value = "/update/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse updateClient(HttpServletRequest request, @PathVariable String clientId, @RequestBody RequestClientDTO clientDto) {
		String idOperation = SystemLog.newTxnIdOperation();
		return clientService.updateClient(request, clientId, clientDto, idOperation);
	}

	@Override
	@PostMapping(value = "/delete/{clientId}")
	public GenericResponse deleteClient(HttpServletRequest request, @PathVariable String clientId) {
		String idOperation = SystemLog.newTxnIdOperation();
		return clientService.deleteClient(request, clientId, idOperation);
	}

}
