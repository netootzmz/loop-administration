package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.BitacoraCookiesContract;
import com.smart.ecommerce.administration.request.BitacoraCookiesRequest;
import com.smart.ecommerce.administration.request.IpRequest;
import com.smart.ecommerce.administration.service.BitacoraCookiesService;
import com.smart.ecommerce.administration.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bitacoraCookie")
public class BitacoraCookiesController implements BitacoraCookiesContract {

    @Autowired
    BitacoraCookiesService bitacoraCookiesService;

    @Override
    @PostMapping("/add")
    public GenericResponse addBitacoraCookie(@RequestBody BitacoraCookiesRequest request) {
        return bitacoraCookiesService.addBitacoraCookie(request);


    }

    @Override
    @PostMapping("/getByIp")
    public GenericResponse getBitacoraCookieByIp(@RequestBody IpRequest request) {
        return bitacoraCookiesService.getBitacoraService(request);
    }
}
