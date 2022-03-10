package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.Custom.UserPrtlSrvRepositoryCustom;
import com.smart.ecommerce.entity.core.UserPtlSrv;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserPrtlSrvRepositoryImpl implements UserPrtlSrvRepositoryCustom {

  @PersistenceContext
  EntityManager em;

  @Override
  public UserPtlSrv getUserPrtlSrvStatusBlocked(Integer userId) {
      List<UserPtlSrv> lst =  em.createNamedQuery("userPrtlSrvByIdStatusBlocked").setParameter("userId", userId).getResultList();
      return lst.size() > 0 ? lst.get(0) :  new UserPtlSrv();
  }

}
