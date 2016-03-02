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

## 生成.h头文件的注意事项

1. 先用javac生成.class文件，再用javah生成.h文件
2. 注意使用javac和javah时所在的目录 com\example\ndktest\jni\HelloWorld.java与com.example.ndktest.jni.HelloWorld必须一一对应
3. 生成的.h文件默认在javah执行时所在的目录，要更换目录可以使用
```
C:\Users\andygzyu\Desktop\NDKTest\src>javac com\example\ndktest\jni\HelloWorld.java

C:\Users\andygzyu\Desktop\NDKTest\src>javah -classpath . -jni com.example.ndktest.jni.HelloWorld // OK 

C:\Users\andygzyu\Desktop\NDKTest\src>javah -jni com.example.ndktest.jni.HelloWorld // OK TOO
```

## 有依赖的.h头文件生成

- 问题：jar包依赖问题
- 解决方法1：用分号";"隔开设置多个classpath，比如-classpath .;../../libs
```
D:\SVN\pub\Assistant_6.2_fabu\out\production\tassistant>javah -classpath .;../../../libs/wup.jar -jni com.tencent.assistant.protocol.scu.cscomm.CsCommManager
```
- 解决方法2：用-Djava.ext.dirs=设置libs目录
```
D:\SVN\pub\Assistant_6.2_fabu\out\production\tassistant>javah -classpath . -Djava.ext.dirs=../../../libs -jni com.tencent.assistant.protocol.scu.cscomm.CsCommMa
nager
```

- 问题:
```
错误: 找不到类android.content.Context。
```
- 解决：
```
在jni开发中，有时候需要传入一个Context类型参数到C层，在使用javah生成头文件的时候，会报 javah Class android.content.Context could not be found.这个错误，原因是找不到android.content.Context该类，解决方法是，把Context类型改为Object类型即可。
```

## Android.mk文件

```
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := analysisapk

LOCAL_C_INCLUDES += $(LOCAL_PATH)

LOCAL_C_INCLUDES += $(LOCAL_PATH)/src

LOCAL_CFLAGS := -fvisibility=hidden # 可以有效减少so大小，注意导出需要符号表的函数

LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -lz

APP_OPTIM := release

LOCAL_SRC_FILES += core.cpp

include $(BUILD_SHARED_LIBRARY) # 这个要写在最后
```

## 签名

| Java类型 | 类型签名 |
| ----------------------------- | -------------------------| 
| boolean |	Z |
| byte |	B |
| char |	C |
| short |	S |
| int	| I |
| long |	L |
| float | 	F |
| double |	D |
| 类	| L全限定名;，比如String, 其签名为Ljava/lang/util/String; |
| 数组	| [类型签名， 比如 [B |