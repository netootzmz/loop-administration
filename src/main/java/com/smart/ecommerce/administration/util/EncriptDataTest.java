package com.smart.ecommerce.administration.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncriptDataTest {

    private static final Logger log = LogManager.getLogger(EncriptDataTest.class);

    public static void main(String[] args) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException {

        String fechaToHexa = toHexadecimal("23-07-2021113957");
        String apiKey = "3bb42127-4a59-453c-a48d-3c1973cbe900";
        //GETCHECKOUT
        //String datos = "{\"clientId\":\"\",\"apiKey\":\"3bb42127-4a59-453c-a48d-3c1973cbe900\",\"amount\":\"1\",\"urlResponseRedirect\":\"\", \"nameCardHolder\": \"\", \"zipCodeCardHolder\":\"\",  \"mailCardHolder\":\"\",\"phoneCardHolder\":\"\" ,\"carrierCardHolder\":\"\" }";
        //DOQUERY
        String datos = "{\n" +
                "\t\"address\": \"oriente 6 312\",\n" +
                "\t\"apikey\": \"3bb42127-4a59-453c-a48d-3c1973cbe900\",\n" +
                "\t\"cardNumber\": \"\",\n" +
                "\t\"cardTokenization\": false,\n" +
                "\t\"city\": \"nezahualcoyotl\",\n" +
                "\t\"cvv\": \"879\",\n" +
                "\t\"email\": \"david02ing@gmail.com\",\n" +
                "\t\"folioTx\": \"FOL_00050\",\n" +
                "\t\"haveMsi\": false,\n" +
                "\t\"lastname\": \"mendoza\",\n" +
                "\t\"lastname2\": \"perez\",\n" +
                "\t\"monthExpiration\": \"03\",\n" +
                "\t\"msiId\": null,\n" +
                "\t\"name\": \"david\",\n" +
                "\t\"nameHolder\": \"DAVID RICARDO MENDOZA PEREz\",\n" +
                "\t\"state\": \"mexico\",\n" +
                "\t\"terms\": false,\n" +
                "\t\"yearExpiration\": \"23\",\n" +
                "\t\"zipCode\": \"57840\"\n" +
                "}";

        Key key = generaLLaveByString32char(apiKey.substring(0, 16) + fechaToHexa.substring(0, 16));
        log.info("ENC  ***  " + encripta(key, datos));
    }

    public static Key generaLLaveByString32char(String secret) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        Key key = keyGenerator.generateKey();
        key = new SecretKeySpec(secret.getBytes(), "AES");
        return key;
    }

    //metodo que recibe una llave y un texto, regresa una cadena ya encriptada
    public static String encripta(Key key, String texto) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptado = aes.doFinal(texto.getBytes());
        String cipherText = Base64.getEncoder().encodeToString(encriptado);
        return cipherText;
    }

    public static String toHexadecimal(String claro) {
        StringBuffer sb = new StringBuffer();
        char ch[] = claro.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        String result = sb.toString();
        return result;
    }


    public static String generateData(String datos) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String fechaToHexa = toHexadecimal("03-09-2020101203");
        String apiKey = "7e13316f-4161-410c-a198-79cae658e518";
        Key key = generaLLaveByString32char(apiKey.substring(0, 16) + fechaToHexa.substring(0, 16));
//          log.info("ENC  ***  " + encripta(key, datos));

        return encripta(key, datos);
    }

}


