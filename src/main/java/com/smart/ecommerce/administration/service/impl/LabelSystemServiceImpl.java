package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.dto.LabelSystemDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.LabelSystemRepository;
import com.smart.ecommerce.administration.service.LabelSystemService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import com.smart.ecommerce.entity.admin.CoreLanguage;
import com.smart.ecommerce.entity.admin.LabelSystem;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.entity.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LabelSystemServiceImpl implements LabelSystemService {

    @Autowired
    private LabelSystemRepository labelSystemDao;
    @Autowired
    private ErrorCodeRepository codeDao;

    @Override
    @Transactional(readOnly = false)
    public GenericResponse createLabel(HttpServletRequest request, LabelSystemDTO labelDto, String idOperation) {
        GenericResponse response = new GenericResponse();
        String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
        List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
        if (!(labelDto.getStatusId() == 0 || labelDto.getStatusId() == 1)) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
            return response;
        }
        if (!(labelDto.getTypeMenu() == 1 || labelDto.getTypeMenu() == 2 || labelDto.getTypeMenu() == 3)) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "tipo de menú"));
            return response;
        }
        if (labelSystemDao.getAllByCoreLanguageAndMenuIdAndTypeMenuAndCveAndStatusIn(labelDto.getLanguageId().getLanguageId(), labelDto.getMenuId(), labelDto.getTypeMenu(), labelDto.getCve()).size() > 0) {
            response.setCodeStatus("02");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "02").getMessage(), "LabelSystem"));
            return response;
        }
        LabelSystem labelSystem = new LabelSystem();
        String userId = InfoToken.getAditionalInfoToken(request, "usrId", idOperation);
        labelSystem.setCoreLanguage(labelDto.getLanguageId());
        labelSystem.setMenuId(labelDto.getMenuId());
        labelSystem.setTypeMenu(labelDto.getTypeMenu());
        labelSystem.setCve(labelDto.getCve());
        labelSystem.setDescription(labelDto.getDescription());
        labelSystem.setStatusId(labelDto.getStatusId());
        User user = new User();
        user.setUser_id(Integer.parseInt(userId));
        labelSystem.setUser(user);
        labelSystem.setCreatedAt(new Date());
        labelSystemDao.save(labelSystem);
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public GenericResponse updateLabel(HttpServletRequest request, LabelSystemDTO labelDto, Long id, String idOperation) {
        GenericResponse response = new GenericResponse();
        String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
        List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
        if (labelDto.getStatusId() != null && (!(labelDto.getStatusId() == 0 || labelDto.getStatusId() == 1))) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "status"));
            return response;
        }
        if (labelDto.getLanguageId() == null || labelDto.getMenuId() == null || labelDto.getTypeMenu() == null) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "languageId/menuId/typeMnu"));
            return response;
        }
        LabelSystem actualLabelSystem = labelSystemDao.findById(id).orElse(null);
        if (actualLabelSystem == null || actualLabelSystem.getStatusId() == 2) {
            response.setCodeStatus("03");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "LabelSystem"));
            return response;
        }
        if (labelDto.getCve() != null) {
            actualLabelSystem.setCve(labelDto.getCve());
        }
        if (labelDto.getDescription() != null) {
            actualLabelSystem.setDescription(labelDto.getDescription());
        }
        if (labelDto.getStatusId() != null) {
            actualLabelSystem.setStatusId(labelDto.getStatusId());
        }
        labelSystemDao.save(actualLabelSystem);
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public GenericResponse deleteLabel(HttpServletRequest request, Long id, String idOperation) {
        GenericResponse response = new GenericResponse();
        String languageId = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
        List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageId));
        LabelSystem actualLabelSystem = labelSystemDao.findById(id).orElse(null);
        if (actualLabelSystem == null || actualLabelSystem.getStatusId() == 2) {
            response.setCodeStatus("03");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), "ProviderCharacteristic"));
            return response;
        }
        actualLabelSystem.setStatusId(2);
        labelSystemDao.save(actualLabelSystem);
        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse getAllLabelByMenu(HttpServletRequest request, Integer languageId, Integer menuId, Integer typeMenu, String idOperation) {
        GenericResponse response = new GenericResponse();
        String languageToken = InfoToken.getAditionalInfoToken(request, "languageId", idOperation);
        List<CoreErrorCode> listCodes = codeDao.getAll(Integer.parseInt(languageToken));
        if (languageId == null) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "languageId"));
            return response;
        }
        CoreLanguage coreLanguage = new CoreLanguage();
        coreLanguage.setLanguageId(languageId);
        List<LabelSystem> listLabelSystem = null;
        if (menuId != null && typeMenu != null) {
            listLabelSystem = labelSystemDao.getAllByCoreLanguageAndMenuIdAndTypeMenuAndStatusId(coreLanguage, menuId, typeMenu, 1);
        } else if (menuId == null && typeMenu != null) {
            listLabelSystem = labelSystemDao.getAllByCoreLanguageAndTypeMenuAndStatusId(coreLanguage, typeMenu, 1);
        } else if (typeMenu == null && menuId != null) {
            listLabelSystem = labelSystemDao.getAllByCoreLanguageAndMenuIdAndStatusId(coreLanguage, menuId, 1);
        } else {
            listLabelSystem = labelSystemDao.getAllByCoreLanguageAndStatusId(coreLanguage, 1);
        }
        Map<String, String> result = listLabelSystem.stream().collect(
                Collectors.toMap(x -> x.getCve(), x -> x.getDescription()));

        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();
        information.put("results", result);
        response.setInformation(information);
        return response;
    }

    @Override
    public GenericResponse getLabelSystemByAction(HttpServletRequest request, Integer languageId, Integer actionId, String idOperation) {

        GenericResponse response = new GenericResponse();
        List<CoreErrorCode> listCodes = codeDao.getAll(languageId);

        if (languageId == null) {
            response.setCodeStatus("05");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "05").getMessage(), "languageId"));
            return response;
        }

        List<Map<String, Object>> labelSystems = labelSystemDao.getLabelSystemByActionId(languageId, actionId);

        Map<String, Object> labelSystemsF = new HashMap<>();

        for (Map<String, Object> labelSystem : labelSystems) {
            labelSystemsF.put(String.valueOf(labelSystem.get("cve")), String.valueOf(labelSystem.get("description")));
        }

        response.setCodeStatus("00");
        response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
        Map<String, Object> information = new HashMap<>();
//		information.put("systemLabel", labelSystemsF);
        response.setInformation(labelSystemsF);
        return response;


    }

}
