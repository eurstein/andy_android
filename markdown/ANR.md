title: About ANR

# About ANR
[TOC]

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