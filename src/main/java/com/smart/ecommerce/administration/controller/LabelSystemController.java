package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.LabelSystemContract;
import com.smart.ecommerce.administration.model.dto.LabelSystemDTO;
import com.smart.ecommerce.administration.service.LabelSystemService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.logging.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/label-system")
public class LabelSystemController implements LabelSystemContract {

    @Autowired
    private LabelSystemService labelSystemService;

    @Override
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse createLabelSystem(HttpServletRequest request, @RequestBody LabelSystemDTO labelSystemDto) {
        String idOperation = SystemLog.newTxnIdOperation();
        return labelSystemService.createLabel(request, labelSystemDto, idOperation);
    }

    @Override
    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse updateLabelSystem(HttpServletRequest request, @RequestBody LabelSystemDTO labelSystemDto, @PathVariable Long id) {
        String idOperation = SystemLog.newTxnIdOperation();
        return labelSystemService.updateLabel(request, labelSystemDto, id, idOperation);
    }

    @Override
    @PostMapping(value = "/delete/{id}")
    public GenericResponse deleteLabelSystem(HttpServletRequest request, @PathVariable Long id) {
        String idOperation = SystemLog.newTxnIdOperation();
        return labelSystemService.deleteLabel(request, id, idOperation);
    }

    @Override
    @PostMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse getLabelSystem(HttpServletRequest request, Integer languageId, Integer menuId, Integer typeMenu) {
        String idOperation = SystemLog.newTxnIdOperation();
        return labelSystemService.getAllLabelByMenu(request, languageId, menuId, typeMenu, idOperation);
    }

    @Override
    @PostMapping(value = "/getLabelSystemByAction", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse getLabelSystemByAction(HttpServletRequest request, Integer languageId, Integer actionId) {
        String idOperation = SystemLog.newTxnIdOperation();
        return labelSystemService.getLabelSystemByAction(request, languageId, actionId, idOperation);
    }

}
