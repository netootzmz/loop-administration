package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.model.ActionDto;
import com.smart.ecommerce.administration.model.ModuleDto;
import com.smart.ecommerce.administration.model.PortalDto;
import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "menu-system")
public interface MenuContract {

    @PostMapping("/portal/newEdit")
    @ApiOperation("Alta y Edición del portal")
    GenericResponse newEditPortal(HttpServletRequest request, @RequestBody PortalDto dto);

    @PostMapping("/portal/delete")
    @ApiOperation("Activar/Desactivar/Eliminar portal")
    GenericResponse deletePortal(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status);

    @ApiOperation("Obtener lista de portales registrados")
    @PostMapping("/portal/getAll")
    GenericResponse allPortal(HttpServletRequest request);

    @PostMapping("portal/getById")
    @ApiOperation("Obtener portal por identificador")
    GenericResponse getPortalById(HttpServletRequest request, @RequestParam Integer portal_id);

    @PostMapping("/module/newEdit")
    @ApiOperation("Agregar/Editar modulo")
    GenericResponse newEdit(HttpServletRequest request, @RequestBody ModuleDto dto);

    @PostMapping("/deleteModule")
    @ApiOperation("Activar/Desactivar/Eliminar módulo")
    GenericResponse deleteModule(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status);

    @PostMapping("/module/getAll")
    @ApiOperation("Obtener todos los módulos")
    GenericResponse allModuls(HttpServletRequest request);

    @PostMapping("/module/getById")
    @ApiOperation("Obtener módulo por id")
    GenericResponse getModuleById(HttpServletRequest request, @RequestParam Integer id);

    @PostMapping("/action/newEdit")
    @ApiOperation("Agregar/Editar una acción al módulo")
    GenericResponse saveEdit(HttpServletRequest request, @RequestBody ActionDto dto);

    @PostMapping("/action/delete")
    @ApiOperation("Activar/Desactivar/Eliminar acción del módulo")
    GenericResponse deleteAction(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status);

    @PostMapping("/action/getAll")
    @ApiOperation("Obtener todas las acciones")
    GenericResponse getAll(HttpServletRequest request);

    @PostMapping("/action/getById")
    @ApiOperation("Obtener acción por identificador")
    GenericResponse getActionById(HttpServletRequest request, @RequestParam Integer id);






}
