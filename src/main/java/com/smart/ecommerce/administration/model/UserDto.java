package com.smart.ecommerce.administration.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String token;
    private Integer user_id;
    private String user_name;
    private String password;
    private Integer roleId;
    private String user_admission;
    private Date date_admission;
    private String name;
    private String last_name1;
    private String last_name2;
    private String mail;
    private String phone;
    private Integer extension;
    private String user_change;
    private Date date_change;
    private Integer status_id;
    private Integer language_id;
    private String groupId;
    private String clientId;
}
