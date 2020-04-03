package com.lxj.androidktx.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.loge
import kotlinx.android.synthetic.main._ktx_activity_web.*


class WebActivity : TitleBarActivity(){

    companion object{
        var onRightClickAction: (()->Unit)? = null
        fun start(title: String? = null, url:String? = null, content: String? = null, leftIconRes: Int = R.mipmap._ktx_ic_back,
                  enableCache: Boolean? = false, showProgress: Boolean = true,
                    rightIconRes: Int = 0, rightIconClickAction: (()->Unit)? = null ){
            onRightClickAction = rightIconClickAction
            val intent = Intent(AndroidKtxConfig.context, WebActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            intent.putExtra("content", content)
            intent.putExtra("leftIconRes", leftIconRes)
            if(rightIconRes!=0){
                intent.putExtra("rightIconRes", rightIconRes)
            }
            intent.putExtra("enableCache", enableCache)
            intent.putExtra("showProgress", showProgress)
            AndroidKtxConfig.context.startActivity(intent)
        }

    }

    override fun getBodyLayout() = R.layout._ktx_activity_web
    var showProgress = true
    var title: String? = null
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
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                if(showProgress) progressBar.hide()
                if(title==null){
                    titleBar().setupTitle(view.title)
                    loge("title: ${view.title}")
                }
            }
        }

        title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")?:""
        val content = intent.getStringExtra("content")?:""
        val leftIconRes = intent.getIntExtra("leftIconRes", R.mipmap._ktx_ic_back)
        val rightIconRes = intent.getIntExtra("rightIconRes", 0)
        showProgress = intent.getBooleanExtra("showProgress", true)

        titleBar().setup(leftImageRes = leftIconRes, title = title ?: "加载中...")
        titleBar().leftImageView().click { finish() }
        if(rightIconRes!=0){
            titleBar().setupRightImage(imageRes = rightIconRes)
            titleBar().rightImageView().click { onRightClickAction?.invoke() }
        }


        if(!url.isNullOrEmpty()){
            webView.loadUrl(url)
        }else{
            webView.loadDataWithBaseURL("", content, null,null,null)
        }
        if(showProgress) progressBar.show() else progressBar.gone()
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
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if(showProgress){
                progressBar.setWebProgress(newProgress)
            }
        }

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
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
//        webView.destroy()
    }
}