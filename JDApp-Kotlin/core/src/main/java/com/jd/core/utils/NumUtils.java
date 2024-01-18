package com.jd.core.utils;

import androidx.annotation.Nullable;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumUtils {
    private static DecimalFormat sFormat = new DecimalFormat("0.0");
    private static DecimalFormat sTwoFormat = new DecimalFormat("0.00");

    public static String getOneDecimal(Object value) {
        return getOneDecimal(value,null);
    }

    public static String getOneDecimal(Object value,String defaultString) {
        if (value == null) {
            return defaultString;
        }
        if (value instanceof String) {
            try {
                value = Float.parseFloat((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                value = 0.0f;
            }
        }
        return sFormat.format(value);
    }

    public static String getTwoDecimal(Object value) {
        return getTwoDecimal(value,null);
    }
    public static String getTwoDecimal(Object value,String defaultString) {
        if (value == null) {
            return defaultString;
        }
        if (value instanceof String) {
            try {
                value = Float.parseFloat((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                value = 0.0f;
            }
        }
        return sTwoFormat.format(value);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s == null) {
            return null;
        }
        if(s.indexOf(".") > 0){
            //去掉多余的0
            s = s.replaceAll("0+?$", "");
            //如最后一位是.则去掉
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }

    /**
     * 四舍五入保留确定位数小数
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 返回值 String类型
     */
    public static String round_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_DOWN).toString();
    }
    public static String round_up(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_UP).toString();
    }
    public static String round_half_up(@Nullable String number, int decimal) {
        if (number == null) {
            return "";
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String round_half_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_DOWN).toString();
    }


    public static String round_down(float number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_DOWN).toString();
    }
    public static String round_up(float number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_UP).toString();
    }
    public static String round_half_up( float number, int decimal) {
        if (number == 0) {
            return "0.0";
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String round_half_down(float number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_DOWN).toString();
    }
}
