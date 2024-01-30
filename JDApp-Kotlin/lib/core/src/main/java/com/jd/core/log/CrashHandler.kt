package com.jd.core.log

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * @author jd
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理类
     */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 程序的Context对象
     */
    private var mContext: Context? = null
    private var logPath: String? = null

    // 用来存储设备信息和异常信息
    private val infos: MutableMap<String, String> = HashMap()

    /**
     * 用于格式化日期,作为日志文件名的一部分
     */
    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINESE)

    /**
     * 初始化
     */
    fun init(context: Context?, logPath: String?) {
        this.logPath = logPath
        mContext = context
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        handleException(ex)
        if (mDefaultHandler != null) {
            mDefaultHandler!!.uncaughtException(thread, ex)
        }
        exitApp()
    }

    private fun exitApp() {
        Process.killProcess(Process.myPid())
        System.exit(0)
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private fun handleException(ex: Throwable?) {
        if (ex == null) {
            return
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext)
        // 保存日志文件
        saveCrashInfo2File(ex)
    }

    /**
     * 收集设备参数信息
     */
    private fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtils.e("exception", "an error occured when collect package info")
        }
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = Objects.requireNonNull(field[null]).toString()
            } catch (e: Exception) {
                LogUtils.e("exception", "an error occured when collect crash info")
            }
        }
    }

    /**
     * 保存错误信息到文件中，需要有对SD的读写权限！
     *
     * @param ex 异常
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuilder()
        for ((key, value) in infos) {
            sb.append(key).append("=").append(value).append("\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            // 崩溃日志的文件
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                if (TextUtils.isEmpty(logPath)) {
                    logPath = mContext!!.getExternalFilesDir("log")?.absolutePath + File.separator
                }
                Log.e("Exception", "logPath:$logPath")
                val dir = File(logPath)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(logPath + fileName)
                val s = sb.toString()
                fos.write(s.toByteArray())
                fos.close()
            }
            return fileName
        } catch (e: Exception) {
            LogUtils.e("exception", "an error occured while writing file...")
        }
        return null
    }
}