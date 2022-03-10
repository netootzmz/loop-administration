package com.smart.ecommerce.administration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestGroupDTO {
	
	@ApiModelProperty(notes = "ID del cliente al que pertenece el grupo", required = true, example = "1")
	private String clientId;
	
	@ApiModelProperty(notes = "ID del grupo padre correspondiente del grupo actual (si aplica)", required = false, example = "N1")
	private String parentGroupId;
	
	@ApiModelProperty(notes = "Nivel del grupo dentro de la jerarquía: Negocio, Sucursal, Departamento", allowableValues = "1, 2, 3", example = "1")
	private Integer groupLevelId;
	
	@ApiModelProperty(notes = "Clave del grupo", required = true, example = "VIPS")
	private String cve;
	
	@ApiModelProperty(notes = "Nombre del grupo", required = true, example = "Vips")
	private String name;
	
	@ApiModelProperty(notes = "Status del grupo: Activo(1), Inactivo(0)", allowableValues = "0, 1", example = "1")
	private Integer statusId;

}