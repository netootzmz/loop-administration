package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.configuration.MailConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MailConfigurationRepository extends JpaRepository <MailConfiguration,Integer> {


    @Query(value = " select mc.* from mail_configuration mc  " +
            " where mc.status_id = 1  and  mc.client_id = :clientId ;", nativeQuery=true)
    MailConfiguration getMailConfigurationByClientId (@Param("clientId")String clientId);



}
