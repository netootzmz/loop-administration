/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import static com.smart.ecommerce.administration.util.Constants.SPACE;
import static com.smart.ecommerce.administration.util.Constants.SQL_LOG;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.model.dto.GenericCatalogDTO;
import com.smart.ecommerce.administration.util.GeneralConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Eduardo Valeriano
 *
 */

@Slf4j
@Repository("GenericCatalogRepository")
public class GenericCatalogRepository {
  @Autowired private JdbcTemplate jdbcTemplate;
  private static final String SQL_SELECT = "SELECT";
  private static final String LOG_ERROR_TRACE = "Error: ";

  public List<Map<String, Object>> getAllOptionsFromCatalog(String nameCatalog, String idCatalog, Integer languageId) {		
    String queryCustom =
      "SELECT catalog.#idCatalog  as idOption, " + 
        "	catalog.cve as cveOption, " + 
        "	catalog.name as nameOption, " + 
        "	catalog.description as descriptionOption, " + 
        "	catalog.status_id as statusIdOption," + 
        "	catalog.user_by_register as userByRegisterOption, " + 
        "	catalog.created_at as createdAtOption, " +
        "	catalog.language_id as languageIdOption " +
        "	FROM #nameCatalog  catalog "
        + "WHERE catalog.language_id = #languageId;";
    queryCustom = queryCustom.replaceAll("#idCatalog", idCatalog);
    queryCustom = queryCustom.replaceAll("#nameCatalog", nameCatalog);
    queryCustom = queryCustom.replaceAll("#languageId", languageId.toString());
    return (List<Map<String, Object>>) jdbcTemplate.queryForList(queryCustom);
  }

  public String saveRecord (String nameCatalog,String cve,String name,
    String description,String status_id,String user_by_register,String created_at, Integer language_id) {
    String response = "";

    String queryCustom ="insert into #nameCatalog(cve,name,description,status_id,user_by_register,created_at,language_id) " + 
      "values (\"#cveCatalog\",\"#name\",\"#descriptionCatalog\",#status_idCatalog,#user_by_register,\"#created_atCatalog\", #language_id);";
    queryCustom = queryCustom.replaceAll("#nameCatalog", nameCatalog);

    queryCustom = queryCustom.replaceAll("#cveCatalog",cve);
    queryCustom = queryCustom.replaceAll("#name",name);
    queryCustom = queryCustom.replaceAll("#descriptionCatalog",description);
    queryCustom = queryCustom.replaceAll("#status_idCatalog",status_id);
    queryCustom = queryCustom.replaceAll("#created_atCatalog",created_at);
    queryCustom = queryCustom.replaceAll("#user_by_register",user_by_register);
    queryCustom = queryCustom.replaceAll("#language_id",language_id.toString());
    jdbcTemplate.execute(queryCustom);
    response = "Nuevo registro guardado ";
    return response;
  }
  public String editOrDelete(String nameCatalog,
    String cve,String name,String description,
    String status_id,String user_by_register,
    String created_at,Integer language_id,String id,int type,String idTable) {

    String response= "";		
    if(type == GeneralConstant.editar) {
      String queryCustom = " update #nameCatalog " + 
        " set cve=\"cveT\", " + 
        " name = \"nameT\" , " + 
        " description =\"descriptionT\", " + 
        " status_id=status_idT, " + 
        " user_by_register=user_by_registerT, " + 
        " created_at =\"created_atT\" " + 
        " where idTableT =idT and language_id = languageT;";

      queryCustom = queryCustom.replaceAll("#nameCatalog", nameCatalog);
      queryCustom = queryCustom.replaceAll("cveT", cve);
      queryCustom = queryCustom.replaceAll("nameT", name);
      queryCustom = queryCustom.replaceAll("descriptionT", description);
      queryCustom = queryCustom.replaceAll("status_idT", status_id);
      queryCustom = queryCustom.replaceAll("user_by_registerT", user_by_register);
      queryCustom = queryCustom.replaceAll("created_atT", created_at);
      queryCustom = queryCustom.replaceAll("idTableT", idTable);
      queryCustom = queryCustom.replaceAll("idT", id);
      queryCustom = queryCustom.replaceAll("languageT", String.valueOf(language_id));

      jdbcTemplate.execute(queryCustom);
      response = "Registro actualizado";

    }
    if(type == GeneralConstant.eliminar) {
      String eliminar = "2";
      String queryCustom = " update #nameCatalog " + 
        "set status_id = eliminar " + 
        "where idTableT = idT ; ";
      queryCustom = queryCustom.replaceAll("#nameCatalog", nameCatalog);

      queryCustom = queryCustom.replaceAll("idTableT", idTable);
      queryCustom = queryCustom.replaceAll("idT", id);
      queryCustom = queryCustom.replaceAll("eliminar", eliminar);


      jdbcTemplate.execute(queryCustom);
      response = "Registro eliminado";

    }

    return response;
  }

  public List<Map<String, Object>> getoneTest() {
    String queryCustom = " SELECT r.role_id as roleId,r.description as description,p.portal_id as portalId,p.name as portalName,m.name as moduleName,m.module_id as moduleId,a.action_id as actionId,a.name as actionName,a.url as actionUrl " + 
      " FROM role_action ra " + 
      " INNER JOIN role r on ra.role_id = r.role_id " + 
      " INNER JOIN portal p on ra.portal_id = p.portal_id " + 
      " INNER JOIN module m on p.portal_id = m.portal_id " + 
      " INNER JOIN action a on m.module_id = a.module_id " + 
      " WHERE ra.role_id = 8 " + 
      " AND p.status = 1 " + 
      " AND m.status = 1 " + 
      " AND a.status = 1 " + 
      " AND r.status = 1; ";
    return (List<Map<String, Object>>) jdbcTemplate.queryForList(queryCustom);

  }

  public List<GenericCatalogDTO> getEmisorCatalog() throws Exception{
    StringBuilder sql = new StringBuilder();
    sql.append(SQL_SELECT + SPACE);
    sql.append("cve , name FROM core_emisor_catalog" + SPACE);
    log.info(SQL_LOG, sql);
    try {
      return jdbcTemplate.query(sql.toString(),  new BeanPropertyRowMapper<GenericCatalogDTO>(GenericCatalogDTO.class));
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
      log.info("Error Transaction ServPtal: {}", errorTrace);
      //return Collections.emptyList()
      throw new Exception(errorTrace);
    }
  }

}
