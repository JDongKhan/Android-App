package com.jd.core.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.List;

public class AppUtils {
    private static Application sApplication;
    private static String currentProcessName;

    /**
     * 初始化工具类
     *
     * @param context
     */
    public static void init(@NonNull final Context context) {
        AppUtils.sApplication = (Application) context.getApplicationContext();
    }

    /**
     * 获取应用实例
     *
     * @return
     */
    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }
        throw new NullPointerException("Please init first");
    }


    public static String getAppName() {
        String name = "";
        PackageManager packageManager = sApplication.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(sApplication.getPackageName(),0);
            name = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getPackageName() {
        String packageName = "";
        try {
            packageName = sApplication.getPackageName();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }


    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        return Settings.Secure.getString(getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 获取应用的版本编号
     *
     * @return
     */
    public static int getAppVersionCode() {
        return getAppVersionCode(getApp().getPackageName());
    }


    /**
     * 获取指定应用的版本编号
     *
     * @param packageName
     * @return
     */
    public static int getAppVersionCode(final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }



    /**
     * 获取应用版本名称
     *
     * @return
     */
    public static String getAppVersionName() {
        return getAppVersionName(getApp().getPackageName());
    }

    /**
     * 获取指定应用的版本名称
     *
     * @param packageName
     * @return
     */
    public static String getAppVersionName(final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    //根据进程名获取进程ID
    public static int getPidByProcessName(Context context, String packageName) {
        if(TextUtils.isEmpty(packageName)) {
            return -1;
        }

        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        if(null == mActivityManager){
            return -1 ;
        }

        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();
        if (appProcessList != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
                if (packageName.equals(appProcess.processName)) {
                    return appProcess.pid;
                }
            }
        }
        return -1;
    }

    //根据进程名kill进程
    public static void killProcessbyProcessName(Context context, String processName) {
        int pid = getPidByProcessName(context, processName);
        if(pid > 0 ){
            try{
                android.os.Process.killProcess(pid);
            }catch (Exception e){
                Log.e(AppUtils.class.getSimpleName(), "killProcessbyProcessName failed: " + e.getMessage());
            }
        }
    }


    /**
     * @return 当前进程名
     */
    @Nullable
    public static String getCurrentProcessName(@NonNull Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //2)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //3)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager(context);
        return currentProcessName;
    }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    private static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    private static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            final Method declaredMethod = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader())
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    private static String getCurrentProcessNameByActivityManager(@NonNull Context context) {
        if (context == null) {
            return null;
        }
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();
            if (runningAppList != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppList) {
                    if (processInfo.pid == pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
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

}
