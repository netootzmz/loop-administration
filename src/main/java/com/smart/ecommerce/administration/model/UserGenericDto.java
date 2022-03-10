package com.smart.ecommerce.administration.model;

public class UserGenericDto {
	
	private String mail;
	private String phone;
	private String mensaje;
	private String codeStatus;
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getCodeStatus() {
		return codeStatus;
	}
	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
	@Override
	public String toString() {
		return "UserGenericDto [mail=" + mail + ", phone=" + phone + ", mensaje=" + mensaje + ", codeStatus="
				+ codeStatus + "]";
	}
	

}
