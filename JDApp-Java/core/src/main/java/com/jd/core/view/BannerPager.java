package com.jd.core.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jd.core.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BannerPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    //上下文
    private Context context;
    //轮播页
    private BannerView pager;
    //轮播数据
    private BannerAdapter adapter;
    //指示器布局
    private LinearLayout indicatorLayout;
    //轮播控制
    private PlayHandler handler;
    //选中图资源
    private int indicatorSelectedResource = R.drawable.android_indicator_selected;
    //未选中图资源
    private int indicatorUnSelectedResource = R.drawable.android_indicator_unselected;
    //指示器布局间距
    private float indicatorLayoutMargin = 0;
    //指示器布局左间距
    private float indicatorLayoutMarginLeft = dpToPx(10);
    //指示器布局上间距
    private float indicatorLayoutMarginTop = dpToPx(10);
    //指示器布局右间距
    private float indicatorLayoutMarginRight = dpToPx(10);
    //指示器布局下间距
    private float indicatorLayoutMarginBottom = dpToPx(10);
    //指示器间距
    private float indicatorMargin = 0;
    //指示器左间距
    private float indicatorMarginLeft = dpToPx(5);
    //指示器上间距
    private float indicatorMarginTop = 0;
    //指示器右间距
    private float indicatorMarginRight = dpToPx(5);
    //指示器下间距
    private float indicatorMarginBottom = 0;
    //指示器位置
    private int indicatorGravity = Gravity.BOTTOM | Gravity.CENTER;
    //是否自动播放
    private boolean isAutoPlay = false;
    private boolean isLoop = true;
    //轮播时间
    private int duration = 3 * 1000;

    private OnPageChangeListener onPageChangeListener;

    public BannerPager(@NonNull Context context) {
        super(context);
        initAttrs(context, null);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        this.context = context;
        handler = new PlayHandler();
        pager = new BannerView(context);
        indicatorLayout = new LinearLayout(context);
        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerPager);
        indicatorSelectedResource = typedArray.getResourceId(R.styleable.BannerPager_indicatorSelected, indicatorSelectedResource);
        indicatorUnSelectedResource = typedArray.getResourceId(R.styleable.BannerPager_indicatorUnSelected, indicatorUnSelectedResource);
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerPager_isAutoPlay, isAutoPlay);
        isLoop = typedArray.getBoolean(R.styleable.BannerPager_isLoop, isLoop);
        duration = typedArray.getInt(R.styleable.BannerPager_duration, duration);
        indicatorGravity = typedArray.getInt(R.styleable.BannerPager_indicatorGravity, indicatorGravity);
        indicatorLayoutMargin = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMargin, indicatorLayoutMargin);
        indicatorLayoutMarginLeft = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginLeft, indicatorLayoutMarginLeft);
        indicatorLayoutMarginTop = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginTop, indicatorLayoutMarginTop);
        indicatorLayoutMarginRight = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginRight, indicatorLayoutMarginRight);
        indicatorLayoutMarginBottom = typedArray.getDimension(R.styleable.BannerPager_indicatorLayoutMarginBottom, indicatorLayoutMarginBottom);
        indicatorMargin = typedArray.getDimension(R.styleable.BannerPager_indicatorMargin, indicatorMargin);
        indicatorMarginLeft = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginLeft, indicatorMarginLeft);
        indicatorMarginTop = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginTop, indicatorMarginTop);
        indicatorMarginRight = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginRight, indicatorMarginRight);
        indicatorMarginBottom = typedArray.getDimension(R.styleable.BannerPager_indicatorMarginBottom, indicatorMarginBottom);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        //图片
        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pager.setLayoutParams(pagerParams);
        addView(pager);
        //指示器容器
        LayoutParams indicatorLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorLayoutParams.gravity = indicatorGravity;
        if (indicatorLayoutMargin != 0) {
            indicatorLayoutParams.leftMargin = (int) indicatorLayoutMargin;
            indicatorLayoutParams.topMargin = (int) indicatorLayoutMargin;
            indicatorLayoutParams.rightMargin = (int) indicatorLayoutMargin;
            indicatorLayoutParams.bottomMargin = (int) indicatorLayoutMargin;
        } else {
            indicatorLayoutParams.leftMargin = (int) indicatorLayoutMarginLeft;
            indicatorLayoutParams.topMargin = (int) indicatorLayoutMarginTop;
            indicatorLayoutParams.rightMargin = (int) indicatorLayoutMarginRight;
            indicatorLayoutParams.bottomMargin = (int) indicatorLayoutMarginBottom;
        }
        indicatorLayout.setLayoutParams(indicatorLayoutParams);
        indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(indicatorLayout);
        super.onFinishInflate();
    }


    private float downX, downY, moveX, moveY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = moveY = 0F;
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += event.getX() - downX;
                moveY += event.getY() - downY;
                if (Math.abs(moveY) - Math.abs(moveX) > 0) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = moveY = 0F;
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += event.getX() - downX;
                moveY += event.getY() - downY;
                if (Math.abs(moveY) - Math.abs(moveX) > 0) {
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 设置数据适配器
     *
     */
    public void setAdapter(BannerAdapter adapter) {
        adapter.setOnPageClickListener(listener);
        adapter.setLoop(isLoop);
        pager.setAdapter(adapter);
        this.adapter = adapter;
        pager.addOnPageChangeListener(this);
        //指示器
        for (int i = 0; i < (adapter == null ? 0 : adapter.getCount()); i++) {
            LinearLayout.LayoutParams indicatorImageParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (indicatorMargin != 0) {
                indicatorImageParams.setMargins((int) indicatorMargin, (int) indicatorMargin, (int) indicatorMargin, (int) indicatorMargin);
            } else {
                indicatorImageParams.setMargins((int) indicatorMarginLeft, (int) indicatorMarginTop, (int) indicatorMarginRight, (int) indicatorMarginBottom);
            }
            ImageView indicator = new ImageView(context);
            indicator.setLayoutParams(indicatorImageParams);
            //指示器样式
            if (isLoop ? i == 1 : i == 0) {
                indicator.setImageResource(indicatorSelectedResource);
            } else {
                indicator.setImageResource(indicatorUnSelectedResource);
            }
            if (isLoop) {
                if (i == 0 || i == adapter.getCount() - 1) {
                    indicator.setVisibility(GONE);
                }
            }
            //添加指示器到容器
            indicatorLayout.addView(indicator, indicatorImageParams);
        }
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setCurrentItem(isLoop ? 1 : 0);
        if (isAutoPlay) {
            play();
        }
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //指示器位置
        if (isLoop) {
            if (pager.getCurrentItem() == 0) {
                setCurrentIndicator(adapter.getCount() - 2);
            }
            if (pager.getCurrentItem() == adapter.getCount() - 1) {
                setCurrentIndicator(1);
            }
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(isLoop ? position - 1 : position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentIndicator(position);
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(isLoop ? position - 1 : position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {

        }
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            stop();
        }
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (isAutoPlay) {
                play();
            }
            //图片位置
            if (isLoop) {
                if (pager.getCurrentItem() == 0) {
                    pager.setCurrentItem(adapter.getCount() - 2, false);
                }
                if (pager.getCurrentItem() == adapter.getCount() - 1) {
                    pager.setCurrentItem(1, false);
                }
            }
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * 设置当前指示器位置
     *
     * @param position
     */
    private void setCurrentIndicator(int position) {
        if (adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            ImageView indicator = (ImageView) indicatorLayout.getChildAt(i);
            if (i == position) {
                indicator.setImageResource(indicatorSelectedResource);
            } else {
                indicator.setImageResource(indicatorUnSelectedResource);
            }
        }
    }

    /**
     * 设置位置
     * 需要注意的是在setAdapter之后设置位置才行
     *
     * @param position
     */
    public void setPosition(int position) {
        if (adapter == null) {
            new Exception("The setPosition() method should be after the setAdapter() method.");
            return;
        }
        if (pager == null) {
            new NullPointerException("BannerPager pager is null,you can't set position.");
            return;
        }
        pager.setCurrentItem(position + 1);
    }

    /**
     * 播放控制器
     */
    private class PlayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pager.setCurrentItem(pager.getCurrentItem() + 1);
            play();
        }
    }

    /**
     * 播放跳转
     */
    public void play() {
        if (handler != null && !handler.hasMessages(1)) {
            handler.sendEmptyMessageDelayed(1, duration);
        }
    }

    /**
     * 停止跳转
     */
    public void stop() {
        if (handler != null) {
            handler.removeMessages(1);
        }
    }

    /**
     * 销毁 - 防止内容泄露
     */
    public void destory() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 设置页面转变动画 - 同ViewPager
     *
     * @param reverseDrawingOrder
     * @param transformer
     */
    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * 设置页面转变动画 - 同ViewPager
     *
     * @param reverseDrawingOrder
     * @param transformer
     * @param pageLayerType
     */
    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer, int pageLayerType) {
        pager.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
    }

    private OnPageClickListener listener;

    public interface OnPageClickListener {
        void onPageClick(int position);
    }

    /**
     * 设置页面改变监听
     *
     * @param listener
     */
    public void setOnPageClickListener(OnPageClickListener listener) {
        this.listener = listener;
        if (adapter != null && adapter instanceof BannerAdapter) {
            ((BannerAdapter) adapter).setOnPageClickListener(listener);
        }
    }


    /**
     * 设置选中的指示器的图片
     *
     * @param indicatorSelectedResource
     */
    public void setSelectedIndicatorResource(int indicatorSelectedResource) {
        this.indicatorSelectedResource = indicatorSelectedResource;
    }

    /**
     * 设置未选中的指示器的图片
     *
     * @param indicatorUnSelectedResource
     */
    public void setUnindicatorSelectedResource(int indicatorUnSelectedResource) {
        this.indicatorUnSelectedResource = indicatorUnSelectedResource;
    }

    /**
     * 设置指示器布局的外间距
     *
     * @param indicatorLayoutMargin
     */
    public void setIndicatorLayoutMargin(float indicatorLayoutMargin) {
        this.indicatorLayoutMargin = dpToPx(indicatorLayoutMargin);
    }

    /**
     * 设置指示器布局的左边间距
     *
     * @param indicatorLayoutMarginLeft
     */
    public void setIndicatorLayoutMarginLeft(float indicatorLayoutMarginLeft) {
        this.indicatorLayoutMarginLeft = dpToPx(indicatorLayoutMarginLeft);
    }

    /**
     * 设置指示器布局的上边间距
     *
     * @param indicatorLayoutMarginTop
     */
    public void setIndicatorLayoutMarginTop(float indicatorLayoutMarginTop) {
        this.indicatorLayoutMarginTop = dpToPx(indicatorLayoutMarginTop);
    }

    /**
     * 设置指示器布局的右边间距
     *
     * @param indicatorLayoutMarginRight
     */
    public void setIndicatorLayoutMarginRight(float indicatorLayoutMarginRight) {
        this.indicatorLayoutMarginRight = dpToPx(indicatorLayoutMarginRight);
    }

    /**
     * 设置指示器布局的下边间距
     *
     * @param indicatorLayoutMarginBottom
     */
    public void setIndicatorLayoutMarginBottom(float indicatorLayoutMarginBottom) {
        this.indicatorLayoutMarginBottom = dpToPx(indicatorLayoutMarginBottom);
    }

    /**
     * 设置指示器之间的间距
     *
     * @param indicatorMargin
     */
    public void setIndicatorMargin(float indicatorMargin) {
        this.indicatorMargin = dpToPx(indicatorMargin);
    }

    /**
     * 设置指示器之间的间距
     *
     * @param indicatorMarginLeft
     */
    public void setIndicatorMarginLeft(float indicatorMarginLeft) {
        this.indicatorMarginLeft = dpToPx(indicatorMarginLeft);
    }

    /**
     * 设置指示器之间的上间距
     *
     * @param indicatorMarginTop
     */
    public void setIndicatorMarginTop(float indicatorMarginTop) {
        this.indicatorMarginTop = indicatorMarginTop;
    }

    /**
     * 设置指示器之间的右间距
     *
     * @param indicatorMarginRight
     */
    public void setIndicatorMarginRight(float indicatorMarginRight) {
        this.indicatorMarginRight = indicatorMarginRight;
    }

    /**
     * 设置指示器之间的下间距
     *
     * @param indicatorMarginBottom
     */
    public void setIndicatorMarginBottom(float indicatorMarginBottom) {
        this.indicatorMarginBottom = indicatorMarginBottom;
    }

    /**
     * 设置指示器的位置
     *
     * @param gravity
     */
    public void setIndicatorGravity(int gravity) {
        this.indicatorGravity = gravity;
    }

    /**
     * 是否自动播放
     *
     * @return
     */
    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    /**
     * 设置自动播放
     *
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    /**
     * 获取Pager对象
     *
     * @return
     */
    public ViewPager getPager() {
        return pager;
    }

    /**
     * 获取数据适配器
     *
     * @return
     */
    public BannerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置是否循环
     *
     * @param loop
     */
    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public boolean isLoop() {
        return isLoop;
    }

    /**
     * 设置轮播时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static float dpToPx(float dp) {
        return dp * getScreenDensity();
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public interface OnPageChangeListener {
        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position             Position index of the first page currently being displayed.
         *                             Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         */
        void onPageScrollStateChanged(int state);
    }




    /************************************* ***********************************/
    /************************************* ***********************************/
    /*************************  BannerView ***********************************/
    /************************************* ***********************************/
    /************************************* ***********************************/

    public static class BannerView extends ViewPager {

        private float lastX = 0, lastY = 0;

        public BannerView(@NonNull Context context) {
            super(context);
        }

        public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            int x = (int) ev.getRawX();
            int y = (int) ev.getRawY();
            int dealtX = 0;
            int dealtY = 0;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 保证子View能够接收到Action_move事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    dealtX += Math.abs(x - lastX);
                    dealtY += Math.abs(y - lastY);
                    // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                    if (dealtX >= dealtY) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }

    }

    /*************************  BannerAdapter ***********************************/

    public static class BannerAdapter<T> extends PagerAdapter {

        private Context context;
        private List<T> data;
        private View convertView;
        private int position;
        private boolean isLoop = true;
        private BannerPager.OnPageClickListener listener;
        private OnMeasureConvertViewListener onMeasureListener;

        public BannerAdapter(Context context, List<T> data) {
            this.context = context;
            this.data = data;
        }

        public void setLoop(boolean isLoop) {
            this.isLoop = isLoop;
            if (getCount() > 0 && isLoop) {
                this.data.add(0, data.get((getCount() - 1)));
                this.data.add(data.get(1));
            }
        }

        public boolean isLoop() {
            return isLoop;
        }

        public Context getContext() {
            return context;
        }

        public List<T> getData() {
            return data;
        }

        public int getPosition() {
            return position;
        }

        public T getItem(int position) {
            return data.get(position);
        }

        public View getConvertView() {
            return convertView;
        }

        public BannerPager.OnPageClickListener getListener() {
            return listener;
        }

        public void setOnPageClickListener(BannerPager.OnPageClickListener listener) {
            this.listener = listener;
        }

        public void setOnMeasureConvertViewListener(OnMeasureConvertViewListener onMeasureListener) {
            this.onMeasureListener = onMeasureListener;
        }

        public interface OnMeasureConvertViewListener {
            void onMeasureConvertView(BannerAdapter adapter, View convertView);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            this.position = position;
            convertView = getView(LayoutInflater.from(context), position, null, container);
            container.addView(convertView);
            if (onMeasureListener != null) {
                onMeasureListener.onMeasureConvertView(this, convertView);
            }
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            this.position = position;
            container.removeView((View) object);
        }

        protected View getView(LayoutInflater inflater, final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = new ImageView(context);
                holder.target = (ImageView) convertView;
                holder.target.setScaleType(ImageView.ScaleType.FIT_XY);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Object object = data.get(position);
            if (object instanceof Integer) {
                holder.target.setImageResource((int) object);
            }
            if (object instanceof File) {
                File file = (File) object;
                holder.target.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            if (object instanceof String) {
                holder.target.setTag(null);
                holder.target.setImageBitmap(BitmapFactory.decodeFile((String) object));
            }
            if (object instanceof Uri) {
                holder.target.setImageURI((Uri) object);
            }
            convertView = holder.target;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPageClick(isLoop ? position - 1 : position);
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            private ImageView target;
        }


    }

}
