package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository{

    Integer save(Action entity, String idOperation);

    List<Action> getAll();

    Action getByName(String name);

    Action getById(Integer id);
}
