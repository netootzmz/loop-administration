package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.UserGenericDto;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface RestorePasswordService {

    GenericResponse requestRestorePass(String email, int typeContact, Integer languageId);

    GenericResponse requestRestorePassUserReg(String email, int typeContact, Integer languageId);

    GenericResponse codeVerification(String username, String codeVerification, Integer languageId);

    GenericResponse newPassword(String email, String newPassword, String newPasswordConfirm, String codeVerification, String user, Integer languageId);

    GenericResponse changePassword(String email, String password, String newPassword, String newPasswordConfirm, String user, Integer languageId);


    UserGenericDto requestRestorePassByUsername(String username, Integer languageId);

    GenericResponse getContacts(String username);
}
