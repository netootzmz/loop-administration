package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

@Data
public class SetPasswordDTO {

    private String email;
    private String password;
    private String newPassword;
    private String newPasswordConfirm;

}
