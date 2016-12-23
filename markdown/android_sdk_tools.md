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
| adb install -r <apkfile> | 更新安装(保留数据和缓存文件) |
| adb install -s <apkfile> | 安装到sdk卡 |
| adb uninstall <package_name> | 卸载 |
| adb uninstall -k <package_name> | 卸载app，但保留数据和缓存文件 |
| adb shell am start -n <package_name>/.<activity_class_name> | 启动应用，比如 adb shell am start com.tencent.android.qqdownloader/com.tencent.assistantv2.activity.MainActivity |
| adb shell am force-stop com.tencent.android.qqdownloader||
| adb shell ps | 查看进程列表 |
| adb shell ps -x [PID] | 查看指定进程状态 |
| adb shell kill <pid> | 杀死一个进程 |
| adb shell kill -3 <pid> | 输出一个进程状态 |
| adb shell top | 查看内存（VSS RSS）和CPU使用情况 |
| adb shell top -m 6 | 查看占用内存前6的app |
| adb shell top -n 1 | 刷新一次内存信息，然后返回 |
| adb shell procrank | 查询各进程内存使用情况 |
| adb shell cat /proc/meminfo | 查看当前内存占用 |
| adb shell service list | 查看后台services信息 |
| adb shell cat /proc/iomem | 查看IO内存分区 |
| adb shell cat /data/misc/wifi/*.conf | 查看wifi密码 |
| adb logcat -c | 清除log缓存 |
| adb shell cat /system/build.prop | 获取设备名称 |
| adb shell dumpsys meminfo <package_name_or_pid> | | 
| adb shell tcpdump -p -vv -s 0 -w /sdcard/capturenet.pcap | tcpdump包 |
| adb pull /data/data/com.android.providers.contacts/databases/contacts2.db C:\Users\gzjaychen\Desktop\contact2.db| 获取联系人db |
| adb shell pm list features | 查看手机feature |
| adb shell getprop ro.build.fingerprint | 查看fingerprint |
| adb get-serialno | 查看序列号 |
| adb reboot | 重启机器 |
| adb reboot bootloader | 重启到bootloader,即刷机模式 |
| adb reboot recovery | 重启到recovery，即恢复模式 |
| adb shell cat /sys/class/net/wlan0/address | 获取机器MAC地址 |
| adb shell cat /proc/cpuinfo | 获取CPU序列号 |


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

C:\Users\andygzyu>adb shell
shell@hammerhead:/ $ su
root@hammerhead:/ # cd /data/app
root@hammerhead:/data/app # ls | grep rootexplorer

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

##  Mac系统启动SDK管理
`/Applications/Android/android-sdk/tools/android sdk`




