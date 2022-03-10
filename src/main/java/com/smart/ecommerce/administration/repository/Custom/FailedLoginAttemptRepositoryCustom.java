package com.smart.ecommerce.administration.repository.Custom;

import java.util.List;


import com.smart.ecommerce.entity.core.FailedLoginAttempt;

public interface FailedLoginAttemptRepositoryCustom {
	List<FailedLoginAttempt> getFailedLoginAttemptsById(Integer userId, int minutes);
}
