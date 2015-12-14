title: ndk command

[TOC]

## 编译错误解决

1. 错误提示
	```
	D:\SVN\cscommjni>ndk-build
	make.exe: *** [clean-installed-binaries] Error 1

	D:\SVN\cscommjni>ndk-build clean
	[armeabi] Clean          : stdc++ [armeabi]
	make.exe: *** [clean-stdc++-armeabi] Error 1
	```
2. 解决方法
	将cscommjni重命名成jni即可
	```
	d:\SVN\jni>ndk-build clean
	[armeabi] Clean          : stdc++ [armeabi]
	[armeabi] Clean          : yyb_cscomm [armeabi]
	```