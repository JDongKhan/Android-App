// Generated by view binder compiler. Do not edit!
package com.jd.core.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.jd.core.R;
import com.jd.core.view.NavigationBar;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BaseLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RelativeLayout baseContent;

  @NonNull
  public final NavigationBar navigationBar;

  private BaseLayoutBinding(@NonNull LinearLayout rootView, @NonNull RelativeLayout baseContent,
      @NonNull NavigationBar navigationBar) {
    this.rootView = rootView;
    this.baseContent = baseContent;
    this.navigationBar = navigationBar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BaseLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BaseLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.base_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BaseLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.base_content;
      RelativeLayout baseContent = ViewBindings.findChildViewById(rootView, id);
      if (baseContent == null) {
        break missingId;
      }

      id = R.id.navigation_bar;
      NavigationBar navigationBar = ViewBindings.findChildViewById(rootView, id);
      if (navigationBar == null) {
        break missingId;
      }

      return new BaseLayoutBinding((LinearLayout) rootView, baseContent, navigationBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
