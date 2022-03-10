package com.smart.ecommerce.administration.model;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class ModuleDto {

  private Integer module_id;
  private Integer portal_id;
  private String name;
  private String description;
  private String user_admission;
  private Date date_admission;
  private String user_change;
  private Date date_change;
  private Integer status;
}
