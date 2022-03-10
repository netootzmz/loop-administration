package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.admin.LogfileLogin;
import com.smart.ecommerce.entity.configuration.BitacoraPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BitacoraPasswordRepository extends JpaRepository<BitacoraPassword,Integer> {


    @Query(value = " select \n" +
            "            bp.* \n" +
            "             \n" +
            "            from bitacora_password bp \n" +
            "            where bp.user_id = :pUserId order by bp.date_register Desc limit 10;", nativeQuery = true)
    List<BitacoraPassword> getListPasswordByUser(@Param("pUserId") Integer pUserId);



}
