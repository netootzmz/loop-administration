package com.smart.ecommerce.administration.repository.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenPaymentLinkDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.PaymentCheckOutParametersDTO;
import com.smart.ecommerce.administration.repository.ConfigurationScreenPaymentCheckOutRepository;

import lombok.extern.slf4j.Slf4j;

import static com.smart.ecommerce.administration.util.Constants.*;

@Slf4j
@Repository
public class ConfigurationScreenPaymentCheckOutRepositoryImpl implements ConfigurationScreenPaymentCheckOutRepository {
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  
  private static final String SQL_SELECT = "SELECT";

  @Override
  public List<ConfigurationScreenPaymentLinkDto> getConfigurationLinkPayScreenByUser(Integer userId) {
    log.info(":: ConfigurationScreenPaymentCheckOutRepositoryImpl - GetConfigurationLinkPayScreenByUser ::");
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("    gc.api_key  as apiKey ," + SPACE);
    sql.append("    ccp.validity_default as validityDefault ," + SPACE);
    sql.append("    ccp.edit_validity as editValidity ," + SPACE);
    sql.append("    ccp.payment_screen_behavior_id as paymentScreenBehaviorId," + SPACE);
    sql.append("    ccp.send_way as sendWay" + SPACE);
    sql.append("FROM user user" + SPACE);
    sql.append("         inner join core_client_solution ccs on user.client_id = ccs.client_id and user.group_id = ccs.group_id" + SPACE);
    sql.append("         inner join config_client_product ccp on ccs.core_client_solution_id = ccp.core_client_solution_id" + SPACE);
    sql.append("         inner join group_conection gc on ccs.group_id = gc.group_id" + SPACE);
    sql.append("WHERE user_id = ?" + SPACE);
    
    Object[] param = {userId};
    
    log.info(SQL_LOG, sql);
    Arrays.asList(param).forEach(p->log.info("Parameter: {}", p));
    
    try {
      return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<ConfigurationScreenPaymentLinkDto>(ConfigurationScreenPaymentLinkDto.class));
    } catch (Exception e) {
      log.error("Error: {} ", e.getMessage());
      return Collections.emptyList();
    }
    
  }
  
  @Override
  public List<PaymentCheckOutParametersDTO> getMsiByIdUser(InfoTokenDto dto) {
    log.info("::: RemotePaymentCheckOutParametersRepositoryImpl - GetMsiByIdUser :::");
    
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("  DISTINCT" + SPACE);
    sql.append("  ccp.payment_screen_behavior_id                                                                blockingBehaviorId" + SPACE);
    sql.append("  ,psbc.name                                                                                    blockingBehaviorName" + SPACE);
    sql.append("  ,cs.have_msi                                                                                  haveMsi " + SPACE);
    sql.append("  ,ccp.have_msi                                                                                 allowMsi" + SPACE);
    sql.append("  ,casm.months_interest_id                                                                      msiId" + SPACE);
    sql.append("  ,mic.name                                                                                     msiName" + SPACE);
    sql.append("  ,IF((MIN(casmb.monto_minimo) IS NOT NULL OR MIN(casmb.monto_minimo) <> '')" + SPACE);
    sql.append("  AND MIN(casmb.monto_minimo) > 0, 1, 0)                                                        monthsAvailable" + SPACE);
    sql.append("  ,MIN(casmb.monto_minimo)                                                                      amountPucharse" + SPACE);
    sql.append("  ,MAX(ccp.validity_default)                                                                    validityValue" + SPACE);
    sql.append("  ,MAX(ccp.validity_unity)                                                                      validityUnity" + SPACE);
    sql.append("  ,IF((ccp.validity_default IS NOT NULL OR ccp.validity_default <> '')" + SPACE);
    sql.append("  AND ccp.validity_default > 0, 1, 0)                                                           haveValidityDefault" + SPACE);
    sql.append("  ,IF((ccp.card_holder_data_request IS NOT NULL OR ccp.card_holder_data_request <> '')" + SPACE);
    sql.append("  AND ccp.card_holder_data_request > 0, 1, 0)                                                   cardHolderDataRequest" + SPACE);
    sql.append("  ,IF((ccp.allow_edition IS NOT NULL OR ccp.allow_edition <> '')" + SPACE);
    sql.append("  AND ccp.allow_edition > 0, 1, 0)                                                              allowEdition" + SPACE);
    sql.append("  ,ccp.config_client_product_id                                                                 configClientProductId" + SPACE);
    sql.append("FROM core_solution_acquire csa" + SPACE);
    sql.append("  INNER JOIN core_acquire_solution cas ON csa.id_acquire_solution = cas.id_acquire_solution" + SPACE);
    sql.append("  INNER JOIN core_acquire_solution_msi casm ON cas.id_acquire_solution = casm.id_acquire_solution" + SPACE);
    sql.append("  INNER JOIN months_interest_catalog mic ON casm.months_interest_id = mic.months_interest_id" + SPACE);
    sql.append("  INNER JOIN core_acquire_solution_msi_bin casmb ON casm.id_acquire_solution_msi = casmb.id_acquire_solution_msi" + SPACE);
    sql.append("  INNER JOIN core_solution cs ON csa.id_solution = cs.core_solution_id" + SPACE);
    sql.append("  INNER JOIN core_client_solution ccs ON cs.core_solution_id = ccs.core_solution_id" + SPACE);
    sql.append("  INNER JOIN config_client_product ccp ON ccs.core_client_solution_id = ccp.core_client_solution_id" + SPACE);
    sql.append("  INNER JOIN payment_screen_behavior_catalog psbc ON ccp.payment_screen_behavior_id = psbc.payment_screen_behavior_id" + SPACE);
    sql.append("  INNER JOIN `user` u ON ccs.client_id = u.client_id AND ccs.group_id =u.group_id" + SPACE);
    sql.append("WHERE u.user_id = :user_by_register" + SPACE);
    sql.append("GROUP BY" + SPACE);
    sql.append("ccp.payment_screen_behavior_id" + SPACE);
    sql.append("  ,psbc.name" + SPACE);
    sql.append("  ,casm.months_interest_id" + SPACE);
    sql.append("  ,mic.name" + SPACE);
    sql.append("  ,ccp.validity_default" + SPACE);
    sql.append("  ,ccp.config_client_product_id" + SPACE);
    
    log.info(SQL_LOG, sql);
    
    try {
      return namedParameterJdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<PaymentCheckOutParametersDTO>(PaymentCheckOutParametersDTO.class));
    } catch (Exception e) {
      log.error("Error - GetMsiByIdUser: {}", e);
      return Collections.emptyList();
    }
    
  }

  @Override
  public Boolean hasMsiConfigured(PaymentCheckOutParametersDTO dto) {
    log.info("::: RemotePaymentCheckOutParametersRepositoryImpl - HasMsiConfigured :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    String sql = "SELECT COUNT(1) Has FROM config_client_product_msi  WHERE config_client_product_id = :configClientProductId AND status_id = 1";
    log.info(SQL_LOG, sql);
    try {
      return namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class) > 0;
    } catch (Exception e) {
      log.error("Error - HasMsiConfigured: {}", e);
      return Boolean.FALSE;
    }
  }

  @Override
  public List<PaymentCheckOutParametersDTO> getMsiByClientProductId(PaymentCheckOutParametersDTO dto) {
    log.info("::: RemotePaymentCheckOutParametersRepositoryImpl - GetMsiByClientProductId :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("  DISTINCT" + SPACE);
    sql.append("  ccp.payment_screen_behavior_id                                              blockingBehaviorId" + SPACE);
    sql.append("  ,psbc.name                                                                  blockingBehaviorName" + SPACE);
    sql.append("  ,cs.have_msi                                                                haveMsi" + SPACE);
    sql.append("  ,ccp.have_msi                                                               allowMsi" + SPACE);
    sql.append("  ,ccpm.id_msi                                                                msiId" + SPACE);
    sql.append("  ,mic.name                                                                   msiName" + SPACE);
    sql.append("  ,ccpm.months_available                                                      monthsAvailable" + SPACE);
    sql.append("  ,MIN(ccpm.min_purchase)                                                     amountPucharse" + SPACE);
    //sql.append("  ,MAX(ccp.validity_default)                                                validityDefault" + SPACE)
    sql.append("  ,CASE WHEN" + SPACE);
    sql.append("    (" + SPACE);
    sql.append("      (ccp.validity_value IS NOT NULL OR ccp.validity_value <> '')" + SPACE);
    sql.append("      AND ccp.validity_value <> 0" + SPACE);
    sql.append("    )" + SPACE);
    sql.append("  THEN MAX(ccp.validity_value) ELSE MAX(ccp.validity_default) END             validityValue" + SPACE);
    sql.append("  ,MAX(ccp.validity_unity)                                                    validityUnity" + SPACE);
    sql.append("  ,IF((ccp.validity_default IS NOT NULL OR ccp.validity_default <> '')" + SPACE);
    sql.append("  AND ccp.validity_default > 0, 1, 0)                                         haveValidityDefault" + SPACE);
    sql.append("  ,IF((ccp.card_holder_data_request IS NOT NULL OR ccp.card_holder_data_request <> '')" + SPACE);
    sql.append("  AND ccp.card_holder_data_request > 0, 1, 0)                                 cardHolderDataRequest" + SPACE);
    sql.append("  ,IF((ccp.allow_edition IS NOT NULL OR ccp.allow_edition <> '')" + SPACE);
    sql.append("  AND ccp.allow_edition > 0, 1, 0)                                            allowEdition" + SPACE);
    sql.append("  ,ccp.config_client_product_id                                               configClientProductId" + SPACE);
    sql.append("FROM config_client_product_msi ccpm" + SPACE);
    sql.append("  INNER JOIN months_interest_catalog mic ON ccpm.id_msi = mic.months_interest_id" + SPACE);
    sql.append("  INNER JOIN config_client_product ccp ON ccpm.config_client_product_id = ccp.config_client_product_id" + SPACE);
    sql.append("  INNER JOIN core_client_solution ccs ON ccp.core_client_solution_id = ccs.core_client_solution_id" + SPACE);
    sql.append("  INNER JOIN core_solution cs ON ccs.core_solution_id = cs.core_solution_id" + SPACE);
    sql.append("  INNER JOIN payment_screen_behavior_catalog psbc ON ccp.payment_screen_behavior_id = psbc.payment_screen_behavior_id" + SPACE);
    sql.append("  INNER JOIN `user` u ON ccs.client_id = u.client_id AND ccs.group_id =u.group_id" + SPACE);
    sql.append("WHERE ccpm.config_client_product_id = :configClientProductId AND ccpm.status_id = 1" + SPACE);
    sql.append("GROUP BY" + SPACE);
    sql.append("  ccp.payment_screen_behavior_id" + SPACE);
    sql.append("  ,psbc.name" + SPACE);
    sql.append("  ,ccpm.id_msi" + SPACE);
    sql.append("  ,mic.name" + SPACE);
    sql.append("  ,ccp.validity_default" + SPACE);
    sql.append("  ,ccp.config_client_product_id" + SPACE);
    sql.append("  ,ccpm.months_available" + SPACE);

    log.info(SQL_LOG, sql);

    try {
      return namedParameterJdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<PaymentCheckOutParametersDTO>(PaymentCheckOutParametersDTO.class));
    } catch (Exception e) {
      log.error("Error - GetMsiByClientProductId: {}", e);
      return Collections.emptyList();
    }
  }
  
}
