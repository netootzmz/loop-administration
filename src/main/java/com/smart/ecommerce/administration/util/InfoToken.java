package com.smart.ecommerce.administration.util;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.logging.Console;

public class InfoToken {

  public static Integer getLanguageId(HttpServletRequest request) {
    Integer languageId = 1;

    try {

      String token[] = request.getHeader("Authorization").replace("Bearer ", "").split("\\.");
      ObjectMapper mapper = new ObjectMapper();
      String languageIdTmp = mapper.readTree(new String(Base64.getDecoder().decode(token[1]))).get("languageId").asText();
      languageId = Integer.parseInt(languageIdTmp);

    } catch (Exception e) {
        return languageId;
//      e.printStackTrace();
    }

    return languageId;
  }

  public static InfoTokenDto getInfoToken(HttpServletRequest request) {
    InfoTokenDto dto = new InfoTokenDto();
    try {

      String token[] = request.getHeader("Authorization").replace("Bearer ", "").split("\\.");
      ObjectMapper mapper = new ObjectMapper();
      String language_idTmp = mapper.readTree(new String(Base64.getDecoder().decode(token[1]))).get("languageId").asText();
      String user_by_registerTmp = mapper.readTree(new String(Base64.getDecoder().decode(token[1]))).get("usrId").asText();
      dto.setLanguage_id(Integer.parseInt(language_idTmp));
      dto.setUser_by_register(Integer.parseInt(user_by_registerTmp));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return dto;
  }
  
  public static String getAditionalInfoToken(HttpServletRequest request, String infoName, String idOperation) {
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String token[] = request.getHeader("Authorization").replace("Bearer ", "").split("\\.");
			result = objectMapper.readTree(new String(Base64.getDecoder().decode(token[1]))).get(infoName).asText();
		} catch (Exception e) {
			Console.logException("Error al obtener informacion adicional del token", e, idOperation);
		}
		return result;
	}

}
