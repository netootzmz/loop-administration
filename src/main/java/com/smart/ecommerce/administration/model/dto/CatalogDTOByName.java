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
public class CatalogDTOByName implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 5169755986627602613L;
	private String nameCatalog;
	private Integer languageId;

}
