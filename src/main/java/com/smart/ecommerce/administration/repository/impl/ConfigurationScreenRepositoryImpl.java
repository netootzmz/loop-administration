package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenDto;
import com.smart.ecommerce.administration.repository.ConfigurationScreenRepository;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service("configurationScreenRepository")
public class ConfigurationScreenRepositoryImpl implements ConfigurationScreenRepository {

    @PersistenceContext
    EntityManager em;


    @Override
    public List<ConfigurationScreenDto> getConfigurationLinkPayScreenByUser(Integer userId) {
        String Query = "select " +
                "    gc.api_key  as apiKey , " +
                "    ccp.validity_default as validityDefault , " +
                "    ccp.edit_validity as editValidity , " +
                "    ccp.payment_screen_behavior_id as paymentScreenBehaviorId, " +
                "    ccp.numbers_patterns as numbersPatterns, " +
                "    ccp.send_way as sendWay " +
                " from user user " +
                "         inner join core_client_solution ccs on user.client_id = ccs.client_id and user.group_id = ccs.group_id " +
                "         inner join config_client_product ccp on ccs.core_client_solution_id = ccp.core_client_solution_id " +
                "         inner join group_conection gc on ccs.group_id = gc.group_id " +
                " where user_id = :userId";
                

        try {

            List<ConfigurationScreenDto> dto = em.createNativeQuery(Query).unwrap(org.hibernate.query.Query.class)
                    .setParameter("userId", userId)
                    .setResultTransformer(Transformers.aliasToBean(ConfigurationScreenDto.class))
                    .getResultList();

            return dto;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}
