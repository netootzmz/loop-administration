package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.LogfileLoginDTO;
import com.smart.ecommerce.administration.util.GenericResponse;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

public interface LogfileLoginService {

GenericResponse createLogFileLogin (HttpServletRequest request, InfoTokenDto tokenDto);

}
