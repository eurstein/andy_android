
[TOC]

## Context Activity Intent Service

- Context

- Activity

- Intent

- Service

## LogCat
http://stackoverflow.com/questions/7959263/android-log-v-log-d-log-i-log-w-log-e-when-to-use-each-one
The different LogCat methods are:

Log.v(); // Verbose
Log.d(); // Debug
Log.i(); // Info
Log.w(); // Warning
Log.e(); // Error

Let's go in reverse order:

- Log.e: This is for when bad stuff happens. Use this tag in places like inside a catch statment. You know and error has occurred and therefore you're logging an error.

- Log.w: Use this when you suspect something shady is going on. You may not be completely in full on error mode, but maybe you recovered from some unexpected behavior. Basically, use this to log stuff you didn't expect to happen but isn't necessarily an error. Kind of like a "hey, this happened, and it's weird, we should look into it."

- Log.i: Use this to post useful information to the log. For example: that you have successfully connected to a server. Basically use it to report successes.

- Log.d: Use this for debugging purposes. If you want to print out a bunch of messages so you can log the exact flow of your program, use this. If you want to keep a log of variable values, use this.

- Log.v: Use this when you want to go absolutely nuts with your logging. If for some reason you've decided to log every little thing in a particular part of your app, use the Log.v tag.

And as a bonus...

- Log.wtf: Use this when stuff goes absolutely, horribly, holy-crap wrong. You know those catch blocks where you're catching errors that you never should get...yea, if you wanna log them use Log.wtf

## Uri 通用资源标志符（Universal Resource Identifier, 简称"URI"）
ContentUris
UriMatcher

## android.os.IBinder
http://blog.csdn.net/luoshengyang/article/details/6642463


## 名词解释
IPC （Inter-Process Communication）进程间通信
AIDL（Android Interface Definition Language）Android接口定义语言
JNI (Java Native Interface) JAVA本地调用

## CMD(很简单的脚本却能大大提高效率，大家多学多分享)

一些经常操作的动作，使用手工操作又耗时又麻烦还可能出错，使用命令行去操作的话会极大提供效率。

- 拖放安装应用，不用再抱怨弹出xx助手xx宝来的蜗牛速度安装了（速度快了，心情好了）：
adb install %1
pause
 
- 卸载应用
adb uninstall com.tencent.qqpim
 
- 拉取SD卡目录文件
adb pull /sdcard/qqpim/log c:\qqpimlog
 
- 获取联系人db
adb pull /data/data/com.android.providers.contacts/databases/contacts2.db C:\Users\gzjaychen\Desktop\contact2.db
 
- tcpdump包
adb shell tcpdump -p -vv -s 0 -w /sdcard/capturenet.pcap

- 内存信息
adb shell dumpsys meminfo $package_name or $pid    //使用程序的包名或者进程id 

- top 
C:\Users\andygzyu>adb shell top | grep test
15060  0   0% S    16 931440K 116824K  fg u0_a74   org.eurstein.test.atest
^C
C:\Users\andygzyu>adb shell dumpsys meminfo org.eurstein.test.atestxdelta3

- SDK裁剪打包
裁目录：rd/s/q S:\qqpim_4.5_sdk_lewa\src\AGJ
裁文件：del S:\qqpim_4.5_sdk_lewa\src\com\tencent\qqpim\QQPimApplication.java
 
## aapt取得包名
C:\Users\andygzyu>aapt d badging C:\Users\andygzyu\Desktop\batchpackage\MobileAssistant_Dev_SDK_IPC_2014-6-9_17-30_for_shouguan.apk | findstr "package"
package: name='com.tencent.android.qqdownloader' versionCode='4300128' versionName='4.3.0'


