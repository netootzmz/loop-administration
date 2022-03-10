package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.repository.Custom.UserPrtlSrvRepositoryCustom;
import com.smart.ecommerce.entity.core.UserPtlSrv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrtlSrvRepository extends JpaRepository<UserPtlSrv, Long>, UserPrtlSrvRepositoryCustom {

}
