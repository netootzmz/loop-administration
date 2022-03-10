package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.PortalRepository;
import com.smart.ecommerce.entity.core.Portal;
import com.smart.ecommerce.logging.Console;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class PortalRespositoryImpl implements PortalRepository {

  @PersistenceContext
  EntityManager em;

  @Override
  public Integer savePortal(Portal entity, String idOperation) {
    Integer success = 0;
    try {
      Session session = em.unwrap(Session.class);
      session.save(entity);
      success = 1;
    }catch (Exception ex){
      Console.logException("Error al guardar portal", ex, idOperation);
    }

    return success;
  }

  @Override
  public List<Portal> findPortalAll() {
    return em.createNamedQuery("findPortalAll").getResultList();
  }

  @Override
  public Portal findByName(String name) {
    List<Portal> portal = em.createNamedQuery("findByName").setParameter("name", name).getResultList();
    return portal.size() > 0 ? portal.get(0) : new Portal();
  }

  @Override
  public Portal portalById(Integer id) {
    List<Portal> portal = em.createNamedQuery("portalById").setParameter("portal_id", id).getResultList();
    return portal.size() > 0 ? portal.get(0) : new Portal();
  }

  @Override
  public Portal validationPortalById(Integer id) {
    List<Portal> portal = em.createNamedQuery("validationPortalById").setParameter("portal_id",id).getResultList();
    return portal.size() > 0 ? portal.get(0) : new Portal();
  }

}
