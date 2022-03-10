package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.model.dto.BitacoraPasswordDTO;
import com.smart.ecommerce.administration.repository.PasswordExpiredRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class PasswordExpiredRepositoryImpl implements PasswordExpiredRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int newRecordBitacoraPassword(BitacoraPasswordDTO bitacoraPasswordDTO) {
        return executeInsert(bitacoraPasswordDTO);
    }

    @Override
    public boolean updateUserIdBitacoraPassword(int idBitPass, int userId) {
        return executeUpdate(new Object[]{idBitPass,userId});
    }

    private int executeInsert(BitacoraPasswordDTO bitacoraPasswordDTO){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement=connection.prepareStatement(INSERT_BITACORA_PASSWORD, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,bitacoraPasswordDTO.getPassword());
            statement.setTimestamp(2, Timestamp.valueOf(bitacoraPasswordDTO.getDateRegister().atStartOfDay()));
            statement.setInt(3,bitacoraPasswordDTO.getStatus());
            statement.setInt(4,bitacoraPasswordDTO.getUserId());
            return statement;
        },keyHolder);

        return keyHolder.getKey().intValue();
    }

    private boolean executeUpdate(Object[] params){
        int affectedRows=jdbcTemplate.update(UPDATE_USER_IDBITPASS, params);
        return affectedRows>0;
    }


}
