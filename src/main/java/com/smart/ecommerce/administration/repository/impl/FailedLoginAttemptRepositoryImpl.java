package com.smart.ecommerce.administration.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smart.ecommerce.administration.repository.Custom.FailedLoginAttemptRepositoryCustom;
import com.smart.ecommerce.entity.core.FailedLoginAttempt;
import com.smart.ecommerce.entity.core.User;

@Repository
@Transactional
public class FailedLoginAttemptRepositoryImpl implements FailedLoginAttemptRepositoryCustom{
	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<FailedLoginAttempt> getFailedLoginAttemptsById(Integer userId, int minutes) {
		List<FailedLoginAttempt> listFailedLoginAttempt = em.createNamedQuery("getUserIdFailedLoginAttempt").setParameter("userId",userId).setParameter("minutes", minutes).getResultList(); 
		return listFailedLoginAttempt;
	}

}
