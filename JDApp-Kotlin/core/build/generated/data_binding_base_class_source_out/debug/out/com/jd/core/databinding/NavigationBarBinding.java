// Generated by view binder compiler. Do not edit!
package com.jd.core.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.jd.core.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NavigationBarBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LinearLayout navigationBack;

  @NonNull
  public final TextView navigationBackText;

  @NonNull
  public final TextView navigationDetail;

  @NonNull
  public final ImageView navigationDetailImg;

  @NonNull
  public final LinearLayout navigationDetailLayout;

  @NonNull
  public final LinearLayout navigationRightView;

  @NonNull
  public final TextView navigationTitle;

  @NonNull
  public final LinearLayout topHead;

  private NavigationBarBinding(@NonNull RelativeLayout rootView,
      @NonNull LinearLayout navigationBack, @NonNull TextView navigationBackText,
      @NonNull TextView navigationDetail, @NonNull ImageView navigationDetailImg,
      @NonNull LinearLayout navigationDetailLayout, @NonNull LinearLayout navigationRightView,
      @NonNull TextView navigationTitle, @NonNull LinearLayout topHead) {
    this.rootView = rootView;
    this.navigationBack = navigationBack;
    this.navigationBackText = navigationBackText;
    this.navigationDetail = navigationDetail;
    this.navigationDetailImg = navigationDetailImg;
    this.navigationDetailLayout = navigationDetailLayout;
    this.navigationRightView = navigationRightView;
    this.navigationTitle = navigationTitle;
    this.topHead = topHead;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NavigationBarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NavigationBarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.navigation_bar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NavigationBarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.navigation_back;
      LinearLayout navigationBack = ViewBindings.findChildViewById(rootView, id);
      if (navigationBack == null) {
        break missingId;
      }

      id = R.id.navigation_back_text;
      TextView navigationBackText = ViewBindings.findChildViewById(rootView, id);
      if (navigationBackText == null) {
        break missingId;
      }

      id = R.id.navigation_detail;
      TextView navigationDetail = ViewBindings.findChildViewById(rootView, id);
      if (navigationDetail == null) {
        break missingId;
      }

      id = R.id.navigation_detail_img;
      ImageView navigationDetailImg = ViewBindings.findChildViewById(rootView, id);
      if (navigationDetailImg == null) {
        break missingId;
      }

      id = R.id.navigation_detail_layout;
      LinearLayout navigationDetailLayout = ViewBindings.findChildViewById(rootView, id);
      if (navigationDetailLayout == null) {
        break missingId;
      }

      id = R.id.navigation_rightView;
      LinearLayout navigationRightView = ViewBindings.findChildViewById(rootView, id);
      if (navigationRightView == null) {
        break missingId;
      }

      id = R.id.navigation_title;
      TextView navigationTitle = ViewBindings.findChildViewById(rootView, id);
      if (navigationTitle == null) {
        break missingId;
      }

      id = R.id.top_head;
      LinearLayout topHead = ViewBindings.findChildViewById(rootView, id);
      if (topHead == null) {
        break missingId;
      }

      return new NavigationBarBinding((RelativeLayout) rootView, navigationBack, navigationBackText,
          navigationDetail, navigationDetailImg, navigationDetailLayout, navigationRightView,
          navigationTitle, topHead);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
