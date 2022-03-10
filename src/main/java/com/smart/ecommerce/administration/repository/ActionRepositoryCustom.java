package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.core.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepositoryCustom  extends JpaRepository<Action, Long> {
}
