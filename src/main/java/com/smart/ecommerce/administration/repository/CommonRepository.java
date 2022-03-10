package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.CoreReader;

import java.io.Serializable;

public interface CommonRepository {

    boolean save(CoreReader coreReader);

    <T> T get(Class<T> clazz, Integer id);


    <T extends Serializable> int saveEntity(T entidad,  String idOperation );


}
