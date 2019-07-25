package com.jd.app.plugin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;

public class DataAnalysis {

    public static void collectionActivity(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                collectionTouch(activity);
                if (activity instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                    collectionFragment(fragmentManager);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }


    public static void collectionFragment(FragmentManager fragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentPreAttached(fm, f, context);
            }

            @Override
            public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentAttached(fm, f, context);
            }

            @Override
            public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentPreCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            }

            @Override
            public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStarted(fm, f);
            }

            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
            }

            @Override
            public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentPaused(fm, f);
            }

            @Override
            public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStopped(fm, f);
            }

            @Override
            public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
                super.onFragmentSaveInstanceState(fm, f, outState);
            }

            @Override
            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
            }

            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
            }

            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
            }
        }, true);

    }

    public static void collectionTouch(Activity activity) {

//        View rootView = activity.getWindow().getDecorView();
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//            }
//        });

        Window.Callback callback = activity.getWindow().getCallback();
        WrapperWindowCallback newCallBack = new WrapperWindowCallback(activity,callback);
        activity.getWindow().setCallback(newCallBack);
    }

    public static class WrapperWindowCallback implements Window.Callback {

        Window.Callback originalCallback;
        Activity activity;

        WrapperWindowCallback(Activity activity,Window.Callback originalCallback) {
            this.activity = activity;
            this.originalCallback = originalCallback;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return originalCallback.dispatchKeyEvent(event);
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            return originalCallback.dispatchKeyShortcutEvent(event);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
//            View view = null;
//            Context mContext = view.getContext();
//            int id = view.getId();
//            String  fillStr = mContext.getResources().getString(id);


            return originalCallback.dispatchTouchEvent(event);
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            return originalCallback.dispatchTrackballEvent(event);
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            return originalCallback.dispatchGenericMotionEvent(event);
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            return originalCallback.dispatchPopulateAccessibilityEvent(event);
        }

        @Nullable
        @Override
        public View onCreatePanelView(int featureId) {
            return originalCallback.onCreatePanelView(featureId);
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            return originalCallback.onCreatePanelMenu(featureId,menu);
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            return originalCallback.onPreparePanel(featureId,view,menu);
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            return originalCallback.onMenuOpened(featureId,menu);
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            return originalCallback.onMenuItemSelected(featureId,item);
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
            originalCallback.onWindowAttributesChanged(attrs);
        }

        @Override
        public void onContentChanged() {
            originalCallback.onContentChanged();
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            originalCallback.onWindowFocusChanged(hasFocus);
        }

        @Override
        public void onAttachedToWindow() {
            originalCallback.onAttachedToWindow();
        }

        @Override
        public void onDetachedFromWindow() {
            originalCallback.onDetachedFromWindow();
        }

        @Override
        public void onPanelClosed(int featureId, Menu menu) {
            originalCallback.onPanelClosed(featureId,menu);
        }

        @Override
        public boolean onSearchRequested() {
            return originalCallback.onSearchRequested();
        }

        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return originalCallback.onSearchRequested(searchEvent);
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return originalCallback.onWindowStartingActionMode(callback);
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            return originalCallback.onWindowStartingActionMode(callback,type);
        }

        @Override
        public void onActionModeStarted(ActionMode mode) {
            originalCallback.onActionModeStarted(mode);
        }

        @Override
        public void onActionModeFinished(ActionMode mode) {
            originalCallback.onActionModeFinished(mode);
        }
    }
}
