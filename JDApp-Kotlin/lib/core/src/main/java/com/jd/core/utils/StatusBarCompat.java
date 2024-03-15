package com.jd.core.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class StatusBarCompat {

    //Get alpha color
    static int calculateStatusBarColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * set statusBarColor
     * @param statusColor color
     * @param alpha       0 - 255
     */
    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int statusColor, int alpha) {
        setStatusBarColor(activity, calculateStatusBarColor(statusColor, alpha));
    }

    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompatLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarCompatKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    public static void translucentStatusBar(@NonNull Activity activity) {
        translucentStatusBar(activity, false);
    }

    public static void setStatusBarBackGround(@NonNull Activity activity, Drawable statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompatLollipop.setStatusBarBackground(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarCompatKitKat.setStatusBarBackground(activity, statusColor);
        }
    }

    /**
     * change to full screen mode
     * @param hideStatusBarBackground hide status bar alpha Background when SDK > 21, true if hide it
     */
    public static void translucentStatusBar(@NonNull Activity activity, boolean hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompatLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarCompatKitKat.translucentStatusBar(activity);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(@NonNull Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
                                                             Toolbar toolbar, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompatLollipop.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarCompatKitKat.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }

    //https://blog.csdn.net/baidu_34816141/article/details/109616639
    //https://blog.csdn.net/LVOEWZDG/article/details/119393517

    //隐藏虚拟按键和状态栏全屏
    /**
     * 1.  View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
     * 2.  View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
     * 3.  View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
     * 4.  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，
     * 5.  但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
     * 6.  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * 7.  View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * 8.  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。
     * 9.  有些手机会用虚拟按键来代替物理按键。
     * 10. View.SYSTEM_UI_FLAG_LOW_PROFILE：
     * 11. 状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
     *
     */
    public static void fullScreen(View view) {
      if(Build.VERSION.SDK_INT >= 19) {
//         int uiOptions =
//                 //全屏显示时保证尺寸不变
//                  View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                  //布局位于状态栏下方
//                  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                  //全屏
//                  View.SYSTEM_UI_FLAG_FULLSCREEN |
//                 //隐藏导航栏
//                 View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                          //Activity全屏显示，状态栏显示在Activity页面上面
//                 |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                          //配合FullScreen和navigation组合使用，达到效果是拉出导航栏后显示一会就消失
//                 |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            if (null != view) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                view.setSystemUiVisibility(uiOptions);
                final int uiOptions2 = uiOptions;
                view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            view.setSystemUiVisibility(uiOptions2);
                        } else {

                        }
                    }
                });
            }
        } else if(Build.VERSION.SDK_INT > 11) { // lower api
          view.setSystemUiVisibility(View.GONE);
      }
    }

}
