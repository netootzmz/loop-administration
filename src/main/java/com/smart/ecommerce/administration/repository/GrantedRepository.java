package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.Action;
import com.smart.ecommerce.entity.core.RoleAction;


public interface GrantedRepository {
	RoleAction getById(Integer roleId);
	Integer save(RoleAction entity, String idOperation);
	

}
