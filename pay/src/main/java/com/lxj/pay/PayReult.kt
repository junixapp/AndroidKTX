package com.lxj.pay

import com.lxj.androidktx.core.toBean
import java.io.Serializable

/**
 *
 * Create by dance, at 2019/6/28
 */
data class AliPayResult(
    var resultStatus: String = "",
    var result: ResultInfo? = null,
    var memo: String? = ""
): Serializable {
    companion object {
        fun fromMap(map: Map<String, String>) = AliPayResult(
            memo = map["memo"],
            result = map["result"]?.toBean<ResultInfo>() ,
            resultStatus = map["resultStatus"] ?: ""
        )
    }

    fun isSuccess() = resultStatus=="9000"

}

data class ResultInfo(
    var alipay_trade_app_pay_response: OrderInfo? = null,
    var sign: String? = null,
    var sign_type: String? = null
): Serializable


data class OrderInfo(
    var code: String? = "",
    var msg: String? = "",
    var app_id: String? = "",
    var auth_app_id: String? = "",
    var charset: String? = "",
    var timestamp: String? = "",
    var out_trade_no: String? = "",
    var total_amount: String? = "",
    var trade_no: String? = "",
    var seller_id: String? = ""
): Serializable


data class WxPayResult(
    var status: String,
    var openId: String,
    var transaction: String,
){
    fun isSuccess() = status=="success"
}

