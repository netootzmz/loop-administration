package com.smart.ecommerce.administration.contract;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.LabelSystemDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "label-system")
public interface LabelSystemContract {
	
	@ApiOperation(value = "Método para dar de alta nuevas etiquetas", notes = "")
	public GenericResponse createLabelSystem(HttpServletRequest request, @RequestBody LabelSystemDTO labelSystemDto);
	
	@ApiOperation(value = "Método para actualizar el valor de las etiquetas", notes = "Sólo se permite actualizar la clave, la descripción y el status de un etiqueta")
	public GenericResponse updateLabelSystem(HttpServletRequest request, @RequestBody LabelSystemDTO labelSystemDto, @PathVariable Long id);
	
	@ApiOperation(value = "Método para eliminar un registro (eliminado lógico)", notes = "Si un registro ya se encuentra con status 2, este no podrá ser eliminado")
	public GenericResponse deleteLabelSystem(HttpServletRequest request, @PathVariable Long id);
	
	@ApiOperation(value = "Método para obtener el listado de etiquetas de una sección", notes = "Se requiere enviar como parámetro el languageId, menuId y typeMenu")
	public GenericResponse getLabelSystem(HttpServletRequest request, Integer languageId, Integer menuId, Integer typeMenu);


	@ApiOperation(value = "Método para obtener el listado de system Label", notes = "Se requiere enviar como parámetro el languageId, menuId y actionId")
	public GenericResponse getLabelSystemByAction(HttpServletRequest request, Integer languageId, Integer actionId);

}
