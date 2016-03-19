[TOC]


# Eclipse setting

- Eclipse自动补全功能和自动生成作者、日期注释等功能设置
http://www.eoeandroid.com/thread-178374-1-1.html

- Eclipse快捷键
    * alt+shift+j 鼠标点击空白处，使用alt+shift+j 快捷键产生作者的名字。
    * ctrl+m 最大化/最小化
    * ctrl+shift+r 搜索类 
    * alt+shift+r 重命名
    * ctrl+k 查找选中内容
    * ctrl+shift+k 反向查找选中内容
    * ctrl+e 快速显示当前Editer的下拉列表
    * ctrl+o 快速显示 OutLine
    * ctrl+shift+o 组织导入
    * ctrl+shift+r 打开资源

- 显示Android SDK and AVD Manager菜单
Window -> Customize Perspective -> Command Groups availability -> Available command groups -> 勾选Android SDK and AVD Manager

- eclipse闪退问题
删掉 /.metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi


# android开发环境配置

- ecplise 代理 web-proxyhk.oa.com:8080

- 环境变量
JAVA_HOME=D:\DevelopTools\java\jre8
path=%JAVA_HOME%\bin;%path%
classpath=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar

- 设置工程使用的sdk版本

- 插件
SVN http://subclipse.tigris.org/update_1.10.x
Eclipse Class Decompiler http://feeling.sourceforge.net/update


# ndk 开发环境配置

1. 下载安装ndk https://developer.android.com/tools/sdk/ndk/index.html
2. 下载安装eclipe
3. 下载安装ADT-23.0.6.zip，要选择安装所有的
4. 设置NDK目录，eclipse -> Preferences -> Android -> NDK(遇到找不到的情况时，重新安装ADT)
5. 配置NDK build环境
	- Run -> External Tools -> External Tools Configurations -> New
	- JavaH设置 ***ndk_eclipse/JavaH设置.jpg***
		* Location: C:\Program Files\Java\jdk1.8.0_45\bin\javah.exe
		* Working Directory: ${project_loc}\src
		* Arguments: -classpath ${project_loc}\bin\classes -d ${project_loc}\jni -jni ${java_type_name}
	- NDK64_10d设置 ***ndk_eclipse/NDK64_10d.jpg***
		* Location: C:\Android\android-ndk-r10d\ndk-build.cmd (不能有空格)
		* Working Directory: ${project_loc}
		* Arguments: 不填
	- Refresh和Common设置见***ndk_eclipse/Setting1.jpg***和***Setting2.jpg***
