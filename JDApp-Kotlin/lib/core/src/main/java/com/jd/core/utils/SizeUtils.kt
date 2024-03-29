package com.jd.core.utils

import android.content.Context
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup

class SizeUtils private constructor() {
    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////
    interface onGetSizeListener {
        fun onGetSize(view: View?)
    }

    companion object {
        /**
         * Value of dp to value of px.
         *
         * @param dpValue The value of dp.
         * @return value of px
         */
       @JvmStatic
        fun dp2px(dpValue: Float): Int {
            return dp2px(AppUtils.getApp(), dpValue)
        }

        /**
         * Value of dp to value of px.
         *
         * @param context The context.
         * @param dpValue The value of dp.
         * @return value of px
         */
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * Value of px to value of dp.
         *
         * @param pxValue The value of px.
         * @return value of dp
         */
        fun px2dp(pxValue: Float): Int {
            return px2dp(AppUtils.getApp(), pxValue)
        }

        /**
         * Value of px to value of dp.
         *
         * @param context The context.
         * @param pxValue The value of px.
         * @return value of dp
         */
        fun px2dp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * Value of sp to value of px.
         *
         * @param spValue The value of sp.
         * @return value of px
         */
        fun sp2px(spValue: Float): Int {
            return sp2px(AppUtils.getApp(), spValue)
        }

        /**
         * Value of sp to value of px.
         *
         * @param context The context.
         * @param spValue The value of sp.
         * @return value of px
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * Value of px to value of sp.
         *
         * @param pxValue The value of px.
         * @return value of sp
         */
        fun px2sp(pxValue: Float): Int {
            return px2sp(AppUtils.getApp(), pxValue)
        }

        /**
         * Value of px to value of sp.
         *
         * @param context The context.
         * @param pxValue The value of px.
         * @return value of sp
         */
        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * Converts an unpacked complex data value holding a dimension to its final floating
         * point value. The two parameters <var>unit</var> and <var>value</var>
         * are as in [TypedValue.TYPE_DIMENSION].
         *
         * @param value The value to apply the unit to.
         * @param unit  The unit to convert from.
         * @return The complex floating point value multiplied by the appropriate
         * metrics depending on its unit.
         */
        fun applyDimension(value: Float, unit: Int): Float {
            return applyDimension(AppUtils.getApp(), value, unit)
        }

        /**
         * Converts an unpacked complex data value holding a dimension to its final floating
         * point value. The two parameters <var>unit</var> and <var>value</var>
         * are as in [TypedValue.TYPE_DIMENSION].
         *
         * @param context The context.
         * @param value   The value to apply the unit to.
         * @param unit    The unit to convert from.
         * @return The complex floating point value multiplied by the appropriate
         * metrics depending on its unit.
         */
        fun applyDimension(context: Context, value: Float, unit: Int): Float {
            val metrics = context.resources.displayMetrics
            when (unit) {
                TypedValue.COMPLEX_UNIT_PX -> return value
                TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
                TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
                TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
                TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
                TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
            }
            return 0.0f
        }

        /**
         * Force get the size of view.
         *
         * e.g.
         * <pre>
         * SizeUtils.forceGetViewSize(view, new SizeUtils.onGetSizeListener() {
         * Override
         * public void onGetSize(final View view) {
         * view.getWidth();
         * }
         * });
        </pre> *
         *
         * @param view     The view.
         * @param listener The get size listener.
         */
        fun forceGetViewSize(view: View, listener: onGetSizeListener?) {
            view.post { listener?.onGetSize(view) }
        }

        /**
         * Return the width of view.
         *
         * @param view The view.
         * @return the width of view
         */
        fun getMeasuredWidth(view: View): Int {
            return measureView(view)[0]
        }

        /**
         * Return the height of view.
         *
         * @param view The view.
         * @return the height of view
         */
        fun getMeasuredHeight(view: View): Int {
            return measureView(view)[1]
        }

        /**
         * Measure the view.
         *
         * @param view The view.
         * @return arr[0]: view's width, arr[1]: view's height
         */
        fun measureView(view: View): IntArray {
            var lp = view.layoutParams
            if (lp == null) {
                lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width)
            val lpHeight = lp.height
            val heightSpec: Int
            heightSpec = if (lpHeight > 0) {
                View.MeasureSpec.makeMeasureSpec(
                    lpHeight,
                    View.MeasureSpec.EXACTLY
                )
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }
            view.measure(widthSpec, heightSpec)
            return intArrayOf(view.measuredWidth, view.measuredHeight)
        }

        /**
         * 获取文本的宽度
         * @param text 文本
         * @param textSize 字体大小
         * @return 文本的宽度
         */
        fun getTextSize(text: String?, textSize: Float): Float {
            val paint = Paint()
            paint.textSize = textSize
            return paint.measureText(text)
        }
    }
}