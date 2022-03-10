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
public class CatalogDTO implements Serializable{
	
	private static final long serialVersionUID = 5515008362028655289L;
	
	private int id;
	private String nameCatalog;

}
