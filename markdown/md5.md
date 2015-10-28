title: MD5 and SHA1 Tools on windows

1. 字符串md5和sha1计算工具
```
E:\workspace\CscommCrashAnalysis\jni>sh
sh-3.1$ echo -n "dfdf" | sha1sum
163907758394e5df46493dcc6d9f48d412c05554 *-
sh-3.1$ echo -n "dfdf" | md5sum
b52c96bea30646abf8170f333bbd42b9 *-
sh-3.1$ exit
exit
```

2. 文件md5计算工具
```
E:\workspace\CscommCrashAnalysis\jni>D:\command\fciv.exe D:\command\winmd5free\License.txt
//
// File Checksum Integrity Verifier version 2.05.
//
bd4d37762a61c379d449f1daa3e4ee0b d:\command\winmd5free\license.txt
```
