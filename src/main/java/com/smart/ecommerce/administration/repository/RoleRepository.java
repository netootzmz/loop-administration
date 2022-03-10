package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.repository.Custom.RoleRepositoryCustom;
import com.smart.ecommerce.entity.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {

}
