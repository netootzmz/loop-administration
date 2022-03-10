package com.smart.ecommerce.administration.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.ClientDTO;
import com.smart.ecommerce.administration.model.dto.ClientGenericDTO;
import com.smart.ecommerce.administration.model.dto.RequestClientDTO;
import com.smart.ecommerce.administration.repository.ClientRepository;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.service.ClientService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.entity.admin.Client;
import com.smart.ecommerce.entity.core.CoreErrorCode;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ErrorCodeRepository codeDao;
	@Autowired
	private ClientRepository clientDao;

	@Override
	@Transactional(readOnly = false)
	public GenericResponse createClient(HttpServletRequest request, ClientGenericDTO clientDto, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (clientDto.getCve() == null || clientDto.getName() == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		if (!(clientDto.getStatusId() == 0 || clientDto.getStatusId() == 1)) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
			return response;
		}
		if (clientDao.findByClientIdAndCveAndNameAndStatusIn(clientDto.getCve()).size() > 0) {
			response.setCodeStatus("02");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "02").getMessage(), "cliente"));
			return response;
		}
		try {
			Client client = new Client();
			String userId = InfoToken.getAditionalInfoToken(request, "usrId", idOperation);
			client.setClientId(clientDto.getCve());
			client.setCve(clientDto.getCve());
			client.setName(clientDto.getName());
			client.setStatusId(clientDto.getStatusId());
			client.setCreatedAt(new Date());
			client.setUserByRegister(Integer.parseInt(userId));
			clientDao.save(client);
			response.setCodeStatus("00");
			response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
			return response;

		} catch (Exception e) {
			e.getMessage();
			response.setCodeStatus("57");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "57").getMessage()));
		}
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public GenericResponse getClient(HttpServletRequest request, String clientId, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (clientId == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		Client client = clientDao.findByClientId(clientId);
		if (client == null || client.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "cliente"));
			return response;
		}
		ClientDTO responseClientDto = new ClientDTO(client.getClientId(), client.getCve(), client.getName(),
				client.getStatusId());
		Map<String, Object> information = new HashMap<>();
		information.put("results", responseClientDto);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		response.setInformation(information);
		return response;
	}

	@Override
	@Transactional(readOnly = false)
	public GenericResponse updateClient(HttpServletRequest request, @PathVariable String clientId,
			@RequestBody RequestClientDTO clientDto, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		if (clientId == null) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "clientId"));
			return response;
		}
		if (clientDto.getStatusId() != null && (!(clientDto.getStatusId() == 0 || clientDto.getStatusId() == 1))) {
			response.setCodeStatus("05");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
			return response;
		}
		if (clientDto.getCve() == null && clientDto.getName() == null && clientDto.getStatusId() == null) {
			response.setCodeStatus("04");
			response.setMessage(ErrorCode.getError(listCodes, "04").getMessage());
			return response;
		}
		Client actualClient = clientDao.findByClientId(clientId);
		if (actualClient == null || actualClient.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "cliente"));
			return response;
		}
		if (clientDto.getCve() != null) {
			actualClient.setCve(clientDto.getCve());
		}
		if (clientDto.getName() != null) {
			actualClient.setName(clientDto.getName());
		}
		if (clientDto.getStatusId() != null) {
			actualClient.setStatusId(clientDto.getStatusId());
		}
		clientDao.save(actualClient);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		return response;
	}

	@Override
	@Transactional(readOnly = false)
	public GenericResponse deleteClient(HttpServletRequest request, @PathVariable String clientId, String idOperation) {
		GenericResponse response = new GenericResponse();
		String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
		List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
		Client client = clientDao.findByClientId(clientId);
		if (client == null || client.getStatusId() == 2) {
			response.setCodeStatus("03");
			response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "cliente"));
			return response;
		}
		client.setStatusId(2);
		clientDao.save(client);
		response.setCodeStatus("00");
		response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
		return response;
	}

}
