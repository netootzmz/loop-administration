package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.RestorePasswordRepository;
import com.smart.ecommerce.entity.core.CoreRestorePassword;
import com.smart.ecommerce.logging.Console;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class RestorePasswordRepositoryImpl implements RestorePasswordRepository {

  @PersistenceContext
  EntityManager em;

  @Override
  public boolean saveRestorePassword(CoreRestorePassword restorePassword, String idOperation) {
    info(idOperation, "Almacenamiento de código de verificación");
    boolean success = false;

    try {

      Session session = em.unwrap(Session.class);
      session.save(restorePassword);
      success = true;

    } catch (Exception ex) {

      Console.logException("Error al almacenar el registro de código de verficación", ex, idOperation);

    }

    return success;
  }

  @Override
  public CoreRestorePassword getByUserIdVerficationCode(Integer userId, String verificationCode) {
    List<CoreRestorePassword> restorePassword = em.createNamedQuery("getByUserIdVerficationCode").setParameter("userId", userId).setParameter("verificationCode", verificationCode).getResultList();
    return restorePassword.size() > 0 ? restorePassword.get(0) : new CoreRestorePassword();
  }

  @Override
  public CoreRestorePassword getById(Integer restorePassId) {
    List<CoreRestorePassword> restorePassword = em.createNamedQuery("getById").setParameter("restorePassId", restorePassId).getResultList();
    return restorePassword.size() > 0 ? restorePassword.get(0) : new CoreRestorePassword();
  }

  public static void info(String msg, String idOperation) {
    Console.writeln(Console.Level.INFO, idOperation, msg);
  }

}
