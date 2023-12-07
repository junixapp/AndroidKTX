package com.lxj.androidktxdemo

import android.os.Handler
import android.os.Looper
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.blankj.utilcode.util.*
import com.lxj.androidktx.base.StateTitleBarActivity
import com.lxj.androidktx.core.asCard
import com.lxj.androidktx.core.bindFragment
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.dp
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.android.synthetic.main.demo.*

val html = """
    <p style="text-align: justify; line-height: 1.75em;"><a href="cheers://book/detail?kid=395"><span style="font-size: 16px; font-family: 微软雅黑;">222</span></a><span style="font-size: 16px; font-family: 微软雅黑;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描</span></span></span><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了</span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><img src="https://cheers-pre-oss.cheerspublishing.com/cheers/tmp/rich/1311ebce-4fbb-4c80-afe0-dccaa2d7c3a9.jpeg" alt="">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><a title="图书" href="cheers://book/detail?kid=395"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><img title="file" src="https://cheers-pre-oss.cheerspublishing.com/cheers/tmp/rich/88d6b95d-e723-4db9-8b80-488bf2d6fc2a.jpeg" alt="250-250.jpeg"></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></a></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">&nbsp;</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">&nbsp;</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了2</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><img src="https://cheers-pre-oss.cheerspublishing.com/cheers/tmp/rich/badef5b1-1832-41d4-82f6-1a942f4fc81b.gif" alt="" width="116" height="135"></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;"><span style="font-size: 16px; font-family: 微软雅黑;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;"><span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">1视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了<span style="line-height: 1.75em; text-align: justify; font-family: 微软雅黑; font-size: 16px;">视频描述来了</span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></span></p>
    <p style="text-align: justify; line-height: 1.75em;">&nbsp;</p>
    <p style="text-align: justify; line-height: 1.75em;"><img src="https://cheers-pre-oss.cheerspublishing.com/cheers/tmp/rich/b0f43551-a6d6-4f8a-a021-62fec2e15840.jpeg" alt=""></p>
    <p style="text-align: justify; line-height: 1.75em;">------</p>
""".trimIndent()

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
class DemoActivity : StateTitleBarActivity(){
    override fun getBodyLayout() = R.layout.demo
    override fun autoShowContent(): Boolean {
        return true
    }
    var isFirst = true
    val loadingPopupView: LoadingPopupView by lazy { XPopup.Builder(this).asLoading("加载中") }
    val base64 : String by lazy {
        EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(ImageUtils.getBitmap(R.drawable.c, 100, 100)))
    }
    val width = 32.dp
    override fun initData() {
        titleBar.setup(title = "大萨达撒大多撒", rightImageRes = R.mipmap.gengxingb)
//        AppVM.data.observe(this, Observer{
//            toast("it：$it")
//        })
//        pager2.adapter = object : EasyAdapter<Int>(listOf(1,1,1,1,1), R.layout.adapter_pager2){
//            override fun bind(holder: ViewHolder, t: Int, position: Int) {
//                (holder.itemView as TextView).text = "${position}"
//            }
//
//        }
//        pager2.isUserInputEnabled = false

        btnSwitchOrientation.text = "切换为${if(pager2.orientation==0) "Vertical" else "Horizontal"}"
        btnSwitchOrientation.click {
            if(pager2.orientation==ORIENTATION_HORIZONTAL) pager2.orientation = ORIENTATION_VERTICAL
            else pager2.orientation = ORIENTATION_HORIZONTAL
            btnSwitchOrientation.text = "切换为${if(pager2.orientation==0) "Horizontal" else "Vertical"}"
            pager2.bindFragment(this, listOf(DemoFragment(0), DemoFragment(1), DemoFragment(2)))
            pager2.asCard()
        }

        pager2.offscreenPageLimit = 1

        pager2.bindFragment(this, listOf(DemoFragment(0), DemoFragment(1), DemoFragment(2)))
        pager2.asCard()
        initWebview()
    }

    fun initWebview(){
        val settings: WebSettings = mWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        //解决图片不显示
        settings.blockNetworkImage = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        mWebView.isVerticalScrollBarEnabled = false
        mWebView.isNestedScrollingEnabled = false
        mWebView.isScrollContainer=false
        mWebView.isVerticalScrollBarEnabled=false
        mWebView.isHorizontalScrollBarEnabled=false
        mWebView.addJavascriptInterface(JsInterface(), "android")

        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Handler(Looper.myLooper()!!).postDelayed({
                    registerImageClickAction()
                    registerTextLinkClickAction()
                }, 800)
            }
        }
//        LogUtils.eTag("xxx", "base64: ${EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(ImageUtils.getBitmap(R.drawable.c, 100, 100)))}")
        val intro = html.replace("style=\";".toRegex(), "style=\"").replace("&#39;".toRegex(), "'")
//            .replace("\n".toRegex(), "<br/>")
        val htmlData = getHtmlData(intro)
//        LogUtils.file(htmlData)
        mWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
    }

    private fun getHtmlData(bodyHTML: String): String {
        var color = ""
        val css = if (true) {
            "*{margin:0px;}p{margin-top:12px;line-height:1.8;color:#121212;font-size:16sp;}"
        } else {
            "*{margin:0px;}"
        }
        val head = ("<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:100%; height:auto;}" + css + "</style>"
                + "</head>")
        val shadowBuilder = StringBuffer()
//        shadowBuilder.append("""
//            <a href="http://www.cheers.com" target="_blank" title="测试">测试超链接点击</a>
//        """.trimIndent())
        val ps=bodyHTML.split("\n")
        ps.forEachIndexed {  i, s ->
            if (s.isNotEmpty()){
                if (i==0){
                    shadowBuilder.append("<p style = 'margin:0px;'>")
                }else{
                    shadowBuilder.append("<p>")
                }
                shadowBuilder.append(s)
                shadowBuilder.append("</p>")
            }
        }
        return "<html>$head<body$color>${shadowBuilder}</body></html>"
    }

    /**
     * 注册图片点击事件
     */
    fun registerImageClickAction() {
        // 获取图片并点击放大
        try {
            val js = """javascript:(function() {
                        var picArray = new Array();
                        var objs=document.getElementsByTagName('img');
                        for(var i=0;i<objs.length;i++){
                          picArray[i] = objs[i].src;
                        }
                        for(var i=0;i<objs.length;i++){
                          var img=objs[i];
                          objs[i].index = i; 
                          if(img.parentNode.href==undefined&&img.src&&img.src.indexOf('data:image/')==-1){
                            img.onclick=function(event){ 
                               window.android.showImages(picArray, this.index);
                               event.stopPropagation();
                            }
                          }
                          let a = findAParent(img);
                          if(a!=null){
                            console.log("xxx:" + a.href);
                            let f = document.createElement("div");
                            f.style.position = "absolute";
                            f.style.width = "32px";
                            f.style.height = "32px";
                            f.style.right = "12px";
                            f.style.top = "12px";
                            f.style["background-image"] = "url(data:image/png;base64,${base64})";
                            f.style["background-size"] = "32px";
                            f.onclick = function(e){
                                window.android.onLinkClick(a.data);
                                e.stopPropagation();
                            };
                            let d = document.createElement("div");
                            d.style.position = "relative";
                            let newImg = img.cloneNode(true);
                            newImg.onclick=function(e){ 
                               window.android.showImages(picArray, this.index);
                               e.stopPropagation();
                            };
                            d.appendChild(f);
                            d.appendChild(newImg);
                            img.parentNode.appendChild(d);
                            img.parentNode.removeChild(img);
                          }
                        }
                        
                        function findAParent(el){
                            if(el==null || el.parentNode==null) return null;
                            var tagName = el.parentNode.tagName;
                            return tagName==='A' ? el.parentNode : findAParent(el.parentNode);
                        }
                        
                    })()"""
            mWebView.loadUrl(js)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 注册超链接点击事件
     */
    fun registerTextLinkClickAction() {
        try {
            val js = """javascript:(function() {
                        var objs = document.getElementsByTagName('a');
                        for(var i=0; i<objs.length; i++){
                          var a = objs[i];
                          var href = a.href;
                          if(href){
                            a.href = 'javascript:void(0);';
                            a.data = href;
                            a.onclick=function(){ window.android.onLinkClick(href)};
                          }
                        }
                    })()"""
            mWebView.loadUrl(js)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    override fun onConfigStateLayout() {
        super.onConfigStateLayout()
        stateLayout?.config()
    }

}