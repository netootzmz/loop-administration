package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.MenuContract;
import com.smart.ecommerce.administration.model.ActionDto;
import com.smart.ecommerce.administration.model.ModuleDto;
import com.smart.ecommerce.administration.model.PortalDto;
import com.smart.ecommerce.administration.service.ActionService;
import com.smart.ecommerce.administration.service.ModuleService;
import com.smart.ecommerce.administration.service.PortalService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/menu")
public class MenuController implements MenuContract {

    @Autowired
    private PortalService portalService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ActionService actionService;

    /*CRUD Portal*/

    @Override
    public GenericResponse newEditPortal(HttpServletRequest request, @RequestBody PortalDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        return portalService.newEdit(dto, auth.getName(), languageId);
    }

    @Override
    public GenericResponse deletePortal(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        return portalService.delete(id, status, auth.getName(), languageId);
    }

    @Override
    public GenericResponse allPortal(HttpServletRequest request) {
        Integer languageId = InfoToken.getLanguageId(request);
        return portalService.all(languageId);
    }

    @Override
    public GenericResponse getPortalById(HttpServletRequest request, @RequestParam Integer portal_id) {
        Integer languageId = InfoToken.getLanguageId(request);
        return portalService.getPortal(portal_id, languageId);
    }

    /*CRUD Módulo*/

    @Override
    public GenericResponse newEdit(HttpServletRequest request, @RequestBody ModuleDto dto) {
        Integer languageId = InfoToken.getLanguageId(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return moduleService.newEdit(dto, auth.getName(), languageId);
    }

    @Override
    public GenericResponse deleteModule(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status) {
        Integer languageId = InfoToken.getLanguageId(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return moduleService.delete(id, status, auth.getName(), languageId);
    }

    @Override
    public GenericResponse allModuls(HttpServletRequest request) {
        Integer languageId = InfoToken.getLanguageId(request);
        return moduleService.getAll(languageId);
    }

    @Override
    public GenericResponse getModuleById(HttpServletRequest request, @RequestParam Integer id) {
        Integer languageId = InfoToken.getLanguageId(request);
        return moduleService.getById(id, languageId);
    }

    /*CRUD Acciones*/

    @Override
    public GenericResponse saveEdit(HttpServletRequest request, @RequestBody ActionDto dto) {
        Integer languageId = InfoToken.getLanguageId(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return actionService.newEdit(dto, auth.getName(), languageId);
    }

    @Override
    public GenericResponse deleteAction(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        return actionService.delete(id, status, auth.getName(), languageId);
    }

    @Override
    public GenericResponse getAll(HttpServletRequest request) {
        Integer languageId = InfoToken.getLanguageId(request);
        return actionService.getAll(languageId);
    }

    @Override
    public GenericResponse getActionById(HttpServletRequest request, @RequestParam Integer id) {
        Integer languageId = InfoToken.getLanguageId(request);
        return actionService.getById(id, languageId);
    }
}
