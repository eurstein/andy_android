title: android

[TOC]

# 1. Android App的签名打包（晋级篇）
-----------------------------------------

Andriod应用程序如果要在手机或模拟器上安装，必须要有签名！ 

## 签名的意义
为了保证每个应用程序开发商合法ID，防止部分开放商可能通过使用相同的Package Name来混淆替换已经安装的程序，我们需要对我们发布的APK文件进行唯一签名，保证我们每次发布的版本的一致性(如自动更新不会因为版本不一致而无法安装)。


```
	文档中提到的默认证书信息如下：
	
	Keystore name: “debug.keystore”
	Keystore password: “android”
	Key alias: “androiddebugkey”
	Key password: “android”
	CN: “CN=Android Debug,O=Android,C=US”
```

## 签名的步骤
1. 创建key 
2. 使用步骤a中产生的key对apk签名

## 具体操作 命令行下对apk签名（原理）
1. 创建key，使用工具keytool.exe (位于jdk1.6.0_24jre\bin目录下)    
```
keytool -genkey -alias androiddebugkey -keyalg RSA -validity 40000 -keystore demo.keystore

说明：-genkey 产生密钥
     -alias androiddebugkey 别名 签名时需要
     -keyalg RSA 使用RSA算法对签名加密
     -validity 40000 有效期限4000天
     -keystore demo.keystore 产生的密钥库文件
```

2. 使用产生的key对apk签名，使用工具jarsigner.exe (位于jdk1.6.0_24\bin目录下)
    ```
    jarsigner -verbose -keystore demo.keystore -signedjar demo_signed.apk demo_unsigned.apk androiddebugkey

    说明：-verbose 输出签名的详细信息
         -keystore demo.keystore 密钥库文件
         -signedjar demo_signed.apk 签名后产生的签名包
          demo_unsigned.apk 未签名的包
          androiddebugkey 密钥库别名
    ```

3. 列出密钥库（keystore)中的条目 的命令 可以查看到密钥库别名
    ```
    E:\SVN\crash\5.2_main_base>keytool -list -v -keystore debug.keystore
    输入密钥库口令: // android密码

    密钥库类型: JKS
    密钥库提供方: SUN

    您的密钥库包含 1 个条目

    别名: androiddebugkey
    创建日期: 2013-9-19
    条目类型: PrivateKeyEntry
    证书链长度: 1
    证书[1]:
    所有者: CN=Android QZone Team, OU=Tencent Company, O=QZone Team of Tencent Company, L=Beijing City, ST=Beijing City, C=86
    发布者: CN=Android QZone Team, OU=Tencent Company, O=QZone Team of Tencent Company, L=Beijing City, ST=Beijing City, C=86
    序列号: 4c26cea2
    有效期开始日期: Sun Jun 27 12:08:02 CST 2010, 截止日期: Thu Jun 21 12:08:02 CST 2035
    证书指纹:
             MD5: A0:95:64:1B:30:78:5F:28:64:27:08:F4:81:60:3E:0B
             SHA1: 26:77:C0:F3:BC:06:B2:BB:62:7C:56:53:04:0E:6D:A8:B2:F5:E3:9C
             SHA256: 9C:28:6B:8B:EB:45:A6:BC:26:42:E2:E5:22:55:C7:F8:92:57:3A:7D:5D:A7:CB:45:98:C4:19:A4:6E:89:8D:36
             签名算法名称: SHA1withRSA
             版本: 3


    *******************************************
    *******************************************
    ```

4. 如何查看第三方应用或系统应用签名
    ```
    1. 解压出apk中的META-INF/CERT.RSA
    2. keytool -printcert -file META-INF/CERT.RSA

    E:\SVN\crash\5.2_main_base>keytool -printcert -file E:\SVN\crash\5.2_main_base\out\production\tassistant\CERT.RSA
    所有者: CN=Android QZone Team, OU=Tencent Company, O=QZone Team of Tencent Company, L=Beijing City, ST=Beijing City, C=86
    发布者: CN=Android QZone Team, OU=Tencent Company, O=QZone Team of Tencent Company, L=Beijing City, ST=Beijing City, C=86
    序列号: 4c26cea2
    有效期开始日期: Sun Jun 27 12:08:02 CST 2010, 截止日期: Thu Jun 21 12:08:02 CST 2035
    证书指纹:
             MD5: A0:95:64:1B:30:78:5F:28:64:27:08:F4:81:60:3E:0B
             SHA1: 26:77:C0:F3:BC:06:B2:BB:62:7C:56:53:04:0E:6D:A8:B2:F5:E3:9C
             SHA256: 9C:28:6B:8B:EB:45:A6:BC:26:42:E2:E5:22:55:C7:F8:92:57:3A:7D:5D:A7:CB:45:98:C4:19:A4:6E:89:8D:36
             签名算法名称: SHA1withRSA
             版本: 3
    ```

5. 验证apk包签名
    ```
    jarsigner -verify -verbose -certs new.apk
    ```
    
## 注意事项：
1. eclipse
    android工程的bin目录下的demo.apk默认是已经使用debug用户签名的，所以不能使用上述步骤对此文件再次签名。正确步骤应该是:在工程点击右键->Anroid Tools-Export Unsigned Application Package导出的apk采用上述步骤签名。
2. idea out目录下的xxx.apk和xxx.unaligned.apk都已经是签过名的包



```

http://hold-on.iteye.com/blog/2064642
参考：http://blog.k-res.net/archives/1229.html

解决：
按照android默认证书规范，更改项目的签名文件的密码，别名和别名密码。然后将 "Custom debug keystore" 设置为修改过后的签名文件
 
方式：
1. 首先当然是先复制一份正式证书出来作为要修改为的临时调试证书。
2. 修改keystore密码的命令(keytool为JDK带的命令行工具)：
keytool -storepasswd -keystore my.keystore
其中，my.keystore是复制出来的证书文件，执行后会提示输入证书的当前密码，和新密码以及重复新密码确认。这一步需要将密码改为android。
3. 修改keystore的alias：
keytool -changealias -keystore my.keystore -alias my_name -destalias androiddebugkey
这一步中，my_name是证书中当前的alias，-destalias指定的是要修改为的alias，这里按规矩来，改为androiddebugkey！这个命令会先后提示输入keystore的密码和当前alias的密码。
4. 修改alias的密码：
keytool -keypasswd -keystore my.keystore -alias androiddebugkey
这一步执行后会提示输入keystore密码，alias密码，然后提示输入新的alias密码，同样，按规矩来，改为android！
 
参考：http://blog.k-res.net/archives/1671.html
```


# 2. 安装失败
--------------------------------------

## 1. Installation error: INSTALL_FAILED_UID_CHANGED 的解决办法

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

## 2. Installation error: INSTALL_FAILED_INSUFFICIENT_STORAGE 的解决方法
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

# 3. Android内存分析
-----------------------------------------

## linux内存机制
由于Linux的内存共享机制，每个应用使用了多少内存，并不能很准确的计算。通常我们有下面几种方式来衡量

- VSS - Virtual Set Size 虚拟耗用内存（包含共享库占用的内存）
- RSS - Resident Set Size 实际使用物理内存（包含共享库占用的内存）
- PSS - Proportional Set Size 实际使用物理内存（比例分配共享库占用的内存）
- USS - Unique Set Size 进程独自占用的物理内存（不包含共享库占用的内存）

一般来说内存占用大小有如下规律：VSS >= RSS >= PSS >= USS. 一般我们会用PSS来作为内存大小的衡量值

## MAT
http://www.cnblogs.com/wisekingokok/archive/2011/11/30/2245790.html
_OQL:  select * from instanceof android.app.Activity_

```
// 开始 trace文件位置: /sdcard/cube.trace
Debug.startMethodTracing("cube");

// ...
// 其他的代码

// 停止
Debug.stopMethodTracing();
```

http://kingbo203.iteye.com/blog/1988636


# 4. 一个应用两个启动图标，理解android.intent.category.LAUNCHER 具体作用
-----------------------------------------

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




# 5. About ANR
-----------------------------------------

## traces.txt文件路径
/data/anr/traces.txt

## 两种提取方法
1. adb pull /data/anr/traces.txt c:\Users\andygzyu\Desktop\log\
    此方法可能会遇到如下错误，`failed to copy '/data/anr/traces.txt' to 'c:\Users\andygzyu\Desktop\log\': Permission denied`
2. adb shell "cd /data/anr && cat traces.txt" > C:\Users\andygzyu\Desktop\log\traces.txt
```
C:\Users\andygzyu>adb root
adb server is out of date.  killing...
* daemon started successfully *
restarting adbd as root
```

3. 主动生成traces.txt文件
```
$chmod 777 /data/anr
$rm /data/anr/traces.txt
$ps | grep -e USER -e qqdownloader
$kill -3 PID
adb pull data/anr/traces.txt ./mytraces.txt 
```


## 分析方法：
- CPU利用率很高时容易发生ANR，检查下高CPU使用率的线程
- ANR是因为主线程超时响应导致的，所以分析main
- 死锁 http://blog.csdn.net/oujunli/article/details/9102101


浅谈ANR及如何分析解决ANR

一：什么是ANR
ANR:Application Not Responding，即应用无响应
二：ANR的类型
ANR一般有三种类型：
1：KeyDispatchTimeout(5 seconds) --主要类型
按键或触摸事件在特定时间内无响应
2：BroadcastTimeout(10 seconds)
BroadcastReceiver在特定时间内无法处理完成
3：ServiceTimeout(20 seconds) --小概率类型
Service在特定的时间内无法处理完成
三：KeyDispatchTimeout
Akey or touch event was not dispatched within the specified time（按键或触摸事件在特定时间内无响应）
具体的超时时间的定义在framework下的
ActivityManagerService.java
//How long we wait until we timeout on key dispatching.
staticfinal int KEY_DISPATCHING_TIMEOUT = 5*1000
四：为什么会超时呢？
超时时间的计数一般是从按键分发给app开始。超时的原因一般有两种：
(1)当前的事件没有机会得到处理（即UI线程正在处理前一个事件，没有及时的完成或者looper被某种原因阻塞住了）
(2)当前的事件正在处理，但没有及时完成
五：如何避免KeyDispatchTimeout
1：UI线程尽量只做跟UI相关的工作
2：耗时的工作（比如数据库操作，I/O，连接网络或者别的有可能阻碍UI线程的操作）把它放入单独的线程处理
3：尽量用Handler来处理UIthread和别的thread之间的交互

六：UI线程
说了那么多的UI线程，那么哪些属于UI线程呢？
UI线程主要包括如下：
Activity:onCreate(), onResume(), onDestroy(), onKeyDown(), onClick(),etc
AsyncTask: onPreExecute(), onProgressUpdate(), onPostExecute(), onCancel,etc
Mainthread handler: handleMessage(), post*(runnable r), etc
other

七:如何去分析ANR
先看个LOG:
04-01 13:12:11.572 I/InputDispatcher( 220): Application is not responding:Window{2b263310com.android.email/com.android.email.activity.SplitScreenActivitypaused=false}.  5009.8ms since event, 5009.5ms since waitstarted
04-0113:12:11.572 I/WindowManager( 220): Input event dispatching timedout sending tocom.android.email/com.android.email.activity.SplitScreenActivity
04-01 13:12:14.123 I/Process(  220): Sending signal. PID: 21404 SIG: 3---发生ANR的时间和生成trace.txt的时间
04-01 13:12:14.123 I/dalvikvm(21404):threadid=4: reacting to signal 3 
……
04-0113:12:15.872 E/ActivityManager(  220): ANR in com.android.email(com.android.email/.activity.SplitScreenActivity)
04-0113:12:15.872 E/ActivityManager(  220): Reason:keyDispatchingTimedOut
04-0113:12:15.872 E/ActivityManager(  220): Load: 8.68 / 8.37 / 8.53
04-0113:12:15.872 E/ActivityManager(  220): CPUusage from 4361ms to 699ms ago ----CPU在ANR发生前的使用情况

04-0113:12:15.872 E/ActivityManager(  220):   5.5%21404/com.android.email: 1.3% user + 4.1% kernel / faults: 10 minor
04-0113:12:15.872 E/ActivityManager(  220):   4.3%220/system_server: 2.7% user + 1.5% kernel / faults: 11 minor 2 major
04-0113:12:15.872 E/ActivityManager(  220):   0.9%52/spi_qsd.0: 0% user + 0.9% kernel
04-0113:12:15.872 E/ActivityManager(  220):   0.5%65/irq/170-cyttsp-: 0% user + 0.5% kernel
04-0113:12:15.872 E/ActivityManager(  220):   0.5%296/com.android.systemui: 0.5% user + 0% kernel
04-0113:12:15.872 E/ActivityManager(  220): 100%TOTAL: 4.8% user + 7.6% kernel + 87% iowait
04-0113:12:15.872 E/ActivityManager(  220): CPUusage from 3697ms to 4223ms later:-- ANR后CPU的使用量
04-0113:12:15.872 E/ActivityManager(  220):   25%21404/com.android.email: 25% user + 0% kernel / faults: 191 minor
04-0113:12:15.872 E/ActivityManager(  220):    16% 21603/__eas(par.hakan: 16% user + 0% kernel
04-0113:12:15.872 E/ActivityManager(  220):    7.2% 21406/GC: 7.2% user + 0% kernel
04-0113:12:15.872 E/ActivityManager(  220):    1.8% 21409/Compiler: 1.8% user + 0% kernel
04-0113:12:15.872 E/ActivityManager(  220):   5.5%220/system_server: 0% user + 5.5% kernel / faults: 1 minor
04-0113:12:15.872 E/ActivityManager(  220):    5.5% 263/InputDispatcher: 0% user + 5.5% kernel
04-0113:12:15.872 E/ActivityManager(  220): 32%TOTAL: 28% user + 3.7% kernel

从LOG可以看出ANR的类型，CPU的使用情况，如果CPU使用量接近100%，说明当前设备很忙，有可能是CPU饥饿导致了ANR
如果CPU使用量很少，说明主线程被BLOCK了
如果IOwait很高，说明ANR有可能是主线程在进行I/O操作造成的
除了看LOG，解决ANR还得需要trace.txt文件，
如何获取呢？可以用如下命令获取
$chmod 777 /data/anr
$rm /data/anr/traces.txt
$ps
$kill -3 PID
adbpull data/anr/traces.txt ./mytraces.txt
从trace.txt文件，看到最多的是如下的信息：
-----pid 21404 at 2011-04-01 13:12:14 -----  
Cmdline: com.android.email

DALVIK THREADS:
(mutexes: tll=0tsl=0 tscl=0 ghl=0 hwl=0 hwll=0)
"main" prio=5 tid=1NATIVE
  | group="main" sCount=1 dsCount=0obj=0x2aad2248 self=0xcf70
  | sysTid=21404 nice=0 sched=0/0cgrp=[fopen-error:2] handle=1876218976
  atandroid.os.MessageQueue.nativePollOnce(Native Method)
  atandroid.os.MessageQueue.next(MessageQueue.java:119)
  atandroid.os.Looper.loop(Looper.java:110)
 at android.app.ActivityThread.main(ActivityThread.java:3688)
 at java.lang.reflect.Method.invokeNative(Native Method)
  atjava.lang.reflect.Method.invoke(Method.java:507)
  atcom.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:866)
 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:624)
 at dalvik.system.NativeStart.main(Native Method)
说明主线程在等待下条消息进入消息队列
八：Thread状态
ThreadState (defined at “dalvik/vm/thread.h “)
THREAD_UNDEFINED = -1, /* makes enum compatible with int32_t */
THREAD_ZOMBIE = 0, /* TERMINATED */
THREAD_RUNNING = 1, /* RUNNABLE or running now */
THREAD_TIMED_WAIT = 2, /* TIMED_WAITING in Object.wait() */
THREAD_MONITOR = 3, /* BLOCKED on a monitor */
THREAD_WAIT = 4, /* WAITING in Object.wait() */
THREAD_INITIALIZING= 5, /* allocated, not yet running */
THREAD_STARTING = 6, /* started, not yet on thread list */
THREAD_NATIVE = 7, /* off in a JNI native method */
THREAD_VMWAIT = 8, /* waiting on a VM resource */
THREAD_SUSPENDED = 9, /* suspended, usually by GC or debugger */

九：如何调查并解决ANR
1：首先分析log
2: 从trace.txt文件查看调用stack.
3: 看代码
4：仔细查看ANR的成因（iowait?block?memoryleak?）

十：案例
案例1：关键词:ContentResolver in AsyncTask onPostExecute, high iowait
Process:com.android.email
Activity:com.android.email/.activity.MessageView
Subject:keyDispatchingTimedOut
CPU usage from 2550ms to -2814ms ago:
5%187/system_server: 3.5% user + 1.4% kernel / faults: 86 minor 20major
4.4% 1134/com.android.email: 0.7% user + 3.7% kernel /faults: 38 minor 19 major
4% 372/com.android.eventstream: 0.7%user + 3.3% kernel / faults: 6 minor
1.1% 272/com.android.phone:0.9% user + 0.1% kernel / faults: 33 minor
0.9%252/com.android.systemui: 0.9% user + 0% kernel
0%409/com.android.eventstream.telephonyplugin: 0% user + 0% kernel /faults: 2 minor
0.1% 632/com.android.devicemonitor: 0.1% user + 0%kernel
100%TOTAL: 6.9% user + 8.2% kernel +84%iowait


-----pid 1134 at 2010-12-17 17:46:51 -----
Cmd line:com.android.email

DALVIK THREADS:
(mutexes: tll=0 tsl=0tscl=0 ghl=0 hwl=0 hwll=0)
"main" prio=5 tid=1 WAIT
|group="main" sCount=1 dsCount=0 obj=0x2aaca180self=0xcf20
| sysTid=1134 nice=0 sched=0/0 cgrp=[fopen-error:2]handle=1876218976
at java.lang.Object.wait(Native Method)
-waiting on <0x2aaca218> (a java.lang.VMThread)
atjava.lang.Thread.parkFor(Thread.java:1424)
atjava.lang.LangAccessImpl.parkFor(LangAccessImpl.java:48)
atsun.misc.Unsafe.park(Unsafe.java:337)
atjava.util.concurrent.locks.LockSupport.park(LockSupport.java:157)
atjava.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:808)
atjava.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:841)
atjava.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1171)
atjava.util.concurrent.locks.ReentrantLock$FairSync.lock(ReentrantLock.java:200)
atjava.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:261)
atandroid.database.sqlite.SQLiteDatabase.lock(SQLiteDatabase.java:378)
atandroid.database.sqlite.SQLiteCursor.<init>(SQLiteCursor.java:222)
atandroid.database.sqlite.SQLiteDirectCursorDriver.query(SQLiteDirectCursorDriver.java:53)
atandroid.database.sqlite.SQLiteDatabase.rawQueryWithFactory(SQLiteDatabase.java:1356)
atandroid.database.sqlite.SQLiteDatabase.queryWithFactory(SQLiteDatabase.java:1235)
atandroid.database.sqlite.SQLiteDatabase.query(SQLiteDatabase.java:1189)
atandroid.database.sqlite.SQLiteDatabase.query(SQLiteDatabase.java:1271)
atcom.android.email.provider.EmailProvider.query(EmailProvider.java:1098)
atandroid.content.ContentProvider$Transport.query(ContentProvider.java:187)
atandroid.content.ContentResolver.query(ContentResolver.java:268)
atcom.android.email.provider.EmailContent$Message.restoreMessageWithId(EmailContent.java:648)
atcom.android.email.Controller.setMessageRead(Controller.java:658)
atcom.android.email.activity.MessageView.onMarkAsRead(MessageView.java:700)
atcom.android.email.activity.MessageView.access$2500(MessageView.java:98)
atcom.android.email.activity.MessageView$LoadBodyTask.onPostExecute(MessageView.java:1290)
atcom.android.email.activity.MessageView$LoadBodyTask.onPostExecute(MessageView.java:1255)
atandroid.os.AsyncTask.finish(AsyncTask.java:417)
atandroid.os.AsyncTask.access$300(AsyncTask.java:127)
atandroid.os.AsyncTask$InternalHandler.handleMessage(AsyncTask.java:429)
atandroid.os.Handler.dispatchMessage(Handler.java:99)
atandroid.os.Looper.loop(Looper.java:123)
atandroid.app.ActivityThread.main(ActivityThread.java:3652)
atjava.lang.reflect.Method.invokeNative(Native Method)
atjava.lang.reflect.Method.invoke(Method.java:507)
atcom.android.internal.os.ZygoteIn
原因：IOWait很高，说明当前系统在忙于I/O，因此数据库操作被阻塞
原来：
        finalMessagemessage=Message.restoreMessageWithId(mProviderContext,messageId);

        if(message==null){

           return;

        }

        Accountaccount=Account.restoreAccountWithId(mProviderContext,message.mAccountKey);

        if(account==null){

           return;//isMessagingController returns false for null, but let's make itclear.

        }

        if(isMessagingController(account)){

           newThread(){

               @Override

               publicvoidrun(){

                  mLegacyController.processPendingActions(message.mAccountKey);

               }

           }.start();

        }


解决后：
newThread() {
        finalMessagemessage=Message.restoreMessageWithId(mProviderContext,messageId);

        if(message==null){

           return;

        }

        Accountaccount=Account.restoreAccountWithId(mProviderContext,message.mAccountKey);

        if(account==null){

           return;//isMessagingController returns false for null, but let's make itclear.

        }

        if(isMessagingController(account)) {

                  mLegacyController.processPendingActions(message.mAccountKey);

              

           }


}.start();
关于AsyncTask:http://developer.android.com/reference/android/os/AsyncTask.html

案例2：关键词:在UI线程进行网络数据的读写
ANRin process: com.android.mediascape:PhotoViewer (last incom.android.mediascape:PhotoViewer)
Annotation:keyDispatchingTimedOut
CPU usage:
Load: 6.74 / 6.89 / 6.12
CPUusage from 8254ms to 3224ms ago:
ovider.webmedia: 4% = 4% user +0% kernel / faults: 68 minor
system_server: 2% = 1% user + 0%kernel / faults: 18 minor
re-initialized>: 0% = 0% user + 0%kernel / faults: 50 minor
events/0: 0% = 0% user + 0%kernel
TOTAL:7% = 6% user + 1% kernel

DALVIKTHREADS:
""main"" prio=5 tid=3 NATIVE
|group=""main"" sCount=1 dsCount=0 s=Yobj=0x4001b240 self=0xbda8
| sysTid=2579 nice=0 sched=0/0cgrp=unknown handle=-1343993184
atorg.apache.harmony.luni.platform.OSNetworkSystem.receiveStreamImpl(NativeMethod)
atorg.apache.harmony.luni.platform.OSNetworkSystem.receiveStream(OSNetworkSystem.java:478)
atorg.apache.harmony.luni.net.PlainSocketImpl.read(PlainSocketImpl.java:565)
atorg.apache.harmony.luni.net.SocketInputStream.read(SocketInputStream.java:87)
atorg.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnection$LimitedInputStream.read(HttpURLConnection.java:303)
atjava.io.InputStream.read(InputStream.java:133)
atjava.io.BufferedInputStream.fillbuf(BufferedInputStream.java:157)
atjava.io.BufferedInputStream.read(BufferedInputStream.java:346)
atandroid.graphics.BitmapFactory.nativeDecodeStream(Native Method)
atandroid.graphics.BitmapFactory.decodeStream(BitmapFactory.java:459)
atcom.android.mediascape.activity.PhotoViewerActivity.getPreviewImage(PhotoViewerActivity.java:4465)
atcom.android.mediascape.activity.PhotoViewerActivity.dispPreview(PhotoViewerActivity.java:4406)
atcom.android.mediascape.activity.PhotoViewerActivity.access$6500(PhotoViewerActivity.java:125)
atcom.android.mediascape.activity.PhotoViewerActivity$33$1.run(PhotoViewerActivity.java:4558)
atandroid.os.Handler.handleCallback(Handler.java:587)
atandroid.os.Handler.dispatchMessage(Handler.java:92)
atandroid.os.Looper.loop(Looper.java:123)
atandroid.app.ActivityThread.main(ActivityThread.java:4370)
atjava.lang.reflect.Method.invokeNative(Native Method)
atjava.lang.reflect.Method.invoke(Method.java:521)
atcom.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:868)
atcom.android.internal.os.ZygoteInit.main(ZygoteInit.java:626)
atdalvik.system.NativeStart.main(Native Method)
关于网络连接，再设计的时候可以设置个timeout的时间或者放入独立的线程来处理。
关于Handler的问题，可以参考：http://developer.android.com/reference/android/os/Handler.html
案例3：
关键词：Memoryleak/Thread leak
11-1621:41:42.560 I/ActivityManager( 1190): ANR in process:android.process.acore (last in android.process.acore)
11-1621:41:42.560 I/ActivityManager( 1190): Annotation:keyDispatchingTimedOut
11-16 21:41:42.560 I/ActivityManager(1190): CPU usage:
11-16 21:41:42.560 I/ActivityManager( 1190):Load: 11.5 / 11.1 / 11.09
11-16 21:41:42.560 I/ActivityManager(1190): CPU usage from 9046ms to 4018ms ago:
11-16 21:41:42.560I/ActivityManager( 1190): d.process.acore:98%= 97% user + 0% kernel / faults: 1134 minor
11-16 21:41:42.560I/ActivityManager( 1190): system_server: 0% = 0% user + 0% kernel /faults: 1 minor
11-16 21:41:42.560 I/ActivityManager( 1190): adbd:0% = 0% user + 0% kernel
11-16 21:41:42.560 I/ActivityManager(1190): logcat: 0% = 0% user + 0% kernel
11-16 21:41:42.560I/ActivityManager( 1190): TOTAL:100% = 98% user + 1% kernel
Cmdline: android.process.acore

DALVIK THREADS:
"main"prio=5 tid=3 VMWAIT
|group="main" sCount=1 dsCount=0 s=N obj=0x40026240self=0xbda8
| sysTid=1815 nice=0 sched=0/0 cgrp=unknownhandle=-1344001376
atdalvik.system.VMRuntime.trackExternalAllocation(NativeMethod)
atandroid.graphics.Bitmap.nativeCreate(Native Method)
atandroid.graphics.Bitmap.createBitmap(Bitmap.java:468)
atandroid.view.View.buildDrawingCache(View.java:6324)
atandroid.view.View.getDrawingCache(View.java:6178)
atandroid.view.ViewGroup.drawChild(ViewGroup.java:1541)
……
atcom.android.internal.policy.impl.PhoneWindow$DecorView.draw(PhoneWindow.java:1830)
atandroid.view.ViewRoot.draw(ViewRoot.java:1349)
atandroid.view.ViewRoot.performTraversals(ViewRoot.java:1114)
atandroid.view.ViewRoot.handleMessage(ViewRoot.java:1633)
atandroid.os.Handler.dispatchMessage(Handler.java:99)
atandroid.os.Looper.loop(Looper.java:123)
atandroid.app.ActivityThread.main(ActivityThread.java:4370)
atjava.lang.reflect.Method.invokeNative(Native Method)
atjava.lang.reflect.Method.invoke(Method.java:521)
atcom.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:868)
atcom.android.internal.os.ZygoteInit.main(ZygoteInit.java:626)
atdalvik.system.NativeStart.main(Native Method)
"Thread-408"prio=5 tid=329 WAIT
|group="main" sCount=1 dsCount=0 s=N obj=0x46910d40self=0xcd0548
| sysTid=10602 nice=0 sched=0/0 cgrp=unknownhandle=15470792
at java.lang.Object.wait(Native Method)
-waiting on <0x468cd420> (a java.lang.Object)
atjava.lang.Object.wait(Object.java:288)
atcom.android.dialer.CallLogContentHelper$UiUpdaterExecutor$1.run(CallLogContentHelper.java:289)
atjava.lang.Thread.run(Thread.java:1096)
分析：
atdalvik.system.VMRuntime.trackExternalAllocation(NativeMethod)内存不足导致block在创建bitmap上
**MEMINFO in pid 1360 [android.process.acore] **
native dalvik other total
size: 17036 23111 N/A 40147
allocated: 16484 20675 N/A 37159
free: 296 2436 N/A 2732
解决：如果机器的内存族，可以修改虚拟机的内存为36M或更大，不过最好是复查代码，查看哪些内存没有释放




# 6. About Crash
-----------------------------------------

android.view.GLES20Canvas.nDrawDisplayList(NativeMethod)
画太多view造成的错误，一般伴随着ANR出现


I'musingNDKr5,sotheexecutableI'musingislocatedat$NDK/toolchains/arm-linux-androideabi-4.4.3/prebuilt/linux-x86/bin;makesurethatisinyour$PATH.Thecommandtouselookslike
`arm-linux-androideabi-addr2line-C-f-eobj/local/armeabi/libXXX.so<address>`
Or,forthecaseabove
中国|
`arm-linux-androideabi-addr2line-C-f-eobj/local/armeabi/libnativemaprender.so0003deb4`



还有三篇自认为有价值的KM文章，嘿嘿
http
中国|//km.oa.com/group/22595/articles/show/194041?kmref=searchAndroid常见错误分析及整理
http
中国|//km.oa.com/group/21451/articles/show/203458?kmref=searchAndroid侧crash问题汇总分析
http
中国|//km.oa.com/group/1746/articles/show/206640?kmref=author_post从汇编层面精准定位AndroidNative层的crash点


# 7. android usb调试模式
-----------------------------------------

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



# 8. Native crash analysis
-----------------------------------------

## 提示NKD中有很全的例子和文档
android-ndk-r10d/docs/Start_Here.html
android-ndk-r10d/docs/Programmers_Guide/html/md_3__key__topics__building__s_t_a_n_d_a_l_o_n_e-_t_o_o_l_c_h_a_i_n.html

## 两个so库区别（关键，一定要同事保留带symbol的so库，和对应的源代码，否则将来出现问题，将不能通过Crash地址找到对应代码行）
1. obj\local\armeabi\xxx.so     含有符号表和调试信息的动态链接库 调试用 (包含所有extern/外部函数的符号表，不包含内部/静态static函数)
2. libs\armeabi\xxx.so     strip过，不含有符号表的动态链接库 发布用 （没有选项的 strip 命令除去行号信息、重定位信息、符号表、调试段、typchk 段和注释段。）

## 两个分析工具
路径D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin

1. i686-linux-android-addr2line.exe
根据地址查找代码行
    - obj\local\armeabi\xxx.so含有符号表的so，可以还原出代码行
```
D:\DevelopTools\android-ndk-r10d>D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-addr2line.exe -C -f -e E:\SV
N\TMAssistantSDK_proj\user\andygzyu\NativeExceptionHandler\obj\local\armeabi\libnativeexceptionhandler.so 0100C
_JavaVM::AttachCurrentThread(_JNIEnv**, void*)
D:/DevelopTools/android-ndk-r10d/platforms/android-21/arch-arm/usr/include/jni.h:1091
```

    - libs\armeabi\xxx.so不含有符号表，不能还原出代码行
```
D:\DevelopTools\android-ndk-r10d>D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-addr2line.exe -C -f -e E:\SV
N\TMAssistantSDK_proj\user\andygzyu\NativeExceptionHandler\libs\armeabi\libnativeexceptionhandler.so 0100C
_JNIEnv::CallStaticVoidMethod(_jclass*, _jmethodID*, ...)
??:?
```

2. i686-linux-android-nm.exe
查看指定程序中的符号表相关内容的工具
    - obj\local\armeabi\xxx.so含有符号表
```
D:\DevelopTools\android-ndk-r10d>D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-nm.exe E:\SVN\TMAssistantSDK
_proj\user\andygzyu\NativeExceptionHandler\libs\armeabi\libnativeexceptionhandler.so
D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-nm.exe: E:\SVN\TMAssistantSDK_proj\user\andygzyu\NativeExcept
ionHandler\libs\armeabi\libnativeexceptionhandler.so: no symbols
```
    - libs\armeabi\xxx.so不含有符号表
```
D:\DevelopTools\android-ndk-r10d>D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-nm.exe E:\SVN\TMAssistantSDK
_proj\user\andygzyu\NativeExceptionHandler\obj\local\armeabi\libnativeexceptionhandler.so
00000e64 t $a
00000e54 t $a
00001bc4 t $a
00001a78 t $a
00001bf0 t $a
00001c1c t $a
00001b98 t $a
000000a4 N $d
00000f8c t $d
```

## 编译参数对so的影响
1. LOCAL_CFLAGS := -funwind-tables  有无这个对编译结果没影响
2. APP_OPTIM := debug/release  debug设置的so较大，且在obj\local\armeabi\xxx.so中会包含static方法的符号表，release设置的so较小，仅有public方法的符号表
3. APP_PLATFORM := android-7 支持的最低平台版本号
4. APP_ABI := armeabi armeabi-v7a arm64-v8a 目标应用程序二进制接口，支持那种arm的架构
5. cmd-strip = $(TOOLCHAIN_PREFIX)strip --strip-debug -x $1 （-x 除去符号表信息，但并不除去静态或外部符号信息。-x 标志同时除去重定位信息，因此将不可能链接到该文件。）
6. LOCAL_CFLAGS +=-fvisibility=hidden属性，
    1. 隐藏非导出函数的符号，防止其他同名函数错链到当前库。
    2. 在设置“5.cmd-strip”的情况下，影响libs/armeabi/xxx.so，在其中，非导出（非JNIEXPORT）的全局函数符号将会被移除

    GCC扩展 `__attribute__ ((visibility("hidden")))`
    http://liulixiaoyao.blog.51cto.com/1361095/814329
    试想这样的情景，程序调用某函数A，A函数存在于两个动态链接库liba.so,libb.so中，并且程序执行需要链接这两个库，此时程序调用的A函数到底是来自于a还是b呢？
    这取决于链接时的顺序，比如先链接liba.so，这时候通过liba.so的导出符号表就可以找到函数A的定义，并加入到符号表中，链接libb.so的时候，符号表中已经存在函数A，就不会再更新符号表，所以调用的始终是liba.so中的A函数
    
    这里的调用严重的依赖于链接库加载的顺序，可能会导致混乱；gcc的扩展中有如下属性__attribute__ ((visibility("hidden")))，可以用于抑制将一个函数的名称被导出，对连接该库的程序文件来说，该函数是不可见的，使用的方法如下：
    -fvisibility=default|internal|hidden|protected
    gcc的visibility是说，如果编译的时候用了这个属性，那么动态库的符号都是hidden的，除非强制声明。
    1.创建一个c源文件，内容简单
    ```
    #include<stdio.h>  
    #include<stdlib.h>  
     
    __attribute ((visibility("default"))) /* jni中使用JNIEXPORT */
    void not_hidden ()  
    {  
    printf("exported symbol\n");  
    }  
     
    void is_hidden ()  
    {  
    printf("hidden one\n");  
    }  
    ```
    想要做的是，第一个函数符号可以被导出，第二个被隐藏。
    先编译成一个动态库，使用到属性-fvisibility
    ```
    gcc -shared -o libvis.so -fvisibility=hidden vis.c 
    ```
    现在查看
    ```
    # readelf -s libvis.so |grep hidden  
    7: 0000040c 20 FUNC GLOBAL DEFAULT 11 not_hidden  
    48: 00000420 20 FUNC LOCAL HIDDEN 11 is_hidden  
    51: 0000040c 20 FUNC GLOBAL DEFAULT 11 not_hidden  
    ```
    可以看到，属性确实有作用了。
    现在试图link
    ```
    vi main.c  
    int main()  
    {  
    not_hidden();  
    is_hidden();  
    return 0;  
    } 
    ```
    试图编译成一个可执行文件，链接到刚才生成的动态库，
    ```
    gcc -o exe main.c -L ./ -lvis 
    ```
    结果提示：
    ```
    /tmp/cckYTHcl.o: In function `main':  
    main.c:(.text+0x17): undefined reference to `is_hidden'  
    ```
    说明了hidden确实起到作用了。

## 如何理解tomb文件
tomb日志关键的行：ip 00004000  sp 556ec7a8  lr 4007aa99  pc 408175b0  cpsr 60800030
1. 理解APCS(ARM Procedure Call Standard)--ARM过程调用标准 http://blog.csdn.net/keyboardota/article/details/6799054
在ARM体系的CPU里有37个寄存器，其中可见的寄存器有15个通用寄存器（叫r0-r14），还有其它的状态寄存器和一个程序计数器（叫r15 或者是pc）。
在汇编里需要使用上面提到的通用寄存器保存各种数据，如果寄存器不够用，还会使用堆栈保存数据。APCS就是规定各个过程如何使用这些寄存器和堆栈，从而保证过程在相互调用的时候可以正常工作。

|r0 |r1 |r2 |r3 |r4 |r5 |r6 |r7 |r8 |r9 |r10 |r11 |r12 |r13 |r14 |r15 |  文件架（堆栈） |
|---|---|---|---|---|---|---|---|---|---|--- |--- |--- |--- |--- |--- | -- |
|a1 |a2 |a3 |a4 |v1 |v2 |v3 |v4 |v5 |v6 | v7 | v8 | ip | sp | lr | pc |  文件架（堆栈） |
| r0-r3 可随便动 |||| r4-r11 不可随便动 ||||||||ip|sp栈指针| lr 链接寄存器| pc 程序计数器 |可用来存放让r4-r11的拷贝|

* pc: 是程序计数器，存储将要执行的指令地址
* lr: 是链接寄存器，假如pc执行完成后，没有跳其他地方，则返回到lr
* sp: 堆栈指针，记录当前文件架堆栈指针
* ip:
* 第1个抽屉到第4个抽屉（r0~r3）：虽然是可以随便用，不过说不定上个人留了什么东西我自己需要的呢，先不动
* 第5个抽屉到第12个抽屉（r4~r11）：是别人的东西，一会要搬到文件架上的
* 第14个抽屉 sp：记录的就是文件架的使用位置
* 第15个抽屉 lr：上一个使用者的名字
* 第16个抽屉 pc：不能乱动的，叫别人过来的时候才用
    @----------------------------------------------------
    @程序刚开始，
    @r0到r3(a1到a4)可能会有参数，不能动
    @r4到r11(v1到v8)是父函数的东西，需要保留，不能动
    @r13(SP)是堆栈指针
    @r14(lr)是返回地址
    @r15(PC)更是不能随便动能

    mov ip,sp @将Sp(堆栈指针，r13)放在ip中，ip就是r12,目前唯一可以使用的寄存器
    sub sp,sp, #12 @ 堆栈指针减12，开出相当于3个寄存器空间的堆栈。
     
    str lr, [sp] @r14入栈，里面是返回地址
    str ip, [sp, #4] @ r12入栈，里面是父函数的堆栈指针
    str fp, [sp, #8] @ r11入栈，里面是需要为父函数保留的东西，一会要用r11，所以将它入栈
     
    sub fp, ip, #4  @ r11(就是fp)等于ip+4，就是自己的栈底，以后可以方便地使用堆栈
     
    @添加自己的代码
     
    @下面是准备退出了，注意堆栈使用时是基于fp寻址的，
    @因为此时SP可能在自己的代码中调整过，不可靠。
    ldr lr, [fp, #-8] @ lr出栈，里面是返回地址
    ldr ip, [fp, #-4] @ ip出栈，里面是父函数的堆栈指针
    ldr fp, [fp,#0]   @fp出栈，里面是需要为父函数保留的东西
    mov sp, ip    @令sp等于ip，帮父函数恢复堆栈指针
    mov pc , lr    @令pc等于lr，就是等于返回地址，退出子函数，返回到父函数。
    @------------------------------------

## 如何分析tombstone文件，找出对应crash的代码行
1. 如何获取tombstone文件（存放至手机目录 /data/tombstones/ 中）
native crash时，会把Core dump写入文件/data/tombstones/tombstone_xx中，可以通过如下方法取出（需要root权限）
    1. 拷贝值可读写目录
    ```
    adb shell
    su
    cd /data/tombstones/
    ls
    cp tombstone_00 /sdcard 
    ```
    2. 取出到pc
    ```
    adb pull /sdcard/tombstone_00 c:\Users\andygzyu\Desktop\log\tombstone1.txt
    ```
2. 如何查找crash代码行
举例分析如下tombstone文件
```
*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
Build fingerprint: 'samsung/ja3gzs/ja3g:4.4.2/KOT49H/I9500ZSUDNB3:user/release-keys'
Revision: '10'
pid: 22815, tid: 22971, name: Thread-1342  >>> com.example.nativeexceptionhandler <<<
signal 8 (SIGFPE), code -6 (SI_TKILL), fault addr 0000591f
    r0 00000000  r1 000059bb  r2 00000008  r3 00000000
    r4 00000008  r5 00000000  r6 000059bb  r7 0000010c
    r8 7b19cb10  r9 794f3fd4  sl 77b30810  fp 7b19cb24
    ip 794f3fdc  sp 7b19cac8  lr 400dc0e1  pc 400eb2d0  cpsr 000f0010
    d0  6172632074696e69  d1  414b4f2064616f6e
    d2  73e97a4873e97969  d3  73e97c9073e97b74
    d4  0000000000000000  d5  3f80000000000000
    d6  3f80000000000000  d7  0000000080000000
    d8  0000000000000000  d9  0000000000000000
    d10 0000000000000000  d11 0000000000000000
    d12 0000000000000000  d13 0000000000000000
    d14 0000000000000000  d15 0000000000000000
    d16 ffffffffffffffff  d17 0000000000000004
    d18 0000000000000000  d19 404ac71c80000000
    d20 404ac71c80000000  d21 4058000000000000
    d22 3fe1da1300000000  d23 c038000000000000
    d24 0000000000000000  d25 0000000000000000
    d26 3ff0000000000000  d27 0000000000000000
    d28 3ff0000000000000  d29 0000000000000000
    d30 0000000000000000  d31 c042000000000000
    scr 60000010

backtrace:
    #00  pc 000222d0  /system/lib/libc.so (tgkill+12)
    #01  pc 000130dd  /system/lib/libc.so (pthread_kill+48)
    #02  pc 000132f1  /system/lib/libc.so (raise+10)
    #03  pc 00000fc8  /data/app-lib/com.example.nativeexceptionhandler-10/libnativeexceptionhandler.so (__aeabi_ldiv0+8)
    #04  pc 00000dd9  /data/app-lib/com.example.nativeexceptionhandler-10/libnativeexceptionhandler.so (init(_JNIEnv*, _jclass*)+40)
    #05  pc 0001e84c  /system/lib/libdvm.so (dvmPlatformInvoke+112)
```
    1. 找到我们最关心的，也就是和我们so相关的行
    ```
    #04  pc 00000dd9  /data/app-lib/com.example.nativeexceptionhandler-10/libnativeexceptionhandler.so (init(_JNIEnv*, _jclass*)+40)
    ```
    其中00000dd9就是对应crash代码在库（libnativeexceptionhandler.so）中的地址
    2. 把地址转换成对应代码行
    ```
    C:\Users\andygzyu>D:\DevelopTools\android-ndk-r10d\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-addr2line.exe -C -f -e E:\SVN\TMAssistantSD
K_proj\user\andygzyu\NativeExceptionHandler\libs\armeabi\libnativeexceptionhandler.so 00000dd9
init
E:\SVN\TMAssistantSDK_proj\user\andygzyu\NativeExceptionHandler/jni/NativeExceptionHandler.cc:227
    ```
    （注意:此处需要使用带符号表的so，否者找不出对应代码行）。
        1. 为了便于测试，我们的“libs\armeabi\libnativeexceptionhandler.so”实际上是从未处理的obj\local\armeabi\libnativeexceptionhandler.so直接拷贝过来的
        2. 为了确定是使用某个库时crash，在build apk的时候，我们将libs\arm64-v8a和armeabi-v7a都删除了


## Android信号定义和行为
1. Android信号定义
所有的符合Unix规范（如POSIX）的系统都统一定义了SIGNAL的数量、含义和行为。 作为Linux系统，Android自然不会更改SIGNAL的定义。在Android代码中，signal的定义在 signal.h (D:\DevelopTools\android-ndk-r10d\platforms\android-19\arch-arm\usr\include\asm\signal.h)中：
```
/* Signals.  */  
#define SIGHUP      1   /* Hangup (POSIX).  */  
#define SIGINT      2   /* Interrupt (ANSI).  */  
#define SIGQUIT     3   /* Quit (POSIX).  */  
#define SIGILL      4   /* Illegal instruction (ANSI).  */  
#define SIGTRAP     5   /* Trace trap (POSIX).  */  
#define SIGABRT     6   /* Abort (ANSI).  */  
#define SIGIOT      6   /* IOT trap (4.2 BSD).  */  
#define SIGBUS      7   /* BUS error (4.2 BSD).  */  
#define SIGFPE      8   /* Floating-point exception (ANSI).  */  
#define SIGKILL     9   /* Kill, unblockable (POSIX).  */  
#define SIGUSR1     10  /* User-defined signal 1 (POSIX).  */  
#define SIGSEGV     11  /* Segmentation violation (ANSI).  */  
#define SIGUSR2     12  /* User-defined signal 2 (POSIX).  */  
#define SIGPIPE     13  /* Broken pipe (POSIX).  */  
#define SIGALRM     14  /* Alarm clock (POSIX).  */  
#define SIGTERM     15  /* Termination (ANSI).  */  
#define SIGSTKFLT   16  /* Stack fault.  */  
#define SIGCLD      SIGCHLD /* Same as SIGCHLD (System V).  */  
#define SIGCHLD     17  /* Child status has changed (POSIX).  */  
#define SIGCONT     18  /* Continue (POSIX).  */  
#define SIGSTOP     19  /* Stop, unblockable (POSIX).  */  
#define SIGTSTP     20  /* Keyboard stop (POSIX).  */  
#define SIGTTIN     21  /* Background read from tty (POSIX).  */  
#define SIGTTOU     22  /* Background write to tty (POSIX).  */  
#define SIGURG      23  /* Urgent condition on socket (4.2 BSD).  */  
#define SIGXCPU     24  /* CPU limit exceeded (4.2 BSD).  */  
#define SIGXFSZ     25  /* File size limit exceeded (4.2 BSD).  */  
#define SIGVTALRM   26  /* Virtual alarm clock (4.2 BSD).  */  
#define SIGPROF     27  /* Profiling alarm clock (4.2 BSD).  */  
#define SIGWINCH    28  /* Window size change (4.3 BSD, Sun).  */  
#define SIGPOLL     SIGIO   /* Pollable event occurred (System V).  */  
#define SIGIO       29  /* I/O now possible (4.2 BSD).  */  
#define SIGPWR      30  /* Power failure restart (System V).  */  
#define SIGSYS      31  /* Bad system call.  */  
#define SIGUNUSED   31  
```
2. 查看linux所支持的信号： kill -l
```
root@ja3g:/data/tombstones # kill -l
kill -l
 1    HUP Hangup                        17   CHLD Child exited
 2    INT Interrupt                     18   CONT Continue
 3   QUIT Quit                          19   STOP Stopped (signal)
 4    ILL Illegal instruction           20   TSTP Stopped
 5   TRAP Trap                          21   TTIN Stopped (tty input)
 6   ABRT Aborted                       22   TTOU Stopped (tty output)
 7    BUS Bus error                     23    URG Urgent I/O condition
 8    FPE Floating point exception      24   XCPU CPU time limit exceeded
 9   KILL Killed                        25   XFSZ File size limit exceeded
10   USR1 User signal 1                 26 VTALRM Virtual timer expired
11   SEGV Segmentation fault            27   PROF Profiling timer expired
12   USR2 User signal 2                 28  WINCH Window size changed
13   PIPE Broken pipe                   29     IO I/O possible
14   ALRM Alarm clock                   30    PWR Power failure
15   TERM Terminated                    31    SYS Bad system call
16 STKFLT Stack fault
```
3. 进程对信号的响应
    1. 忽略信号，即对信号不做任何处理，其中，有两个信号不能忽略：SIGKILL及SIGSTOP；
    2. 捕捉信号。定义信号处理函数，当信号发生时，执行相应的处理函数；
    3. 执行缺省操作，Linux对每种信号都规定了默认操作，注意，进程对实时信号的缺省反应是进程终止。

4. 常用信号类型

|信号名称  |  值  | 说明 |
| -------- | ---- | ---- |
|SIGHUP  |1 |  终端挂起或控制进程终止。当用户退出Shell时，由该进程启动的所有进程都会收到这个信号，默认动作为终止进程。|
|SIGINT  |2 |  键盘中断。当用户按下<Ctrl+C>组合键时，用户终端向正在运行中的由该终端启动的程序发出此信号。默认动作为终止进程。 |
|SIGQUIT |3 |  键盘退出键被按下。当用户按下<Ctrl+D>或<Ctrl+\>组合键时，用户终端向正在运行中的由该终端启动的程序发出此信号。默认动作为退出程序。 |
|SIGILL  |4 | invalid program image, such as invalid instruction
|SIGABRT |6 | abnormal termination condition, as is e.g. initiated by abort()
|SIGFPE  |8 |  发生致命的运算错误时发出。不仅包括浮点运算错误，还包括溢出及除数为0等所有的算法错误。默认动作为终止进程并产生core文件。|
|SIGKILL |9 |  无条件终止进程。进程接收到该信号会立即终止，不进行清理和暂存工作。该信号不能被忽略、处理和阻塞，它向系统管理员提供了可以杀死任何进程的方法。|
|SIGSEGV |11| invalid memory access (segmentation fault)|
|SIGALRM |14|  定时器超时，默认动作为终止进程。|
|SIGTERM |15|  程序结束信号，可以由 kill 命令产生。与SIGKILL不同的是，SIGTERM 信号可以被阻塞和终止，以便程序在退出前可以保存工作或清理临时文件等。|


## 几个警告
1. 去除警告 Android NDK: WARNING: APP_PLATFORM android-14 is larger than android:minSdkVersion 8
使用ndk-build编译项目的时候会看到一个警告“Android NDK: WARNING: APP_PLATFORM android-14 is larger than android:minSdkVersion 8”，虽然”不怎么”影响结果，看着碍眼

    - 解决方法
        在项目里的jni/Application.mk文件里加入一行
        ```
        APP_PLATFORM := android-8
        ```
        即可.

    - 为什么会有这个警告?
        在android上项目里，可以在AndroidManifest.xml中写入

        ```
        <uses-sdk android:minSdkVersion="8" android:targetSdkVersion="17"/>
        ```
        来表示程序可以运行的最低android设备是android 2.2(API Version 8), 经过详细测试的目标android版本是android 4.2.2(API Version 17).这里定义的是Java API Version

    - 再来看一下ndk(版本r8e)目录下的platforms文件夹,可以看到
        ```
        android-3
        android-4
        android-5
        android-8
        android-9
        android-14
        ```
        一共有6个文件夹,分别表示相应的Native API Version

        看到这里就明白了,那个警告的意思就是说,使用的Native API Version比最低版本Java API要高,可能导致的问题就是:
        在Native Code里使用了一个platforms/android-14下的API函数,然后程序在 android-8 的设备上运行,当然这个函数在android-8设备上是不存在的,就会崩溃了

    - 为什么Native API的版本数量会少于Java API?
        因为android在版本升级的时候,有时候只升级了Java层的API,而Native层的却没有变化
        
2. 不支持的源文件后缀警告
```
Android NDK: WARNING: Unsupported source file extensions in jni/Android.mk for module yyb_cscomm    
Android NDK:   LOCAL_CFLAGS := -fvisibility=hidden       
```
警告的意思是说有不支持的源文件后缀

- 问题原因
```
LOCAL_SRC_FILES := \
    jce/Jce_c.c \
    jce/wup_c.c \
    jce/interface.c \
    jce/dataparser.c \
    jce/cmd.c \
    utils.c \
    ioapi.c \
    unzip.c \
    readcert.c \
    teacryptor.c \
    com_tencent_assistant_protocol_scu_cscomm_CsCommManager.c \
LOCAL_CFLAGS := -fvisibility=hidden
```
注意到最后一行，“LOCAL_CFLAGS := -fvisibility=hidden”紧接着上一行的反斜杠"\"，所以它也是LOCAL_SRC_FILES的一部分，被当成了一个源文件
- 修改方法(加空行，分开LOCAL_CFLAGS := -fvisibility=hidden和LOCAL_SRC_FILES)
```
LOCAL_SRC_FILES := \
    jce/Jce_c.c \
    jce/wup_c.c \
    jce/interface.c \
    jce/dataparser.c \
    jce/cmd.c \
    utils.c \
    ioapi.c \
    unzip.c \
    readcert.c \
    teacryptor.c \
    com_tencent_assistant_protocol_scu_cscomm_CsCommManager.c \

LOCAL_CFLAGS := -fvisibility=hidden
```



# 9. other
----------------------------------------

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

## Log打印调用栈信息
	Log.d("andygzyu", "notifyApkSwapFinished stack = " + Log.getStackTraceString(new Throwable()));


## Uri 通用资源标志符（Universal Resource Identifier, 简称"URI"）
ContentUris
UriMatcher

## android.os.IBinder
http://blog.csdn.net/luoshengyang/article/details/6642463


## 名词解释
IPC （Inter-Process Communication）进程间通信
AIDL（Android Interface Definition Language）Android接口定义语言
JNI (Java Native Interface) JAVA本地调用



# 10. 
-----------------------------------------
