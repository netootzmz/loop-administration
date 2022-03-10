package com.smart.ecommerce.administration.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class PortalDto {

  private Integer portal_id;
  private String description;
  private String url;
  private String name;
  private String user_admission;
  private Date date_admission;
  private String user_change;
  private Date date_change;
  private Integer status;
}
