package com.jd.core.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process


/**
 * 进程工具类
 */
object ProcessUtils {

    /**
     * 获取当前所有进程
     *
     * @param context Context
     * @return List<ActivityManager.RunningAppProcessInfo>
     */
    fun getRunningAppProcessList(context: Context): List<ActivityManager.RunningAppProcessInfo> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.runningAppProcesses
    }

    /**
     * 判断该进程id是否属于该进程名的进程
     *
     * @param context Context
     * @param processId Int
     * @param processName String
     * @return Boolean
     */
    fun isPidOfProcessName(context: Context, processId: Int, processName: String): Boolean {
        // 遍历所有进程找到该进程id对应的进程
        for (process in getRunningAppProcessList(context)) {
            if (process.pid == processId) {
                // 判断该进程id是否和进程名一致
                return (process.processName == processName)
            }
        }
        return false
    }

    /**
     * 获取主进程名
     *
     * @param context Context
     * @return String
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getMainProcessName(context: Context): String {
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
        return applicationInfo.processName
    }

    /**
     * 判断当前进程是否是主进程
     *
     * @param context Context
     * @return Boolean
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun isMainProcess(context: Context): Boolean {
        val processId = Process.myPid()
        val mainProcessName = getMainProcessName(context)
        return isPidOfProcessName(context, processId, mainProcessName)
    }
}