package com.smart.ecommerce.administration.request;

import lombok.Data;

@Data
public class BitacoraCookiesRequest {
    private String ip;
    private boolean cookieAccept;
}
