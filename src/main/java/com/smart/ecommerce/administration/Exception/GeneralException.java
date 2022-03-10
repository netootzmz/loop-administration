package com.smart.ecommerce.administration.Exception;

import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.logging.Console;

public class GeneralException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private String errorCode;
  private String errorMesage;

  public GeneralException(ResponseType type, String idOperacion) {
    super(type.getMessage());
    this.errorCode = type.getCode();
    this.errorMesage = type.getMessage();
    info(idOperacion);
  }

  public GeneralException(ResponseType type, String message, String idOperacion) {
    super(message);
    this.errorMesage = message;
    this.errorCode = type.getCode();
    info(idOperacion);
  }

  public GeneralException(String message, Throwable cause, String errorCode,
                          String errorMesage) {
    super(message, cause);
    this.errorCode = errorCode;
    this.errorMesage = errorMesage;
  }

  public GeneralException(String cause, String errorCode, String errorMesage) {
    super(cause);
    this.errorCode = errorCode;
    this.errorMesage = errorMesage;
  }

  public GeneralException(String message, Throwable cause, boolean enableSuppression,
                          boolean writableStackTrace, String errorCode, String errorMesage) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
    this.errorMesage = errorMesage;
  }

  private void info(String idOperacion){
    Console.writeln(Console.Level.INFO, idOperacion, this.errorMesage);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMesage() {
    return errorMesage;
  }
}
