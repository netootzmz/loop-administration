package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LogfileLoginDTO {

    private Integer logfileLoginId;
    private Long userId;
    private Date    dateLogin;



}
