package com.jd.core.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class NetworkUtil {

    public static final int NETWORK_NO = -1;        // no network
    public static final int NETWORK_WIFI = 1;       // wifi network
    public static final int NETWORK_2G = 2;         // "2G" networks
    public static final int NETWORK_3G = 3;         // "3G" networks
    public static final int NETWORK_4G = 4;         // "4G" networks
    public static final int NETWORK_UNKNOWN = 5;    // unknown network

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    /**
     * 当前网络类型
     */
    public static String sCurrentNetworkType = "";

    private NetworkUtil() {
        throw new UnsupportedOperationException("Can not be created！");
    }

    /**
     * 打开网络设置
     *
     * @param context
     */
    public static void openNetWorkSettings(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 获取活动网路设置
     *
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                // 记录网络类型
                sCurrentNetworkType = networkInfo.getTypeName();
                return networkInfo;
            }
        }
        return null;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.w("Sonar Catch Exception", ex);
        }
        return "02:00:00:00:00:00";
    }
}
