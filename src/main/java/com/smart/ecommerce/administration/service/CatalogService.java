/**
 * 
 */
package com.smart.ecommerce.administration.service;

import java.util.List;

import com.smart.ecommerce.administration.model.dto.CatalogDTO;
import com.smart.ecommerce.administration.model.dto.CatalogDTOByName;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.RequestCatalogDTO;
import com.smart.ecommerce.administration.model.dto.ResponseCatalogDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

/**
 * @author Eduardo Valeriano
 *
 */
public interface CatalogService {
	GenericResponse getNameOfCatalogs( Integer languageId ) throws Exception;
	GenericResponse getlistOfOneCat(CatalogDTOByName catalogDTO, Integer languageId )throws Exception;
	GenericResponse editCatByName(RequestCatalogDTO request, Integer languageId )throws Exception;
	GenericResponse addCatByNameCatByName(RequestCatalogDTO request, Integer languageId )throws Exception;
	GenericResponse deleteCatByNameCatByName(RequestCatalogDTO request, Integer languageId )throws Exception;
	GenericResponse getEmisorCatalog(InfoTokenDto infoToken) throws Exception;
	

}
