package com.jd.core.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import com.jd.core.log.LogUtils

object CpuUtils {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun printMemory() {
        if (AppUtils.getApp() == null) {
            return
        }
        val rt = Runtime.getRuntime()
        val sb = StringBuilder()
        //虚拟机内存信息
        sb.append("VM maxMemory:" + rt.maxMemory() / (1024 * 1024) + "M")
        sb.append("\t totalMemory:" + rt.totalMemory() / (1024 * 1024) + "M")
        sb.append("\t freeMemory:" + rt.freeMemory() / (1024 * 1024) + "M")
        val activityManager =
            AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        sb.append("\t memoryClass:" + activityManager.memoryClass)
        sb.append("\t largeMemoryClass:" + activityManager.largeMemoryClass)

        //整个手机的内存信息
        val info = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(info)
        sb.append(
            """
    
    系统总内存:${info.totalMem / (1024 * 1024)}M
    """.trimIndent()
        )
        sb.append("\t剩余内存:" + info.availMem / (1024 * 1024) + "M")
        sb.append("\t是否处于低内存运行:" + info.lowMemory)
        sb.append("\t剩余内存低于" + info.threshold / (1024 * 1024) + "M时为低内存运行")
        val memInfo = activityManager.getProcessMemoryInfo(intArrayOf(Process.myPid()))
        if (memInfo.size > 0) {
            // TotalPss = dalvikPss + nativePss + otherPss, in KB
            val totalPss = memInfo[0].totalPss
            sb.append(
                """
    
    应用使用内存:${totalPss / 1024}M
    """.trimIndent()
            )
        }
        sb.append("\n")
        sb.append(dataInfo)
        sb.append("\n")
        sb.append(sDCardInfo)
        LogUtils.i("AppInfo", sb.toString())

//        Formatter.formatFileSize(AppUtil.getApp(), info.totalMem);
    }

    val sDCardInfo: String
        /**
         * SD卡
         */
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        get() {
            try {
                val path = Environment.getExternalStorageDirectory()
                val s = StatFs(path.path)
                val availableBlocks = s.availableBlocksLong
                val blockCount = s.blockCountLong
                val blockSize = s.blockSizeLong
                val totalsize = blockSize * blockCount
                val availsize = blockSize * availableBlocks
                val totalsizeStr = Formatter.formatFileSize(AppUtils.getApp(), totalsize)
                val availsizeStr = Formatter.formatFileSize(AppUtils.getApp(), availsize)
                return "SDCard信息:totalsize($totalsizeStr),availsize($availsizeStr)"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    val dataInfo: String
        /**
         * 手机内存
         */
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        get() {
            try {
                val path = Environment.getDataDirectory()
                val s = StatFs(path.path)
                val availableBlocks = s.availableBlocksLong
                val blockCount = s.blockCountLong
                val blockSize = s.blockSizeLong
                val totalsize = blockSize * blockCount
                val availsize = blockSize * availableBlocks
                val totalsizeStr = Formatter.formatFileSize(AppUtils.getApp(), totalsize)
                val availsizeStr = Formatter.formatFileSize(AppUtils.getApp(), availsize)
                return "手机内存信息:totalsize($totalsizeStr),availsize($availsizeStr)"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
}