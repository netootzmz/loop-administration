package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.admin.LogfileLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogfileLoginRepository extends JpaRepository<LogfileLogin, Integer> {


    @Query(value = "select lFl.* from logfile_login lFl where lFl.user_id = :userId ; ", nativeQuery = true)
    LogfileLogin getLogfileLoginByUserId(@Param("userId") Integer userId);


}
