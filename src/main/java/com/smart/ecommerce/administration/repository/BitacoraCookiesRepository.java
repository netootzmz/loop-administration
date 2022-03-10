package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.model.dto.BitacoraCookiesDTO;

public interface BitacoraCookiesRepository {
    String INSERT_BITACORA_COOKIES = "INSERT INTO bitacora_cookies_ip " +
            "(`ip`, `accept_cookie`, `status_id`, `created_at`) " +
            "VALUES (?, ?, ?, ?);";
    String GET_BITACORA_COOKIES = "SELECT `accept_cookie` FROM bitacora_cookies_ip WHERE ip=? order by created_at desc limit 1";

    Boolean addBitacoraCookies(BitacoraCookiesDTO bitacora, String idOperation);

    Boolean getBitacoraCookies(String ip, String idOperation);
}
