package com.smart.ecommerce.administration.contract;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "mail_payment")
public interface MailPaymentContract {
  @ApiOperation(value = "Método para dar de alta nuevos template de envio de liga de pagos", notes = "")
  public GenericResponse addMailPaymentBody(HttpServletRequest request, @RequestBody MailPaymentBodyDTO dto);
  
  @ApiOperation(value = "Método para dar de alta nuevos template de comprobante de liga de pagos", notes = "")
  public GenericResponse addMailPaymentProof(HttpServletRequest request, @RequestBody MailPaymentProofDTO dto);
  
  @ApiOperation(value = "Método para obtener los textos de envio de liga de pagos", notes = "")
  public GenericResponse getPaymentBody(HttpServletRequest request, @RequestBody ParamGetMailPaymentDTO dto);
  
  @ApiOperation(value = "Método para obtener los textos de comprobante de liga de pagos", notes = "")
  public GenericResponse getPaymentProof(HttpServletRequest request, @RequestBody ParamGetMailPaymentDTO dto);
}
