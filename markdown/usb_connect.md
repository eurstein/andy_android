title: android usb调试模式

[TOC]
## 手机弹不出usb调试授权框的解决方法 

1. 确认已经打开调试模式 [详见:怎么打开Android开发者选项](#usbdebug)
2. 尝试重新连接手机
3. adb kill-server后再尝试adb shell

## 什么是Android开发者选项

在手机的设置中，打开开发者选项，用户就可以用USB连接手机，直接在手机硬件上安装，调试自己的应用程序。

当然，也可以通过USB，看到手机中Android系统的一些数据和信息。

<span id="usbdebug" /><!--<span id="usbdebug">怎么打开Android开发者选项</span>-->
## 怎么打开Android开发者选项

1. Android3.2 和 Android3.2之前的版本，直接在设置菜单中就能打开Settings > Applications > Development

2. Android4.0 和 Android4.0之上的版本，菜单位置改为 Settings > Developer options

3. Android4.2 版本之后，此菜单选项默认为被隐藏了，要想让它重新出来，需要做以下操作

	找到Settings > About phone 然后点击其中的 Build number （版本号）7次。返回到之前的Settings 界面，就可以找到 Developer options了。

## 怎么隐藏开发者选项
	
设置 > 应用管理 > 找到”设置“程序 > 清除数据
