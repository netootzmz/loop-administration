/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.entity.admin.HierarchyLevelCatalog;

/**
 * @author Eduardo Valeriano
 *
 */
@Repository
public interface HierarchyLevelCatalogRepository extends JpaRepository<HierarchyLevelCatalog,Integer>{
	
	@Query(value = " SELECT * FROM hierarchy_level_catalog ", nativeQuery = true)
	  public List<HierarchyLevelCatalog> getALLInTypeOperationCatalog();
	
	@Query(value = " select * from hierarchy_level_catalog " + 
			"	where hierarchy_level_id = :idTable", nativeQuery = true)
	  public HierarchyLevelCatalog getOneInHierarchyLevelCatalog(@Param("idTable") Integer idTable);

}
