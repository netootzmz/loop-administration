package com.smart.ecommerce.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.ecommerce.entity.admin.Client;

public interface ClientRepository extends JpaRepository<Client, String> {
	
	@Query(value = "SELECT * FROM client WHERE cve = ? ",nativeQuery = true)
	
	public List<Client> findByClientIdAndCveAndNameAndStatusIn(String cve);

	public List<Client> findByClientIdAndCveAndName(String clientId, String cve, String name);

	public Client findByClientId(String clientId);

}
