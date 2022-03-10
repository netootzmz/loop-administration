package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.Custom.RoleRepositoryCustom;
import com.smart.ecommerce.entity.core.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RoleRepositoryImpl  implements RoleRepositoryCustom {

  @PersistenceContext
  EntityManager em;

  @Override
  public List<Role> getAllRole() {
    List<Role> roles = em.createNamedQuery("rulerAll").getResultList();
    return roles;
  }

  @Override
  public Role getRoleById(Integer id) {
    List<Role> role = em.createNamedQuery("rulerById").setParameter("id", id).getResultList();
    return role.size() > 0 ? role.get(0) : new Role();
  }

  @Override
  public Role getRoleByDescription(String description) {
    List<Role> role = em.createNamedQuery("rulerByDescription").setParameter("description", description).getResultList();
    return role.size() > 0 ? role.get(0) : new Role();
  }

  @Override
  public List<Role> getRolesAssociatedToUser(Integer roleId) {
    List<Role> role = em.createNamedQuery("isAssociateUser").setParameter("id", roleId).getResultList();
    return role;
  }

}
