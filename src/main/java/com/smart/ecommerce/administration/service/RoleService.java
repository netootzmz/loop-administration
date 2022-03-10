package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.RoleDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface RoleService {

  GenericResponse getAllRole();

  GenericResponse getRoleById(Integer id);

  GenericResponse getRoleBydescription(String description);

  GenericResponse saveRole(RoleDto dto);

  GenericResponse updateRole(RoleDto dto);

  GenericResponse deleteRole(Integer id);
}
