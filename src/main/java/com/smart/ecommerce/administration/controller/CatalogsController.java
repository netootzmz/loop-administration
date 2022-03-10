/**
 * 
 */
package com.smart.ecommerce.administration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.logging.SystemLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.ecommerce.administration.model.dto.CatalogDTO;
import com.smart.ecommerce.administration.model.dto.CatalogDTOByName;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.RequestCatalogDTO;
import com.smart.ecommerce.administration.model.dto.ResponseCatalogDTO;
import com.smart.ecommerce.administration.service.CatalogService;
import com.smart.ecommerce.administration.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eduardo Valeriano
 *
 */
@RestController
@RequestMapping("/catalogs")
@Api("Este servicio nos sirve para como un CRUD de catalogos ")
public class CatalogsController {
  @Autowired
  private CatalogService catalogService;

  @PostMapping("/getAll")
  @ApiOperation("Servicio que regresa un listado de las tablas que terminen en catalog")
  public GenericResponse getAllTablesCatalog(HttpServletRequest request) throws Exception {
    Integer languageId = InfoToken.getLanguageId(request);
    if( languageId > 0 ) {
      return catalogService.getNameOfCatalogs(languageId);
    } else {
      Map<String, Object> informationResponse = new HashMap<>();
      informationResponse.put("exception", "Invalid language");
      return new GenericResponse( null, null, informationResponse);
    }
  }

  @PostMapping("/getByName")
  @ApiOperation("Servicio que lista el contenido de un catalogo segun su nombre:"
    + " Datos necesarios:nameCat,id")
  public GenericResponse getAllInACat(HttpServletRequest request, @RequestBody CatalogDTOByName catalogDTO) throws Exception {
    Integer languageId = InfoToken.getLanguageId(request);

    return catalogService.getlistOfOneCat(catalogDTO, languageId);
  }

  @PostMapping("/edit")
  @ApiOperation("Servicio que edita el registro de un catalogo."
    + " Datos necesarios:nameCat,id,cve,name,description,status_id ")
  public GenericResponse editCatByName(HttpServletRequest request, @RequestBody RequestCatalogDTO requestCatalog) throws Exception {
    Integer languageId = InfoToken.getLanguageId(request);

    return catalogService.editCatByName(requestCatalog, languageId);
  }

  @PostMapping("/add")
  @ApiOperation("Servicio que agrega un registro de un catalogo, dependiendo del nombre del catalogo que reciba."
    + " Datos necesarios:nameCat,cve,name,description,status_id ")
  public GenericResponse addCatByName(HttpServletRequest request, @RequestBody RequestCatalogDTO requestCatalog) throws Exception {
    Integer languageId = InfoToken.getLanguageId(request);

    return catalogService.addCatByNameCatByName(requestCatalog, languageId);
  }

  @PostMapping("/delete")
  @ApiOperation("Servicio que cambia el estado de registro, al cual se le considerara eliminar el registro. "
    + "Datos necesarios:nameCat,id,status_id ")
  public GenericResponse deleteCatByName(HttpServletRequest request, @RequestBody RequestCatalogDTO requestCatalog) throws Exception {
    Integer languageId = InfoToken.getLanguageId(request);

    return catalogService.deleteCatByNameCatByName(requestCatalog, languageId);
  }

  @PostMapping("/getEmisorCatalog")
  @ApiOperation("Servicio que regresa los datos del catalogo de emisor")
  public GenericResponse getEmisorCatalog(HttpServletRequest request) throws Exception {
    InfoTokenDto infoToken = InfoToken.getInfoToken(request);
    //String idOperation = SystemLog.newTxnIdOperation()
    Integer languageId = infoToken.getLanguage_id();
    if( languageId > 0 ) {
      return catalogService.getEmisorCatalog(infoToken);
    } else {
      Map<String, Object> informationResponse = new HashMap<>();
      informationResponse.put("exception", "Invalid language");
      return new GenericResponse( null, null, informationResponse);
    }
  }
}
