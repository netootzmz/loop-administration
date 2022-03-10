package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.request.BitacoraCookiesRequest;
import com.smart.ecommerce.administration.request.IpRequest;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface BitacoraCookiesContract {
    GenericResponse addBitacoraCookie(BitacoraCookiesRequest request);
    GenericResponse getBitacoraCookieByIp(IpRequest request);
}
