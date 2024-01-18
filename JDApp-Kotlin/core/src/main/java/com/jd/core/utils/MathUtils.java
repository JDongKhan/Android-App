package com.jd.core.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtils {
    public static final double PRECISION = 0.0000001D;

    public static  boolean isZero(double df) {
        if (Math.abs(df) < PRECISION) {
            return true;
        }
        return false;
    }

    public static  boolean isDoubleEquals(double df_org, double df_new) {
        if (Math.abs(df_org-df_new) < PRECISION) {
            return true;
        }
        return false;
    }

    public static  boolean isDoubleEquals(double df_org, double df_new, double dfPrecision) {
        if (Math.abs(df_org-df_new) < dfPrecision) {
            return true;
        }
        return false;
    }

    public static boolean isDoubleBigThan(double df_org, double df_new){
        if(df_org-df_new > PRECISION ){//|| Math.abs(df_org-df_new) < PRECISION
            return true;
        }
        return false;
    }

    public static boolean isSmallThan(double df_org, double df_new){
        if(df_org-df_new < PRECISION ){
            return true;
        }
        return false;
    }

    public static int valueOf(String str, final int defVal)  {
        int retValue = defVal;//0xFFFF
        if (TextUtils.isEmpty(str)) {
            return retValue;
        }
        try{
            if (str.contains(".")) {
                str = str.substring(0, str.indexOf("."));
            }
            retValue = Integer.parseInt( str );
        }catch(  Exception e ){
            e.printStackTrace();
        }
        return retValue ;
    }

    private static final Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    public static String replaceBlank(String str) {
        String dest = "";
        if(TextUtils.isEmpty(str)){
            return dest;
        }
        if (str!=null) {
            Matcher m = BLANK_PATTERN.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static float ValueOfFloat(String str, final float defVal){
        str = replaceBlank(str);
        if(TextUtils.isEmpty(str)){
            return defVal;
        }
        float retValue = defVal;
        try{
            retValue = Float.parseFloat( str );
        }catch(  Exception e ){
            retValue = 0f;
        }
        return retValue ;
    }

    public static int valueOfInt(String str, final int defVal){
        str = replaceBlank(str);
        if(TextUtils.isEmpty(str)){
            return defVal;
        }
        int retValue = defVal;
        try{
            if (str.contains(".")) {
                str = str.substring(0, str.indexOf("."));
            }
            retValue = Integer.parseInt( str );
        }catch( Exception e ){
            e.printStackTrace();
        }
        return retValue ;
    }

    public static long valueOfLong(String str, final long defVal)  {
        str = replaceBlank(str);
        if(TextUtils.isEmpty(str)){
            return defVal;
        }
        long retValue = defVal;//0xFFFF
        try{
            retValue = Long.parseLong( str );
        }catch( Exception e ){
            retValue = 0;
        }
        return retValue ;
    }

    public static int valueOf(String str)  {
        int retValue = -1;//0xFFFF
        if (TextUtils.isEmpty(str)) {
            return retValue;
        }
        try{
            if (str.contains(".")) {
                str = str.substring(0, str.indexOf("."));
            }

            retValue = Integer.parseInt( str );
        }catch( Exception e ){
            e.printStackTrace();
        }
        return retValue ;
    }

    public static float ValueOfFloat(String str){
        float retValue = 0f;
        if (TextUtils.isEmpty(str)) {
            return retValue;
        }
        try{
            retValue = Float.parseFloat( str );
        }catch( Exception e ){
            retValue = 0f;
        }
        return retValue ;
    }

    public static long valueOfLong(String str)  {
        long retValue = -1;//0xFFFF
        if (TextUtils.isEmpty(str)) {
            return retValue;
        }
        try{
            retValue = Long.parseLong( str );
        }catch( Exception e ){
            retValue = 0;
        }
        return retValue ;
    }

    public static int getIntegerValue(Integer value){
        if(null == value){
            return 0;
        }
        return value;
    }

    public static int getIntegerValue(Integer value, final int defVal){
        if(null == value){
            return defVal;
        }
        return value;
    }

    public static double ValueOfDouble(String str){

        double retValue = 0.0;
        try{
            retValue = Double.parseDouble( str );
        }catch( Exception e ){
            retValue = 0.0;
        }
        return retValue ;
    }

    public static double valueOfDouble(String str, double defaultvalue){

        double retValue ;
        try{
            retValue = Double.parseDouble( str );
        }catch( Exception e ){
            retValue = defaultvalue;
        }
        return retValue ;
    }

    /**
     * 加
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    public static double addDouble(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 减
     *
     * @param s1
     * @param s2
     * @return
     */
    public static double sub(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 金额精度保留两位
     *
     * @param param
     * @return
     */
    public static String format(String param) {
        return new BigDecimal(param).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static String appendZero(String val, int zeroNum){
        if(TextUtils.isEmpty(val) || zeroNum <= 0){
            return "";
        }
        int length = val.length();

        int pointIdx = val.lastIndexOf('.');
        if(pointIdx < 0 ){
            return val+"."+String.format("%1$0"+(zeroNum)+"d",0);
        }
        String retStr = val;
        pointIdx = length-pointIdx-1;
        if(zeroNum > pointIdx ) {
            retStr = val + String.format("%1$0" + (zeroNum - pointIdx) + "d", 0);
        }
        return retStr;
    }
}
