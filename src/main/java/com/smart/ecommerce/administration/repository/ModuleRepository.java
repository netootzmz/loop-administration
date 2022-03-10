package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.Module;

import java.util.List;

public interface ModuleRepository {

  boolean save(Module entity, String idOpertion);

  List<Module> findModuleAll();

  Module findModuleByName(String name);

  Module findModuleById(Integer id);

  Module validationModuleById(Integer id);
}
