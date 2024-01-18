package com.jd.app.plugin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks

object DataAnalysis {

    fun collectionActivity(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                collectionTouch(activity)
                if (activity is FragmentActivity) {
                    val fragmentManager = activity.supportFragmentManager
                    collectionFragment(fragmentManager)
                }
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })

    }


    fun collectionFragment(fragmentManager: FragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks() {
            override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentPreAttached(fm, f, context)
            }

            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentAttached(fm, f, context)
            }

            override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentPreCreated(fm, f, savedInstanceState)
            }

            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentCreated(fm, f, savedInstanceState)
            }

            override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState)
            }

            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            }

            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                super.onFragmentStarted(fm, f)
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
            }

            override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                super.onFragmentPaused(fm, f)
            }

            override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                super.onFragmentStopped(fm, f)
            }

            override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
                super.onFragmentSaveInstanceState(fm, f, outState)
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
            }

            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                super.onFragmentDetached(fm, f)
            }
        }, true)

    }

    fun collectionTouch(activity: Activity) {

        //        View rootView = activity.getWindow().getDecorView();
        //        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //            @Override
        //            public void onGlobalLayout() {
        //
        //            }
        //        });

        val callback = activity.window.callback
        val newCallBack = WrapperWindowCallback(activity, callback)
        activity.window.callback = newCallBack
    }

    class WrapperWindowCallback internal constructor(internal var activity: Activity, internal var originalCallback: Window.Callback) : Window.Callback {

        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
            return originalCallback.dispatchKeyEvent(event)
        }

        override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
            return originalCallback.dispatchKeyShortcutEvent(event)
        }

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            //            View view = null;
            //            Context mContext = view.getContext();
            //            int id = view.getId();
            //            String  fillStr = mContext.getResources().getString(id);


            return originalCallback.dispatchTouchEvent(event)
        }

        override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
            return originalCallback.dispatchTrackballEvent(event)
        }

        override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
            return originalCallback.dispatchGenericMotionEvent(event)
        }

        override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
            return originalCallback.dispatchPopulateAccessibilityEvent(event)
        }

        override fun onCreatePanelView(featureId: Int): View? {
            return originalCallback.onCreatePanelView(featureId)
        }

        override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
            return originalCallback.onCreatePanelMenu(featureId, menu)
        }

        override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
            return originalCallback.onPreparePanel(featureId, view, menu)
        }

        override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
            return originalCallback.onMenuOpened(featureId, menu)
        }

        override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
            return originalCallback.onMenuItemSelected(featureId, item)
        }

        override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
            originalCallback.onWindowAttributesChanged(attrs)
        }

        override fun onContentChanged() {
            originalCallback.onContentChanged()
        }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
            originalCallback.onWindowFocusChanged(hasFocus)
        }

        override fun onAttachedToWindow() {
            originalCallback.onAttachedToWindow()
        }

        override fun onDetachedFromWindow() {
            originalCallback.onDetachedFromWindow()
        }

        override fun onPanelClosed(featureId: Int, menu: Menu) {
            originalCallback.onPanelClosed(featureId, menu)
        }

        override fun onSearchRequested(): Boolean {
            return originalCallback.onSearchRequested()
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
            return originalCallback.onSearchRequested(searchEvent)
        }

        override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
            return originalCallback.onWindowStartingActionMode(callback)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? {
            return originalCallback.onWindowStartingActionMode(callback, type)
        }

        override fun onActionModeStarted(mode: ActionMode) {
            originalCallback.onActionModeStarted(mode)
        }

        override fun onActionModeFinished(mode: ActionMode) {
            originalCallback.onActionModeFinished(mode)
        }
    }
}
