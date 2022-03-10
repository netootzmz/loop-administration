package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.ActionRepository;
import com.smart.ecommerce.entity.core.Action;
import com.smart.ecommerce.logging.Console;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class ActionRepositoryImpl implements ActionRepository {

  @PersistenceContext
  EntityManager em;

  @Override
  public Integer save(Action entity, String idOperation) {
    Integer success = 0;

    try {
      Session session = em.unwrap(Session.class);
      session.update(entity);

      success = 1;
    }catch (Exception ex){
      Console.logException("Error al guardar acción", ex, idOperation);
    }

    return success;
  }

  @Override
  public List<Action> getAll() {
    return em.createNamedQuery("getAllAction").getResultList();
  }

  @Override
  public Action getByName(String name) {
    List<Action> action = em.createNamedQuery("getByNameAction").setParameter("name", name).getResultList();
    return action.size() > 0 ? action.get(0) : new Action();
  }

  @Override
  public Action getById(Integer id) {
    List<Action> action = em.createNamedQuery("getByIdAction").setParameter("actionId", id).getResultList();
    return action.size() > 0 ? action.get(0) : new Action();
  }

}
