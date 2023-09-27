// Generated by view binder compiler. Do not edit!
package com.jd.list.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.jd.list.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListActivityMainBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout activityMain;

  @NonNull
  public final Button gridDemoButton;

  @NonNull
  public final Button gridSecondDemoButton;

  @NonNull
  public final Button listDemoButton;

  @NonNull
  public final Button listSecondDemoButton;

  private ListActivityMainBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout activityMain, @NonNull Button gridDemoButton,
      @NonNull Button gridSecondDemoButton, @NonNull Button listDemoButton,
      @NonNull Button listSecondDemoButton) {
    this.rootView = rootView;
    this.activityMain = activityMain;
    this.gridDemoButton = gridDemoButton;
    this.gridSecondDemoButton = gridSecondDemoButton;
    this.listDemoButton = listDemoButton;
    this.listSecondDemoButton = listSecondDemoButton;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      LinearLayout activityMain = (LinearLayout) rootView;

      id = R.id.grid_demo_button;
      Button gridDemoButton = ViewBindings.findChildViewById(rootView, id);
      if (gridDemoButton == null) {
        break missingId;
      }

      id = R.id.grid_second_demo_button;
      Button gridSecondDemoButton = ViewBindings.findChildViewById(rootView, id);
      if (gridSecondDemoButton == null) {
        break missingId;
      }

      id = R.id.list_demo_button;
      Button listDemoButton = ViewBindings.findChildViewById(rootView, id);
      if (listDemoButton == null) {
        break missingId;
      }

      id = R.id.list_second_demo_button;
      Button listSecondDemoButton = ViewBindings.findChildViewById(rootView, id);
      if (listSecondDemoButton == null) {
        break missingId;
      }

      return new ListActivityMainBinding((LinearLayout) rootView, activityMain, gridDemoButton,
          gridSecondDemoButton, listDemoButton, listSecondDemoButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
