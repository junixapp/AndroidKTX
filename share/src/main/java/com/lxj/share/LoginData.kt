package com.lxj.share

/**
 * Description: 三方登录数据封装
 * Create by dance, at 2019/6/24
 */
data class LoginData(
    var country: String? = "",
    var unionid: String? = "",
    var gender: String? = "",
    var city: String? = "",
    var openid: String? = "",
    var language: String? = "",
    var profile_image_url: String? = "",
    var accessToken: String? = "",
    var refreshToken: String? = "",
    var uid: String? = "",
    var province: String? = "",
    var screen_name: String? = "",
    var name: String? = "",
    var iconurl: String? = "",
    var expiration: String? = "",
    var expires_in: String? = ""
){
    companion object{
        fun fromMap(map: Map<String, String>): LoginData {
            return LoginData(country = map["country"], unionid = map["unionid"],
                gender = map["gender"], city = map["city"], openid = map["openid"],
                language = map["language"], profile_image_url = map["profile_image_url"], accessToken = map["accessToken"],
                refreshToken = map["refreshToken"], uid = map["uid"], province = map["province"],
                screen_name = map["screen_name"], name = map["name"], iconurl = map["iconurl"],
                expiration = map["expiration"], expires_in = map["expires_in"]
                )
        }
    }
}
