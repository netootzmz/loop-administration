package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.PortalDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface PortalService {
  GenericResponse newEdit(PortalDto dto, String user, Integer languageId);

  GenericResponse delete(Integer id, Integer status, String user, Integer languageId);

  GenericResponse all(Integer languageId);

  GenericResponse getPortal(Integer id, Integer languageId);
}
