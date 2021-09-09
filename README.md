# MarqueeLib 多个跑马灯lib混合打包 ![](https://jitpack.io/v/csfwff/MarqueeLib.svg)

## 起因
 因为要用到跑马灯，所以搜索一番
 但是几个库都是support的，没有androidx
 而且kotlin用起来似乎有点问题
 所以就简单copy了一下
 弄到了androidx
 但是我估计该有的bug还是得有

 ## 裤子
    1. https://github.com/sunfusheng/MarqueeView
        对应 MarqueeViewSun
    2. https://github.com/gongwen/MarqueeViewLibrary
        对应 MarqueeViewGong
    3. https://gitee.com/zjianxin/MarqueeTextView
        对应 MarqueeTextViewXin
   具体使用请前往对应链接
   
   ## 使用
   项目根目录的build.gradle 或者是 setting.gradle
   ```
   allprojects {
   		repositories {
   			...
   			maven { url 'https://jitpack.io' }
   		}
   	}
   ```
 module的 build.gradle
```
 dependencies {
	        implementation 'com.github.csfwff:MarqueeLib:Tag'
	}
```
