package com.smart.ecommerce.administration.service.impl;

import com.smart.ecommerce.administration.model.dto.BitacoraCookiesDTO;
import com.smart.ecommerce.administration.repository.ErrorCodeRepository;
import com.smart.ecommerce.administration.repository.impl.BitacoraCookiesRepositoryImpl;
import com.smart.ecommerce.administration.request.BitacoraCookiesRequest;
import com.smart.ecommerce.administration.request.IpRequest;
import com.smart.ecommerce.administration.service.BitacoraCookiesService;
import com.smart.ecommerce.administration.util.ErrorCode;
import com.smart.ecommerce.administration.util.GenericResponse;
import com.smart.ecommerce.entity.core.CoreErrorCode;
import com.smart.ecommerce.logging.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BitacoraCookiesServiceImpl implements BitacoraCookiesService {
    @Autowired
    private ErrorCodeRepository codeRepository;

    @Autowired
    private BitacoraCookiesRepositoryImpl bitacoraDao;

    @Override
    public GenericResponse addBitacoraCookie(BitacoraCookiesRequest request) {
        GenericResponse response=new GenericResponse();
        try{
            log.info("Registrando cookie para la ip:{}",request.getIp());
            String idOperation = SystemLog.newTxnIdOperation();
            List<CoreErrorCode> listCodes = codeRepository.getAll(1);
            Map<String, Object> information = new HashMap<>();
            BitacoraCookiesDTO dto=new BitacoraCookiesDTO();
            dto.setCreatedAt(LocalDateTime.now());
            dto.setIp(request.getIp());
            dto.setStatusId(1);
            dto.setAcceptCookie(request.isCookieAccept());
            information.put("bitacoraCookieSaved",bitacoraDao.addBitacoraCookies(dto,idOperation));
            log.info("cookie registrada");
            response.setCodeStatus("00");
            response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
            response.setInformation(information);
            return response;
        }catch (Exception e){
            log.error("Error al registrar cookie para ip:{}, {}",request.getIp(),e);
            return response;
        }
    }

    @Override
    public GenericResponse getBitacoraService(IpRequest request) {
        GenericResponse response=new GenericResponse();
        try {
            log.info("Consultando cookie para ip:{}",request.getIp());
            String idOperation = SystemLog.newTxnIdOperation();
            List<CoreErrorCode> listCodes = codeRepository.getAll(1);
            Map<String, Object> information = new HashMap<>();
            Boolean responseDao=bitacoraDao.getBitacoraCookies(request.getIp(),idOperation);
            if (responseDao==null){
                responseDao=false;
            }
            information.put("cookieAccept",responseDao);
            log.info("cookie consultada");
            response.setCodeStatus("00");
            response.setMessage(ErrorCode.getError(listCodes, "00").getMessage());
            response.setInformation(information);
            return response;
        }catch (Exception e){
            log.error("Error al consultar cookie para ip:{}, {}",request.getIp(),e);
            return response;
        }
    }
}
