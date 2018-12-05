# AndroidKTX
Some very useful kotlin extensions for android developping!

一些列非常有用的Kotlin扩展，旨在提高Android开发速度！注意这个不是官方的AndroidKTX！

# Gradle
[![Download](https://api.bintray.com/packages/li-xiaojun/jrepo/androidktx/images/download.svg)](https://bintray.com/li-xiaojun/jrepo/androidktx/_latestVersion)

```
implementation 'com.lxj:androidktx:latest release'
```


# Usage

### Hash相关
我们使用hash的时候，大都是对字符串操作，所以给String增加了扩展方法，用法如下：
```kotlin
"123456".md5()  // E10ADC3949BA59ABBE56E057F20F883E
"123456".sha1()  // 7C4A8D09CA3762AF61E59520943DC26494F8941B
"123456".sha256()  // 8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92
"123456".sha512()  // BA3253876AED6BC22D4A6FF53D8406C6AD864195ED144AB5C87621B6C233B548BAEAE6956DF346EC8C17F5EA10F35EE3CBC514797ED7DDD3145464E2A0BAB413
"123456".md5Hmac()  // 4270C88B36C8D232DC2098FE0287A490
"123456".sha1Hmac()  // E3DDE36BBDF2CA05A96DBE11BDE696FEF00C4299
"123456".sha256Hmac()  // 12FC58997EA2FBD1861E1FBD4AFD5D69321A122BAB6FBD1695A2820B42D7F7B8

// 对称加密解密
"123456".encryptDES(aaaabbbb)  //TQEoRuLPiHo=
"TQEoRuLPiHo=".decryptDES(aaaabbbb)  //123456
```


### Log相关
我们输出的log大都是字符串，所以也是给String增加扩展方法，用法如下：
```kotlin
"我是测试".v()
"我是测试".i()
"我是测试".w()
"我是测试".d()
"我是测试".e()
```
![weather_humidity](imgs/log.png)
log的默认tag和开关配置在AndroidKtxConfig类中，可动态配置。


### Span相关
封装了颜色，大小，背景色，删除线等常用的文本装饰，用法如下：
```kotlin
val str = "我是测试文字"
tvSizeResult.text = str.toSizeSpan(0..2)
```
![weather_humidity](imgs/size_span.png)

```kotlin
tvColorResult.text = str.toColorSpan(2..6)
```
![weather_humidity](imgs/color_span.png)

```kotlin
tvBgColorResult.text = str.toBackgroundColorSpan(2..6)
```
![weather_humidity](imgs/bg_color_span.png)

```kotlin
tvStrikethrougthResult.text = str.toStrikethrougthSpan(2..6)
```
![weather_humidity](imgs/strikethrough_span.png)


# 意见收集
为了让这个库更好用，更快加速Android开发，请到Issue中提出您宝贵的意见或建议。我将对其进行评估，如果合适，立即采用。


# 联系方式
**QQ: 16167479**

**Email: 16167479@qq.com**