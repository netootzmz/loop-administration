/**
 * 
 */
package com.smart.ecommerce.administration.model.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Eduardo Valeriano
 *
 */
@Data
public class GrantedDTO implements Serializable{	
	
	private static final long serialVersionUID = 3946197841588805841L;
	
	private int roleId;
	private int portalId;
	private int moduleId;
	private int actionId;
	

}
