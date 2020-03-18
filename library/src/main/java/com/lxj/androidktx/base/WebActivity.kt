package com.lxj.androidktx.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import kotlinx.android.synthetic.main._ktx_activity_web.*


class WebActivity : TitleBarActivity(){

    companion object{
        fun start(title: String, url:String? = null, content: String? = null, leftIconRes: Int = R.mipmap._ktx_ic_back,
                  enableCache: Boolean? = false){
            val intent = Intent(AndroidKtxConfig.context, WebActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            intent.putExtra("content", content)
            intent.putExtra("leftIconRes", leftIconRes)
            intent.putExtra("enableCache", enableCache)
            AndroidKtxConfig.context.startActivity(intent)
        }

    }

    override fun getBodyLayout() = R.layout._ktx_activity_web
    override fun initData() {
        val enableCache = intent.getBooleanExtra("enableCache", false)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = if(enableCache) WebSettings.LOAD_CACHE_ELSE_NETWORK else WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.webChromeClient = MyWebChromeClient()
        webView.webViewClient = WebViewClient()

        val title = intent.getStringExtra("title")?:""
        val url = intent.getStringExtra("url")?:""
        val content = intent.getStringExtra("content")?:""
        val leftIconRes = intent.getIntExtra("leftIconRes", R.mipmap._ktx_ic_back)

        titleBar().setup(leftImageRes = leftIconRes, title = title)
        titleBar().leftImageView().click { finish() }

        if(!url.isNullOrEmpty()){
            webView.loadUrl(url)
        }else{
            webView.loadDataWithBaseURL("", content, null,null,null)
        }
    }

    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)
        when (config.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                hideTitleBar()
                window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                showTitleBar()
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }
    }

    private fun fullScreen() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
    inner class MyWebChromeClient: WebChromeClient(){
        var mCallback: CustomViewCallback? = null
        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            LogUtils.e("onShowCustom")
            fullScreen()
            webView.setVisibility(View.GONE)
            flFull.setVisibility(View.VISIBLE)
            flFull.addView(view)
            mCallback = callback
            super.onShowCustomView(view, callback)
        }

        override fun onHideCustomView() {
            fullScreen()
            webView.setVisibility(View.VISIBLE)
            flFull.setVisibility(View.GONE)
            flFull.removeAllViews()
            super.onHideCustomView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        webView.clearCache(true)
//        webView.clearHistory()
    }
}