package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.request.BitacoraCookiesRequest;
import com.smart.ecommerce.administration.request.IpRequest;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface BitacoraCookiesService {
    GenericResponse addBitacoraCookie(BitacoraCookiesRequest request);

    GenericResponse getBitacoraService(IpRequest request);
}
