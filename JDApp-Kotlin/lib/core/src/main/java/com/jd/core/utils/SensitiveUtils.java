package com.jd.core.utils;

import android.text.TextUtils;

/**
 * 脱敏
 * @author jd
 */
public class SensitiveUtils {
    /**
     * 姓脱敏
     * @param name
     * @return
     */
    public static String nameDesensitization(String name) {
        if(TextUtils.isEmpty(name)) {
            return name;
        }
        char[] sArr = name.toCharArray();
        if (sArr.length == 2) {
            return sArr[0]+"*";
        } else if (sArr.length  > 2) {
            for (int i = 1; i < sArr.length-1 ; i++) {
                // if ('·' != sArr[i]) {
                sArr[i] = '*';
                // }
            }
            return new String(sArr);
        }
        return name;
    }

    public static String phoneDesensitization(String number) {
        if (TextUtils.isEmpty(number) || (number.length() != 11)) {
            return number;
        }
        return number.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
