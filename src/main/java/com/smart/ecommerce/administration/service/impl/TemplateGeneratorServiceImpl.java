package com.smart.ecommerce.administration.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.model.dto.TemplateClientDTO;
import com.smart.ecommerce.administration.repository.CoreTemplateRepository;
import com.smart.ecommerce.administration.repository.MailPaymentRepository;
import com.smart.ecommerce.administration.repository.TemplateGeneratorRepository;
import com.smart.ecommerce.administration.service.TemplateGeneratorService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.configuration.CoreTemplateSystem;
import com.smart.ecommerce.logging.SystemLog;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.smart.ecommerce.administration.util.Constants.QUERY_ERROR_LOG;
import static com.smart.ecommerce.administration.util.Constants.SPACE;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service("templateGeneratorService")
@Slf4j
public class TemplateGeneratorServiceImpl implements TemplateGeneratorService {

  @Autowired private CoreTemplateRepository coreTemplateSystemRepository;
  @Autowired private MailPaymentRepository mailPaymentRepository;
  @Autowired private TemplateGeneratorRepository templateGeneratorRepository;
  //@Autowired private RestTemplate restTemplate
  private static final String LOG_ERROR_TRACE = "Error: ";

  @Override
  public GenericResponse newTemplate() {

    CoreTemplateSystem coreTemplateSystem = coreTemplateSystemRepository.findById(6).get();

    byte[] reCipherBytes = Base64.getDecoder().decode(coreTemplateSystem.getTemplate());

    String htmllimpio = new String(reCipherBytes);

    //        log.info(htmllimpio);

    Document doc = Jsoup.parse(htmllimpio);

    Elements pngs = doc.select("img[src$=.png]");


    for (Element e:pngs){

      e.removeAttr("src");


    }





    log.info("***"+ pngs );
    log.info("***"+ doc );

    return null;
  }
  
  @SuppressWarnings("unchecked")
  public GenericResponse generatedTemplate(InfoTokenDto infoToken, ParamGetMailPaymentDTO dto) {
    ObjectMapper oMapper = new ObjectMapper();
    try {
      List<MailPaymentBodyDTO> lst = getPaymentBodyByValidateExist(dto);
      MailPaymentBodyDTO mpb;
      if (!lst.isEmpty()) {
        String text = templateGeneratorRepository.getTemplateGeneric(dto);
        mpb = lst.get(0);
        log.info("mpb: {}", mpb);
        Map<String, String> map = oMapper.convertValue(mpb, Map.class);
        //Map<String, Object> map = oMapper.convertValue(mpb, Map.class)
        log.info("map: {}", map);
        log.info(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        String html = replaceStringWithMap(text, map);
        TemplateClientDTO tc = setTemplateClient(infoToken, dto, html);
        addTemplateClient(tc);
      }else {
        log.info("Lista de texto vacio");
      }
    } catch (Exception e) {
      log.error("Error: {}", e);
    }
    return new GenericResponse();
  }
  
  public List<MailPaymentBodyDTO> getPaymentBodyByValidateExist(ParamGetMailPaymentDTO dto) throws GeneralException  {
    List<MailPaymentBodyDTO> results;
    String idOperation = SystemLog.newTxnIdOperation();
    try {
      Boolean haveTemplate = mailPaymentRepository.haveTemplatePaymentBodyByClientId(dto);
      if (Boolean.TRUE.equals(haveTemplate)) {
        results = mailPaymentRepository.getPaymentBody(dto);
      }else {
        results = mailPaymentRepository.getGenericPaymentBody();
      }
    } catch (Exception e) {
      StringBuilder errorStack = new StringBuilder();
      for (StackTraceElement er : e.getStackTrace()) {
        errorStack.append(LOG_ERROR_TRACE);
        errorStack.append(er.getClassName() + SPACE);
        errorStack.append(er.getFileName() + SPACE);
        errorStack.append(er.getLineNumber() + SPACE);
        errorStack.append(er.getMethodName() + SPACE);
        errorStack.append("");
      }
      String errorTrace = errorStack.toString();
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
    return results;
  }
  
  public List<MailPaymentProofDTO> getPaymentProofByValidateExist(ParamGetMailPaymentDTO dto) throws GeneralException  {
    List<MailPaymentProofDTO> results;
    String idOperation = SystemLog.newTxnIdOperation();
    try {
      Boolean haveTemplate = mailPaymentRepository.haveTemplatePaymentProofByClientId(dto);
      if (Boolean.TRUE.equals(haveTemplate)) {
        results = mailPaymentRepository.getPaymentProof(dto);
      }else {
        results = mailPaymentRepository.getGenericPaymentProof();
      }
    } catch (Exception e) {
      StringBuilder errorStack = new StringBuilder();
      for (StackTraceElement er : e.getStackTrace()) {
        errorStack.append(LOG_ERROR_TRACE);
        errorStack.append(er.getClassName() + SPACE);
        errorStack.append(er.getFileName() + SPACE);
        errorStack.append(er.getLineNumber() + SPACE);
        errorStack.append(er.getMethodName() + SPACE);
        errorStack.append("");
      }
      String errorTrace = errorStack.toString();
      log.error(QUERY_ERROR_LOG, errorTrace);
      throw new GeneralException("", errorTrace, idOperation);
    }
    return results;
  }

  public String replaceStringWithMap(String text, Map<String, String> mapValue) {
    if (!mapValue.isEmpty()) {
      for (Map.Entry<String, String> p : mapValue.entrySet()) {
        if (StringUtils.isNotEmpty(p.getValue())) {
          text = text.replace("${" + p.getKey() + "}", p.getValue());
        }else {
          text = text.replace("${" + p.getKey() + "}", "");
        }
        
      } 
    }
    mapValue.forEach((k,v) ->  log.info("Key: {} , Value: {}", k,v) );
    //log.info("results: {}", text)
    return text;
  }
  
  public String replaceStringWithMapObj(String text, Map<String, Object> mapValue) {
    if (!mapValue.isEmpty()) {
      for (Map.Entry<String, Object> p : mapValue.entrySet()) {
        if (p.getValue() != null) {
          text = text.replace("${" + p.getKey() + "}", p.getValue().toString()); 
        }else {
          text = text.replace("${" + p.getKey() + "}","");
        }
        
      } 
    }
    mapValue.forEach((k,v) ->  log.info("Key: {} , Value: {}", k,v) );
    //log.info("results: {}", text)
    return text;
  }
  
  public TemplateClientDTO setTemplateClient(InfoTokenDto infoTokenDto,
    ParamGetMailPaymentDTO dto, String html) {
    TemplateClientDTO tc = new TemplateClientDTO();
    tc.setTypeOwnerId(1);
    tc.setTemplateStr(stringToBase64(html));
    tc.setDescription(getDescription(dto.getCve()));
    tc.setClientId(dto.getClientId());
    tc.setCve(dto.getCve());
    tc.setUserId(infoTokenDto.getUser_by_register());
    return tc;
  }
  
  public String stringToBase64(String html) {
    String baseStr = "";
    org.apache.commons.codec.binary.Base64 base = new org.apache.commons.codec.binary.Base64();
    if (StringUtils.isNotEmpty(html)) {
      baseStr = new String(base.encode(html.getBytes()));
    }
    return baseStr;
  }
  
  public String getDescription(String cve) {
    switch (cve) {
      case "TLP":

        return "Template de liga de pago";
      case "TCP":

        return "Template de comprobante de pago";  

      case "TCC":

        return "Template de cambio de contraseña";

      default:
        return "Prueba";
    }
  }
  
  public void addTemplateClient(TemplateClientDTO dto) {
    Boolean hasTemplateClient = templateGeneratorRepository.hasTemplateClient(dto);
    if (Boolean.TRUE.equals(hasTemplateClient)) {
      templateGeneratorRepository.updateTemplateClient(dto);
    }else {
      templateGeneratorRepository.saveTemplateClient(dto);
    }
  }
    
}
