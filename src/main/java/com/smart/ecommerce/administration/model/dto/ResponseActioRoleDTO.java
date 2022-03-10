/**
 * 
 */
package com.smart.ecommerce.administration.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Eduardo Valeriano
 *
 */
@Data
public class ResponseActioRoleDTO implements Serializable{
	
	private static final long serialVersionUID = 5518932997560556655L;
	
	private String  roleid;
	private String description;
	private String portalId;
	private String portalName;
	private String moduleNam;
	private String moduleId;
	private String actionName;
	private String actuinUrl;
	private String actionId;

}
