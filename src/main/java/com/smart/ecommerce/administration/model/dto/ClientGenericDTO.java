package com.smart.ecommerce.administration.model.dto;

public class ClientGenericDTO {
	
	private String cve;
	private String name;
	private int statusId;
	
	public String getCve() {
		return cve;
	}
	public void setCve(String cve) {
		this.cve = cve;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	@Override
	public String toString() {
		return "ClientGenericDTO [cve=" + cve + ", name=" + name + ", statusId=" + statusId + "]";
	}

}
