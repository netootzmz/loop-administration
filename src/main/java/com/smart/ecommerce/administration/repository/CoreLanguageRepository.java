package com.smart.ecommerce.administration.repository;

import com.smart.ecommerce.entity.admin.CoreLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CoreLanguageRepository extends JpaRepository <CoreLanguage,Integer > {
    
    
    
    @Query (value ="Select\n" +
            "cl.language_id as languageId ,\n" +
            "cl.name as languageName       \n" +
            "from core_language cl"+
            " where cl.status_id = 1", nativeQuery=true)
    List<Map<String,Object>> getCoreLanguage ();
    
    
}
 