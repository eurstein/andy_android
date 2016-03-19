title: android sdk, tools ...

[TOC]

## adb 常用命令
                              
| adb command                   | 说明                                              |
| ----------------------------- | ------------------------------------------------- |
| adb devices                   | 列出所有android设备 |
| adb shell                     | 多设备时，使用-s参数指定设备，比如: adb -s 04bef3bb0044becd shell |
| adb start-server              | 启动adb服务 |
| adb kill-server               | 关闭adb服务， adb shell 连接不上时可以尝试这个命令                    |
| adb pull /sdcard/xx C:\xx     ||
| adb push C:\xx /sdcard/xx     ||
| adb shell dumpsys activity [packagename] | 查看活动页面 | 
| dumpsys package | 查看所有安装的包信息 |
| adb logcat | |
| adb install -r <package_name>| 更新安装 |
| adb uninstall <package_name>| 卸载 |
| adb shell am force-stop com.tencent.android.qqdownloader||
| adb shell ps | 查看进程列表 |
| adb shell ps -x [PID] | 查看指定进程状态 |
| adb shell top | 查看内存（VSS RSS）和CPU使用情况 |
| adb shell dumpsys meminfo <package_name_or_pid> | | 
| adb shell tcpdump -p -vv -s 0 -w /sdcard/capturenet.pcap | tcpdump包 |
|adb pull /data/data/com.android.providers.contacts/databases/contacts2.db C:\Users\gzjaychen\Desktop\contact2.db| 获取联系人db |

## 拖放安装应用，不用再抱怨弹出xx助手xx宝来的蜗牛速度安装了（速度快了，心情好了）：
install.bat
```
adb install %1
pause
```

## aapt
1. 取得包名
```
C:\Users\andygzyu>aapt d badging C:\Users\andygzyu\Desktop\batchpackage\MobileAssistant_Dev_SDK_IPC_2014-6-9_17-30_for_shouguan.apk | findstr "package"
package: name='com.tencent.android.qqdownloader' versionCode='4300128' versionName='4.3.0'
```

## pm
1. pm get-install-location

```
pm get-install-location: returns the current install location.
    0 [auto]: Let system decide the best location
    1 [internal]: Install on internal device storage
    2 [external]: Install on external media
```
2. pm set-install-location

```
pm set-install-location: changes the default install location.
  NOTE: this is only intended for debugging; using this can cause
  applications to break and other undersireable behavior.
    0 [auto]: Let system decide the best location
    1 [internal]: Install on internal device storage
    2 [external]: Install on external media
```

## 在非root手机上取已安装的apk文件
adb shell
pm list packages -f xxx
exit
adb pull /data/app/xxx.apk

## apktool
1. 反编译
http://ibotpeaches.github.io/Apktool/install/
```
apktool d xx.apk -o directory
```
2. 回编译
```
apktool b directory
```





