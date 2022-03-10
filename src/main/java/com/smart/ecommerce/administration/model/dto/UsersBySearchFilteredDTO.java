package com.smart.ecommerce.administration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersBySearchFilteredDTO {

	Integer status_id;
	String status_name;
	String hierarchy;
	String group;
	String group_description;
	String name_user;
	Integer role;
	String role_description;
	String last_login;

}
