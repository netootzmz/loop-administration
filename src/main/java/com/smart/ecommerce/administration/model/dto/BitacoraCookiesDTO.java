package com.smart.ecommerce.administration.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BitacoraCookiesDTO {
    private Integer bitacoraCookieIpId;
    private String ip;
    private Boolean acceptCookie;
    private Integer statusId;
    private LocalDateTime createdAt;
}
