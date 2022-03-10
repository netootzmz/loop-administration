package com.smart.ecommerce.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.ecommerce.entity.admin.Group;

public interface GroupRepository extends JpaRepository<Group, String> {

	@Query(
			value = "SELECT * FROM smart_core_platform.group WHERE cve = ?",
			nativeQuery = true)
	public List<Group> findByGroupIdAndClientIdAndStatusIn(String cve);

	public Group findByGroupId(String groupId);

}
