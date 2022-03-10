package com.smart.ecommerce.administration.repository;

import java.util.List;

import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;

public interface MailPaymentRepository {
  Integer saveMailPaymentBody(MailPaymentBodyDTO dto) throws GeneralException;
  Integer updateMailPaymentBody(MailPaymentBodyDTO dto) throws GeneralException;
  Integer saveMailPaymentProof(MailPaymentProofDTO dto) throws GeneralException;
  Integer updateMailPaymentProof(MailPaymentProofDTO dto) throws GeneralException;
  Boolean haveTemplatePaymentBodyByClientId(ParamGetMailPaymentDTO dto);
  Boolean haveTemplatePaymentProofByClientId(ParamGetMailPaymentDTO dto);
  List<MailPaymentBodyDTO> getPaymentBody(ParamGetMailPaymentDTO dto) throws GeneralException;
  List<MailPaymentProofDTO> getPaymentProof(ParamGetMailPaymentDTO dto) throws GeneralException;
  List<MailPaymentBodyDTO> getGenericPaymentBody() throws GeneralException;
  List<MailPaymentProofDTO> getGenericPaymentProof() throws GeneralException;
  
}
