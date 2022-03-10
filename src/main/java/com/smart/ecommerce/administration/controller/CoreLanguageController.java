package com.smart.ecommerce.administration.controller;


import com.smart.ecommerce.administration.contract.CoreLanguageContract;
import com.smart.ecommerce.administration.service.CoreLanguageService;
import com.smart.ecommerce.administration.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/CoreLanguageController")
public class CoreLanguageController implements CoreLanguageContract {
    
    @Autowired private  CoreLanguageService coreLanguageService;
    
    @Override
    @PostMapping
    
    public GenericResponse getAllCoreLanguage(){

        return coreLanguageService.getAllCoreLanguage();

    }
    
    
    
    
    
    
    
}
