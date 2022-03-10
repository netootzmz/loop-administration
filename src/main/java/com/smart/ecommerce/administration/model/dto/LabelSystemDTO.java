package com.smart.ecommerce.administration.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.ecommerce.entity.admin.CoreLanguage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelSystemDTO {

	@ApiModelProperty(notes = "ID del lenguage de la etiqueta", required = true, example = "1")
	private CoreLanguage languageId;
	@ApiModelProperty(notes = "ID del menú al cual pertenece la etiqueta", required = true)
	private Integer menuId;
	@ApiModelProperty(notes = "Tipo de menú de la etiqueta", required = true, allowableValues = "1, 2, 3")
	private Integer typeMenu;
	@ApiModelProperty(notes = "Identificador de la etiqueta; una clave de etiqueta puede estar disponible en más de un idioma", required = true)
	private String cve;
	@ApiModelProperty(notes = "Valor de la etiqueta el cual será mostrado en la vista")
	private String description;
	@ApiModelProperty(notes = "Status de la etiqueta: Activo(1), Inactivo(0), Eliminado(2)", allowableValues = "0, 1, 2", example = "1")
	private Integer statusId;	
	
	@JsonProperty("languageId")
	private void unpackLanguage(Integer languageId) {
		this.languageId = new CoreLanguage();
		this.languageId.setLanguageId(languageId);
	}
	
}
