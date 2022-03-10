package com.smart.ecommerce.administration.service;

import com.smart.ecommerce.administration.model.UserDto;
import com.smart.ecommerce.administration.model.UserPtlSrvDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.model.dto.UsersSearchDTO;
import com.smart.ecommerce.administration.util.GenericResponse;

public interface UserService {

    GenericResponse getUsersBySearchFiltered(UsersSearchDTO dto, InfoTokenDto infoTokenDto);

    GenericResponse newUpdateUser(UserDto dto, String name, Integer languageId);

    GenericResponse deleteUser(Integer id, String name, Integer languageId);

    GenericResponse listUsers(Integer languageId);

    GenericResponse getUser(Integer userId, Integer languageId);
    
    GenericResponse getUserIncludingBlocked(Integer userId, Integer languageId);

    GenericResponse getByEmail(String email, Integer languageId);

    GenericResponse getUser(String user, Integer languageId);
    
    GenericResponse getUserIncludingBlocked(String user, Integer languageId);
    
    GenericResponse failedLoginAttempt( Integer userId, Integer languageId);
    GenericResponse getRoles();
    GenericResponse getLanguages();
    GenericResponse getUserStatus();

    GenericResponse getListHierarchies();

    GenericResponse getListStatus();

    GenericResponse newUpdateUserPtlSrv(UserPtlSrvDto dto, String name, Integer languageId);

    GenericResponse deleteUserPrtlSrv(Integer id, String name, Integer languageId);

    GenericResponse blockUserPrtlSrv(Integer id, String name, Integer languageId);

    GenericResponse getUserIncludingBlockedPrtlSrv(Integer userId, Integer languageId);

    GenericResponse unblockUserPrtlSrv(Integer id, String name, Integer languageId);
}
