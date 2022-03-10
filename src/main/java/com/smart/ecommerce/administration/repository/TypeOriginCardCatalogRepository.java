/**
 * 
 */
package com.smart.ecommerce.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.ecommerce.entity.admin.TypeOriginCardCatalog;

/**
 * @author Eduardo Valeriano
 *
 */
@Repository
public interface TypeOriginCardCatalogRepository extends JpaRepository<TypeOriginCardCatalog,Integer>{
	
	@Query(value = " SELECT * FROM type_origin_card_catalog ", nativeQuery = true)
	  public List<TypeOriginCardCatalog> getALLInTypeOriginCardCatalog();
	
	@Query(value = " select * from type_origin_card_catalog " + 
			"	where type_operation_id = :idTable", nativeQuery = true)
	  public TypeOriginCardCatalog getOneInTypeOriginCardCatalog(@Param("idTable") Integer idTable);
	
	

}
