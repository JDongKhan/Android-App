package com.jd.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("Can not be created！");
    }


    /**
     * 判断字符串是否为空（去除空格）
     *
     * @param str
     * @return
     */
    public static boolean isEmptyWithBlank(String str) {
        return (null == str || str.trim().length() == 0);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 替换字符串中的特殊字符
     *
     * @param str
     * @return
     */
    public static String replaceSpecial(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 替换字符串中的英文
     *
     * @param str
     * @return
     */
    public static String replaceEnglish(String str) {
        String regEx = "[qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 替换字符串中的不可见字符
     *
     * @param str
     * @return
     */
    public static String replaceInvisiable(String str) {
        Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     * 字符串转整型
     *
     * @param str
     * @return
     */
    public static int toInt(String str) {
        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 字符串转double
     *
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        double i = 0.0;
        try {
            i = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.w("Sonar Exception", e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Sonar Exception", e);
        }
        return i;
    }

    /**
     * 根据指定字符串拼接字符串
     *
     * @param strings
     * @param combiner
     * @return
     */
    public static String combineStrings(Collection<String> strings, String combiner) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s).append(combiner);
        }
        sb.delete(sb.length() - combiner.length(), sb.length());
        return sb.toString();
    }


    /**
     * 字符串转16进制字符串
     *
     * @param strPart
     * @return
     */
    public static String stringToHex(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * 16进制字符串转字符串
     *
     * @param src
     * @return
     */
    public static String hexToString(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    /**
     * 字符串转ASCII
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append((int) chars[i]);
        }
        return sbu.toString();
    }

    /**
     * ASCII转字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < value.length() / 2; i++) {
            sbu.append(
                    (char) Integer.parseInt(
                            value.substring(i * 2, i * 2 + 2)
                    ));
        }
        return sbu.toString();
    }

    /**
     * 从URL中获取文件名
     *
     * @param url
     * @return
     */
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 根据资源ID 获取字符串
     *
     * @param context
     * @param ID
     * @return
     */
    public static String stringForID(Context context, int ID) {

        return context.getResources().getString(ID);
    }

    public static boolean isBlank(String str) {
        /**
         *  判断是否为NULL，去除空格
         *  名称： isBlank
         *  @author 17093029
         *  创建日期： 2017/9/30
         *  @param   [str]
         *  @retun boolean
         */
        return (str == null || str.trim().length() == 0);
    }


    /**
     * 手机号脱敏
     *
     * @param phoneNumber
     * @return
     */
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        if (phoneNumber.length() < 7) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + " **** " + phoneNumber.substring(phoneNumber.length() - 4);
    }

    /**
     * 获取字符串长度 区分中文
     *
     * @param value
     * @return
     */
    public static int getStringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5|\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d" +
                "|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c" +
                "|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }


    /**
     * 按字节截取字符串 ，指定截取起始字节位置与截取字节长度
     */
    public static String subStringByte(String orignal, int start, int count) {
        //如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal)) {
            return orignal;
        }
        //截取Byte长度必须>0
        if (count <= 0) {
            return orignal;
        }
        //截取的起始字节数必须比
        if (start < 0) {
            start = 0;
        }
        String result = null;
        try {
            //截取字节起始字节位置大于目标String的Byte的length则返回空值
            if (start >= StringUtils.getStringLength(orignal)) {
                return null;
            }
            result = doSubStringByte(orignal, start, count);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Sonar Exception", e);
        }
        return result;
    }

    /**
     * 按字节截取字符串 ，指定截取起始字节位置与截取字节长度
     *
     * @param orignal 原始字符串
     * @param start   起始
     * @param count   长度
     * @return 处理后的字符串
     */
    private static String doSubStringByte(String orignal, int start, int count) {
        StringBuffer buff = new StringBuffer();
        int len = 0;
        char c;
        //如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
        for (int i = 0; i < orignal.toCharArray().length; i++) {
            c = orignal.charAt(i);
            //当起始位置为0时候
            if (start == 0) {
                len += StringUtils.getStringLength(String.valueOf(c));
                if (len <= count) {
                    buff.append(c);
                } else {
                    break;
                }
            } else {
                //截取字符串从非0位置开始
                len += StringUtils.getStringLength(String.valueOf(c));
                if (len >= start && len <= start + count) {
                    buff.append(c);
                }
                if (len > start + count) {
                    break;
                }
            }
        }
        return buff.toString();
    }

    /**
     * 截取指定长度字符串
     *
     * @param orignal 要截取的目标字符串
     * @param count   指定截取长度
     * @return 返回截取后的字符串
     */
    public static String subStringByte(String orignal, int count) {
        return subStringByte(orignal, 0, count);
    }


    /**
     * 脱敏姓名
     *
     * @param name
     * @return
     */
    public static String formatName(String name, String replaceSign) {
        if (StringUtils.isEmptyWithBlank(name) || "顾客".equals(name)) {
            return "顾客";
        }
        if (name.length() < 2) {
            return name;
        }
        StringBuilder nameBuilder = new StringBuilder();
        if (name.length() == 2) {
            nameBuilder.append(replaceSign);
            nameBuilder.append(name.substring(1));
        } else {
            nameBuilder.append(name.substring(0, 1));
            for (int i = 0; i < name.length() - 2; ++i) {
                nameBuilder.append(replaceSign);
            }
            nameBuilder.append(name.substring(name.length() - 1));
        }
        return nameBuilder.toString();
    }

    /**
     * 获取例如 26.00 格式
     *
     * @param string
     * @return
     */
    public static String getMoneyString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        Double totalDouble = getDoubleFromStr(string);
        return String.format("%.2f", totalDouble);
    }


    public static Double getDoubleFromStr(String str) {
        /**
         *  字符串转换为Double
         *  名称： getDoubleFromStr
         *  @author 17093029
         *  创建日期： 2017/9/30
         *  @param   [str]
         *  @retun java.lang.Double
         */
        Double i = 0.0;
        try {
            i = Double.valueOf(str);
        } catch (Exception e) {
        }
        return i;
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 格式化券号 比如1234 1234 12
     * @param str 原字符串
     * @return 格式化后的字符串
     */
    public static String formatCoupon(String str){
        if(isEmpty(str)||str.length()<=4){
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 计算总共分成主要的几段
        int parts = str.length()/4;
        for(int i=0;i<parts;i++){
            // 除当前最后一段外，每一端后面都增加空格
            String part = str.substring(i*4,i*4+4);
            stringBuilder.append(part);
            if(i!=parts-1) {
                stringBuilder.append(" ");
            }
        }
        // 判断是否有剩余，如果有则直接添加最后的并添加之前的空格，如果没有则结束
        int left = str.length()%4;
        if(left!=0){
            stringBuilder.append(" ");
            stringBuilder.append(str.substring(parts*4));
        }
        return stringBuilder.toString();
    }

    /**
     * 去掉
     * @param str
     * @return
     */
    public static String getRidOfDotStr(String str){
        if(TextUtils.isEmpty(str) || !str.contains(".")){
            return str ;
        }
        int idx =  str.indexOf(".");
        if(idx <= 0){
            return str;
        }

        return str.substring(0, idx);
    }
}
