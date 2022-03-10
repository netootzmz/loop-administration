package com.smart.ecommerce.administration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.contract.MailPaymentContract;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.service.MailPaymentService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;

@RestController
@RequestMapping("/mail_payment_template")
public class MailPaymentController implements MailPaymentContract {

  @Autowired private MailPaymentService mailPaymentService;
  
  @Override
  @PostMapping(value = "/addMailPaymentBody", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse addMailPaymentBody(HttpServletRequest request,
    MailPaymentBodyDTO dto) {
    InfoTokenDto infoToken = InfoToken.getInfoToken(request);
    return mailPaymentService.addMailPaymentBody(infoToken, dto);
  }

  @Override
  @PostMapping(value = "/addMailPaymentProof", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse addMailPaymentProof(HttpServletRequest request,
    MailPaymentProofDTO dto) {
    InfoTokenDto infoToken = InfoToken.getInfoToken(request);
    return mailPaymentService.addMailPaymentProof(infoToken, dto);
  }

  @Override
  @PostMapping(value = "/getPaymentBody", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse getPaymentBody(HttpServletRequest request,
    ParamGetMailPaymentDTO dto) {
    InfoTokenDto infoToken = InfoToken.getInfoToken(request);
    return mailPaymentService.getPaymentBody(infoToken, dto);
  }

  @Override
  @PostMapping(value = "/getPaymentProof", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public GenericResponse getPaymentProof(HttpServletRequest request,
    ParamGetMailPaymentDTO dto) {
    InfoTokenDto infoToken = InfoToken.getInfoToken(request);
    return mailPaymentService.getPaymentProof(infoToken, dto);
  }

}
