/**
 * 
 */
package com.smart.ecommerce.administration.service.impl;

import static com.smart.ecommerce.administration.util.Constants.*;

import java.util.*;

import javax.annotation.Resource;

import com.smart.ecommerce.administration.model.dto.*;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.entity.admin.ServiceCharacteristic;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.administration.Exception.GeneralException;
import com.smart.ecommerce.administration.repository.GenericCatalogRepository;
import com.smart.ecommerce.administration.repository.TypeOperationCatalogRepository;
import com.smart.ecommerce.administration.service.CatalogService;
import com.smart.ecommerce.administration.util.ConvertDates;
import com.smart.ecommerce.administration.util.GeneralConstant;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Eduardo Valeriano
 *
 */

@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private TypeOperationCatalogRepository typeOperationCatalogRepository;
	@Autowired
	private ErrorCodeRepository codeRepository;
	
	@Resource
	GenericCatalogRepository genericCatalogRepository;
	
	private static final String KEY_RESULTS = "results";
	private static final String LOG_ERROR_TRACE = "Error: ";

	@Override
	public GenericResponse getNameOfCatalogs( Integer languageId ) throws Exception {
		String idOperation = SystemLog.newTxnIdOperation();
		int count = 1;
		List<String> getCat = typeOperationCatalogRepository.listCatalogs();
		List<CatalogDTO> lstObjtCat = new ArrayList<CatalogDTO>();
		List<CoreErrorCode> error = codeRepository.getAll(languageId);
		CoreErrorCodeDto errorItem = null;
		Map<String, Object> informationResponse = new HashMap<>();

		if(getCat.isEmpty() || getCat.size()==0) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);

			informationResponse.put( "id_operation", idOperation );
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse );
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
//			 throw new GeneralException(ResponseType.NOT_EXIST,String.format("no se encontraron catalogos que terninen con el sufijo _catalog", "usuario"), idOperation);
		}

		for (String forGetCat : getCat) {
			CatalogDTO objCat = new CatalogDTO();
			objCat.setId(count);
			objCat.setNameCatalog(forGetCat);
			lstObjtCat.add(objCat);
			count++;
		}

		List<Map> list = new ArrayList<>();

		for (CatalogDTO item : lstObjtCat) {
			Map<String, Object> mapservice = new HashMap<>();
			mapservice.put("id", item.getId());
			mapservice.put("nameCatalog", item.getNameCatalog());

			list.add(mapservice);
		}

		informationResponse.put("list", list);
		errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
		return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//		return lstObjtCat;
	}

	@Override
	public GenericResponse getlistOfOneCat(CatalogDTOByName catalogDTO, Integer languageId ) throws Exception {
		String idOperation = SystemLog.newTxnIdOperation();
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Map<String, Object> informationResponse = new HashMap<>();
		List<CoreErrorCode> error = codeRepository.getAll(languageId);
		CoreErrorCodeDto errorItem = null;

		if(catalogDTO.getNameCatalog()== null || catalogDTO.getLanguageId() == null) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
			informationResponse.put("idOperation", idOperation);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
		}
		try {
			response = makeADecisionBytableName(catalogDTO.getNameCatalog(), catalogDTO.getLanguageId());
		} catch ( GeneralException generalException ) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
			informationResponse.put("id_operation", idOperation );
			return new GenericResponse( errorItem.getCode(), errorItem.getMessage(), informationResponse );
		}
		informationResponse.put("information", response);
		errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PROCESADO_CORRECTAMENTE);
		return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//		return response;
	}

	@Override
	public GenericResponse editCatByName(RequestCatalogDTO request, Integer languageId ) throws Exception {
		String response="";
		String idOperation = SystemLog.newTxnIdOperation();
		Map<String, Object> informationResponse = new HashMap<>();
		List<CoreErrorCode> error = codeRepository.getAll(languageId);
		CoreErrorCodeDto errorItem = null;

		if(request.getNameCat()==null || request.getCve()== null ||request.getId()<=0||
				request.getName()== null || request.getDescription()== null ||
				request.getStatus_id()== null || request.getLanguage_id() == null) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
			informationResponse.put("id_operation", idOperation );
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse );
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
		}
		response= makeADecisionBytableNameAddOrEdit(request, GeneralConstant.editar);
		if(response.isEmpty()) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage());
//			throw new GeneralException(ResponseType.NOT_AVAILABLE, idOperation);
		}
//		System.out.println("esta es la respuesta al editar ---> " + response);
		return new GenericResponse(ResponseType.ACEPT);
	}

	@Override
	public GenericResponse addCatByNameCatByName(RequestCatalogDTO request, Integer languageId) throws GeneralException {
		String response ="";
		String idOperation = SystemLog.newTxnIdOperation();
		List<CoreErrorCode> error = codeRepository.getAll(languageId);
		CoreErrorCodeDto errorItem = null;
		Map<String, Object> informationResponse = new HashMap<>();

		if(request.getNameCat()==null || request.getCve()== null || 
				request.getName()== null || request.getDescription()== null) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);

			informationResponse.put( "id_operation", idOperation);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
		}
		try {
			response = makeADecisionBytableNameAddOrEdit(request, GeneralConstant.agregar);
		} catch ( GeneralException generalException ) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
			informationResponse.put("id_operation", idOperation );
			return new GenericResponse( errorItem.getCode(), errorItem.getMessage(), informationResponse );
		}
		if(response.isEmpty()) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);

			informationResponse.put( "id_operation", idOperation);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//			throw new GeneralException(ResponseType.NOT_AVAILABLE, idOperation);
		}
		return new GenericResponse(ResponseType.ACEPT);

	}

	@Override
	public GenericResponse deleteCatByNameCatByName(RequestCatalogDTO request, Integer languageId) throws GeneralException {
		String response="";
		String idOperation = SystemLog.newTxnIdOperation();
		List<CoreErrorCode> error = codeRepository.getAll(languageId);
		CoreErrorCodeDto errorItem = null;
		Map<String, Object> informationResponse = new HashMap<>();

		if(request.getNameCat() == null || request.getId()<=0 || !request.getStatus_id().equals("2")) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_PARAMETROS_INCOMPLETOS);
			informationResponse.put( "id_operation", idOperation);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
			
		}
		response = makeADecisionBytableNameAddOrEdit(request, GeneralConstant.eliminar);			
		if(response.isEmpty()) {
			errorItem = ErrorCode.getError(error, ErrorCode.ERROR_CODE_SIN_REGISTROS);
			informationResponse.put( "id_operation", idOperation);
			return new GenericResponse(errorItem.getCode(), errorItem.getMessage(), informationResponse);
//			throw new GeneralException(ResponseType.NOT_AVAILABLE, idOperation);
		}
		return new GenericResponse(ResponseType.ACEPT);
	}

//	este metodo estara tomando una decicion dependiento del valor de la variable type; si queremos agregar 1, editar 2, eliminar 3
	public String makeADecisionBytableNameAddOrEdit(RequestCatalogDTO request, int type) throws GeneralException {

		String response ="";
		String idOperation = SystemLog.newTxnIdOperation();
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		String fechaFormateada = ConvertDates.getDateDDMMYYYY(date);
//		System.out.println("resultado de la fecha formateada --->  " +fechaFormateada);
		String tableName = findIfAcatExist(request.getNameCat());
//		System.out.println("el valor de la variable flagTable    " + tableName );
		
		if (tableName.isEmpty()) {
			throw new GeneralException(ResponseType.LISTEMPTY, idOperation);
		}
		String idTable = getIdofATable(tableName);
//		System.out.println("este es el nombre de el id columna de la tabla ---> " + idTable );
		String id = String.valueOf(request.getId());
		if(type== GeneralConstant.agregar) {
			try {
				//los datos que usaremos para agregar un registro en una tabla y son de caracter obligatorio son:
				//tableName ---> nombre de la tabla
				//cvv
				//name
				//description
				//status_id
				//user_register_by
				//created_at
				//language_id
				String getAnswereAgregar = genericCatalogRepository.saveRecord(tableName, request.getCve(),
						request.getName(), request.getDescription(),
						request.getStatus_id().toString(), "1", fechaFormateada, request.getLanguage_id());
				response = getAnswereAgregar + " en la tabla " + tableName;
				
			} catch (Exception ex) {
				// TODO: handle exception
				Console.logException("Error al realizar la consulta", ex, idOperation);
			}
			
			
		}else if(type == GeneralConstant.editar) {
			try {				
				//Los datos que usaremos para editar el registro de una tabla y son de caracter obligatorio:
				//tableName ---> nombre de la tabla
				//id        --> corresponse al id del registro
				//cvv
				//name
				//status_id
				//user_register_by
				//created_at
				//language_id
				//tipo de operacion en este caso Editar
				//idTable nombre del id de la tabla				
				
				String getAnswereEditar = genericCatalogRepository.editOrDelete(
						tableName,
						request.getCve(), 
						request.getName(),
						request.getDescription(), 
						request.getStatus_id().toString(), 
						"1", 						
						fechaFormateada,
						request.getLanguage_id(),
						id,
						GeneralConstant.editar,
						idTable);

				response = getAnswereEditar + " en la tabla " + tableName;

			} catch (Exception ex) {
				Console.logException("Error al realizar la consulta", ex, idOperation);
				
			}
			
			
		}else if(type == GeneralConstant.eliminar) {
			try {
				//Los datos que usaremos para eliminar un registro de una tabla y son de caracter obligatorio:				
				//tableName --> nombre de la tabla
				//id        --> corresponse al id del registro
				//Nota* los demas campos pueden ir vacios, ESTO SOLO APLICA EN ELIMINAR
				String getAnswereEditar = genericCatalogRepository.editOrDelete(
						tableName,
						request.getCve(), 
						request.getName(),
						request.getDescription(), 
						request.getStatus_id().toString(), 
						"1", 						
						fechaFormateada,
						request.getLanguage_id(),
						id,
						GeneralConstant.eliminar,
						idTable);

				response = getAnswereEditar + " en la tabla " + tableName;
			} catch (Exception ex) {
				Console.logException("Error al realizar la consulta", ex, idOperation);
			}
			
		}
	

		return response;
	}

	//metodoq ue recibe el nombre del catalogo o nombre de la tabla y obtiene el id de la misma
	public String getIdofATable(String nameCat) {
		String suFixCatId="_id";
		String suFixCat= "_catalog";
		String getArreglo [] =nameCat.split(suFixCat);
		String idTable = getArreglo[0]+suFixCatId;
//		System.out.println("este es el nombre de el id columna de la tabla ---> " + idTable );		
		return idTable;
	}

	
	//metodo en enlista un catalogo o tabla dependiendo del nombre de la misma.
	public List<Map<String, Object>> makeADecisionBytableName(String nameCat, Integer languageId) throws GeneralException {
		String idOperation = SystemLog.newTxnIdOperation();
		List<ResponseCatalogDTO> lstResponse = new ArrayList<ResponseCatalogDTO>();		
		
		String nameTable = findIfAcatExist(nameCat);
		if (nameTable.isEmpty()) {
			throw new GeneralException(ResponseType.DATA_EMPTY, idOperation);
		}
		String idTable = getIdofATable(nameCat);
				
		List<Map<String, Object>> lst = new ArrayList<>();
		lst = genericCatalogRepository.getAllOptionsFromCatalog(nameTable, idTable, languageId);
		if(lst.isEmpty() || lst== null) {
			throw new GeneralException(ResponseType.LISTEMPTY, idOperation);
			
		}
		return lst;
//		for (Map<String, Object> map : lst) {
//			ResponseCatalogDTO response=  new ResponseCatalogDTO();
//			response.setId(Integer.parseInt(map.get("idOption").toString()));
//			response.setCve(map.get("cveOption").toString());
//			response.setName(map.get("nameOption").toString());
//			response.setDescription(map.get("descriptionOption").toString());
//			response.setStatus_id(Integer.parseInt(map.get("statusIdOption").toString()));
//			response.setUser_by_register(Integer.parseInt(map.get("userByRegisterOption").toString()));
//			response.setCreated_at(map.get("createdAtOption").toString());
//			response.setLanguage_id(Integer.parseInt(map.get("languageIdOption").toString()));
//			lstResponse.add(response);
//
//		}
//
//		return lstResponse;
	}

	// metodo que va a buscar en el esquema de base de datos si una tabla existe
	// en caso de que exista te regresa una bandera, caso contrario regresa vacio
	private String findIfAcatExist(String nameCat) throws GeneralException {
		String idOperation = SystemLog.newTxnIdOperation();
		String response = "";
		List<String> getCat = typeOperationCatalogRepository.listCatalogs();
		if(getCat.isEmpty() || getCat.size()==0) {
			throw new GeneralException(ResponseType.NOT_EXIST,String.format("no se encontraron catalogos que terninen con el sufijo _catalog", "usuario"), idOperation);
		}
		int findWord = getCat.indexOf(nameCat);
		if (findWord != -1) {
//			System.out.println("este es el numero de findWord --->  " + findWord);
			response = getCat.get(findWord);
		}
//		System.out.println("esta respuesta nos dice en que base de datos buscar   ---> " + response);
		return response;
	}

  @Override
  public GenericResponse getEmisorCatalog(InfoTokenDto infoToken) throws Exception {
    GenericResponse response = new GenericResponse();
    List<CoreErrorCode> listCodes = codeRepository.getAll(infoToken.getLanguage_id());
    try {
      List<GenericCatalogDTO> results = genericCatalogRepository.getEmisorCatalog();
      if (!results.isEmpty()) {
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();
        information.put(KEY_RESULTS, results);
        response.setInformation(information);
      } else {
        response.setCodeStatus("01");
        response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
      }
      return response;
    } catch (Exception e) {
      StringBuilder errorStack = new StringBuilder();
      for (StackTraceElement er : e.getStackTrace()) {
        errorStack.append(LOG_ERROR_TRACE);
        errorStack.append(er.getClassName() + SPACE);
        errorStack.append(er.getFileName() + SPACE);
        errorStack.append(er.getLineNumber() + SPACE);
        errorStack.append(er.getMethodName() + SPACE);
        errorStack.append("");
      }
      String errorTrace = errorStack.toString();
      log.info("Error Transaction ServPtal: {}", errorTrace);
      response.setCodeStatus("03");
      //response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), e.getMessage()))
      response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), errorTrace));
      return response;
    }
  }

}
