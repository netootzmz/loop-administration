package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.repository.CoreLanguageRepository;
import com.smart.ecommerce.administration.service.CoreLanguageService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.admin.CoreLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("coreLanguageService")
public class CoreLanguageServiceImpl implements CoreLanguageService {
    
    @Autowired
    CoreLanguageRepository coreLanguageRepository;
    
    
    @Override
    public GenericResponse getAllCoreLanguage() {
        Map<String, Object> informationResponse = new HashMap<>();


        GenericResponse response = new GenericResponse();
        List<Map<String,Object>> coreLanguages = coreLanguageRepository.getCoreLanguage();


        informationResponse.put("languages",coreLanguages);
        
        response.setInformation(informationResponse);

        return response;

    }
}
