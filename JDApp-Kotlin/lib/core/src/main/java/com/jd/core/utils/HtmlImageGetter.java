package com.jd.core.utils;

import android.graphics.drawable.Drawable;
import android.text.Html;

public class HtmlImageGetter implements Html.ImageGetter {
    @Override
    public Drawable getDrawable(String source) {
        int id = Integer.parseInt(source);
        Drawable d = AppUtils.getApp().getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d .getIntrinsicHeight());
        return d;
    }
}
