package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.SystemConfigRepository;
import com.smart.ecommerce.entity.checkout.SystemConfig;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class SystemConfigRepositoryImpl implements SystemConfigRepository {

  @PersistenceContext
  EntityManager em;

  @Override
  public SystemConfig getSystemConfigByName(String name) {
    List<SystemConfig> systemConfig = em.createNamedQuery("SystemConfig.findByName").setParameter("name", name).getResultList();
    return systemConfig.size() > 0 ? systemConfig.get(0) : new SystemConfig();
  }
}
