package com.jd.core.utils;

import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonSyntaxException;

public class ToastUtils {

    /**
     * Show the sToast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(@Nullable CharSequence text) {
        if (text == null) {
            text = "";
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_SHORT);
        toast.show();
    }



    public static void showLong(@Nullable CharSequence text) {
        if (text == null) {
            text = "";
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showTopLong(@Nullable CharSequence text) {
        if (text == null) {
            text = "";
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    public static void showCenterLong(@Nullable CharSequence text) {
        if (text == null) {
            text = "";
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void showShort(@Nullable Exception e) {
        if (e == null) {
           return;
        }
        String text = handleExceptionMessage(e);
        if (text == null) {
            return;
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLong(@Nullable Exception e) {
        if (e == null) {
          return;
        }
        String text = handleExceptionMessage(e);
        if (text == null) {
            return;
        }
        Toast toast = Toast.makeText(AppUtils.getApp(),text,Toast.LENGTH_LONG);
        toast.show();
    }

    @Nullable
    private static String handleExceptionMessage(Exception e){
        String text = e.getMessage();
        if (e instanceof JsonSyntaxException) {
            text = "数据格式异常";
        } else if (e.getClass().getName().contains("JobCancellationException")) {
            return null;
        }
        return text;
    }
}
