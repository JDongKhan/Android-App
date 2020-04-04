package com.jd.core.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * 转换工具
 */
object ConvertUtils {

    // 根据手机的分辨率将dp的单位转成px(像素)
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    // 根据手机的分辨率将px(像素)的单位转成dp
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    // 将px值转换为sp值
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    // 将sp值转换为px值
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    // 屏幕宽度（像素）
    fun getWindowWidth(context: Activity): Int {
        val metric = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    // 屏幕高度（像素）
    fun getWindowHeight(activity: Activity): Int {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

    /**
     * 获取屏幕横向(宽度)分辨率
     */
    fun getResolutionX(context: Context): Int {
        val mDisplayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(mDisplayMetrics)

        return mDisplayMetrics.widthPixels
    }

    /**
     * 获取屏幕纵向(高度)分辨率
     */
    fun getResolutionY(context: Context): Int {
        val mDisplayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(mDisplayMetrics)

        return mDisplayMetrics.heightPixels
    }
}
