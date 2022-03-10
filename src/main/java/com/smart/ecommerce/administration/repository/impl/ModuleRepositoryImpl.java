package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.ModuleRepository;
import com.smart.ecommerce.entity.core.Module;
import com.smart.ecommerce.logging.Console;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ModuleRepositoryImpl implements ModuleRepository {

  @PersistenceContext
  EntityManager em;

  @Override
  public boolean save(Module entity, String idOperation) {
    boolean success = false;
    try {
      Session session = em.unwrap(Session.class);
      session.save(entity);
      success = true;
    }catch (Exception ex){
      Console.logException("Error al guardar", ex, idOperation);
    }

    return success;
  }

  @Override
  public List<Module> findModuleAll() {
    return em.createNamedQuery("allModules").getResultList();
  }

  @Override
  public Module findModuleByName(String name) {
    List<Module> module = em.createNamedQuery("moduleByName").setParameter("name", name).getResultList();
    return module.size() > 0 ? module.get(0) : new Module();
  }

  @Override
  public Module findModuleById(Integer id) {
    List<Module> module = em.createNamedQuery("moduleById").setParameter("module_id", id).getResultList();
    return module.size() > 0 ? module.get(0) : new Module();
  }

  @Override
  public Module validationModuleById(Integer id) {
    List<Module> module = em.createNamedQuery("validateModuleById").setParameter("module_id", id).getResultList();
    return module.size() > 0 ? module.get(0) : new Module();
  }
}
