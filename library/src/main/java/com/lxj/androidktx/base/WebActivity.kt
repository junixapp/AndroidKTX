package com.lxj.androidktx.base

import android.content.Intent
import android.graphics.Color
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.widget.FrameLayout
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.dp
import com.lxj.androidktx.core.string
import kotlinx.android.synthetic.main._ktx_activity_web.*


open class WebActivity : TitleBarActivity(){

    companion object{
        var onRightClickAction: (()->Unit)? = null

        /**
         * 开启WebView界面
         * @param title 网页标题，如果不填写会自动获取网页的标题
         * @param url 网页的url地址
         * @param content 本地的html内容
         * @param leftIconRes 自定义左边返回按钮的图片
         * @param hideTitleBar 是否保持隐藏标题栏
         * @param keepMarginTop 是否保持顶部边距
         * @param statusBarColor 状态栏颜色
         * @param isLightStatusBar 是否是亮色调状态栏
         * @param enableCache 是否开启缓存，默认不开启
         * @param showProgress 是否显示进度条，默认显示
         * @param rightIconRes 右边按钮的图片
         * @param rightIconClickAction 右边按钮的点击监听器
         */
        fun start(title: String? = null, url: String? = null, content: String? = null,
                  hideTitleBar: Boolean = false, keepMarginTop: Boolean = false, leftIconRes: Int = R.mipmap._ktx_ic_back,
                  enableCache: Boolean? = false, showProgress: Boolean = true, indicatorColor : Int = 0,
                  statusBarColor : Int = -1, isLightStatusBar: Boolean = true, rightIconRes: Int = 0, rightIconClickAction: (() -> Unit)? = null){
            onRightClickAction = rightIconClickAction
            if(AndroidKTX.isDebug){
                if(!url.isNullOrEmpty()){
                    LogUtils.d("url: $url")
                }else{
                    LogUtils.d("content: $content")
                }
            }
            val intent = Intent(AndroidKTX.context, WebActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("hideTitleBar", hideTitleBar)
            intent.putExtra("keepMarginTop", keepMarginTop)
            intent.putExtra("isLightStatusBar", isLightStatusBar)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            intent.putExtra("content", content)
            intent.putExtra("leftIconRes", leftIconRes)
            if(indicatorColor!=-1){
                intent.putExtra("statusBarColor", statusBarColor)
            }
            if(indicatorColor!=0){
                intent.putExtra("indicatorColor", indicatorColor)
            }
            if(rightIconRes!=0){
                intent.putExtra("rightIconRes", rightIconRes)
            }
            intent.putExtra("enableCache", enableCache)
            intent.putExtra("showProgress", showProgress)
            AndroidKTX.context.startActivity(intent)
        }

    }

    override fun getBodyLayout() = R.layout._ktx_activity_web
    var title: String? = null
    lateinit var agentWeb: AgentWeb
    var enableCache = false
    val indicatorColor : Int by lazy { intent.getIntExtra("indicatorColor", Color.parseColor("#14d068")) }
    val mStatusBarColor : Int by lazy { intent.getIntExtra("statusBarColor", -1) }
    val hideTitleBar: Boolean by lazy { intent.getBooleanExtra("hideTitleBar", false) }
    val keepMarginTop: Boolean by lazy { intent.getBooleanExtra("keepMarginTop", false) }
    val isLightStatusBar: Boolean by lazy { intent.getBooleanExtra("isLightStatusBar", false) }

    override fun isLightMode() = isLightStatusBar

    override fun initView() {
        super.initView()
        KeyboardUtils.fixAndroidBug5497(this)
    }

    override fun initData() {
        setupTitle()

        val url = intent.getStringExtra("url")?:""
        val content = intent.getStringExtra("content")?:""
        loadData(url, content)
    }

    fun setupTitle(){
        title = intent.getStringExtra("title")
        val leftIconRes = intent.getIntExtra("leftIconRes", R.mipmap._ktx_ic_back)
        val rightIconRes = intent.getIntExtra("rightIconRes", 0)
        enableCache = intent.getBooleanExtra("enableCache", false)

        if(mStatusBarColor!=-1) setStatusBarColor(mStatusBarColor)

        if(hideTitleBar){
            if(keepMarginTop) hideTitleBarWithMarginTop()
            else hideTitleBar()
        }else{
            titleBar().setup(leftImageRes = leftIconRes, title = title ?: string(R.string._ktx_loading))
            titleBar().leftImageView().click {
                if(!agentWeb.back()){
                    finish()
                }
            }
            if(rightIconRes!=0){
                titleBar().setupRightImage(imageRes = rightIconRes)
                titleBar().rightImageView().click { onRightClickAction?.invoke() }
            }
        }
    }

    fun loadData(url: String, content: String){
        if(url.isNotEmpty()){
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webViewParent, FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator(indicatorColor, 1.dp)
                    .setWebViewClient(mWebViewClient)
                    .setWebChromeClient(mWebChromeClient)
                    .createAgentWeb()
                    .ready().go(url)
        }else{
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webViewParent, FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator(indicatorColor, 1.dp)
                    .setWebViewClient(mWebViewClient)
                    .setWebChromeClient(mWebChromeClient)
                    .createAgentWeb()
                    .ready().go(null)
            agentWeb.webCreator.webView.loadDataWithBaseURL("", content, null,null,null)
        }
    }

    private val mWebViewClient = object : WebViewClient(){
//        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
////            super.onReceivedSslError(view, handler, error)
//            handler?.proceed()
//        }
    }

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, t: String?) {
            super.onReceivedTitle(view, t)
            if(title.isNullOrEmpty()){
                titleBar().setup(title = t?:"")
            }
        }
    }

    override fun onBackPressed() {
        if (!agentWeb.back()){
            super.onBackPressed()
        }
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }
    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        if(!enableCache) {
            agentWeb.clearWebCache()
            agentWeb.webLifeCycle.onDestroy()
        }
        super.onDestroy()
    }
}