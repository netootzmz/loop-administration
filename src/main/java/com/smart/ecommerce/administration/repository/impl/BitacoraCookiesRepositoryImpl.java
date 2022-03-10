package com.smart.ecommerce.administration.repository.impl;

import com.smart.ecommerce.administration.model.dto.BitacoraCookiesDTO;
import com.smart.ecommerce.administration.repository.BitacoraCookiesRepository;
import com.smart.ecommerce.logging.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Repository
public class BitacoraCookiesRepositoryImpl implements BitacoraCookiesRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean addBitacoraCookies(BitacoraCookiesDTO bitacora, String idOperation) {
        try {
            Console.writeln(Console.Level.INFO, idOperation, "Guardando bitacora cookie");
            int affectedRows = jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_BITACORA_COOKIES);
                statement.setString(1, bitacora.getIp());
                statement.setBoolean(2, bitacora.getAcceptCookie());
                statement.setInt(3, bitacora.getStatusId());
                statement.setTimestamp(4, Timestamp.valueOf(bitacora.getCreatedAt()));

                return statement;
            });
            return affectedRows > 0;
        } catch (Exception e) {
            Console.logException("Error al guardar bitacora cookie:", e, idOperation);
            return false;
        }
    }

    @Override
    public Boolean getBitacoraCookies(String ip, String idOperation) {
        try {
            Console.writeln(Console.Level.INFO, idOperation, "Consultando bitacora cookie");
            return jdbcTemplate.queryForObject(GET_BITACORA_COOKIES, new Object[]{ip}, Boolean.class);
        } catch (Exception e) {
            Console.logException("Error al consultar bitacora cookie:", e, idOperation);
            return null;
        }
    }
}
