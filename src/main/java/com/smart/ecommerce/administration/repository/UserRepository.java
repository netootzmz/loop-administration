package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.repository.Custom.UserRepositoryCustom;
import com.smart.ecommerce.entity.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query(value = "SELECT IFNULL(MAX(1),0) FROM user WHERE status_id <> 2 AND mail= ?1 ",
            nativeQuery = true)
    Integer validEmailRegistration(String email);

    @Query(value = "SELECT IFNULL(MAX(1),0) FROM user WHERE status_id <> 2 AND user_name= ?1 ",
            nativeQuery = true)
    Integer validUserNameRegistration(String name);

    @Query(value = " select " +
            "       rol.role_id as optionId, " +
            "       rol.description as optionDescription " +
            "from role rol " +
            "where status = 1;", nativeQuery = true)
    List<Map<String, Object>> getRoles();


    @Query(value = " select " +
            "       lang.language_id as optionId, " +
            "       lang.name as optionDescription " +
            "from core_language lang " +
            "where lang.status_id = 1;", nativeQuery = true)
    List<Map<String, Object>> getLanguages();


    @Query(value = " select " +
            "       cus.core_user_status_id as optionId, " +
            "       cus.name as optionDescription " +
            "from core_user_status cus " +
            "where cus.core_user_status_id in (0,1);", nativeQuery = true)
    List<Map<String, Object>> getUserStatus();
    
    @Query(value="  select u.* from user u  where u.user_id = :userId", nativeQuery=true)
    User getUserByUId(@Param ("userId")Integer userId);

    @Query(value="  select u.* from user u  where u.password = :pPassword", nativeQuery=true)
    User getUserByPass(@Param ("pPassword")String password);


    @Query(value="select IFNULL(usr.mail, '') as mailUser, IFNULL(usr.phone, '') as phoneUser from user usr where usr.user_name = :userName limit 1", nativeQuery=true)
    Map<String,Object> getContactsByUserName(@Param ("userName") String userName);

    @Query(value = " select " +
            "       pgl.group_level_id as optionId, " +
            "       pgl.name as optionDescription " +
            "from group_level_catalog pgl " +
            "where pgl.status_id = 1;", nativeQuery = true)
    List<Map<String, Object>> getHierarchies();

    @Query(value = " select " +
            "       s.id as optionId, " +
            "       s.name as optionDescription " +
            "from status s ", nativeQuery = true)
    List<Map<String, Object>> getStatus();
}
