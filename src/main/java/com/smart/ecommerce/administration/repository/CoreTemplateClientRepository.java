package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.admin.HierarchyLevelCatalog;
import com.smart.ecommerce.entity.configuration.CoreTemplateClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoreTemplateClientRepository extends JpaRepository <CoreTemplateClient,Integer> {


    @Query(value = " SELECT * from core_template_client where client_id = :clientId and cve = :cve ;", nativeQuery = true)
    public CoreTemplateClient getCoreTemplateClientByClientAndType(@Param("clientId")String clientID,@Param("cve") String cve);


}
