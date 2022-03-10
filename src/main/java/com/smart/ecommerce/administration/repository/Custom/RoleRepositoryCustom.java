package com.smart.ecommerce.administration.repository.Custom;

import com.smart.ecommerce.entity.core.Role;

import java.util.List;

public interface RoleRepositoryCustom {
  List<Role> getAllRole();

  Role getRoleById(Integer id);

  Role getRoleByDescription(String description);

  List<Role> getRolesAssociatedToUser(Integer roleId);
}
