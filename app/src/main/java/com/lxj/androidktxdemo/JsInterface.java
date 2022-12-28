package com.lxj.androidktxdemo;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * Author: cheers
 * Time ： 2020-08-21
 * Description ：阅读资料 - 打开图片预览js
 */
public class JsInterface {
    @JavascriptInterface
    public void showImages(String[] src, int index){
        if (src == null || src.length == 0){
            return;
        }
        ToastUtils.showShort("showImages: " + GsonUtils.toJson(src)+ "  index: " + index);
    }
    @JavascriptInterface
    public void onLinkClick(String href){
        if (StringUtils.isEmpty(href)){
            return;
        }
        ToastUtils.showShort("onLinkClick: " + href);
    }
}
