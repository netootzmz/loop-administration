package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.Portal;

import java.util.List;


public interface PortalRepository  {
  Integer savePortal(Portal entity, String idOperation);

  List<Portal> findPortalAll();

  Portal findByName(String name);

  Portal portalById(Integer id);

  Portal validationPortalById(Integer id);
}
