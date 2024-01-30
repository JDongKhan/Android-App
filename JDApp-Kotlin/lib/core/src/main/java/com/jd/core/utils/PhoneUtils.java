package com.jd.core.utils;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresPermission;

import java.util.List;

public final class PhoneUtils {

    private PhoneUtils() {

        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the device is isPhoneOrPad.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhoneOrPad() {
        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 是否是平板
     *
     * @param context 上下文
     * @return 是平板则返回true，反之返回false
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        return screenInches >= 7.0;
    }

    /**
     * 是否是手机
     *
     * @param context 上下文
     * @return 是平板则返回true，反之返回false
     */
    public static boolean isPhone(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        return screenInches < 7.0;
    }



    //判断是不是电视
    public static boolean isTV () {
        PackageManager pm = AppUtils.getApp().getPackageManager();
        UiModeManager uiModeManager = (UiModeManager) AppUtils.getApp().getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        }
        boolean touchscreen = pm.hasSystemFeature("android.hardware.touchscreen");
        if (!touchscreen) {
            return true;
        }
        boolean television = pm.hasSystemFeature("android.hardware.type.television");
        if (television) {
            return true;
        }
        //通过尺寸大小判断
        int with = (int) (ScreenUtils.getScreenWidth() / ScreenUtils.getScreenDensity());
        int height = (int) (ScreenUtils.getScreenHeight() / ScreenUtils.getScreenDensity());
        if (with > 1500 && height > 900 ) {
            return true;
        }
        return false;
    }

    /**
     * Return the unique device id.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            if (tm == null)
                return "";
            String imei = tm.getImei();
            if (! TextUtils.isEmpty(imei))
                return imei;
            String meid = tm.getMeid();
            return TextUtils.isEmpty(meid) ? "" : meid;

        }
        return tm != null ? tm.getDeviceId() : "";
    }

    /**
     * Return the serial of device.
     *
     * @return the serial of device
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getSerial () {

        return Build.VERSION.SDK_INT >= VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
    }

    /**
     * Return the IMEI.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI () {
        try {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
                String androidId = Settings.System.getString(AppUtils.getApp().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
                return androidId;
            }
            TelephonyManager tm =
                    (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                return tm != null ? tm.getImei() : "";
            }
            return tm != null ? tm.getDeviceId() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the MEID.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the MEID
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getMEID () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            return tm != null ? tm.getMeid() : "";
        } else {
            return tm != null ? tm.getDeviceId() : "";
        }
    }

    /**
     * Return the IMSI.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMSI
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMSI () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSubscriberId() : "";
    }

    /**
     * Returns the current phone type.
     *
     * @return the current phone type
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM }</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP }</li>
     * </ul>
     */
    public static int getPhoneType () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : - 1;
    }

    /**
     * Return whether sim card state is ready.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSimCardReady () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : "";
    }

    /**
     * Return the sim operator using mnc.
     *
     * @return the sim operator
     */
    public static String getSimOperatorByMnc () {

        TelephonyManager tm =
                (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null)
            return null;
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
                return "中国移动";
            case "46001":
                return "中国联通";
            case "46003":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * Skip to dial.
     *
     * @param phoneNumber The phone number.
     */
    public static void dial (final String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        AppUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Make a phone call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     */
    @RequiresPermission(CALL_PHONE)
    public static void call (final String phoneNumber) {

        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        AppUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Send sms.
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    public static void sendSms (final String phoneNumber, final String content) {

        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        AppUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Send sms silently.
     * <p>Must hold {@code <uses-permission android:name="android.permission.SEND_SMS" />}</p>
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    @RequiresPermission(SEND_SMS)
    public static void sendSmsSilent (final String phoneNumber, final String content) {

        if (TextUtils.isEmpty(content))
            return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(AppUtils.getApp(), 0, new Intent("send"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    public static String getMacAddress(){
        return null;
    }


    /**
     * 判断当前应用是否处于前台
     */
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
