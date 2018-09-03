package com.sce.data.gaia.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberUtils {
    /**
     * 小数转化为百分形式字符串(带百分号)
     * @param bd
     * @param keep
     * @return
     */
    public static String formatToStrWithPercent(BigDecimal bd, int keep) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(keep);
        nf.setGroupingUsed(false);
        return nf.format(bd);
    }

    /**
     * 小数转化为百分形式字符串(无百分号)
     * @param bd
     * @param keep
     * @return
     */
    public static String formatToStrWithoutPercent(BigDecimal bd, int keep) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(keep);
        nf.setGroupingUsed(false);
        return nf.format(bd).replaceAll("%","");
    }

    /**
     * BigDecimal null convert to zero
     * @param source
     * @return BigDecimal
     */
    public static BigDecimal convertNullToZeroBigDecimal(BigDecimal source){
        return source==null?BigDecimal.ZERO:source;
    }

    /**
     * Long null convert to zero
     * @param source
     * @return Long
     */
    public static Long convertNullToZeroLong(Long source){
        return source==null?0:source;
    }


    public static void main(String[] args) {
        System.out.println(formatToStrWithoutPercent(new BigDecimal("0.3344"), 2));
    }

}
