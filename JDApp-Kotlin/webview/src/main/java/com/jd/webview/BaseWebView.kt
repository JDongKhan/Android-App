package com.jd.webview

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.webkit.DownloadListener
import android.webkit.WebSettings
import android.webkit.WebView

class BaseWebView : WebView {
    private var mContext: Context? = null
    private var mWebViewDownLoadListener: ExWebViewDownLoadListener? = null
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun init(context: Context) {
        mContext = context
        mWebViewDownLoadListener = ExWebViewDownLoadListener()
        setDownloadListener(mWebViewDownLoadListener)
        val s = settings
        s.builtInZoomControls = true
        s.displayZoomControls = false
        // 不展示缩放按钮，否则会导致webview.onDestroy奔溃
        s.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        s.useWideViewPort = true
        s.loadWithOverviewMode = true
        s.savePassword = true
        s.saveFormData = true
        s.javaScriptEnabled = true
        s.allowFileAccess = true
        s.allowUniversalAccessFromFileURLs = false
        s.setRenderPriority(WebSettings.RenderPriority.HIGH)
        // 把图片加载放在最后来加载渲染
        s.blockNetworkImage = false
        // WebView不使用缓存
        s.cacheMode = WebSettings.LOAD_NO_CACHE
        s.defaultTextEncodingName = "UTF-8"

        // 启用地理定位
        s.databaseEnabled = true
        s.setGeolocationEnabled(true)
        val dir = mContext!!.applicationContext.getDir("database", Context.MODE_PRIVATE).path
        s.setGeolocationDatabasePath(dir)
        s.domStorageEnabled = true

        // webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    /**
     * 初始化H5页面
     *
     * @param context
     */
    constructor(context: Context) : super(context) {
        init(context)
    }

    /**
     * 初始化H5页面
     *
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    /**
     * 初始化H5页面
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    /**
     * WebView适配链接下载
     */
    private inner class ExWebViewDownLoadListener : DownloadListener {
        override fun onDownloadStart(
            url: String, userAgent: String, contentDisposition: String, mimetype: String,
            contentLength: Long
        ) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mContext!!.startActivity(intent)
        }
    }

    companion object {
        private const val DEBUG = true
        private const val TAG = "BaseWebView"
    }
}