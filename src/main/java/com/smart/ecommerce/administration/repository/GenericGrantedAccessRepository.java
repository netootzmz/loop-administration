/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Eduardo Valeriano
 *
 */
@Repository("GenericGrantedAccessRepository")
public class GenericGrantedAccessRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getListRole(int roleId,String active) {		
		String queryCustom= " SELECT " + 
				"       r.role_id as roleId, " + 
				"       r.description as roleDescription, " + 
				"       p.portal_id as portalId, " + 
				"       p.name as portalName," + 
				"       m.name as moduleName," + 
				"       m.module_id as moduleId," + 
				"       a.action_id as actionId," + 
				"       a.name as actionName," + 
				"       a.url as actionUrl " + 
				"FROM role_action ra " + 
				"INNER JOIN role r on ra.role_id = r.role_id " + 
				"INNER JOIN portal p on ra.portal_id = p.portal_id " + 
				"INNER JOIN module m on p.portal_id = m.portal_id "  + 
				"INNER JOIN action a on m.module_id = a.module_id " + 
				"WHERE ra.role_id = #roleId " + 
				"AND p.status = active " + 
				"AND m.status = active " + 
				"AND a.status = active " + 
				"AND r.status = active;";
		queryCustom = queryCustom.replaceAll("active",active );
		queryCustom = queryCustom.replaceAll("#roleId",String.valueOf(roleId));
		
		return (List<Map<String, Object>>) jdbcTemplate.queryForList(queryCustom);
	}
	public String insertRecord(int roleId,int portalId,int moduleId,int actionId,int userBy,String created) {
		String response = "";
		String queryCustom= " insert into role_action(role_id,portal_id,module_id,action_id,user_by_register,created_at ) " + 
				"values(#roleId,#portalId,#moduleId,#actionId,#userBy,\"#created\"); ";
		
		queryCustom = queryCustom.replaceAll("#roleId",String.valueOf(roleId) );
		queryCustom = queryCustom.replaceAll("#portalId",String.valueOf(portalId) );
		queryCustom = queryCustom.replaceAll("#moduleId",String.valueOf(moduleId) );		
		queryCustom = queryCustom.replaceAll("#actionId",String.valueOf(actionId) );
		queryCustom = queryCustom.replaceAll("#userBy",String.valueOf(userBy) );		
		queryCustom = queryCustom.replaceAll("#created",String.valueOf(created) );
		jdbcTemplate.execute(queryCustom);
		response="ok";
		
		return response;
	}
	public List<Map<String, Object>> getModule(int moduleId){
		String queryCustom = "select * from module where module_id=#moduleId and status=1 ;";
		queryCustom = queryCustom.replaceAll("#moduleId", String.valueOf(moduleId));
		return (List<Map<String, Object>>)jdbcTemplate.queryForList(queryCustom);
	}
	
	public List<Map<String, Object>> getPortal(int portalId){
		String queryCustom = "select * from portal where portal_id=#portalId and status=1;";
		queryCustom = queryCustom.replaceAll("#portalId", String.valueOf(portalId));
		return (List<Map<String, Object>>)jdbcTemplate.queryForList(queryCustom);
	}
	
	public List<Map<String, Object>> getAction(int actionId){
		String queryCustom = "select * from action where action_id=#actionId and status=1;";
		queryCustom = queryCustom.replaceAll("#actionId", String.valueOf(actionId));
		return (List<Map<String, Object>>)jdbcTemplate.queryForList(queryCustom);
	}
	
	public String deleteRoleAction(int roleId,int portalId, int moduleId,int actionId) {
		String response = "";
		String queryCustom = " DELETE FROM role_action WHERE role_id=#roleId and portal_id=#portalId and module_id=#moduleId and action_id=#actionId";
		queryCustom = queryCustom.replaceAll("#roleId", String.valueOf(roleId));
		queryCustom = queryCustom.replaceAll("#portalId", String.valueOf(portalId));
		queryCustom = queryCustom.replaceAll("#moduleId", String.valueOf(moduleId));
		queryCustom = queryCustom.replaceAll("#actionId", String.valueOf(actionId));

		jdbcTemplate.execute(queryCustom);
		response = "ok";
		return response;
		
	}
	
	public List<Map<String, Object>> getRoleAction(int roleId,int actionId,int portalId,int moduleId){
		
		String queryCustom = " select * from role_action " + 
				"	where role_id = #roleId and " + 
				"	portal_id = #portalId and  " + 
				"	module_id = #moduleId and " + 
				"	action_id = #actionId;";
		queryCustom = queryCustom.replaceAll("#roleId", String.valueOf(roleId));
		queryCustom = queryCustom.replaceAll("#actionId", String.valueOf(actionId));
		queryCustom = queryCustom.replaceAll("#portalId", String.valueOf(portalId));
		queryCustom = queryCustom.replaceAll("#moduleId", String.valueOf(moduleId));
		return (List<Map<String, Object>>)jdbcTemplate.queryForList(queryCustom);
	}
	
}
