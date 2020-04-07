package com.lxj.androidktx.base

import android.content.Intent
import android.widget.FrameLayout
import com.just.agentweb.AgentWeb
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import kotlinx.android.synthetic.main._ktx_activity_web.*


class WebActivity : TitleBarActivity(){

    companion object{
        var onRightClickAction: (()->Unit)? = null
        fun start(title: String? = null, url: String? = null, content: String? = null, leftIconRes: Int = R.mipmap._ktx_ic_back,
                  enableCache: Boolean? = false, showProgress: Boolean = true,
                  rightIconRes: Int = 0, rightIconClickAction: (() -> Unit)? = null){
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
    var title: String? = null
    lateinit var agentWeb: AgentWeb
    var enableCache = false
    override fun initData() {
        title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")?:""
        val content = intent.getStringExtra("content")?:""
        val leftIconRes = intent.getIntExtra("leftIconRes", R.mipmap._ktx_ic_back)
        val rightIconRes = intent.getIntExtra("rightIconRes", 0)
        enableCache = intent.getBooleanExtra("enableCache", false)

        titleBar().setup(leftImageRes = leftIconRes, title = title ?: "加载中...")
        titleBar().leftImageView().click { finish() }
        if(rightIconRes!=0){
            titleBar().setupRightImage(imageRes = rightIconRes)
            titleBar().rightImageView().click { onRightClickAction?.invoke() }
        }

        if(url.isNotEmpty()){
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webViewParent, FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready().go(url)
        }else{
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webViewParent, FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready().go(null)
            agentWeb.webCreator.webView.loadDataWithBaseURL("", content, null,null,null)
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
        if(!enableCache) agentWeb.webLifeCycle.onDestroy()
        agentWeb.clearWebCache()
        super.onDestroy()
    }
}