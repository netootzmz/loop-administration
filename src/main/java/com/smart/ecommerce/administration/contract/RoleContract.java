package com.smart.ecommerce.administration.contract;

import com.smart.ecommerce.administration.model.RoleDto;
import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api("Administración de roles")
public interface RoleContract {

    @PostMapping("/getAll")
    @ApiOperation("Obtiene todos los roles")
    GenericResponse getAllRole();

    @PostMapping("/getById")
    @ApiOperation("Obtiene los roles por identificador")
    GenericResponse getRoleById(@RequestParam Integer id);

    @PostMapping("/getByDescription")
    @ApiOperation("Obtiene los roles por descripción")
    GenericResponse getRoleBydescription(@RequestParam String description);

    @PostMapping("/save")
    @ApiOperation("Alamacena la información del rol")
    GenericResponse saveRole(@RequestBody RoleDto dto);

    @PostMapping("/update")
    @ApiOperation("Edita la información del rol")
    GenericResponse updateRole(@RequestBody RoleDto dto);

    @PostMapping("/delete")
    @ApiOperation("Elimina la información del rol en BD")
    GenericResponse delete(@RequestParam Integer id);

}
