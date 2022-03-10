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
public class UsersSearchDTO {

	@ApiModelProperty(notes = "Información de usuario a buscar", required = true, example = "Pedro Pérez")
	private String stringToSearch;
	@ApiModelProperty(notes = "Id Nivel de Jerarquía del Cliente", required = true, example = "1")
	private Integer hierarchyId;
	@ApiModelProperty(notes = "Id de Perfil/Rol de usuarios", required = true, example = "1")
	private Integer profileId;
	@ApiModelProperty(notes = "Status del usuario: Activo(1), Inactivo(2), Bloqueado(3), Baja(4)", allowableValues = "0, 1", example = "1")
	private Integer statusId;

}
