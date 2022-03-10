package com.smart.ecommerce.administration.model;

import lombok.Data;

import java.util.Date;

@Data
public class ActionDto {

  private Integer actionId;
  private Integer moduleId;
  private Integer portalId;
  private String name;
  private String description;
  private String url;
  private String userAdmission;
//  private Date date_admission;
  private String userChange;
//  private Date date_change;
  private Integer order;
  private Integer status;
}
