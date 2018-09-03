package com.sce.data.gaia.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    static DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * jdk 8 转换字符串yyyy-MM-dd 为date类型
     *
     * @param dateStr
     * @return
     */
    public static Date parseToDate(String dateStr) {
        LocalDate lt = LocalDate.parse(dateStr, dayFmt);
        ZonedDateTime zdt = lt.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }


    /**
     * jdk 8 转换 date类型 为 字符串yyyy-MM-dd
     *
     * @param date
     * @return String
     */
    public static String formatToStr(Date date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dayFmt.format(ldt);
    }
    
    /**
     * jdk 8 转换 date类型 为 字符串yyyy-MM
     *
     * @param date
     * @return String
     */
    public static String formatToMonthStr(Date date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return monthFmt.format(ldt);
    }


    /**
     * jdk 8 转换 localDate类型 为 util.Date類型
     *
     * @param localDate
     * @return Date
     */
    public static Date convertToDate(LocalDate localDate) {
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    /**
     * 取某年某月的最后一天，例如 ‘2018-08’
     *
     * @param ym
     * @return Date
     */
    public static Date getLastDayOfMonth(String ym) {

    	if(StringUtils.isEmpty(ym)) {
    		ym = formatToMonthStr(new Date());
    	}
    	String year = ym.split("-")[0];
    	String month = ym.split("-")[1].replaceAll("0", "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DATE));
       
        return calendar.getTime();
    }
    
    /**
     * 取某年某月的第一天，例如 ‘2018-08’
     *
     * @param ym
     * @return Date
     */
    public static Date getFirstDayOfMonth(String ym) {

    	if(StringUtils.isEmpty(ym)) {
    		ym = formatToMonthStr(new Date());
    	}
    	String year = ym.split("-")[0];
    	String month = ym.split("-")[1].replaceAll("0", "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMinimum(Calendar.DATE));
       
        return calendar.getTime();
    }


}
