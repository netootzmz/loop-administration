package com.smart.ecommerce.administration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.ecommerce.entity.admin.LabelSystem;
import com.smart.ecommerce.entity.admin.CoreLanguage;

public interface LabelSystemRepository extends JpaRepository<LabelSystem, Long> {
	
	@Query(
			value = "SELECT * FROM label_system WHERE language_id = ?1 and menu_id = ?2 and type_menu = ?3 and cve = ?4 and status_id in(1, 0)",
			nativeQuery = true)
	public List<LabelSystem> getAllByCoreLanguageAndMenuIdAndTypeMenuAndCveAndStatusIn(Integer languageId, Integer menuId, Integer typeMenu, String cve);
	
	public List<LabelSystem> getAllByCoreLanguageAndMenuIdAndTypeMenuAndStatusId(CoreLanguage coreLanguage, Integer menuId, Integer typeMenu, Integer statusId);

	public List<LabelSystem> getAllByCoreLanguageAndTypeMenuAndStatusId(CoreLanguage coreLanguage, Integer typeMenu, Integer statusId);

	public List<LabelSystem> getAllByCoreLanguageAndMenuIdAndStatusId(CoreLanguage coreLanguage, Integer typeMenu, Integer statusId);

	public List<LabelSystem> getAllByCoreLanguageAndStatusId(CoreLanguage coreLanguage, Integer statusId);


	@Query(
			value = "SELECT * FROM label_system WHERE menu_id = :actionId AND language_id  = :coreLanguage AND type_menu = 1    ",
			nativeQuery = true)
	public List<Map<String,Object>> getLabelSystemByActionId(Integer  coreLanguage, Integer actionId);

}
