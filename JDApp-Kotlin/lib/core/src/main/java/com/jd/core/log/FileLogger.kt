package com.jd.core.log

import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 *
 * @author jd
 */
class FileLogger(
    /**
     * 文件路径
     */
    private val folderPath: String,
    /**
     * 日志清理间隔时间
     */
    private val maxTimeMillis: Long
) {
    private val mLocalDateFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
            }
        }
    private val mLocalLogDateFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("MM-dd HH:mm:ss:SS", Locale.CHINESE)
            }
        }

    /**
     * writer.
     */
    private val writer: Writer = Writer()
    private val worker: Worker = Worker()

    /**
     * 构造
     *
     * @param folderPath 文件路径
     */
    init {
        checkLogFolder()
    }

    /**
     * 文件路径校验
     */
    private fun checkLogFolder() {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    fun println(logLevel: Int, tag: String, msg: String) {
        val timeMillis = System.currentTimeMillis()
        if (!worker.isStarted()) {
            worker.start()
        }
        worker.enqueue(LogItem(timeMillis, logLevel, tag, msg))
    }

    /**
     * 打印
     */
    private fun doPrintln(timeMillis: Long, logLevel: Int, tag: String, msg: String) {
        val lastFileName: String ?= writer.lastFileName
        val newFileName = generateFileName(System.currentTimeMillis())
        require(newFileName.trim { it <= ' ' }.isNotEmpty()) { "File name should not be empty." }
        if (newFileName != lastFileName) {
            if (writer.isOpened) {
                writer.close()
            }
            cleanLogFilesIfNecessary()
            if (!writer.open(newFileName)) {
                return
            }
        }
        val flattenedLog = flatten(timeMillis, logLevel, tag, msg).toString()
        writer.appendLog(flattenedLog)
    }

    private fun flatten(timeMillis: Long, logLevel: Int, tag: String, message: String): CharSequence {
        val sdf = mLocalLogDateFormat.get()
        sdf.timeZone = TimeZone.getDefault()
        return (sdf.format(timeMillis)
                + '|' + getShortLevelName(logLevel)
                + '|' + tag
                + '|' + message)
    }

    private fun generateFileName(timestamp: Long): String {
        val sdf = mLocalDateFormat.get()
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp)) + LOGEXT
    }

    /**
     * 定期清除
     */
    private fun cleanLogFilesIfNecessary() {
        val logDir = File(folderPath)
        val files = logDir.listFiles() ?: return
        for (file in files) {
            if (shouldClean(file)) {
                file.delete()
            }
        }
    }

    private fun shouldClean(file: File): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val lastModified = file.lastModified()
        return currentTimeMillis - lastModified > maxTimeMillis
    }

    private class LogItem internal constructor(
        val timeMillis: Long,
        val level: Int,
        val tag: String,
        val msg: String
    )

    /**
     * 日志处理线程
     */
    private inner class Worker : Runnable {
        private val logs: BlockingQueue<LogItem> = LinkedBlockingQueue()

        @Volatile
        private var started = false

        /**
         * 日志入队
         *
         * @param log 日志
         */
        fun enqueue(log: LogItem) {
            try {
                logs.put(log)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        /**
         * 是否已经启动
         *
         * @return true , false
         */
        fun isStarted(): Boolean {
            synchronized(this) { return started }
        }

        /**
         * 启动线程
         */
        fun start() {
            synchronized(this) {
                Thread(this).start()
                started = true
            }
        }

        override fun run() {
            var log: LogItem
            try {
                while (logs.take().also { log = it } != null) {
                    doPrintln(log.timeMillis, log.level, log.tag, log.msg)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                synchronized(this) { started = false }
            }
        }
    }

    /**
     * 日志写入
     */
    private inner class Writer {
        /**
         * 获取最新的文件名
         *
         * @return 文件名
         */
        /**
         * 文件名
         */
        var lastFileName: String? = null
        /**
         * 获取当前日志文件
         *
         * @return 当前日志文件
         */
        /**
         * 当前写入文件
         */
        var file: File? = null
            private set
        private var bufferedWriter: BufferedWriter? = null
        val isOpened: Boolean
            /**
             * 是否已经打开
             *
             * @return true , false
             */
            get() = bufferedWriter != null

        /**
         * 打开文件
         *
         * @param newFileName 文件名
         * @return true , false
         */
        fun open(newFileName: String?): Boolean {
            lastFileName = newFileName
            file = File(folderPath, newFileName)

            // Create log file if not exists.
            if (!file!!.exists()) {
                try {
                    val parent = file!!.parentFile
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs()
                    }
                    file!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    lastFileName = null
                    file = null
                    return false
                }
            }
            try {
                bufferedWriter = BufferedWriter(FileWriter(file, true))
            } catch (e: Exception) {
                e.printStackTrace()
                lastFileName = null
                file = null
                return false
            }
            return true
        }

        /**
         * 关闭当前文件
         *
         * @return true , false
         */
        fun close(): Boolean {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                } finally {
                    bufferedWriter = null
                    lastFileName = null
                    file = null
                }
            }
            return true
        }

        /**
         * 添加日志到文件
         *
         * @param flattenedLog 日志
         */
        fun appendLog(flattenedLog: String?) {
            try {
                bufferedWriter!!.write(flattenedLog)
                bufferedWriter!!.newLine()
                bufferedWriter!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getShortLevelName(logLevel: Int): String {
        val levelName: String
        levelName = when (logLevel) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            else -> if (logLevel < Log.VERBOSE) {
                "V-" + (Log.VERBOSE - logLevel)
            } else {
                "E+" + (logLevel - Log.ERROR)
            }
        }
        return levelName
    }

    companion object {
        private const val LOGEXT = ".txt"
    }
}