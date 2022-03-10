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
public class RequestClientDTO {

	@ApiModelProperty(notes = "Clave del cliente", required = true, example = "IA")
	private String cve;
	@ApiModelProperty(notes = "Nombre del cliente", required = true, example = "ALSEA")
	private String name;
	@ApiModelProperty(notes = "Status del cliente: Activo(1), Inactivo(0)", allowableValues = "0, 1", example = "1")
	private Integer statusId;

}
