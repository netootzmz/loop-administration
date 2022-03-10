/**
 * 
 */
package com.smart.ecommerce.administration.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Eduardo Valeriano
 *
 */
public class ConvertDates {

	
	public static String getDateDDMMYYYY(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static Date addMinutesDate(Date date, Integer minutes){
		Date dateAdd = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // Configuramos la fecha que se recibe
		calendar.add(Calendar.MINUTE, minutes);  // numero de minutos a añadir, o restar en caso de horas<0
		dateAdd = calendar.getTime();

		return dateAdd;
	}

	public static Date addHours(Date date, Integer hour){
		Date dateAdd = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // Configuramos la fecha que se recibe
		calendar.add(Calendar.HOUR_OF_DAY, hour);  // numero de horas a añadir, o restar en caso de horas<0
		dateAdd = calendar.getTime();

		return dateAdd;
	}



}
