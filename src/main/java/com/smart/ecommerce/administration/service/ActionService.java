package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.ActionDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface ActionService {

  GenericResponse newEdit(ActionDto dto, String user,Integer languageId);

  GenericResponse delete(Integer id, Integer status, String user,Integer languageId);

  GenericResponse getAll(Integer languageId);

  GenericResponse getById(Integer id,Integer languageId);
}
