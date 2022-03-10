package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.checkout.SystemConfig;

public interface SystemConfigRepository {

  SystemConfig getSystemConfigByName(String name);
}
