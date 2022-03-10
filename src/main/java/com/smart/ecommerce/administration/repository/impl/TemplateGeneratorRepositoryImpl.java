package com.smart.ecommerce.administration.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.model.dto.TemplateClientDTO;
import com.smart.ecommerce.administration.repository.TemplateGeneratorRepository;

import lombok.extern.slf4j.Slf4j;
import static com.smart.ecommerce.administration.util.Constants.*;

@Slf4j
@Repository
public class TemplateGeneratorRepositoryImpl implements TemplateGeneratorRepository {
  //@Autowired private JdbcTemplate jdbcTemplate
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  //private static final String SQL_SELECT = "SELECT"
  private static final String SQL_INSERT_INTO = "INSERT INTO";
  private static final String SQL_VALUES = "VALUES";
  private static final String SQL_NOW = "NOW()";
  private static final String LOG_ERROR_TRACE = "Error: ";

  @Override
  public String getTemplateGeneric(ParamGetMailPaymentDTO dto) {
    log.info(":: TemplateGeneratorRepositoryImpl - getTemplateGeneric ::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    String sql = "SELECT code_template text FROM core_template_replace_text_mail WHERE client_id = :clientId AND cve = :cve";
    try {
      return namedParameterJdbcTemplate.queryForObject(sql, param, String.class); 
    } catch (Exception e) {
      log.error("Error: {}", e);
      return EMPTY;
    }
    
  }
  
  @Override
  public Boolean hasTemplateClient(TemplateClientDTO dto) {
    log.info("::: ServicesPortalDAOImpl - HasTemplateClient :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    String sql = "SELECT COUNT(1) FROM core_template_client WHERE client_id = :clientId AND cve = :cve";
    log.info(SQL_LOG, sql);
    try {
      return namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class) > 0 ;
    } catch (Exception e) {
      log.error(QUERY_ERROR_LOG, e);
      return Boolean.FALSE;
    }
  }

  @Override
  public Integer saveTemplateClient(TemplateClientDTO dto) {
    log.info("::: ServicesPortalDAOImpl - SaveTemplateClient :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);

    StringBuilder sql = new StringBuilder();
    sql.append(SQL_INSERT_INTO + SPACE);
    sql.append("core_template_client" + SPACE);
    sql.append("(" + SPACE);
    sql.append("type_owner_id" + SPACE);
    sql.append(",template" + SPACE);
    sql.append(",description" + SPACE);
    sql.append(",cve" + SPACE);
    sql.append(",client_id" + SPACE);
    sql.append(",status_id" + SPACE);
    sql.append(",user_by_register" + SPACE);
    sql.append(",created_at" + SPACE);
    sql.append(")" + SPACE);
    sql.append(SQL_VALUES + SPACE);
    sql.append("(" + SPACE);
    sql.append(":typeOwnerId" + SPACE);
    sql.append(", :templateStr" + SPACE);
    sql.append(", :description" + SPACE);
    sql.append(", :cve" + SPACE);
    sql.append(", :clientId" + SPACE);
    sql.append(", 1" + SPACE);
    sql.append(", :userId" + SPACE);
    sql.append(COMMA + SQL_NOW + SPACE);
    sql.append(")" + SPACE);
    log.info(SQL_LOG, sql);

    try {
      return namedParameterJdbcTemplate.update(sql.toString(), param);
    } catch (Exception e) {
      StringBuilder errorStack = new StringBuilder();
      for (StackTraceElement er : e.getStackTrace()) {
        errorStack.append(LOG_ERROR_TRACE);
        errorStack.append(er.getClassName() + SPACE);
        errorStack.append(er.getFileName() + SPACE);
        errorStack.append(er.getLineNumber() + SPACE);
        errorStack.append(er.getMethodName() + SPACE);
        errorStack.append("");
      }
      String errorTrace = errorStack.toString();
      log.info(QUERY_ERROR_LOG, errorTrace);
      return 0;
    }
  }

  @Override
  public Integer updateTemplateClient(TemplateClientDTO dto) {
    log.info("::: ServicesPortalDAOImpl - UpdateTemplateClient :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);

    StringBuilder sql = new StringBuilder();
    sql.append("UPDATE core_template_client SET" + SPACE);
    sql.append("template = :templateStr" + SPACE);
    sql.append(",user_by_register = :userId" + SPACE);
    sql.append("WHERE client_id = :clientId" + SPACE);
    sql.append("AND cve = :cve ");
    log.info(SQL_LOG, sql);
    try {
      return namedParameterJdbcTemplate.update(sql.toString(), param);
    } catch (Exception e) {
      StringBuilder errorStack = new StringBuilder();
      for (StackTraceElement er : e.getStackTrace()) {
        errorStack.append(LOG_ERROR_TRACE);
        errorStack.append(er.getClassName() + SPACE);
        errorStack.append(er.getFileName() + SPACE);
        errorStack.append(er.getLineNumber() + SPACE);
        errorStack.append(er.getMethodName() + SPACE);
        errorStack.append("");
      }
      String errorTrace = errorStack.toString();
      log.info(QUERY_ERROR_LOG, errorTrace);
      return 0;
    }
  }
  
}
