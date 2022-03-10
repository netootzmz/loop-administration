package com.smart.ecommerce.administration.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenPaymentLinkDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MSIResponse;
import com.smart.ecommerce.administration.model.dto.PaymentCheckOutParametersDTO;
import com.smart.ecommerce.administration.repository.ConfigurationScreenPaymentCheckOutRepository;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.service.ConfigurationScreenPaymentCheckOutService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;

import static com.smart.ecommerce.administration.util.Constants.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfigurationScreenPaymentCheckOutServiceImpl implements ConfigurationScreenPaymentCheckOutService {
  
  @Autowired private ConfigurationScreenPaymentCheckOutRepository cspcRepository;
  @Resource private ErrorCodeRepository codeDao;
  
  private static final String LOG_ERROR_TRACE = "Error: ";
  
  @Override
  public GenericResponse getConfigurationLinkPayScreenByUser(InfoTokenDto infoToken) {
    log.info(":: ConfigurationScreenPaymentCheckOutServiceImpl - GetConfigurationLinkPayScreenByUser ::");
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeDao.getAll(infoToken.getLanguage_id());
    try {
      List<ConfigurationScreenPaymentLinkDto> results = cspcRepository.getConfigurationLinkPayScreenByUser(infoToken.getUser_by_register());
      //List<ConfigurationScreenDto> results = cspcRepository.getConfigurationLinkPayScreenByUser(infoToken.getUser_by_register())
      if (!results.isEmpty()) {
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();

        information.put("configuration", validateConfigurationScreenPaymentLink(results, infoToken));
        //information.put("configuration", results.get(0))
        response.setInformation(information);
      } else {
        response.setCodeStatus("01");
        response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
      }
      return response;
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
      log.error("Errro: {}", errorTrace);
      response.setCodeStatus("03");
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      e.printStackTrace();
      return response; 
    }
    
  }
  
  public ConfigurationScreenPaymentLinkDto validateConfigurationScreenPaymentLink
    (List<ConfigurationScreenPaymentLinkDto> results, InfoTokenDto token) {
    ConfigurationScreenPaymentLinkDto rs;
    List<PaymentCheckOutParametersDTO> data = getMSIConfigurationScreenPaymentLink(token);
    if (!data.isEmpty()) {
      rs = getAllConfigurationScreenPaymentLink(results, data);
    }else {
      rs = results.get(0);
    }
    return rs;
  }
  
  public List<PaymentCheckOutParametersDTO> getMSIConfigurationScreenPaymentLink(InfoTokenDto token){
    List<PaymentCheckOutParametersDTO> results;
    List<PaymentCheckOutParametersDTO> msiByUser= cspcRepository.getMsiByIdUser(token);
    if (!msiByUser.isEmpty()) {
      PaymentCheckOutParametersDTO dt = msiByUser.get(0);
      Boolean hasMsi = cspcRepository.hasMsiConfigured(dt);
      if (Boolean.TRUE.equals(hasMsi)) {
        results = cspcRepository.getMsiByClientProductId(dt);
      }else {
        results = msiByUser;
      }
      
    }else {
      results = Collections.emptyList();
    }
    return results;
    
    
  }
  
  public ConfigurationScreenPaymentLinkDto getAllConfigurationScreenPaymentLink
    (List<ConfigurationScreenPaymentLinkDto> results, List<PaymentCheckOutParametersDTO> data) {
    ConfigurationScreenPaymentLinkDto rs = results.get(0);
    Map<Object, List<PaymentCheckOutParametersDTO>> uniqueHaveMsi = getUniqueValuesMap(data, PaymentCheckOutParametersDTO::getHaveMsi);
    Map<Object, List<PaymentCheckOutParametersDTO>> uniqueUnity = getUniqueValuesMap(data, PaymentCheckOutParametersDTO::getValidityUnity);
    Map<Object, List<PaymentCheckOutParametersDTO>> uniqueValue = getUniqueValuesMap(data, PaymentCheckOutParametersDTO::getValidityValue);
    Map<Object, List<PaymentCheckOutParametersDTO>> uniqueAllowEdition = getUniqueValuesMap(data, PaymentCheckOutParametersDTO::getAllowEdition);
    Map<Object, List<PaymentCheckOutParametersDTO>> uniqueAllowMsi = getUniqueValuesMap(data, PaymentCheckOutParametersDTO::getAllowMsi);

    rs.setHaveMsi(getDataIntByMap(uniqueHaveMsi));
    rs.setValidityUnity(getDataIntByMap(uniqueUnity));
    rs.setValidityValue(getDataIntByMap(uniqueValue));
    rs.setAllowEdition(getDataIntByMap(uniqueAllowEdition));
    rs.setAllowMsi(getDataIntByMap(uniqueAllowMsi));
    rs.setMsi(getMsiResponse(data));
    return rs;
  }
  
  public Integer getDataIntByMap(Map<Object, List<PaymentCheckOutParametersDTO>> data) {
    Integer dataInt = 0;
    if (!data.isEmpty()) {
      for (Entry<Object, List<PaymentCheckOutParametersDTO>> intData : data.entrySet()) {
        dataInt = Integer.parseInt(intData.getKey().toString());
      }
    }
    return dataInt;
  }
  
  public String getDataStringByMap(Map<Object, List<PaymentCheckOutParametersDTO>> data) {
    String dataStr = "";
    if (!data.isEmpty()) {
      for (Entry<Object, List<PaymentCheckOutParametersDTO>> strData : data.entrySet()) {
        dataStr = strData.getKey().toString();
      }
    }
    return dataStr;
  }
  
  public Map<Object, List<PaymentCheckOutParametersDTO>> getUniqueValuesMap
  (List<PaymentCheckOutParametersDTO> results, Function<? super PaymentCheckOutParametersDTO, ?> f){
    Map<Object, List<PaymentCheckOutParametersDTO>> strValue;
    if (f != null) {
      strValue =   results.stream().collect(Collectors.groupingBy(f));
    }else {
      strValue =  Collections.emptyMap();
    }
    return strValue;
  }
  
  public List<MSIResponse> getMsiResponse(List<PaymentCheckOutParametersDTO> results) {
    List<MSIResponse> lst = new ArrayList<>();
    if (!results.isEmpty()) {
      for (PaymentCheckOutParametersDTO r : results) {
        MSIResponse msi = new MSIResponse();
        msi.setMonthsAvailable(r.getMonthsAvailable());
        msi.setMsiId(r.getMsiId());
        msi.setMsiName(r.getMsiName());
        msi.setAmountPucharse(r.getAmountPucharse());
        lst.add(msi);
      }
    }
    return lst;
  }
  
}
