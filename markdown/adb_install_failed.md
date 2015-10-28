title: Apk安装失败问题


# APK Install failed
[TOC]

## Installation error: INSTALL_FAILED_UID_CHANGED 的解决办法

出现此问题的原因大多是apk冲突造成，解决的办法如下：

1. Settings -> Applications, 卸载出现问题的apk，重新安装即可。
2. 如果apk无法卸载，则将apk相关文件和相关内容删除。Step:

    1. 删除可能相关的文件：/data/app(apk file), /system/app/(apk file), /data/data/(data file)，

    2. 除了删除以上文件之外，还需要将/data/system/packages.xml文件中与apk相关的内容全部删除。Step:
        1. adb pull /data/system/packages.xml  ~/Desktop.
        2. 修改pakcages.xml，将与apk相关的packages标签及其内容删除。
        3. adb push ～/Desktop/packages.xml /data/system/.

    3. 重启手机
    
    4. 重新安装apk文件。

一般方法一就能解决问题，方法二算是后补。


## Installation error: INSTALL_FAILED_INSUFFICIENT_STORAGE
9300，没插内存卡，报INSTALL_FAILED_INSUFFICIENT_STORAGE的错，之前也有app碰到过这种问题，

解决方法
1. 在Apk的 AndroidManifest.xml中设置android:installLocation="auto"
```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tencent.android.qqdownloader"
    android:versionCode="5212129"
    android:versionName="5.2.1"
    android:installLocation="auto" >
```
介绍adb shell下一个命令pm
```
查看install-location
pm get-install-location: returns the current install location.
    0 [auto]: Let system decide the best location
    1 [internal]: Install on internal device storage
    2 [external]: Install on external media

设置install-location
pm set-install-location: changes the default install location.
  NOTE: this is only intended for debugging; using this can cause
  applications to break and other undersireable behavior.
    0 [auto]: Let system decide the best location
    1 [internal]: Install on internal device storage
    2 [external]: Install on external media
```

