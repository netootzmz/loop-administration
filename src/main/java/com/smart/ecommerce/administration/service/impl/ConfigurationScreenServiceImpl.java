package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.dto.ConfigurationScreenDto;
import com.smart.ecommerce.administration.model.dto.InfoTokenDto;
import com.smart.ecommerce.administration.repository.ConfigurationScreenRepository;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.service.ConfigurationScreenService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationScreenServiceImpl implements ConfigurationScreenService {

    @Autowired
    ConfigurationScreenRepository configurationScreenRepository;

    @Resource
    private ErrorCodeRepository codeDao;

    @Override
    public GenericResponse getConfigurationLinkPayScreenByUser(InfoTokenDto infoToken) {
        GenericResponse response = new GenericResponse();
        List<CoreErrorCode> listCodes = codeDao.getAll(infoToken.getLanguage_id());
        try {


            List<ConfigurationScreenDto> results = configurationScreenRepository.getConfigurationLinkPayScreenByUser(infoToken.getUser_by_register());
            if (results.size() > 0) {
                response.setCodeStatus("00");
                response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
                Map<String, Object> information = new HashMap<>();

                information.put("configuration", results.get(0));
                response.setInformation(information);
            } else {
                response.setCodeStatus("01");
                response.setMessage(ErrorCode.getError(listCodes, "01").getMessage());
            }
            return response;
        } catch (Exception e) {

            response.setCodeStatus("03");
            response.setMessage(String.format(ErrorCode.getError(listCodes, "03").getMessage(), e.getMessage()));
            e.printStackTrace();
            return response;

        }    }
}
