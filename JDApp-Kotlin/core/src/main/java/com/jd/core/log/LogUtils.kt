package com.jd.core.log

import android.app.Application
import com.jd.core.BuildConfig
import com.jd.core.utils.ClassUtils
import com.jd.core.utils.StringUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 日志帮助类
 * 名称：LogHelper
 *
 * @author jd
 */
public object LogUtils {
    /**
     * 调试
     */
    const val DEBUG = 1

    /**
     * 信息
     */
    const val INFO = 2

    /**
     * 警告
     */
    const val WARNING = 3

    /**
     * 错误
     */
    const val ERROR = 4
    private const val TAG_DEBUG = "调试"
    private const val TAG_INFO = "信息"
    private const val TAG_WARNING = "警告"
    private const val TAG_ERROR = "错误"
    private const val METHOD_COUNT = 2
    private const val METHOD_OFFSET = 3
    private var isShowLog = false
    private var isUpload = false
    private var fileLog: FileLogger? = null

    /**
     * 机台号
     */
    private var sMachineId = ""

    /**
     * 初始化
     *
     * @param application 应用
     * @param showLog     是否显示日志
     * @param upLoad      是否上传
     */
    fun init(application: Application, showLog: Boolean, upLoad: Boolean) {
        isShowLog = showLog
        isUpload = upLoad
        val path = application.getExternalFilesDir("log")?.absolutePath ?: return
        ///maxTimeMillis 604800000 7天
        fileLog = FileLogger(path, 604800000)
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(METHOD_COUNT)
            .methodOffset(METHOD_OFFSET)
            .tag(ClassUtils.getSimpleName(application.packageName))
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    /**
     * 打印json文件
     *
     * @param json
     */
    fun json(json: String?) {
        if (isShowLog) {
            Logger.json(json)
        }
    }

    /**
     * 设置机台号
     *
     * @param machineId 机台号
     */
    fun setMachineId(machineId: String) {
        sMachineId = machineId
    }

    /**
     * 打印对象
     *
     * @param object
     */
    @JvmStatic
    fun `object`(`object`: Any) {
        if (isShowLog) {
            Logger.d(`object`)
        }
    }


    /**
     * 调试日志
     *
     * @param obj
     * @param msg
     */
    @JvmStatic
    fun d(obj: String, msg: String?) {
        log(DEBUG, obj, msg)
    }


    /**
     * 信息日志
     *
     * @param obj
     * @param msg
     */
    @JvmStatic
    fun i(obj: String, msg: String?) {
        log(INFO, obj, msg)
    }



    /**
     * 警告日志
     *
     * @param obj
     * @param msg
     */
    @JvmStatic
    fun w(obj: String, msg: String?) {
        log(WARNING, obj, msg)
    }

    /**
     * 错误日志
     *
     * @param obj
     * @param msg
     */
    @JvmStatic
    fun e(obj: String, msg: String?) {
        log(ERROR, obj, msg)
    }

    /**
     * 打印信息
     *
     * @param level 日志级别
     * @param tag   标签
     * @param msg   信息
     */
    fun log(level: Int, tag: String, msg: String?) {
        var tag = tag
        if (StringUtils.isEmptyWithBlank(msg)) {
            return
        }
        if (BuildConfig.DEBUG) {
            tag = "smart_$tag"
        }
        // 打印logcat日志
        localLog(level, tag, msg)
        if (fileLog != null && level != DEBUG) {
            fileLog!!.println(level, tag, msg!!)
        }
    }

    /**
     * 打印本地日志
     *
     * @param level
     * @param tag
     * @param msg、
     */
    private fun localLog(level: Int, tag: String, msg: String?) {
        if (!isShowLog) {
            return
        }
        when (level) {
            INFO -> Logger.t(tag).i(msg ?: "")
            WARNING -> Logger.t(tag).w(msg ?: "")
            ERROR -> Logger.t(tag).e(msg ?: "" )
            DEBUG -> Logger.t(tag).d(msg ?: "")
            else -> Logger.t(tag).d(msg ?: "")
        }
    }
}