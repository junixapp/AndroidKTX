## AndroidKTX
Some very useful kotlin extensions for android developping!

一些列非常有用的Kotlin扩展，旨在提高Android开发速度！注意这个不是官方的AndroidKTX！

## Gradle
[![Download](https://api.bintray.com/packages/li-xiaojun/jrepo/androidktx/images/download.svg)](https://bintray.com/li-xiaojun/jrepo/androidktx/_latestVersion)

```
implementation 'com.lxj:androidktx:latest release'
```


## Usage

### Hash相关
我们使用hash的时候，大都是对字符串操作，所以给String增加了扩展方法，用法如下：
```kotlin
"123456".md5()  // E10ADC3949BA59ABBE56E057F20F883E
"123456".sha1()  // 7C4A8D09CA3762AF61E59520943DC26494F8941B
"123456".sha256()  // 8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92
"123456".sha512()  // BA3253876AED6BC22D4A6FF53D8406C6AD864195ED144AB5C87621B6C233B548BAEAE6956DF346EC8C17F5EA10F35EE3CBC514797ED7DDD3145464E2A0BAB413
// 随机数增强哈希
"123456".md5Hmac(salt)  // 4270C88B36C8D232DC2098FE0287A490
"123456".sha1Hmac(salt)  // E3DDE36BBDF2CA05A96DBE11BDE696FEF00C4299
"123456".sha256Hmac(salt)  // 12FC58997EA2FBD1861E1FBD4AFD5D69321A122BAB6FBD1695A2820B42D7F7B8

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


### View相关
```kotlin
textView.width(100)           // 设置View的宽度为100
textView.widthAndHeight(100)  // 改变View的宽度和高度为100
textView.margin(leftMargin = 100)  // 设置View左边距为100
textView.toBitmap()           // 获取View的截图，支持RecyclerView长列表截图
```

### ImageView相关
```kotlin
// 使用Glide加载图片
image1.load(url)
image1.load(url, isCenterCrop = true, isCircle = true)
image1.load(url, noTransition = true)
```

### SharedPref相关
```kotlin
// 以下代码可以在项目的任何地方调用
putStringToSP("str", "哈哈")
putIntToSP("int", 11)
putBooleanToSP("bool", true)
putFloatToSP("float", 11.22f)
putLongToSP("long", 10000000000L)
putStringSetToSP("stringset", setOf("a", "b", "c"))

getStringFromSP("a")
```

### Toast相关
```kotlin
toast("测试短吐司")
longToast("测试长吐司")
```

### Fragment相关
```kotlin
//替换一个Fragment不传参
replace(R.id.frame1, TempFragment())
//替换一个Fragment并传参数
replace(R.id.frame1, TempFragment(), arrayOf(
                TempFragment.Key1 to "我是第一个Fragment",
                TempFragment.Key2 to "床前明月光"
        ))
```


### 通用扩展(可以在项目的任何地方使用)
dp和px转换：
```kotlin
dp2px(100)
px2dp(100)
```
实体转json字符串：
```kotlin
User("李晓俊", 25).toJson()   // {"age":25,"name":"李晓俊"}
```


## 注意事项
为了覆盖各种使用场景，该库对常用类库进行了封装，因此依赖了很多三方库：
```groovy
implementation "com.github.bumptech.glide:glide:4.8.0"
implementation 'com.google.code.gson:gson:2.8.5'
```

由于我依赖的三方库都是最新版本，可能与您当前项目中的类库版本不一致，从而导致因为API变化而编译失败。此时需要排除我这个库中的依赖，假设我的Glide版本与你项目中的不一致，则需要在gradle中配置如下：
```groovy
implementation ('com.lxj:androidktx:latest release version') {
        exclude group: 'com.github.bumptech.glide'
}
```


## 意见收集
为了让这个库更好用，更快加速Android开发，请到Issue中提出您宝贵的意见或建议。我将对其进行评估，如果合适，立即采用。


## 联系方式
**QQ: 16167479**

**Email: 16167479@qq.com**