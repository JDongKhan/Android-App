package com.jd.webview

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.JsPromptResult
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jd.core.log.LogUtils

class WebViewActivity : AppCompatActivity() {
    private var mWebView: BaseWebView? = null
    private var mLoadUrl: String? = null
    private var mWebChromeClient: WebChromeClient? = null
    private var mWebViewClient: WebViewClient? = null
    //private WebViewJssdk mWebViewJssdk;
    /**
     * 本地实现H5中Input标签（手机系统上传文件）
     */
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mUploadMessages: ValueCallback<Array<Uri>>? = null
    private var mProgessBar: ProgressBar? = null
    private var mActionBar: ViewGroup? = null
    private var mBackView: ImageView? = null
    private var mCloseView: TextView? = null
    private var mNeedStageBack = false
    private var tvTitle: TextView? = null
    private var mFrom = ""
    private var vEmpty: View? = null
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                WEBVIEW_LOAD_OVERTIME -> {}
                else -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_webview)
        mProgessBar = findViewById<ProgressBar>(R.id.webPro)
        mWebView = findViewById<BaseWebView>(R.id.webview)
        mActionBar = findViewById<ViewGroup>(R.id.actionbar)
        mBackView = findViewById<ImageView>(R.id.ivBack)
        mCloseView = findViewById<TextView>(R.id.tvClose)
        //        mCloseView.setVisibility(View.VISIBLE);
        tvTitle = findViewById<TextView>(R.id.tvTitle)
        vEmpty = findViewById<View>(R.id.ll_empty)
        mFrom = intent.getStringExtra(BUNDLE_KEY_FROM) ?: ""
        mLoadUrl = intent.getStringExtra(BUNDLE_KEY_URL)
        if (TextUtils.isEmpty(mLoadUrl)) {
            return
        }
        LogUtils.d("WebView", mLoadUrl)
        mNeedStageBack = intent.getBooleanExtra(BUNDLE_KEY_STAGE_BACK, true)

        // 根据路由参数隐藏标题栏
        if (!intent.getBooleanExtra(BUNDLE_KEY_TITLE, true)) {
            mActionBar?.visibility = View.GONE
        }

        // 去除手机系统自带ScrollView滑动效果
        mWebView?.overScrollMode = View.OVER_SCROLL_NEVER
        WebView.setWebContentsDebuggingEnabled(true)
        // 设置UA
        val userAgent =  "ANDROID"
        mWebView?.settings?.userAgentString = userAgent
        initWebClient()
        initJsSdk()
        mLoadUrl?.let {
            mWebView?.loadUrl(it)
        }
        initListener()
    }

    private fun initListener() {
        mBackView!!.setOnClickListener { onBackPressed() }
        mCloseView?.setOnClickListener { finish() }
    }

    /**
     * 选择文件
     */
     private val openFileChooserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
         if (it.resultCode == RESULT_OK) {
             if (null == mUploadMessage) {
                 return@registerForActivityResult
             }
             mUploadMessage?.onReceiveValue(it.data?.data)
             mUploadMessage = null
         }
     }

    /**
     * 选择文件
     */
    private val onShowFileChooserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (null == mUploadMessages) {
                return@registerForActivityResult
            }
            val result: Uri? =  it.data?.data
            if (result != null) {
                mUploadMessages!!.onReceiveValue(arrayOf(result))
            } else {
                mUploadMessages!!.onReceiveValue(arrayOf<Uri>())
            }
            mUploadMessages = null
        }
    }


    private fun initWebClient() {

        // WebChromeClient初始化
        mWebChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                if (!TextUtils.isEmpty(title)) {
                    tvTitle?.text = title
                }
            }

            // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>?, acceptType: String?) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                openFileChooserLauncher.launch(Intent.createChooser(i, "上传图片"))
            }

            // For Android < 3.0
            fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                openFileChooserLauncher.launch(Intent.createChooser(i, "上传图片"))
            }

            // For Android  > 4.1.1
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri>?,
                acceptType: String?,
                capture: String?
            ) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                openFileChooserLauncher.launch(Intent.createChooser(i, "上传图片"))
            }

            //Android 5.0+
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                mWebView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (mUploadMessages != null) {
                    mUploadMessages = null
                }
                mUploadMessages = filePathCallback
                val intent: Intent = fileChooserParams.createIntent()
                try {
                    onShowFileChooserLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    mUploadMessages = null
                    return false
                }
                return true //返回true只能上传一次
            }

            // 支持网页定位功能
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    hideLoadProgress()
                } else {
                    setLoadProgress(newProgress)
                }
            }

            override fun onJsPrompt(
                view: WebView,
                url: String,
                message: String,
                defaultValue: String,
                result: JsPromptResult
            ): Boolean {
                result.confirm("true")
                return true
            }
        }
        mWebViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.cancel()
            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
//                int statusCode = errorResponse.getStatusCode();
//                if (404 == statusCode || 500 == statusCode) {
//                    view.removeAllViews();
//                    vEmpty.setVisibility(View.VISIBLE);
//                }
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                vEmpty!!.visibility = View.GONE
                showLoadProgress()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                mCloseView?.visibility = if (mWebView!!.canGoBack()) View.VISIBLE else View.GONE
                hideLoadProgress()
            }

            override fun onLoadResource(view: WebView, url: String) {
                super.onLoadResource(view, url)
            }

            /**
             * 加载WebView异常处理
             */
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                vEmpty!!.visibility = View.VISIBLE
                super.onReceivedError(view, request, error)
            }

            override fun doUpdateVisitedHistory(view: WebView, url: String, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                mWebView!!.loadUrl("javascript:navigator.sakey = 2001;")
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        mWebView?.webViewClient = mWebViewClient!!
        mWebView?.webChromeClient = mWebChromeClient
    }


    /**
     * 初始化处理JSSDK
     */
    private fun initJsSdk() {
//        mWebViewJssdk = new WebViewJssdk(UnifyWebViewActivity.this, mWebView);
//        mWebView?.addJavascriptInterface(mWebViewJssdk, WebViewJssdk.JS_INTERFACE_NAME);
    }
    /**
     * 设置标题栏Title
     *
     * @param title
     */
    fun setTitle(title: String?) {
        if (tvTitle != null && title != null) {
            tvTitle?.text = title
        }
        mActionBar?.visibility = View.VISIBLE
    }

    val titleView: View?
        /**
         * 获取标题栏
         *
         * @return
         */
        get() = mActionBar


    override fun onBackPressed() {
        if (mWebView!!.canGoBack() && mNeedStageBack) {
            mWebView!!.goBack()
        } else {
            finish()
        }
    }

    /**
     * 设置网页加载进度
     */
    fun setLoadProgress(progress: Int) {
        mProgessBar?.progress = progress
    }

    /**
     * 显示网页加载进度条
     */
    fun showLoadProgress() {
        mProgessBar?.visibility = View.VISIBLE
        mProgessBar?.progress = 0
    }

    /**
     * 隐藏网页进度条
     */
    fun hideLoadProgress() {
        mProgessBar?.visibility = View.GONE
    }

    companion object {
        private const val TAG = "UnifyWebViewActivity"
        private const val WEBVIEW_LOAD_OVERTIME = 0x110
        private const val SELECT_DATE = 0x13
        const val BUNDLE_KEY_URL = "bundle_key_url"
        const val BUNDLE_KEY_FROM = "bundle_key_from"
        const val BUNDLE_KEY_TITLE = "bundle_key_title"
        const val BUNDLE_KEY_STAGE_BACK = "bundle_key_stage_back"
        /**
         * 提供方法供不同页面传值使用  from：页面来源
         * startActivity(WebViewActivity.toWebPage("", url, false, true));
         */
        /**
         * 提供方法供不同页面传值使用  from：页面来源
         */
        @JvmOverloads
        fun toWebPage(
            context: Context,
            from: String?,
            loadUrl: String?,
            showTitle: Boolean,
            needStageBack: Boolean = true
        ): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(BUNDLE_KEY_FROM, from)
            intent.putExtra(BUNDLE_KEY_URL, loadUrl)
            intent.putExtra(BUNDLE_KEY_TITLE, showTitle)
            intent.putExtra(BUNDLE_KEY_STAGE_BACK, needStageBack)
            return intent
        }
    }
}