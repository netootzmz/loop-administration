package com.smart.ecommerce.administration.repository.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.repository.GrantedRepository;
import com.smart.ecommerce.entity.core.RoleAction;
import com.smart.ecommerce.logging.Console;

@Transactional
@Repository
public class GrantedRepositoryImp implements GrantedRepository{

	@PersistenceContext
	  EntityManager em;
	
	@Override
	public RoleAction getById(Integer roleId) {
		List<RoleAction> roleAction = em.createNamedQuery("getListRoleActionByRoleId").setParameter("roleId", roleId).getResultList();
		return roleAction.size()>0?roleAction.get(0):new RoleAction();		
	}

	@Override
	public Integer save(RoleAction entity, String idOperation) {
		Integer success = 0;
		System.out.println("estanos a un punto de insertar ");
		System.out.println(entity.getRole());
		
		 try {
		      Session session = em.unwrap(Session.class);
		      session.save(entity);
		      success = 1;
		    }catch (Exception ex){
		      Console.logException("Error al guardar accion", ex, idOperation);
		    }
		
		return success;
	}

}
