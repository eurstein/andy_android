title: 理解android.intent.category.LAUNCHER 具体作用

## 理解android.intent.category.LAUNCHER 具体作用
[TOC]

http://blog.csdn.net/jackrex/article/details/9189657

android.intent.category.LAUNCHER 具体有什么作用？我做一个小例子希望帮助大家理解
```
<activity android:name="APAct">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:name="URLAct">
    <intent-filter>
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
一个应用程序可以有多个Activity，每个Activity是同级别的，那么在启动程序时，最先启动哪个Activity呢？有些程序可能需要显示在程序列表里，有些不需要。怎么定义呢？android.intent.action.MAIN决定应用程序最先启动的Activity android.intent.category.LAUNCHER决定应用程序是否显示在程序列表里

因为你的程序可能有很多个activity
只要xml配置文件中有这么一个intent-filter，而且里面有这个launcher，那么这个activity就是点击程序时最先运行的那个activity。
```
<activity android:name="APAct">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />    
    </intent-filter>
</activity>
<activity android:name="URLAct">
    <intent-filter>
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
如果代码是这个样子的，那么还是会不会在显示出到列表中的， 也就是说main 和 launcher是配合使用的


```
<activity android:name="APAct">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:name="URLAct">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
如果是这样的话，就会出现两个图标在列表里。

用于模拟器启动时设置为默认打开为的activity

还有 为什么加入android.intent.category.DEFAULT
意思是说，每一个通过 startActivity() 方法发出的隐式 Intent 都至少有一个 category，就是 "android.intent.category.DEFAULT"，所以只要是想接收一个隐式 Intent 的 Activity 都应该包括 "android.intent.category.DEFAULT" category，不然将导致 Intent 匹配失败。

从上面的论述还可以获得以下信息：
1. 一个 Intent 可以有多个 category，但至少会有一个，也是默认的一个 category。
2. 只有 Intent 的所有 category 都匹配上，Activity 才会接收这个 Intent。

