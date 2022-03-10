/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.entity.core.RoleAction;

/**
 * @author Eduardo Valeriano
 *
 */
@Repository
public interface RoleActionRepository extends JpaRepository<RoleAction, Integer>{
	
	@Query(value = " SELECT * FROM role_action " + 
			"WHERE role_id= 8 ", nativeQuery = true)
	  public RoleAction getRoleActionByRoleId(@Param("role_id")int role_id);
	
	@Query(value = " select  ", nativeQuery = true)
	  public RoleAction getRoleId(@Param("role_id")int role_id);

}
