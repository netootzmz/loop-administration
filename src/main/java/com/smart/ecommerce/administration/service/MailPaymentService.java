package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface MailPaymentService {
  GenericResponse addMailPaymentBody(InfoTokenDto infoToken, MailPaymentBodyDTO dto);
  GenericResponse addMailPaymentProof(InfoTokenDto infoToken, MailPaymentProofDTO dto);
  GenericResponse getPaymentBody(InfoTokenDto infoToken, ParamGetMailPaymentDTO dto);
  GenericResponse getPaymentProof(InfoTokenDto infoToken, ParamGetMailPaymentDTO dto);
}
