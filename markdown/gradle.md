title: Android Studio and Gradle Configure

## Android Studio and Gradle Configure
[TOC]

1. 代理设置
	1. 全局代理: File -> Settings -> Appearance & Behavior -> System Settings -> HTTP Proxy
	2. C:\Android\android-sdk(sdk_home) -> SDK Manager.exe -> Tools -> Options...

2. [手动下载gradle](http://www.cnblogs.com/smyhvae/p/4456420.html)
	1. 打开Android Studio内置终端，输入如下命令`gradlew -v`,实际上是调用了项目根目录下的**gradlew.bat**脚本
	2. 确定下载目录：首次输入`gradlew -v`，脚本会下载gradlew，对应目录在C:\Users\andygzyu\.gradle\wrapper\dists\gradle-2.4-all\3i2gobhdl0fm2tosnn15g540i0下
	3. gradlew下载地址：可以在MyFirstApp/gradle/wrapper/gradle-wrapper.properites文件中找到
	>distributionUrl=https\://services.gradle.org/distributions/gradle-2.4-all.zip
	4. 将下载的`gradle-2.4-all.zip`拷贝到`C:\Users\andygzyu\.gradle\wrapper\dists\gradle-2.4-all\3i2gobhdl0fm2tosnn15g540i0`
	5. 再次执行`gradlew -v`

3. [Android Support Repository](http://stackoverflow.com/questions/18025942/how-do-i-add-a-library-android-support-v7-appcompat-in-intellij-idea)
```
	dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
//        testCompile 'junit:junit:4.12'
        compile 'com.android.support:appcompat-v7:22.+'
        compile 'com.android.support:design:22.+'
	}
```
	Android Studio若使用到了support library，必须下载Android Support Repository
	1. 下载步骤路径: C:\Android\android-sdk(sdk_home) -> SDK Manager.exe -> Extras -> Android Support Repository
	2. 下载后的存储路径：C:\Android\android-sdk\extras\android\m2repository
	PS: mac系统下打开SDK Manager和AVD Manager的方法，
```
cd /Applications/Android/android-sdk/tools
chmod +x android
./android sdk
./android avd
```

4. Run the app from a command line (gradle build command line)
	1. 打开Android Studio内置终端
	2. 执行`gradlew.bat assembleDebug --offline`
	3. 解决如下错误
	```
	No cached version of com.android.tools.build:gradle:1.3.0 available for offline mode.
	```
	[修改MyFirstApp/gradle/build.gradle,设置Android Support Repository本地路径](https://code.google.com/p/android/issues/detail?id=58151)
	```
	buildscript {
	    repositories {
	//        jcenter()
	//        maven{
	//            url "http://maven.oa.com/nexus/content/repositories/android"
	//        }
	        maven {
	//            url "C:\\Users\\andygzyu\\.gradle\\caches\\m2repository"
	            url "C:/Android/Android Studio/gradle/m2repository" // 使用本地Android Studio的库
	//            url System.getProperty("user.home") + '/.m2/repository'
	        }
	    }
	```