# Android App的签名打包（晋级篇）
Andriod应用程序如果要在手机或模拟器上安装，必须要有签名！ 

## 签名的意义
为了保证每个应用程序开发商合法ID，防止部分开放商可能通过使用相同的Package Name来混淆替换已经安装的程序，我们需要对我们发布的APK文件进行唯一签名，保证我们每次发布的版本的一致性(如自动更新不会因为版本不一致而无法安装)。

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
