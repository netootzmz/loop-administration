package com.smart.ecommerce.administration.util;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	/*
	 * ^                 # start-of-string
	(?=.*[0-9])       # a digit must occur at least once
	(?=.*[a-z])       # a lower case letter must occur at least once
	(?=.*[A-Z])       # an upper case letter must occur at least once
	(?=.*[@#$%^&+=])  # a special character must occur at least once
	(?=\S+$)          # no whitespace allowed in the entire string
	.{8,}             # anything, at least eight places though
	$                 # end-of-string
	*/
	
	public static boolean isPassValid(String pass) {
		boolean flag = false;
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{8,}$";
		if(pass.matches(pattern)) {
			flag=true;
		}
		return flag;
	}

  public static boolean isMailValid(String email) {

    if (email == null || !email.contains("@")) {
      return false;
    }

    Pattern pattern =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\" +
                    ".[A-Za-z]{2,})$");

    Matcher mather = pattern.matcher(email);

    return mather.find();
  }

  public static String encryptSha256(String value) {
    return Hashing.sha256().hashString(value, StandardCharsets.UTF_8).toString();
  }

  public static String generateCodeVerfication() {
    String codeVerification = "";

    SecureRandom random = new SecureRandom();
    codeVerification = new BigInteger(130, random).toString(32);

    return codeVerification.substring(0, 8);

  }
  
  public static boolean validatePhoneNumber(String phone) {
		// validate phone numbers of format "1234567890"
		if (phone.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phone.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phone.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;

	}


	public static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(""))
			return "";

		if (start < 0)
			start = 0;

		if (end > strText.length())
			end = strText.length();

		if (start > end)
			return null;

		int maskLength = end - start;

		if (maskLength == 0)
			return strText;

		String strMaskString = StringUtils.repeat(maskChar, maskLength);

		return StringUtils.overlay(strText, strMaskString, start, end);
	}

}
