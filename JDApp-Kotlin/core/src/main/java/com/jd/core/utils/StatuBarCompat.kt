package com.jd.core.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Field
import java.lang.reflect.Method

/*
 * 沉浸式状态栏 兼容4.4
 */
object StatuBarCompat {
    private val TAG = "StatuBarCompat"

    private var osType: String? = null
    private val MIUI_OS = "miui"
    private val FLY_OS = "fly"
    private val ANDROID_OS = "6.0+"

    private val COLOR_DEFAULT = Color.TRANSPARENT

    /**
     * 判断miui系统
     *
     * @return
     */
    val isMIUI: Boolean
        get() {
            if (MIUI_OS == osType || !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name")) && "V9" != getSystemProperty("ro.miui.ui.version.name")) {
                osType = MIUI_OS
                return true
            }
            return false
        }

    /**
     * 判断魅族手机
     *
     * @return
     */
    val isFlyme: Boolean
        get() {
            if (FLY_OS == osType) return true
            try {
                val method = Build::class.java.getMethod("hasSmartBar")
                if (method != null) {
                    osType = FLY_OS
                    return true
                }
                return false
            } catch (e: Exception) {
                return false
            }

        }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun compat(activity: Activity, statusColor: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = statusColor
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (contentView.findViewById<View>(android.R.id.title) != null) {
                contentView.findViewById<View>(android.R.id.title).setBackgroundColor(statusColor)
            } else {
                val statusBarView = View(activity)
                statusBarView.id = android.R.id.title
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        StatuBarCompat.getStatusBarHeight(activity))
                statusBarView.setBackgroundColor(statusColor)
                contentView.addView(statusBarView, lp)
            }
        }

    }

    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0

        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (var7: Exception) {
            var7.printStackTrace()
        }

        return statusBarHeight
    }


    fun compat(activity: Activity) {
        compat(activity, COLOR_DEFAULT)

    }

    /**
     * 设置状态栏透明
     */
    fun setTranslucentStatus(activity: Activity) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    fun setImmersiveStatusBar(fontIconDark: Boolean, statusBarColor: Int, activity: Activity) {
        var statusBarColor = statusBarColor
        setTranslucentStatus(activity)
        if (fontIconDark) {
            if (isMIUI) {
                // 小米MIUI
                setStatusBarFontIconDarkForMIUI(true, activity)
            } else if (isFlyme) {
                // 魅族FlymeUI
                setStatusBarFontIconDarkForFlyme(true, activity)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // android6.0+系统
                setStatusBarFontIconDarkForAndroidM(true, activity)
            } else {
                if (statusBarColor == Color.WHITE) {
                    statusBarColor = -0x333334
                }
            }
        }
        compat(activity, statusBarColor)
    }


    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体和图标是否为深色
     */
    internal fun setStatusBarFontIconDarkForAndroidM(dark: Boolean, activity: Activity) {
        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    // 魅族FlymeUI
    private fun setStatusBarFontIconDarkForFlyme(dark: Boolean, activity: Activity) {
        try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //MIUI 设置状态栏
    private fun setStatusBarFontIconDarkForMIUI(dark: Boolean, activity: Activity) {
        try {
            val window = activity.window
            val clazz = activity.window.javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return line
    }

    /**
     * 侧拉专用沉浸式
     *
     * @param activity      有drawlayout页面
     * @param holdSpaceView 与状态栏等高的占位view
     */
    fun setImmersiveStatusBarWithView(fontIconDark: Boolean, activity: Activity, holdSpaceView: View) {
        setImmersiveStatusBarWithView(fontIconDark, activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //            holdSpaceView.setVisibility(View.GONE);
            //            activity.getWindow().setStatusBarColor(statusBarColor);
            setHolderViewHeightEqualsStatuBar(activity, holdSpaceView)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setHolderViewHeightEqualsStatuBar(activity, holdSpaceView)
        }
    }

    /**
     * 标题栏沉在状态栏里面
     *
     * @param fontIconDark
     * @param activity
     */
    fun setImmersiveStatusBarWithView(fontIconDark: Boolean, activity: Activity) {
        setTranslucentStatus(activity)
        if (fontIconDark) {
            if (isMIUI) {
                // 小米MIUI
                setStatusBarFontIconDarkForMIUI(true, activity)
            } else if (isFlyme) {
                // 魅族FlymeUI
                setStatusBarFontIconDarkForFlyme(true, activity)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // android6.0+系统
                setStatusBarFontIconDarkForAndroidM(true, activity)
            }
        }
    }

    fun setHolderViewHeightEqualsStatuBar(context: Context, holdSpaceView: View) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                StatuBarCompat.getStatusBarHeight(context))
        holdSpaceView.layoutParams = layoutParams
    }

    fun setHolderViewHeightEqualsStatuBar(context: Context, holdSpaceView: View, color: Int) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                StatuBarCompat.getStatusBarHeight(context))
        holdSpaceView.layoutParams = layoutParams
        holdSpaceView.setBackgroundColor(color)
    }

    fun setHolderViewWithColor(context: Context, holdSpaceView: View) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                StatuBarCompat.getStatusBarHeight(context))
        holdSpaceView.layoutParams = layoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            holdSpaceView.setBackgroundColor(Color.parseColor("#ffcccccc"))
        }
    }

}
