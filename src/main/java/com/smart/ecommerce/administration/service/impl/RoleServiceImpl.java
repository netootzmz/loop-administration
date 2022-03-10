package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.Enum.ResponseType;
import com.smart.ecommerce.logging.Console;
import com.smart.ecommerce.logging.SystemLog;
import com.smart.ecommerce.administration.model.RoleDto;
import com.smart.ecommerce.entity.core.Role;
import com.smart.ecommerce.administration.repository.RoleRepository;
import com.smart.ecommerce.administration.service.RoleService;
import com.smart.ecommerce.administration.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

  @Autowired
  RoleRepository roleRepository;

  @Override
  public GenericResponse getAllRole() {
    String idOperation = SystemLog.newTxnIdOperation();
    Map<String, Object> information = new HashMap<>();
    info("Consulta lista de roles activos e inactivos ", idOperation);

    try {
      List<Role> listRole = roleRepository.getAllRole();
      information.put("list", listRole);

      if (!listRole.isEmpty()) {
        return new GenericResponse(ResponseType.ACEPT, information);
      }
    } catch (Exception ex) {
      Console.logException("Error al recuperar datos en BD", ex, idOperation);
    }

    return new GenericResponse(ResponseType.LISTEMPTY);
  }

  @Override
  public GenericResponse getRoleById(Integer id) {
    Map<String, Object> information = new HashMap<>();
    String idOperation = SystemLog.newTxnIdOperation();
    info("Consultando rol con id: " + id, idOperation);

    try {
      Role role = roleRepository.getRoleById(id);
      information.put("list", role);

      if (role != null) {
        return new GenericResponse(ResponseType.ACEPT, information);
      }
    } catch (Exception ex) {
      Console.logException("Error al consultar datos en BD rol: " + id, ex, idOperation);
    }

    return new GenericResponse(ResponseType.LISTEMPTY);
  }

  @Override
  public GenericResponse getRoleBydescription(String description) {
    Map<String, Object> information = new HashMap<>();
    String idOperation = SystemLog.newTxnIdOperation();
    info("Consultando rol con id: " + description, idOperation);

    try {
      Role role = roleRepository.getRoleByDescription(description);

      if (role != null && role.getRoleId() != null){
        information.put("list", role);
        return new GenericResponse(ResponseType.ACEPT, information);
      }
    } catch (Exception ex) {
      Console.logException("Error al consultar datos en BD rol: " + description, ex, idOperation);
    }

    return new GenericResponse(ResponseType.LISTEMPTY);
  }

  @Override
  public GenericResponse saveRole(RoleDto dto) {
    String idOperation = SystemLog.newTxnIdOperation();
    info("Crendo rol " + dto.getDescription(), idOperation);

    try {
      Role role = roleRepository.getRoleByDescription(dto.getDescription());

      if (role != null && role.getRoleId() != null) {
        return new GenericResponse(ResponseType.EXIST, String.format(ResponseType.EXIST.getMessage(), "rol"));
      } else {
        roleRepository.save(setPropertiesAdmissionRole(dto));
        return new GenericResponse(ResponseType.ACEPT);
      }
    } catch (Exception ex) {
      Console.logException("Error al almacenar nuevo rol", ex, idOperation);
    }
    return new GenericResponse(ResponseType.NOT_AVAILABLE);
  }

  @Override
  public GenericResponse updateRole(RoleDto dto) {
    String idOperation = SystemLog.newTxnIdOperation();
    info("Actualizando rol con id: " + dto.getRoleId(), idOperation);

    try{
      List<Role> listRole = roleRepository.getRolesAssociatedToUser(dto.getRoleId());

      if (listRole == null || listRole.isEmpty() || listRole.size() == 0) {
        Role role = roleRepository.getRoleById(dto.getRoleId());
        role.setDateChange(new Date());
        roleRepository.save(setPropertiesChangeRole(dto, role));
        return new GenericResponse(ResponseType.ACEPT);
      } else {
        return new GenericResponse(ResponseType.ROLE_ASSOCIATE);

      }
    }catch (Exception ex) {
      Console.logException("Error al actualizar rol", ex, idOperation);
    }

    return new GenericResponse(ResponseType.NOT_AVAILABLE);
  }

  @Override
  public GenericResponse deleteRole(Integer id) {
    String idOperation = SystemLog.newTxnIdOperation();
    try {
      info("Eliminando rol con id: " + id, idOperation);
      Role rol = roleRepository.getRoleById(id);

      rol.setStatus(2);
      roleRepository.save(rol);
      return new GenericResponse(ResponseType.ACEPT);
    }catch (Exception ex){
      Console.logException("Error al eliminar rol" , ex, idOperation);
    }

    return new GenericResponse(ResponseType.NOT_AVAILABLE);
  }

  public Role setPropertiesAdmissionRole(RoleDto dto) {

    Role role = new Role();

    role.setDescription(dto.getDescription());
    role.setUserAdmission(dto.getUserAdmission());
    role.setDateAdmission(new Date());
    role.setStatus(dto.getStatus());

    return role;
  }

  public Role setPropertiesChangeRole(RoleDto dto, Role role){

    role.setDescription(dto.getDescription());
    role.setDateChange(new Date());
    role.setUserChange(dto.getUserChange());
    role.setStatus(dto.getStatus());

    return role;
  }

  public static void info(String msg, String idOperation){
    Console.writeln(Console.Level.INFO,idOperation, msg);
  }
}
