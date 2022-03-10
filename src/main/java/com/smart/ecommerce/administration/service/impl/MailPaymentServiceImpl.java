package com.smart.ecommerce.administration.service.impl;

import static com.smart.ecommerce.administration.util.Constants.QUERY_ERROR_LOG;
import static com.smart.ecommerce.administration.util.Constants.SPACE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.MailPaymentBodyDTO;
import com.smart.ecommerce.administration.model.dto.MailPaymentProofDTO;
import com.smart.ecommerce.administration.model.dto.ParamGetMailPaymentDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.MailPaymentRepository;
import com.smart.ecommerce.administration.service.MailPaymentService;
import com.smart.ecommerce.administration.service.TemplateGeneratorService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.logging.SystemLog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailPaymentServiceImpl implements MailPaymentService {
  @Autowired private MailPaymentRepository mailPaymentRepository;
  @Autowired private ErrorCodeRepository codeRepository;
  @Autowired private TemplateGeneratorService tgService; 
  private static final String KEY_RESULTS = "results";
  private static final String LOG_ERROR_TRACE = "Error: ";
  private static final String VALUE_SUCCESS = "Informacion procesada correctamente";
  
  @Override
  public GenericResponse addMailPaymentBody(InfoTokenDto infoToken,
    MailPaymentBodyDTO dto) {
    log.info("::: MailPaymentServiceImpl - AddMailPaymentBody :::");
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeRepository.getAll(infoToken.getLanguage_id());
    dto.setUserId(infoToken.getUser_by_register());
    try {
      ParamGetMailPaymentDTO pmp = new ParamGetMailPaymentDTO();
      pmp.setClientId(dto.getClientId());
      Boolean haveTemplate = mailPaymentRepository.haveTemplatePaymentBodyByClientId(pmp);
      if (Boolean.FALSE.equals(haveTemplate)) {
        Integer save = mailPaymentRepository.saveMailPaymentBody(dto);
        if (save.equals(1)) {
          response.setCodeStatus("00");
          response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
          Map<String, Object> information = new HashMap<>();
          information.put(KEY_RESULTS, VALUE_SUCCESS);
          response.setInformation(information);
        }else {
          response.setCodeStatus("01");
          response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
        }
      }else {
        Integer update = mailPaymentRepository.updateMailPaymentBody(dto);
        if (update.equals(1)) {
          response.setCodeStatus("00");
          response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
          Map<String, Object> information = new HashMap<>();
          information.put(KEY_RESULTS, VALUE_SUCCESS);
          response.setInformation(information);
        }else {
          response.setCodeStatus("01");
          response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
        }
      }
      ParamGetMailPaymentDTO param = new ParamGetMailPaymentDTO();
      param.setClientId(dto.getClientId());
      param.setCve(dto.getCve());
      tgService.generatedTemplate(infoToken, param);
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
      log.info("Error AddMailPaymentBodyl: {}", errorTrace);
      response.setCodeStatus("03");
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      return response;
    }
  }

  @Override
  public GenericResponse addMailPaymentProof(InfoTokenDto infoToken,
    MailPaymentProofDTO dto) {
    log.info("::: MailPaymentServiceImpl - AddMailPaymentProof :::");
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeRepository.getAll(infoToken.getLanguage_id());
    dto.setUserId(infoToken.getUser_by_register());
    try {
      ParamGetMailPaymentDTO pmp = new ParamGetMailPaymentDTO();
      pmp.setClientId(dto.getClientId());
      Boolean haveTemplate = mailPaymentRepository.haveTemplatePaymentProofByClientId(pmp);
      if (Boolean.FALSE.equals(haveTemplate)) {
        Integer save = mailPaymentRepository.saveMailPaymentProof(dto);
        if (save.equals(1)) {
          response.setCodeStatus("00");
          response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
          Map<String, Object> information = new HashMap<>();
          information.put(KEY_RESULTS, VALUE_SUCCESS);
          response.setInformation(information);
        }else {
          response.setCodeStatus("01");
          response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
        }
      }else {
        Integer update = mailPaymentRepository.updateMailPaymentProof(dto);
        if (update.equals(1)) {
          response.setCodeStatus("00");
          response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
          Map<String, Object> information = new HashMap<>();
          information.put(KEY_RESULTS, VALUE_SUCCESS);
          response.setInformation(information);
        }else {
          response.setCodeStatus("01");
          response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
        }
      }
      ParamGetMailPaymentDTO param = new ParamGetMailPaymentDTO();
      param.setClientId(dto.getClientId());
      param.setCve(dto.getCve());
      tgService.generatedTemplate(infoToken, param);
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
      log.info("Error AddMailPaymentProof: {}", errorTrace);
      response.setCodeStatus("03");
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      return response;
    }
  }

  @Override
  public GenericResponse getPaymentBody(InfoTokenDto infoToken,
    ParamGetMailPaymentDTO dto) {
    log.info("::: MailPaymentServiceImpl - GetPaymentBody :::");
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeRepository.getAll(infoToken.getLanguage_id());
    dto.setUserId(infoToken.getUser_by_register());
    try {
      List<MailPaymentBodyDTO> lst = getPaymentBodyByValidateExist(dto);
      //List<MailPaymentBodyDTO> lst = mailPaymentRepository.getPaymentBody(dto)
      if (!lst.isEmpty()) {
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();
        information.put(KEY_RESULTS, lst.get(0));
        response.setInformation(information);
      }else {
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
      log.info("Error GetPaymentBody: {}", errorTrace);
      response.setCodeStatus("03");
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      return response;
    }
  }

  @Override
  public GenericResponse getPaymentProof(InfoTokenDto infoToken,
    ParamGetMailPaymentDTO dto) {
    log.info("::: MailPaymentServiceImpl - GetPaymentProof :::");
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeRepository.getAll(infoToken.getLanguage_id());
    dto.setUserId(infoToken.getUser_by_register());
    try {
      List<MailPaymentProofDTO> lst = getPaymentProofByValidateExist(dto);
      //List<MailPaymentProofDTO> lst = mailPaymentRepository.getPaymentProof(dto)
      if (!lst.isEmpty()) {
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();
        information.put(KEY_RESULTS, lst.get(0));
        response.setInformation(information);
      }else {
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
      log.info("Error GetPaymentProof: {}", errorTrace);
      response.setCodeStatus("03");
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      return response;
    }
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
  
}
