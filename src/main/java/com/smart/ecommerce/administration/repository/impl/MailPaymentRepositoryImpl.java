package com.smart.ecommerce.administration.repository.impl;

import static com.smart.ecommerce.administration.util.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.repository.MailPaymentRepository;
import com.smart.ecommerce.logging.SystemLog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MailPaymentRepositoryImpl implements MailPaymentRepository {
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private static final String SQL_SELECT = "SELECT";
  private static final String SQL_INSERT_INTO = "INSERT INTO";
  private static final String SQL_VALUES = "VALUES";
  private static final String SQL_NOW = "NOW()";
  private static final String LOG_ERROR_TRACE = "Error: ";
  
  @Override
  public Integer saveMailPaymentBody(MailPaymentBodyDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - SaveMailPaymentBody :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_INSERT_INTO + SPACE);
    sql.append("mail_payment_body" + SPACE);
    sql.append("(" + SPACE);
    sql.append("client_id" + SPACE);
    sql.append(",message_header" + SPACE);
    sql.append(",line_one_title" + SPACE);
    sql.append(",line_two_title" + SPACE);
    sql.append(",message_body" + SPACE);
    sql.append(",message_footer" + SPACE);
    sql.append(",phone_footer" + SPACE);
    sql.append(",mail_footer" + SPACE);
    sql.append(",disclaimer_footer" + SPACE);
    sql.append(",id_status" + SPACE);
    sql.append(",user_by_register" + SPACE);
    sql.append(",created_at" + SPACE);
    sql.append(",modificated_at" + SPACE);
    sql.append(")" + SPACE);
    sql.append(SQL_VALUES + SPACE);
    sql.append("(" + SPACE);
    sql.append(":clientId" + SPACE);
    sql.append(", :messageHeader" + SPACE);
    sql.append(", :lineOneTitle" + SPACE);
    sql.append(", :lineTwoTitle" + SPACE);
    sql.append(", :messageBody" + SPACE);
    sql.append(", :messageFooter" + SPACE);
    sql.append(", :phoneFooter" + SPACE);
    sql.append(", :mailFooter" + SPACE);
    sql.append(", :disclaimerFooter" + SPACE);
    sql.append(", 1" + SPACE);
    sql.append(", :userId" + SPACE);
    sql.append(COMMA + SQL_NOW + SPACE);
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
      throw new GeneralException("", errorTrace, idOperation);
    }
    
  }

  @Override
  public Integer updateMailPaymentBody(MailPaymentBodyDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - UpdateMailPaymentBody :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append("UPDATE mail_payment_body SET" + SPACE);
    sql.append("message_header = :messageHeader" + SPACE);
    sql.append(",line_one_title = :lineOneTitle" + SPACE);
    sql.append(",line_two_title = :lineTwoTitle" + SPACE);
    sql.append(",message_body = :messageBody" + SPACE);
    sql.append(",message_footer = :messageFooter" + SPACE);
    sql.append(",phone_footer = :phoneFooter" + SPACE);
    sql.append(",mail_footer = :mailFooter" + SPACE);
    sql.append(",disclaimer_footer = :disclaimerFooter" + SPACE);
    sql.append(",id_status = 1" + SPACE);
    sql.append(",user_by_register = :userId" + SPACE);
    sql.append(",modificated_at = now()" + SPACE);
    sql.append("WHERE client_id = :clientId ");
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
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public Integer saveMailPaymentProof(MailPaymentProofDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - SaveMailPaymentProof :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_INSERT_INTO + SPACE);
    sql.append("mail_payment_proof" + SPACE);
    sql.append("(" + SPACE);
    sql.append("client_id" + SPACE);
    sql.append(",message_header" + SPACE);
    sql.append(",line_one_title" + SPACE);
    sql.append(",line_two_title" + SPACE);
    sql.append(",message_body" + SPACE);
    sql.append(",id_status" + SPACE);
    sql.append(",user_by_register" + SPACE);
    sql.append(",created_at" + SPACE);
    sql.append(",modificated_at" + SPACE);
    sql.append(")" + SPACE);
    sql.append(SQL_VALUES + SPACE);
    sql.append("(" + SPACE);
    sql.append(":clientId" + SPACE);
    sql.append(", :messageHeader" + SPACE);
    sql.append(", :lineOneTitle" + SPACE);
    sql.append(", :lineTwoTitle" + SPACE);
    sql.append(", :messageBody" + SPACE);
    sql.append(", 1" + SPACE);
    sql.append(", :userId" + SPACE);
    sql.append(COMMA + SQL_NOW + SPACE);
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
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public Integer updateMailPaymentProof(MailPaymentProofDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - UpdateMailPaymentProof :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append("UPDATE mail_payment_proof SET" + SPACE);
    sql.append("message_header = :messageHeader" + SPACE);
    sql.append(",line_one_title = :lineOneTitle" + SPACE);
    sql.append(",line_two_title = :lineTwoTitle" + SPACE);
    sql.append(",message_body = :messageBody" + SPACE);
    sql.append(",id_status = 1" + SPACE);
    sql.append(",user_by_register = :userId" + SPACE);
    sql.append(",modificated_at = now()" + SPACE);
    sql.append("WHERE client_id = :clientId ");
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
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public Boolean haveTemplatePaymentBodyByClientId(ParamGetMailPaymentDTO dto) {
    log.info("::: MailPaymentRepositoryImpl - HaveTemplatePaymentBodyByClientId :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    String sql = "SELECT COUNT(1) Has FROM mail_payment_body WHERE client_id = :clientId";
    log.info(SQL_LOG, sql);
    try {
      return namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class) > 0 ;
    } catch (Exception e) {
      log.error(QUERY_ERROR_LOG, e);
      return Boolean.FALSE;
    }
  }

  @Override
  public Boolean haveTemplatePaymentProofByClientId(ParamGetMailPaymentDTO dto) {
    log.info("::: MailPaymentRepositoryImpl - HaveTemplatePaymentProofByClientId :::");
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    String sql = "SELECT COUNT(1) Has FROM mail_payment_proof WHERE client_id = :clientId";
    log.info(SQL_LOG, sql);
    try {
      return namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class) > 0 ;
    } catch (Exception e) {
      log.error(QUERY_ERROR_LOG, e);
      return Boolean.FALSE;
    }
  }

  @Override
  public List<MailPaymentBodyDTO> getPaymentBody(ParamGetMailPaymentDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - GetPaymentBody :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("    client_id                       clientId" + SPACE);
    sql.append("    ,message_header         messageHeader" + SPACE);
    sql.append("    ,line_one_title         lineOneTitle" + SPACE);
    sql.append("    ,line_two_title         lineTwoTitle" + SPACE);
    sql.append("    ,message_body           messageBody" + SPACE);
    sql.append("    ,message_footer         messageFooter" + SPACE);
    sql.append("    ,phone_footer           phoneFooter" + SPACE);
    sql.append("    ,mail_footer            mailFooter" + SPACE);
    sql.append("    ,disclaimer_footer      disclaimerFooter" + SPACE);
    sql.append("FROM mail_payment_body" + SPACE);
    sql.append("WHERE client_id = :clientId" + SPACE);
    
    try {
      return  namedParameterJdbcTemplate.query(sql.toString() ,param , new BeanPropertyRowMapper<MailPaymentBodyDTO>(MailPaymentBodyDTO.class));
      
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
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public List<MailPaymentProofDTO> getPaymentProof(ParamGetMailPaymentDTO dto)
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - GetPaymentProof :::");
    String idOperation = SystemLog.newTxnIdOperation();
    SqlParameterSource param = new BeanPropertySqlParameterSource(dto);
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("    client_id                       clientId" + SPACE);
    sql.append("    ,message_header         messageHeader" + SPACE);
    sql.append("    ,line_one_title         lineOneTitle" + SPACE);
    sql.append("    ,line_two_title         lineTwoTitle" + SPACE);
    sql.append("    ,message_body           messageBody" + SPACE);
    sql.append("FROM mail_payment_proof" + SPACE);
    sql.append("WHERE client_id = :clientId" + SPACE);
    
    try {
      return  namedParameterJdbcTemplate.query(sql.toString() ,param , new BeanPropertyRowMapper<MailPaymentProofDTO>(MailPaymentProofDTO.class));
      
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
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public List<MailPaymentBodyDTO> getGenericPaymentBody()
    throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - GetGenericPaymentBody :::");
    String idOperation = SystemLog.newTxnIdOperation();
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("GROUP_CONCAT( messageHeader ORDER BY messageHeader ASC SEPARATOR ',') AS messageHeader" + SPACE);
    sql.append(",GROUP_CONCAT( lineOneTitle ORDER BY lineOneTitle ASC SEPARATOR ',') AS lineOneTitle" + SPACE);
    sql.append(",GROUP_CONCAT( lineTwoTitle ORDER BY lineTwoTitle ASC SEPARATOR ',') AS lineTwoTitle" + SPACE);
    sql.append(",GROUP_CONCAT( messageBody ORDER BY messageBody ASC SEPARATOR ',') AS messageBody" + SPACE);
    sql.append(",GROUP_CONCAT( messageFooter ORDER BY messageFooter ASC SEPARATOR ',') AS messageFooter" + SPACE);
    sql.append(",GROUP_CONCAT( phoneFooter ORDER BY phoneFooter ASC SEPARATOR ',') AS phoneFooter" + SPACE);
    sql.append(",GROUP_CONCAT( mailFooter ORDER BY mailFooter ASC SEPARATOR ',') AS mailFooter" + SPACE);
    sql.append(",GROUP_CONCAT( disclaimerFooter ORDER BY disclaimerFooter ASC SEPARATOR ',') AS disclaimerFooter" + SPACE);
    sql.append("FROM" + SPACE);
    sql.append("(" + SPACE);
    sql.append("  SELECT" + SPACE);
    sql.append("  CASE WHEN name = 'DEFAULT_MAIL_BODY_MESSAGE_HEADER' THEN value ELSE NULL END messageHeader" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_LINE_ONE_TITLE' THEN value ELSE NULL END lineOneTitle" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_LINE_TWO_TITLE' THEN value ELSE NULL END lineTwoTitle" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_MESSAGE_BODY' THEN value ELSE NULL END messageBody" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_MESSAGE_FOOTER' THEN value ELSE NULL END messageFooter" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_PHONE_FOOTER' THEN value ELSE NULL END phoneFooter" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_MAIL_FOOTER' THEN value ELSE NULL END mailFooter" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_BODY_DISCLAIMER_FOOTER' THEN value ELSE NULL END disclaimerFooter" + SPACE);
    sql.append("FROM system_config sc" + SPACE);
    sql.append("WHERE name" + SPACE);
    sql.append("  IN (" + SPACE);
    sql.append("    'DEFAULT_MAIL_BODY_MESSAGE_HEADER'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_LINE_ONE_TITLE'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_LINE_TWO_TITLE'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_MESSAGE_BODY'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_MESSAGE_FOOTER'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_PHONE_FOOTER'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_MAIL_FOOTER'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_BODY_DISCLAIMER_FOOTER'" + SPACE);
    sql.append("    )" + SPACE);
    sql.append("GROUP BY name, value" + SPACE);
    sql.append(") pivot_table_body" + SPACE);
    
    try {
      return  jdbcTemplate.query(sql.toString() , new BeanPropertyRowMapper<MailPaymentBodyDTO>(MailPaymentBodyDTO.class));
      
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
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

  @Override
  public List<MailPaymentProofDTO>
    getGenericPaymentProof() throws GeneralException {
    log.info("::: MailPaymentRepositoryImpl - GetGenericPaymentProof :::");
    String idOperation = SystemLog.newTxnIdOperation();
    
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("GROUP_CONCAT( messageHeader ORDER BY messageHeader ASC SEPARATOR ',') AS messageHeader" + SPACE);
    sql.append(",GROUP_CONCAT( lineOneTitle ORDER BY lineOneTitle ASC SEPARATOR ',') AS lineOneTitle" + SPACE);
    sql.append(",GROUP_CONCAT( lineTwoTitle ORDER BY lineTwoTitle ASC SEPARATOR ',') AS lineTwoTitle" + SPACE);
    sql.append(",GROUP_CONCAT( messageBody ORDER BY messageBody ASC SEPARATOR ',') AS messageBody" + SPACE);
    sql.append("FROM" + SPACE);
    sql.append("(" + SPACE);
    sql.append("  SELECT " + SPACE);
    sql.append("  CASE WHEN name = 'DEFAULT_MAIL_PROOF_MESSAGE_HEADER' THEN value ELSE NULL END messageHeader " + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_PROOF_LINE_ONE_TITLE' THEN value ELSE NULL END lineOneTitle" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_PROOF_LINE_TWO_TITLE' THEN value ELSE NULL END lineTwoTitle" + SPACE);
    sql.append("  ,CASE WHEN name = 'DEFAULT_MAIL_PROOF_MESSAGE_BODY' THEN value ELSE NULL END messageBody" + SPACE);
    sql.append("FROM system_config sc" + SPACE);
    sql.append("WHERE name" + SPACE);
    sql.append("  IN (" + SPACE);
    sql.append("    'DEFAULT_MAIL_PROOF_MESSAGE_HEADER'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_PROOF_LINE_ONE_TITLE'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_PROOF_LINE_TWO_TITLE'" + SPACE);
    sql.append("    ,'DEFAULT_MAIL_PROOF_MESSAGE_BODY'" + SPACE);
    sql.append("    )" + SPACE);
    sql.append("GROUP BY name, value" + SPACE);
    sql.append(") pivot_table_proof" + SPACE);
    
    try {
      return  jdbcTemplate.query(sql.toString()  , new BeanPropertyRowMapper<MailPaymentProofDTO>(MailPaymentProofDTO.class));
      
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
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
  }

}
