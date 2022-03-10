package com.smart.ecommerce.administration.util;

import java.util.List;

import com.smart.ecommerce.administration.model.dto.CoreErrorCodeDto;
import com.smart.ecommerce.entity.core.CoreErrorCode;

public class ErrorCode {

  public static final String ERROR_CODE_PROCESADO_CORRECTAMENTE     = "00";
  public static final String ERROR_CODE_SIN_REGISTROS               = "01";
  public static final String ERROR_CODE_PARAMETROS_PREEXISTENTES    = "02";
  public static final String ERROR_CODE_NO_EXISTE_EN_BD             = "03";
  public static final String ERROR_CODE_PARAMETROS_INCOMPLETOS      = "04";
  public static final String ERROR_CODE_NO_VALIDO                   = "05";
  public static final String ERROR_CODE_ERROR_AL_ELIMINAR           = "06";
  public static final String ERROR_CODE_ERROR_EN_EL_SISTEMA         = "07";
  public static final String ERROR_CODE_ROL_ASOSCIADO_A_UN_USUARIO  = "08";
  public static final String ERROR_CODE_IMPOSIBLE_REALIZAR_EDICION  = "09";
  public static final String ERROR_CODE_USUARIO_CORREO_NO_EXISTE    = "10";
  public static final String ERROR_CODE_INACTIVO                    = "11";
  public static final String ERROR_CODE_CODIGO_VERIFICACION_ERRONEO = "12";
  public static final String ERROR_CODE_CODIGO_VERIFICACION_VENCIDO = "13";
  public static final String ERROR_CODE_EL_PASSWORD_NO_ES_VALIDO 	= "35";
  public static final String ERROR_CODE_CREDENCIALES_INVALIDAS 		= "53";
  public static final String ERROR_CODE_VUELVE_A_INTENTARLO 		= "54";
  public static final String ERROR_CODE_USUARIO_BLOQUEADO	 		= "55";
  public static final String ERROR_CODE_USUARIO_NO_EXISTE	 		= "51";
  public static final String ERROR_CODE_PASSWORD_NO_COINCIDE		= "59";
  public static final String ERROR_CODE_VALORES_NULOS				= "57";
  public static final String ERROR_CODE_PASSWORD_PREVIO				= "71";
  public static final String ERROR_CODE_PASSWORD_ACTUAL_NO_COINCIDE = "73";
  public static final String ERROR_CODE_PASSWORD_NUEVO_PASSWORD_ACTUAL = "74";
  public static final String ERROR_CODE_USUARIO_INACTIVO = "75";
  public static final String ERORR_CODE_CONFIGURACION_MAIL_INEXISTENTE = "81";


  public static CoreErrorCodeDto getError(List<CoreErrorCode> listError, String code) {
    CoreErrorCodeDto dto = new CoreErrorCodeDto();

    for (CoreErrorCode error : listError) {

      if (error.getCode().equals(code)) {

        dto.setCode(error.getCode());
        dto.setMessage(error.getMessage());
        dto.setStatus(error.getStatus());
        break;

      }

    }
    return dto;
  }


}
