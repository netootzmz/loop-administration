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
public class RequestCatalogDTO  implements Serializable{

	private static final long serialVersionUID = -9140116898656160323L;
	
	//nameCat corresponde al nombre de la base de datos
	private String nameCat;
	
	//id corresponde al registro que se va a modificar
	private int id;
	
	//datos a modificar
	private String cve;
	private String name;
	private String description;
	private String status_id;
	private Integer language_id;
	

}
