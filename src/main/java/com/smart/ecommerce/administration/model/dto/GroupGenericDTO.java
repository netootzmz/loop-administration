package com.smart.ecommerce.administration.model.dto;

public class GroupGenericDTO {
	
	private String clientId;
	
	private String parentGroupId;
	
	private Integer groupLevelId;
	
	private String cve;
	
	private String name;
	
	private Integer statusId;


	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public Integer getGroupLevelId() {
		return groupLevelId;
	}

	public void setGroupLevelId(Integer groupLevelId) {
		this.groupLevelId = groupLevelId;
	}

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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	@Override
	public String toString() {
		return "GroupGenericDTO [clientId=" + clientId + ", parentGroupId=" + parentGroupId
				+ ", groupLevelId=" + groupLevelId + ", cve=" + cve + ", name=" + name + ", statusId=" + statusId + "]";
	}
	
}
