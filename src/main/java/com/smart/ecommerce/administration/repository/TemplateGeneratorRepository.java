package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.model.dto.TemplateClientDTO;

public interface TemplateGeneratorRepository {
  String getTemplateGeneric(ParamGetMailPaymentDTO dto);
  Boolean hasTemplateClient(TemplateClientDTO dto);
  Integer saveTemplateClient(TemplateClientDTO dto);
  Integer updateTemplateClient(TemplateClientDTO dto);
}
