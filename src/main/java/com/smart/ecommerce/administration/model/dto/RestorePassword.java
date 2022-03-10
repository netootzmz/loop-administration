package com.smart.ecommerce.administration.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestorePassword {

	private String userName;
	private String newPassword;
	private String newPasswordConfirm;
	private String codeVerification;

}
