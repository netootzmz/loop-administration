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
public class ResponseCatalogDTO implements Serializable{	
	
	private static final long serialVersionUID = -532726210562890178L;
	
	private Integer id;
	private String cve;
	private String name;
	private String description;
	private Integer status_id;
	private Integer user_by_register;
	private String created_at;
	private Integer language_id;

}
