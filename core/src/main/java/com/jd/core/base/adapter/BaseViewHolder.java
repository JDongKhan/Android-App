package com.jd.core.base.adapter;

import android.view.View;

public interface BaseViewHolder {

    public int layout_id();

    public void onCreateView(View view);

    public void onStart(Object data);
}
