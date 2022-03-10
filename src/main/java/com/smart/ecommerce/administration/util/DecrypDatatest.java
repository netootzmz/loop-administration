package com.smart.ecommerce.administration.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DecrypDatatest {
    private static final Logger log = LogManager.getLogger(DecrypDatatest.class);

    public static void main(String[] args) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException {


        String fechaToHexa = toHexadecimal("06-08-2021135838");
        String apiKey = "zM=KlNcDNNuYTN4tQHJ0LSaT2TGy2osGRoBxnCamQJBAKVvdzvGWMpWsglhD+S+xnQLT1HO8sPbHOBdYKJAOw+BueKHHPEWrgy5fePv4L+tvv/eXC+";
        String datos = "SHIduUPlAXekm489eMJyYTBewV9D2z8cqxzumpZm4/fkxPMEoOIC7K6rJ/RixezwCAy1nYA5FJP6IrPF8uOEeiu26KjLyQfHAidCgGL66E70vqq6vvJ9tnKGh1QibQ/uw01A4ntzrUaPiaT6XR7DKDM5bAp54jzJRnYDhabla++kIhH+ehsK9d2dzIkQDmXRjHPxmqfViYqCkezTgPbcoo044XJmEnqagwObl+eP+aFgTqAnZ/vgyvmjZq7pjcIFYmjJqU2KJEQ5TW451S8BalePN9BQJ3rMjzrzjrIBsdfnuc9EtCo0UAFKMbdvSbzXaGvdEzk2TQPmYj+NJz16X+6CmtAgNcubn4zl/y+lbk+JVrNbmD+vq/slkI/RicUT4PEI/PemvQClBrAEl5KQXvZnRc8Kul9fkd6smzKC6zLAZ02zIi0qc28BvGW8s2L5AukgQwVSvYhbFtb4QN5M3tFixJRqxGyLFQS3ndAdZLZuNalh4iTZll+sum5ShrZwP2hh+7jOduE3Oh/IfmQcNg9wQ4IfCg3siHRs75ysj9zotbbHdO+oHcwZsAs416/WOo/1noI+/1Mm1Oz0CxUDKgyjOLhWQ2wNfrg8Lbdzp81sQwfI8xQhtroyz45m59LsnhpKDzzDROJph6nuVGh4T30kcaFGU+8mR6HRSiXiF88LW7NPk1dMKmR6l+3C7dlntN941X26mNJBhUmbtlIvK0Znzu3OSXHRSKahg4rxsqV2qOJdq51x2Xqden4PhXsQSR2yb/m/VvE5WQmUif7FcsB6rfNBqoGbNIYNK7AwNAieyijfK1oceB3p3UiDdRf20e5o7CYIHd1r/qhJlU0OV4VkVI2b4kacEePCbB4WdXgEUrDaW/xipzKnkxn5KpRY0JOLStkiu+B4lsUlK3rPXieKBc1iReza3zatlHbe/HLq207/P1DOjG0AG27+cdvAXmW+ZalZnXd/wMuDZWbx4R0dP0ahTKjB/hH5xFGmCsm63/+dPCDqUiu7YoYssFpNSjx8KmIusGSinNn9N+oVsYUc0PkcdYEWi0HRKmJAEJloZ4UVEWVPJis5O/Qv0hN/KrgkHTPZqtOE6+hDhf+QHWhodZSiMX9blm3hIofx06jrDuKV/Xb79xoa4vO4gry2gY1TBFgodd1Pqc7HGCDWQZWS3jzJ6VyyEDk1G0s1h/GLVscYoK3UvOQTJysZ+uJUq8sZKbaHVaS2rdeQuMKTNugbwmVqG0i8gMtQQ3D6P0auAkibDIuwYY4qdup1lVrL9jRxpAXdbg3Hec6mXNu9aQf4/wU9u2OnFD7Hq8CzfE6Yr7wtb3WNL0CPmTMc60vwop5xPB6KJtMaKQcFCxyQLtWxe/DDa1jxowFXHFEueFQf70KL0B07YLqOdwLeBgUK2+74z3dolsDmUj7IfYjVCaTlJZ0Y3va7RsWfWrAJRynXT2pN40l1KMjoMJ8KjTXARiJJCCB8Tp6gL7tRK1p+IlAvx736xy7cqiNz5YqRFdsJucv2ZRo0mQRGkeCV6t+GnnLcB/0n6YdTDvJtuQloob9FYkmFfEJubfHboD04tG82IVqsvmKmKkvtyNaJfnO8//5yMTeHY8RM5Z+6FETjR+vJqm5WDVOx3y1ThACr5aGv08k4cPP+zheWNzAUFyXSvKpbtzxdudabAgjzQN5l1JrobJ0+8PWk76A9b6x1z6zOs4KKSPBsmZq6Ru2dcyzTFO4ndT5rcGKCunrJY+U7ybNBASfTji40T76ImxUP9rQ7t7Ug/oOyNBqappQBqhNQHsycVxv6QAJ0EVqMC/Dib1Sl/QxpWLayixOsIxDPD8Z9iIzrwA6ugt9/TTrq17XEhOzmHdNWg0o8bJA/X9iAueKlzPABacZTO9rURstFvlqSgxQxkyqx+tzXKon821Dh";
        Key key = generaLLaveByString32char(apiKey.substring(0, 16) + fechaToHexa.substring(0, 16));
        log.info("***  " + desencriptaCOnLLave(key, datos));

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

    public static Key generaLLaveByString32char(String secret) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        Key key = keyGenerator.generateKey();
        key = new SecretKeySpec(secret.getBytes(), "AES");
        return key;
    }

    //metodo que recibe una llave y una cadena en base 64 la cual corresponde al texto cifrado,
    // y te regresa el resultado de la conversion
    public static String desencriptaCOnLLave(Key key, String cadena) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] reCipherBytes = Base64.getDecoder().decode(cadena);
        aes.init(Cipher.DECRYPT_MODE, key);
        byte[] desencriptado = aes.doFinal(reCipherBytes);
        return new String(desencriptado);
    }

}


