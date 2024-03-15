package com.jd.core.view

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout

import com.jd.core.R

import java.io.File
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class BannerPager : FrameLayout, ViewPager.OnPageChangeListener {

    //上下文
    private var mContext: Context? = null
    //轮播页
    private var pager: BannerView? = null
    //轮播数据
    /**
     * 获取数据适配器
     *
     * @return
     */
    /**
     * 设置数据适配器
     *
     */
    //指示器
    //指示器样式
    //添加指示器到容器
    var adapter: BannerAdapter<*>? = null
        set(adapter) {
            adapter!!.setOnPageClickListener(listener)
            adapter.isLoop = isLoop
            pager!!.adapter = adapter
            field = adapter
            pager!!.addOnPageChangeListener(this)
            for (i in 0 until (adapter?.count ?: 0)) {
                val indicatorImageParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                if (indicatorMargin != 0f) {
                    indicatorImageParams.setMargins(indicatorMargin.toInt(), indicatorMargin.toInt(), indicatorMargin.toInt(), indicatorMargin.toInt())
                } else {
                    indicatorImageParams.setMargins(indicatorMarginLeft.toInt(), indicatorMarginTop.toInt(), indicatorMarginRight.toInt(), indicatorMarginBottom.toInt())
                }
                val indicator = ImageView(context)
                indicator.layoutParams = indicatorImageParams
                if (if (isLoop) i == 1 else i == 0) {
                    indicator.setImageResource(indicatorSelectedResource)
                } else {
                    indicator.setImageResource(indicatorUnSelectedResource)
                }
                if (isLoop) {
                    if (i == 0 || i == adapter.count - 1) {
                        indicator.visibility = View.GONE
                    }
                }
                indicatorLayout!!.addView(indicator, indicatorImageParams)
            }
            pager!!.offscreenPageLimit = adapter.count
            pager!!.currentItem = if (isLoop) 1 else 0
            if (isAutoPlay) {
                play()
            }
        }
    //指示器布局
    private var indicatorLayout: LinearLayout? = null
    //轮播控制
    private var handler: PlayHandler? = null
    //选中图资源
    private var indicatorSelectedResource = R.drawable.android_indicator_selected
    //未选中图资源
    private var indicatorUnSelectedResource = R.drawable.android_indicator_unselected
    //指示器布局间距
    private var indicatorLayoutMargin = 0f
    //指示器布局左间距
    private var indicatorLayoutMarginLeft = dpToPx(10f)
    //指示器布局上间距
    private var indicatorLayoutMarginTop = dpToPx(10f)
    //指示器布局右间距
    private var indicatorLayoutMarginRight = dpToPx(10f)
    //指示器布局下间距
    private var indicatorLayoutMarginBottom = dpToPx(10f)
    //指示器间距
    private var indicatorMargin = 0f
    //指示器左间距
    private var indicatorMarginLeft = dpToPx(5f)
    //指示器上间距
    private var indicatorMarginTop = 0f
    //指示器右间距
    private var indicatorMarginRight = dpToPx(5f)
    //指示器下间距
    private var indicatorMarginBottom = 0f
    //指示器位置
    private var indicatorGravity = Gravity.BOTTOM or Gravity.CENTER
    //是否自动播放
    /**
     * 是否自动播放
     *
     * @return
     */
    /**
     * 设置自动播放
     *
     * @param autoPlay
     */
    var isAutoPlay = false
    /**
     * 设置是否循环
     *
     * @param loop
     */
    var isLoop = true
    //轮播时间
    private var duration = 3 * 1000

    private var onPageChangeListener: OnPageChangeListener? = null


    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()
    private var moveX: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()

    private var listener: OnPageClickListener? = null

    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        this.mContext = context
        handler = PlayHandler()
        pager = BannerView(context)
        indicatorLayout = LinearLayout(context)
        //自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerPager)
        indicatorSelectedResource = typedArray.getResourceId(R.styleable.BannerPager_indicatorSelected, indicatorSelectedResource)
        indicatorUnSelectedResource = typedArray.getResourceId(R.styleable.BannerPager_indicatorUnSelected, indicatorUnSelectedResource)
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerPager_isAutoPlay, isAutoPlay)
        isLoop = typedArray.getBoolean(R.styleable.BannerPager_isLoop, isLoop)
        duration = typedArray.getInt(R.styleable.BannerPager_duration, duration)
        indicatorGravity = typedArray.getInt(R.styleable.BannerPager_indicatorGravity, indicatorGravity)
        indicatorLayoutMargin = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMargin, indicatorLayoutMargin)
        indicatorLayoutMarginLeft = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginLeft, indicatorLayoutMarginLeft)
        indicatorLayoutMarginTop = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginTop, indicatorLayoutMarginTop)
        indicatorLayoutMarginRight = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginRight, indicatorLayoutMarginRight)
        indicatorLayoutMarginBottom = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginBottom, indicatorLayoutMarginBottom)
        indicatorMargin = typedArray.getDimension(R.styleable.BannerPager_indicatorMargin, indicatorMargin)
        indicatorMarginLeft = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginLeft, indicatorMarginLeft)
        indicatorMarginTop = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginTop, indicatorMarginTop)
        indicatorMarginRight = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginRight, indicatorMarginRight)
        indicatorMarginBottom = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginBottom, indicatorMarginBottom)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        //图片
        val pagerParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        pager!!.layoutParams = pagerParams
        addView(pager)
        //指示器容器
        val indicatorLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        indicatorLayoutParams.gravity = indicatorGravity
        if (indicatorLayoutMargin != 0f) {
            indicatorLayoutParams.leftMargin = indicatorLayoutMargin.toInt()
            indicatorLayoutParams.topMargin = indicatorLayoutMargin.toInt()
            indicatorLayoutParams.rightMargin = indicatorLayoutMargin.toInt()
            indicatorLayoutParams.bottomMargin = indicatorLayoutMargin.toInt()
        } else {
            indicatorLayoutParams.leftMargin = indicatorLayoutMarginLeft.toInt()
            indicatorLayoutParams.topMargin = indicatorLayoutMarginTop.toInt()
            indicatorLayoutParams.rightMargin = indicatorLayoutMarginRight.toInt()
            indicatorLayoutParams.bottomMargin = indicatorLayoutMarginBottom.toInt()
        }
        indicatorLayout!!.layoutParams = indicatorLayoutParams
        indicatorLayout!!.orientation = LinearLayout.HORIZONTAL
        addView(indicatorLayout)
        super.onFinishInflate()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moveY = 0f
                moveX = moveY
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                moveX += event.x - downX
                moveY += event.y - downY
                if (Math.abs(moveY) - Math.abs(moveX) > 0) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moveY = 0f
                moveX = moveY
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                moveX += event.x - downX
                moveY += event.y - downY
                if (Math.abs(moveY) - Math.abs(moveX) > 0) {
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun addOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //指示器位置
        if (isLoop) {
            if (pager!!.currentItem == 0) {
                setCurrentIndicator(this.adapter!!.count - 2)
            }
            if (pager!!.currentItem == this.adapter!!.count - 1) {
                setCurrentIndicator(1)
            }
        }
        if (onPageChangeListener != null) {
            onPageChangeListener!!.onPageScrolled(if (isLoop) position - 1 else position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        setCurrentIndicator(position)
        if (onPageChangeListener != null) {
            onPageChangeListener!!.onPageSelected(if (isLoop) position - 1 else position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {

        }
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            stop()
        }
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (isAutoPlay) {
                play()
            }
            //图片位置
            if (isLoop) {
                if (pager!!.currentItem == 0) {
                    pager!!.setCurrentItem(this.adapter!!.count - 2, false)
                }
                if (pager!!.currentItem == this.adapter!!.count - 1) {
                    pager!!.setCurrentItem(1, false)
                }
            }
        }
        if (onPageChangeListener != null) {
            onPageChangeListener!!.onPageScrollStateChanged(state)
        }
    }

    /**
     * 设置当前指示器位置
     *
     * @param position
     */
    private fun setCurrentIndicator(position: Int) {
        if (this.adapter == null) {
            return
        }
        for (i in 0 until this.adapter!!.count) {
            val indicator = indicatorLayout!!.getChildAt(i) as ImageView
            if (i == position) {
                indicator.setImageResource(indicatorSelectedResource)
            } else {
                indicator.setImageResource(indicatorUnSelectedResource)
            }
        }
    }

    /**
     * 设置位置
     * 需要注意的是在setAdapter之后设置位置才行
     *
     * @param position
     */
    fun setPosition(position: Int) {
        if (this.adapter == null) {
            Exception("The setPosition() method should be after the setAdapter() method.")
            return
        }
        if (pager == null) {
            NullPointerException("BannerPager pager is null,you can't set position.")
            return
        }
        pager!!.currentItem = position + 1
    }

    /**
     * 播放控制器
     */
    private inner class PlayHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            pager!!.currentItem = pager!!.currentItem + 1
            play()
        }
    }

    /**
     * 播放跳转
     */
    fun play() {
        if (handler != null && !handler!!.hasMessages(1)) {
            handler!!.sendEmptyMessageDelayed(1, duration.toLong())
        }
    }

    /**
     * 停止跳转
     */
    fun stop() {
        if (handler != null) {
            handler!!.removeMessages(1)
        }
    }

    /**
     * 销毁 - 防止内容泄露
     */
    fun destory() {
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    /**
     * 设置页面转变动画 - 同ViewPager
     *
     * @param reverseDrawingOrder
     * @param transformer
     */
    fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: ViewPager.PageTransformer) {
        pager!!.setPageTransformer(reverseDrawingOrder, transformer)
    }

    /**
     * 设置页面转变动画 - 同ViewPager
     *
     * @param reverseDrawingOrder
     * @param transformer
     * @param pageLayerType
     */
    fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: ViewPager.PageTransformer, pageLayerType: Int) {
        pager!!.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType)
    }

    interface OnPageClickListener {
        fun onPageClick(position: Int)
    }

    /**
     * 设置页面改变监听
     *
     * @param listener
     */
    fun setOnPageClickListener(listener: OnPageClickListener) {
        this.listener = listener
        if (this.adapter != null && this.adapter is BannerAdapter<*>) {
            (this.adapter as BannerAdapter<*>).setOnPageClickListener(listener)
        }
    }


    /**
     * 设置选中的指示器的图片
     *
     * @param indicatorSelectedResource
     */
    fun setSelectedIndicatorResource(indicatorSelectedResource: Int) {
        this.indicatorSelectedResource = indicatorSelectedResource
    }

    /**
     * 设置未选中的指示器的图片
     *
     * @param indicatorUnSelectedResource
     */
    fun setUnindicatorSelectedResource(indicatorUnSelectedResource: Int) {
        this.indicatorUnSelectedResource = indicatorUnSelectedResource
    }

    /**
     * 设置指示器布局的外间距
     *
     * @param indicatorLayoutMargin
     */
    fun setIndicatorLayoutMargin(indicatorLayoutMargin: Float) {
        this.indicatorLayoutMargin = dpToPx(indicatorLayoutMargin)
    }

    /**
     * 设置指示器布局的左边间距
     *
     * @param indicatorLayoutMarginLeft
     */
    fun setIndicatorLayoutMarginLeft(indicatorLayoutMarginLeft: Float) {
        this.indicatorLayoutMarginLeft = dpToPx(indicatorLayoutMarginLeft)
    }

    /**
     * 设置指示器布局的上边间距
     *
     * @param indicatorLayoutMarginTop
     */
    fun setIndicatorLayoutMarginTop(indicatorLayoutMarginTop: Float) {
        this.indicatorLayoutMarginTop = dpToPx(indicatorLayoutMarginTop)
    }

    /**
     * 设置指示器布局的右边间距
     *
     * @param indicatorLayoutMarginRight
     */
    fun setIndicatorLayoutMarginRight(indicatorLayoutMarginRight: Float) {
        this.indicatorLayoutMarginRight = dpToPx(indicatorLayoutMarginRight)
    }

    /**
     * 设置指示器布局的下边间距
     *
     * @param indicatorLayoutMarginBottom
     */
    fun setIndicatorLayoutMarginBottom(indicatorLayoutMarginBottom: Float) {
        this.indicatorLayoutMarginBottom = dpToPx(indicatorLayoutMarginBottom)
    }

    /**
     * 设置指示器之间的间距
     *
     * @param indicatorMargin
     */
    fun setIndicatorMargin(indicatorMargin: Float) {
        this.indicatorMargin = dpToPx(indicatorMargin)
    }

    /**
     * 设置指示器之间的间距
     *
     * @param indicatorMarginLeft
     */
    fun setIndicatorMarginLeft(indicatorMarginLeft: Float) {
        this.indicatorMarginLeft = dpToPx(indicatorMarginLeft)
    }

    /**
     * 设置指示器之间的上间距
     *
     * @param indicatorMarginTop
     */
    fun setIndicatorMarginTop(indicatorMarginTop: Float) {
        this.indicatorMarginTop = indicatorMarginTop
    }

    /**
     * 设置指示器之间的右间距
     *
     * @param indicatorMarginRight
     */
    fun setIndicatorMarginRight(indicatorMarginRight: Float) {
        this.indicatorMarginRight = indicatorMarginRight
    }

    /**
     * 设置指示器之间的下间距
     *
     * @param indicatorMarginBottom
     */
    fun setIndicatorMarginBottom(indicatorMarginBottom: Float) {
        this.indicatorMarginBottom = indicatorMarginBottom
    }

    /**
     * 设置指示器的位置
     *
     * @param gravity
     */
    fun setIndicatorGravity(gravity: Int) {
        this.indicatorGravity = gravity
    }

    /**
     * 获取Pager对象
     *
     * @return
     */
    fun getPager(): ViewPager? {
        return pager
    }

    /**
     * 设置轮播时间
     *
     * @param duration
     */
    fun setDuration(duration: Int) {
        this.duration = duration
    }

    interface OnPageChangeListener {
        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position             Position index of the first page currently being displayed.
         * Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        fun onPageSelected(position: Int)

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see ViewPager.SCROLL_STATE_IDLE
         *
         * @see ViewPager.SCROLL_STATE_DRAGGING
         *
         * @see ViewPager.SCROLL_STATE_SETTLING
         */
        fun onPageScrollStateChanged(state: Int)
    }


    /*************************************  */
    /*************************************  */
    /*************************  BannerView  */
    /*************************************  */
    /*************************************  */

    class BannerView : ViewPager {

        private var lastX = 0f
        private var lastY = 0f

        constructor(context: Context) : super(context) {}

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()
            var dealtX = 0
            var dealtY = 0
            when (ev.action) {
                MotionEvent.ACTION_DOWN ->
                    // 保证子View能够接收到Action_move事件
                    parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_MOVE -> {
                    dealtX += Math.abs(x - lastX).toInt()
                    dealtY += Math.abs(y - lastY).toInt()
                    // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                    if (dealtX >= dealtY) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                    lastX = x.toFloat()
                    lastY = y.toFloat()
                }
                MotionEvent.ACTION_CANCEL -> {
                }
                MotionEvent.ACTION_UP -> {
                }
            }
            return super.dispatchTouchEvent(ev)
        }

    }

    /*************************  BannerAdapter  */

    open class BannerAdapter<T>(val context: Context, private val data: MutableList<T>?) : PagerAdapter() {
        lateinit var convertView: View
            private set
        var position: Int = 0
            private set
        var isLoop = true
            set(isLoop) {
                field = isLoop
                if (count > 0 && isLoop) {
                    this.data!!.add(0, data[count - 1])
                    this.data.add(data[1])
                }
            }
        var listener: BannerPager.OnPageClickListener? = null
            private set
        private var onMeasureListener: OnMeasureConvertViewListener? = null

        fun getData(): List<T>? {
            return data
        }

        fun getItem(position: Int): T {
            return data!![position]
        }

        fun setOnPageClickListener(listener: BannerPager.OnPageClickListener?) {
            this.listener = listener
        }

        fun setOnMeasureConvertViewListener(onMeasureListener: OnMeasureConvertViewListener) {
            this.onMeasureListener = onMeasureListener
        }

        interface OnMeasureConvertViewListener {
            fun onMeasureConvertView(adapter: BannerAdapter<*>, convertView: View?)
        }

        override fun getCount(): Int {
            return data?.size ?: 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            this.position = position
            convertView = getView(LayoutInflater.from(context), position, null, container)
            container.addView(convertView)
            if (onMeasureListener != null) {
                onMeasureListener!!.onMeasureConvertView(this, convertView)
            }
            return convertView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            this.position = position
            container.removeView(`object` as View)
        }

        protected fun getView(inflater: LayoutInflater, position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder<T>
            if (convertView == null) {
                holder = ViewHolder()
                convertView = ImageView(context)
                holder.target = convertView
                holder.target!!.scaleType = ImageView.ScaleType.FIT_XY
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder<T>
            }
            val `object` = data!![position]
            if (`object` is Int) {
                holder.target!!.setImageResource(`object` as Int)
            }
            if (`object` is File) {
                val file = `object` as File
                holder.target!!.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            }
            if (`object` is String) {
                holder.target!!.tag = null
                holder.target!!.setImageBitmap(BitmapFactory.decodeFile(`object` as String))
            }
            if (`object` is Uri) {
                holder.target!!.setImageURI(`object` as Uri)
            }
            convertView = holder.target
            convertView!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (listener != null) {
                        listener!!.onPageClick(if (this@BannerAdapter.isLoop) position - 1 else position)
                    }
                }
            })
            return convertView
        }

        inner class ViewHolder<T> {
            public var target: ImageView? = null
        }


    }

    companion object {

        fun dpToPx(dp: Float): Float {
            return dp * screenDensity
        }

        val screenDensity: Float
            get() = Resources.getSystem().displayMetrics.density
    }

}
