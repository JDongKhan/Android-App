package com.jd.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log


import com.jd.core.base.BaseApplication

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.Arrays

/**
 * App相关工具类
 */

object AppUtils {


    /**
     * 获取 App 包名
     *
     * @return the application's package name
     */
    val appPackageName: String
        get() = BaseApplication.appContext!!.getPackageName()


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    fun getVerName(context: Context): String {
        var verName = ""
        try {
            verName = context.packageManager.getPackageInfo(
                    context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("msg", e.message)
        }

        return verName
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    fun getVerCode(context: Context): Int {
        var verCode = -1
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.packageManager.getPackageInfo(
                    context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("msg", e.message)
        }

        return verCode
    }


    /**
     * 对比本地与线上的版本号
     */
    fun needUpdateV2(local: String?, online: String?): Boolean {


        var need = false

        if (local != null && online != null) {

            val onlines = online.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            AppLog.e(Arrays.toString(onlines))
            val locals = local.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            AppLog.e(Arrays.toString(locals))

            // 2.0.0-1.0.0
            // 2.1.0-2.0.0
            // 2.1.1-2.1.0


            //3.0.0-4.0.0
            //3.1.0-3.2.0
            //3.1.1-3.2.0

            if (Integer.parseInt(onlines[0]) > Integer.parseInt(locals[0])) {
                need = true
                //                Logger.e("1-1");

            } else if (Integer.parseInt(onlines[0]) == Integer.parseInt(locals[0])) {
                //                Logger.e("1-2");
                if (Integer.parseInt(onlines[1]) > Integer.parseInt(locals[1])) {
                    need = true
                    //                    Logger.e("1-2-1");

                } else if (Integer.parseInt(onlines[1]) == Integer.parseInt(locals[1])) {
                    //                    Logger.e("1-2-2");
                    if (Integer.parseInt(onlines[2]) > Integer.parseInt(locals[2])) {
                        //                        Logger.e("1-2-2-1");
                        need = true
                    } else {
                        //                        Logger.e("1-2-2-2");
                        need = false
                    }

                } else if (Integer.parseInt(onlines[1]) < Integer.parseInt(locals[1])) {
                    need = false
                    //                    Logger.e("1-2-3");
                }


            } else if (Integer.parseInt(onlines[0]) < Integer.parseInt(locals[0])) {
                need = false
                //                Logger.e("1-3");
            }

        }
        return need


    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }

}
