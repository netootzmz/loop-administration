package com.smart.ecommerce.administration.model;

import lombok.Data;

@Data
public class RoleDto {

  private Integer roleId;
  private String description;
  private String userAdmission;
  private String userChange;
  private Integer status;

}
