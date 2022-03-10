package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BitacoraPasswordDTO {
    private int idBitPass;
    private String password;
    private LocalDate dateRegister;
    private int status;
    private int userId;
}
