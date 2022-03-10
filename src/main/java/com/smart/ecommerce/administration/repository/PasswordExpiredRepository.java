package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.model.dto.BitacoraPasswordDTO;

public interface PasswordExpiredRepository {
    String INSERT_BITACORA_PASSWORD = "INSERT INTO `bitacora_password` " +
            "(`password`, `date_register`, `status`,`user_id`) " +
            "VALUES (?, ? , ?, ?);";
    String UPDATE_USER_IDBITPASS = "UPDATE `user` " +
            "SET `idbitpass` = ? " +
            "WHERE `user_id` = ? ";

    int newRecordBitacoraPassword(BitacoraPasswordDTO bitacoraPasswordDTO);
    boolean updateUserIdBitacoraPassword(int idBitPass, int userId);


}
