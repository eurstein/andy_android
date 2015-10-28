title: Native crash analysis

# Native crash analysis
[TOC]

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