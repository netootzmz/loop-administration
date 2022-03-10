package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.RoleContract;
import com.smart.ecommerce.administration.model.RoleDto;
import com.smart.ecommerce.administration.repository.RoleRepository;
import com.smart.ecommerce.administration.service.RoleService;
import com.smart.ecommerce.administration.util.GenericResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController implements RoleContract {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public GenericResponse getAllRole() {

        return roleService.getAllRole();
    }

    @Override
    public GenericResponse getRoleById(Integer id) {
        return roleService.getRoleById(id);
    }

    @Override
    public GenericResponse getRoleBydescription(String description) {
        return roleService.getRoleBydescription(description);
    }

    @Override
    public GenericResponse saveRole(RoleDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dto.setUserAdmission(auth.getName());
        return roleService.saveRole(dto);
    }

    @Override
    public GenericResponse updateRole(RoleDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dto.setUserChange(auth.getName());
        return roleService.updateRole(dto);
    }

    @Override
    public GenericResponse delete(Integer id) {
        return roleService.deleteRole(id);
    }

}
