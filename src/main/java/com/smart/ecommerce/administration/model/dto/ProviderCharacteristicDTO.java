package com.smart.ecommerce.administration.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.ecommerce.entity.admin.ProviderCatalog;
import com.smart.ecommerce.entity.admin.CharacteristicCatalog;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ProviderCharacteristicDTO {

	@ApiModelProperty(notes = "ID del proveedor con el cual se registrará el nuevo registro, el proveedor deberá de existir previamente en base de datos", required = true)
	private ProviderCatalog provider;
	@ApiModelProperty(notes = "ID de la característica con la cual se guardará el nuevo registro, la característica deberá de existir previamente en base de datos", required = true)
	private CharacteristicCatalog characteristic;
	@ApiModelProperty(notes = "Nombre del nuevo registro", required = true)
	private String name;
	@ApiModelProperty(notes = "Clave del nuevo registro", required = true)
	private String code;
	@ApiModelProperty(notes = "Costo del producto", required = false)
	private Double cost;
	@ApiModelProperty(notes = "Status del nuevo registro, Activo(1), Inactivo(2)", required = true)
	private Integer status;
	
	@JsonProperty("provider")
	private void unpackProvider(Long providerId) {
		this.provider = new ProviderCatalog();
		provider.setId(providerId);
	}
	
	@JsonProperty("characteristic")
	private void unpackCharacteristic(Long characteristicId) {
		this.characteristic = new CharacteristicCatalog();
		characteristic.setId(characteristicId);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public ProviderCatalog getProvider() {
		return provider;
	}

	public void setProvider(ProviderCatalog provider) {
		this.provider = provider;
	}

	public CharacteristicCatalog getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(CharacteristicCatalog characteristic) {
		this.characteristic = characteristic;
	}
	
}
