package com.smart.ecommerce.administration.repository.Custom;

import com.smart.ecommerce.entity.core.User;

import java.util.List;
import java.util.Map;

public interface UserRepositoryCustom {

  List<User> usersAll();

  User getUser(Integer userId);
  
  User getUserStatusBlocked(Integer userId);

  User getUserByEmail(String email);


    User getUser(String user);
  
  User getUserStatusBlocked(String user);

  int getUserByUsernamer(String username, String mail);

  List<Map<String, Object>> getUsersBySearchFiltered(String clientId,String stringToSearch,
                                                     Integer hierarchyId, Integer profileId, Integer statusId);
}
