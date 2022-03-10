package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.CommonRepository;
import com.smart.ecommerce.entity.core.CoreReader;
import com.smart.ecommerce.logging.Console;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Repository("commonRepository")
public class CommonRepositoryImpl implements CommonRepository {


    @PersistenceContext
    EntityManager em;


    @Override
    public boolean save(CoreReader coreReader) {
        boolean save = false;

        try {

            Session session = em.unwrap(Session.class);
            session.save(coreReader);
            return save = true;

        } catch (Exception ex) {

            ex.getMessage();

            return save;
        }

    }

    @Override
    public <T> T get(Class<T> clazz, Integer id) {
        return (T) em.find(clazz, id);
    }

    public <T extends Serializable> int saveEntity(T entidad, String idOperation) {
        try {
            Session session = em.unwrap(Session.class);
            session.save(entidad);

            em.flush();
            return 1;
        } catch (Exception e) {
            Console.logException("Error al guardar", e, idOperation);

            e.getMessage();
            return 0;
        }
    }
}
