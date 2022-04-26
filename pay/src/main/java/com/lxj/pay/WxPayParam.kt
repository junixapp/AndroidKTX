package com.lxj.androidktx.pay

data class WxPayParam(
        var appId: String = "",
        var partnerId: String = "",
        var prepayId: String = "",
        var nonceStr: String = "",
        var timeStamp: String = "",
        var packageValue: String = "",
        var sign: String = "",
        var extData: String = "",
)