/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.entity.admin.TypeOperationCatalog;

/**
 * @author Eduardo Valeriano
 *
 */
@Repository
public interface TypeOperationCatalogRepository extends JpaRepository<TypeOperationCatalog, Integer>{
	
	
	//-----------------------------inicio del bloque de consultas Genericas------------------------------------
	@Query(value=" SELECT table_name FROM information_schema.tables " + 
			" WHERE table_schema = 'smart_core_platform_prod' " + 
			" AND table_name LIKE '%_catalog' ",nativeQuery=true )
	  public List<String> listCatalogs();
	
	@Query(value = " SELECT * FROM type_operation_catalog ", nativeQuery = true)
	  public List<TypeOperationCatalog> getALLInTypeOperationCatalog();
	
	@Query(value = " select * from type_operation_catalog " + 
			"	where type_operation_id = :idTable", nativeQuery = true)
	  public TypeOperationCatalog getOneInTypeOperationCatalog(@Param("idTable") Integer idTable);
	
	
	
	//-----------------------------fin del bloque de consultas Genericas------------------------------------

	
	

}
