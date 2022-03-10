package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.repository.Custom.UserRepositoryCustom;
import com.smart.ecommerce.entity.core.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

  @PersistenceContext
  EntityManager em;

  @Override
  public List<User> usersAll() {
    List<User> lst = em.createNamedQuery("usersAll").getResultList();
    return lst;
  }

  @Override
  public User getUser(Integer userId) {
    List<User> lst =  em.createNamedQuery("userById").setParameter("userId", userId).getResultList();
    return lst.size() > 0 ? lst.get(0) :  new User();
  }

  @Override
  public User getUser(String user) {
    List<User> lst =  em.createNamedQuery("userByName").setParameter("user", user).getResultList();
    return lst.size() > 0 ? lst.get(0) :  new User();
  }
  
  @Override
  public User getUserStatusBlocked(String user) {
    List<User> lst =  em.createNamedQuery("userByNameStatusBlocked").setParameter("user", user).getResultList();
    return lst.size() > 0 ? lst.get(0) :  new User();
  }

  @Override
  public int getUserByUsernamer(String username, String mail) {
    List<User> lst =  em.createNamedQuery("existUser").setParameter("username", username).setParameter("mail", mail).getResultList();
    return lst.size();
  }

    @Override
    public List<Map<String, Object>> getUsersBySearchFiltered(String clientId, String stringToSearch,
                                                              Integer hierarchyId, Integer profileId,
                                                              Integer statusId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String Query = "SELECT \n" +
                "           u.status_id,\n" +
                "           s.name AS status_name, -- Nota: Llave foranea no establecida. Se toma relación tabla status valores con fecha 2021-07-20 1=Activo, 2=Inactivo, (Creados para la PDS100-46: US09. Usuarios 3=Bloqueado, 4=Baja)\n" +
                "           glc.name AS hierarchy,\n" +
                "           g.group_id as `group`, \n" +
                "           g.name as group_description, \n" +
                "           CONCAT(u.name,IF(ISNULL(u.second_name)=0,CONCAT(' ',u.second_name, ' '),' '),u.last_name1,IF(ISNULL(u.last_name2)=0,' ',''),u.last_name2) as name_user, " +
                "           r.role_id as role, \n" +
                "           r.description as role_description, \n" +
                "           u.date_admission AS last_login,\n" +
                "           u.user_id AS id\n" +
                "       FROM user AS u \n" +
                "       INNER JOIN role AS r ON u.role_id=r.role_id\n" +
                "       INNER JOIN status AS s ON s.id = u.status_id\n" +
                "       INNER JOIN `group` g ON g.group_id = u.group_id \n" +
                "       INNER JOIN group_level_catalog glc ON glc.group_level_id = g.group_level_id\n" +
                "       WHERE r.status = 1 AND u.client_id = :clientId";

        if( null != stringToSearch && !stringToSearch.isEmpty() ) {
            Query += " AND " +
                    "CONCAT(u.name,' ',u.last_name1,IF(ISNULL(u.last_name2)=0,' ',''),u.last_name2) " +
                    "LIKE :stringToSearch";
        }

        if( null != hierarchyId && 0 != hierarchyId ) {
            Query += " AND g.group_level_id=:groupLevelId";
        }

        if( null != profileId && 0 != profileId ) {
            Query += " AND r.role_id = :roleId";
        }

        if( null != statusId && 0 != statusId ) {
            Query += " AND u.status_id = :statusId";
        }

        Query query = em.createNativeQuery( Query );

        if( null != clientId && !clientId.isEmpty()) {
            query.setParameter("clientId", clientId);
        }

        if( null != stringToSearch && !stringToSearch.isEmpty() ) {
            query.setParameter("stringToSearch", '%' + stringToSearch + '%');
        }

        if( null != hierarchyId && 0 != hierarchyId ) {
            query.setParameter("groupLevelId", hierarchyId);
        }

        if( null != profileId && 0 != profileId ) {
            query.setParameter("roleId", profileId);
        }

        if( null != statusId && 0 != statusId ) {
            query.setParameter("statusId", statusId);
        }

        List<Object[]> results = query.getResultList();

        for ( int iRecords = 0; iRecords < results.size(); iRecords++ ) {

            Map<String, Object> user = new HashMap<>();

            user.put("status_id", results.get(iRecords)[0]);
            user.put("status_name", results.get(iRecords)[1]);
            user.put("hierarchy", results.get(iRecords)[2]);
            user.put("group", results.get(iRecords)[3]);
            user.put("group_description", results.get(iRecords)[4]);
            user.put("name_user", results.get(iRecords)[5]);
            user.put("role", results.get(iRecords)[6]);
            user.put("role_description", results.get(iRecords)[7]);
            user.put("last_login", results.get(iRecords)[8]);
            user.put("id", results.get(iRecords)[9]);

            list.add(user);

        }

        return list;
    }

    @Override
	public User getUserStatusBlocked(Integer userId) {
		List<User> lst =  em.createNamedQuery("userByIdStatusBlocked").setParameter("userId", userId).getResultList();
	    return lst.size() > 0 ? lst.get(0) :  new User();
	  }

  @Override
  public User getUserByEmail(String email) {
    List<User> lst =  em.createNamedQuery("userByEmail").setParameter("email", email).getResultList();
    return lst.size() > 0 ? lst.get(0) :  new User();
  }



}
