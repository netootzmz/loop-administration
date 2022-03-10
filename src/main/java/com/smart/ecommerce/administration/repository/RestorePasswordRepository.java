package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.CoreRestorePassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestorePasswordRepository {

  boolean saveRestorePassword(CoreRestorePassword restorePassword, String idOperation);

  CoreRestorePassword getByUserIdVerficationCode(Integer userId, String verificationCode);

  CoreRestorePassword getById(Integer restorePassId);

}
