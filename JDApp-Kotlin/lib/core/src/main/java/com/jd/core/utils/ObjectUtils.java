package com.jd.core.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class ObjectUtils {
    private static  DecimalFormat df = new DecimalFormat("0.0");

    /**
     * 转double
     * @param string
     * @return
     */
    public static double toDouble(@Nullable  String string) {
        if (TextUtils.isEmpty(string)) {
            return  0.0;
        }
        return Double.valueOf(string);
    }

    public static float toFloat(@Nullable Object string) {
        if (string == null) {
            return 0.0f;
        }
        if (string instanceof Float) {
            return (Float)string;
        }
        if (TextUtils.isEmpty(string.toString())) {
            return  0.0f;
        }
        return Float.valueOf(string.toString());
    }

    /**
     * 转int
     * @param object
     * @return
     */
    public static Integer toInt(@Nullable  Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof String) {
            if (TextUtils.isEmpty((String)object)) {
                return 0;
            }
        } else  if (object instanceof Double) {
            return ((Double) object).intValue();
        }
        return Integer.valueOf(object.toString());
    }

    public static Long toLong(@Nullable Object object) {
        if (object == null) {
            return 0L;
        }
        if (object instanceof String) {
            if (TextUtils.isEmpty((String)object)) {
                return 0L;
            }
        } else  if (object instanceof Double) {
            return ((Double) object).longValue();
        }
        return Long.valueOf(object.toString());
    }

    /**
     * 转bool
     * @param value
     * @return
     */
    public static boolean toBool(Object value) {
        if (value == null) {
            return false;
        }
        if (TextUtils.isEmpty(value.toString())) {
            return false;
        }
        return Boolean.valueOf(value.toString());
    }

    //处理string
    public static String toString(Object string,String defaultValue) {
        if (string == null) {
            return  defaultValue;
        }
        if (string instanceof String) {
            return (String)string;
        }
        return string.toString();
    }

    /**
     * 一位小数string
     * @param string
     * @return
     */
    public static String oneDecimalString(String string) {
        if (TextUtils.isEmpty(string)) {
            return  "0.0";
        }

        Double doubleValue = Double.valueOf(string);
        return df.format(doubleValue);
    }

    /**
     * 一位小数string
     * @param value
     * @return
     */
    public static String oneDecimalDouble(Double value) {
        if (value == null) {
            return  "0.0";
        }
        return df.format(value);
    }
}
