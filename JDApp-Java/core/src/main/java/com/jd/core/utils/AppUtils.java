package com.jd.core.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;


import com.jd.core.base.BaseApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * App相关工具类
 */

public class AppUtils {


    /**
     * 获取 App 包名
     *
     * @return the application's package name
     */
    public static String getAppPackageName() {
        return BaseApplication.getAppContext().getPackageName();
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }


    /**
     * 对比本地与线上的版本号
     * */
    public static boolean needUpdateV2(String local, String online) {
        boolean need = false;
        if (local != null && online != null) {
            String[] onlines = online.split("\\.");
            AppLog.e(Arrays.toString(onlines));
            String[] locals = local.split("\\.");
            AppLog.e(Arrays.toString(locals));
            if (Integer.parseInt(onlines[0]) > Integer.parseInt(locals[0])) {
                need = true;
            } else if (Integer.parseInt(onlines[0]) == Integer.parseInt(locals[0])) {
                if (Integer.parseInt(onlines[1]) > Integer.parseInt(locals[1])) {
                    need = true;
                } else if (Integer.parseInt(onlines[1]) == Integer.parseInt(locals[1])) {
                    if (Integer.parseInt(onlines[2]) > Integer.parseInt(locals[2])) {
                        need = true;
                    } else {
                        need = false;
                    }

                } else if (Integer.parseInt(onlines[1]) < Integer.parseInt(locals[1])) {
                    need = false;
                }
            } else if (Integer.parseInt(onlines[0]) < Integer.parseInt(locals[0])) {
                need = false;
            }
        }
        return need;
    }


    public static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
