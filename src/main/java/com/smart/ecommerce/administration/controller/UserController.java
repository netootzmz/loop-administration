package com.smart.ecommerce.administration.controller;

import com.smart.ecommerce.administration.contract.UserContract;
import com.smart.ecommerce.administration.model.UserDto;
import com.smart.ecommerce.administration.model.UserPtlSrvDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.UsersSearchDTO;
import com.smart.ecommerce.administration.service.UserService;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.administration.util.InfoToken;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController implements UserContract {

    @Autowired
    private UserService service;

    @Override
    public GenericResponse addUpdateUser(HttpServletRequest request, UserDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.newUpdateUser(dto, name, languageId);
    }

    @Override
    public GenericResponse deleteUser(HttpServletRequest request, Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.deleteUser(id, name, languageId);
    }

    @Override
    public GenericResponse listUsers(HttpServletRequest request) {
        Integer languageId = InfoToken.getLanguageId(request);
        return service.listUsers(languageId);
    }
    
    @Override
	public GenericResponse getUserByIdBlocked(HttpServletRequest request, Integer userId) {
    	Integer languageId = InfoToken.getLanguageId(request);
        return service.getUserIncludingBlocked(userId, languageId);
   }
//   Metodo para  Obtener usuario por Email

    @Override
	public GenericResponse getByEmail(HttpServletRequest request, String email) {
    	Integer languageId = InfoToken.getLanguageId(request);
         return service.getByEmail(email, languageId);
    }

	@Override
	public GenericResponse getUserByNameBlocked(HttpServletRequest request, String user) {
		Integer languageId = InfoToken.getLanguageId(request);
        return service.getUserIncludingBlocked(user, languageId);
    }

    @Override
    public GenericResponse listUser(HttpServletRequest request, Integer userId) {
        Integer languageId = InfoToken.getLanguageId(request);
        return service.getUser(userId, languageId);
    }

    @Override
    public GenericResponse listUser(HttpServletRequest request, String user) {
        Integer languageId = InfoToken.getLanguageId(request);
        return service.getUser(user, languageId);
    }
    
	@Override
	public GenericResponse failedLoginAttempt(HttpServletRequest request, Integer userId) {
		 Integer languageId = InfoToken.getLanguageId(request);
	     return service.failedLoginAttempt(userId, languageId);
	}

    @PostMapping("/getRoles")
    @ApiOperation("Obtiene el catalogo de roles activos.")
    public GenericResponse getRoles() {
        return service.getRoles();
    }

    @PostMapping("/getLaguage")
    @ApiOperation("Obtiene el catalogo de roles activos.")
    public GenericResponse getLanguages() {
        return service.getLanguages();
    }


    @PostMapping("/getUserStatus")
    @ApiOperation("Obtiene el catalogo de roles activos.")
    public GenericResponse getUserStatus() {
        return service.getUserStatus();
    }

    @PostMapping(value = "/getUsersBySearchFiltered", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse getUsersBySearchFiltered(HttpServletRequest request, @RequestBody UsersSearchDTO usersSearchDTO) {
        InfoTokenDto infoTokenDto = InfoToken.getInfoToken(request);

        return service.getUsersBySearchFiltered( usersSearchDTO, infoTokenDto);
    }

    @PostMapping("/getAllHierarchies")
    @ApiOperation("Obtiene el catalogo de jerarquías activas.")
    public GenericResponse getListHierarchies() {
        return service.getListHierarchies();
    }

    @PostMapping("/getAllStatus")
    @ApiOperation("Obtiene el catalogo de status.")
    public GenericResponse getListStatus() {
        return service.getListStatus();
    }

    @Override
    public GenericResponse addUpdateUserPtlSrv(HttpServletRequest request, UserPtlSrvDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.newUpdateUserPtlSrv(dto, name, languageId);
    }

    @Override
    public GenericResponse deleteUserPrtlSrv(HttpServletRequest request, Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.deleteUserPrtlSrv(id, name, languageId);
    }

    @Override
    public GenericResponse blockUserPrtlSrv(HttpServletRequest request, Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.blockUserPrtlSrv(id, name, languageId);
    }

    @Override
    public GenericResponse getUserByIdBlockedPrtlSrv(HttpServletRequest request, Integer userId) {
        Integer languageId = InfoToken.getLanguageId(request);
        return service.getUserIncludingBlockedPrtlSrv(userId, languageId);
    }

    @Override
    public GenericResponse unblockUserPrtlSrv(HttpServletRequest request, Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer languageId = InfoToken.getLanguageId(request);
        String name = auth.getName();
        return service.unblockUserPrtlSrv(id, name, languageId);
    }
}
