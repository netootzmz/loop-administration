package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.CoreRestorePassword;
import com.smart.ecommerce.entity.core.RoleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestorePasswordRepositoryJPA extends JpaRepository <CoreRestorePassword,Integer> {


    @Query(value = " select * \n" +
            " from core_restore_password crp \n" +
            " where crp.user_id = :pUserId \n" +
            "  and crp.status <> 2; ", nativeQuery = true)
    List<CoreRestorePassword> getCoreRestorePasswordByUserID(@Param("pUserId")int userID);


}
