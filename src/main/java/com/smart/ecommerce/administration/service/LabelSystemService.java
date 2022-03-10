package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.dto.LabelSystemDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

import javax.servlet.http.HttpServletRequest;

public interface LabelSystemService {

    public GenericResponse createLabel(HttpServletRequest request, LabelSystemDTO label, String idOperation);

    public GenericResponse updateLabel(HttpServletRequest request, LabelSystemDTO label, Long id, String idOperation);

    public GenericResponse deleteLabel(HttpServletRequest request, Long id, String idOperation);

    public GenericResponse getAllLabelByMenu(HttpServletRequest request, Integer languageId, Integer menuId, Integer typeMenu, String idOperation);

    public GenericResponse getLabelSystemByAction(HttpServletRequest request, Integer languageId, Integer actionId, String idOperation);

}
