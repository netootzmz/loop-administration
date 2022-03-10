package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.ModuleDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface ModuleService {

  GenericResponse newEdit(ModuleDto dto, String user, Integer languageId);

  GenericResponse delete(Integer id, Integer status, String user, Integer languageId);

  GenericResponse getAll(Integer languageId);

  GenericResponse getById(Integer id, Integer languageId);
}
