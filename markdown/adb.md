title: adb 常用命令

## adb 常用命令

[TOC]
                                
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
| adb install -r package| 更新安装 |
| adb shell am force-stop com.tencent.android.qqdownloader||
| adb shell ps | 查看进程列表 |
| adb shell ps -x [PID] | 查看指定进程状态 |
| adb shell top | 查看内存（VSS RSS）和CPU使用情况 |
| adb shell dumpsys meminfo <package_name> | |



# Android内存分析
由于Linux的内存共享机制，每个应用使用了多少内存，并不能很准确的计算。通常我们有下面几种方式来衡量

- VSS - Virtual Set Size 虚拟耗用内存（包含共享库占用的内存）
- RSS - Resident Set Size 实际使用物理内存（包含共享库占用的内存）
- PSS - Proportional Set Size 实际使用物理内存（比例分配共享库占用的内存）
- USS - Unique Set Size 进程独自占用的物理内存（不包含共享库占用的内存）

一般来说内存占用大小有如下规律：VSS >= RSS >= PSS >= USS. 一般我们会用PSS来作为内存大小的衡量值


# MAT
http://www.cnblogs.com/wisekingokok/archive/2011/11/30/2245790.html
_OQL:  select * from instanceof android.app.Activity_

http://kingbo203.iteye.com/blog/1988636