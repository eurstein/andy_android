title:svn代码回滚命令

# svn笔记本
[TOC]

## svn代码回滚命令
### 取消对代码的修改分为两种情况
 
#### 1. 第一种情况：改动没有被提交（commit）。
  这种情况下，使用svn revert就能取消之前的修改。
  svn revert用法如下：
  `svn revert [-R] something`
  其中something可以是（目录或文件的）相对路径也可以是绝对路径。
  当something为单个文件时，直接svn revert something就行了；当something为目录时，需要加上参数-R(Recursive,递归)，否则只会将something这个目录的改动。
  在这种情况下也可以使用svn update命令来取消对之前的修改，但不建议使用。因为svn update会去连接仓库服务器，耗费时间。
  **注意：svn revert本身有固有的危险，因为它的目的是放弃未提交的修改。一旦你选择了恢复，Subversion没有方法找回未提交的修改。**
  ```
  svn revert -R . // 回滚当前目录"."下所有文件
  ```
#### 2. 第二种情况：改动已经被提交（commit）。
  这种情况下，用svn merge命令来进行回滚。 
    回滚的操作过程如下： 
    1. 保证我们拿到的是最新代码： 
    `svn update `
    假设最新版本号是28。 
    2. 然后找出要回滚的确切版本号： 
    `svn log [something] -l n`
    假设根据svn log日志查出要回滚的版本号是25，此处的something可以是文件、目录或整个项目
    如果想要更详细的了解情况，可以使用svn diff -r 28:25 [something]
    3. 回滚到版本号25：
    svn merge -r 28:25 something
    为了保险起见，再次确认回滚的结果：
    `svn diff [something]`
    发现正确无误，提交。
    4. 提交回滚：
    `svn commit -m "Revert revision from r28 to r25,because of ..."`
    提交后版本变成了29。

    将以上操作总结为三条如下：
    1. svn update，svn log，找到最新版本（latest revision）
    2. 找到自己想要回滚的版本号（rollbak revision）
    3. 用svn merge来回滚： svn merge -r : something
    ```
    svn log -l 10 // 查看最近10条日志
    svn diff -r 69010:69009 // 查看69010到69009之间的修改
    svn merge -r 69010:69009 . // 将69010版本提交且在目录"."下的所有文件回滚（注意，若只想回滚某个文件，可以用具体文件代替当前目录".")
    svn diff // 检查回滚结果
    svn stat 
    svn commit -m "Revert revision from r69010 to r69009,because of ..."
    ```

---

## [查看修改的文件记录](http://www.cnblogs.com/zhenjing/archive/2012/12/22/svn_usage.html)

### svn list -- 显示一个目录或某一版本存在的文件列表
```
svn list http://svn.test.com/svn     #查看目录中的文件。
svn list -v http://svn.test.com/svn  #查看详细的目录的信息(修订人,版本号,文件大小等)。
svn list [-v]                        #查看当前当前工作拷贝的版本库URL。
```

### svn cat -- 显示特定版本的某文件内容
```
svn cat -r 4 test.c     #查看版本4中的文件test.c的内容,不进行比较。
```

### svn diff -- 显示特定修改的行级详细信息
```
svn diff               #什么都不加，会坚持本地代码和缓存在本地.svn目录下的信息的不同;信息太多，没啥用处。
svn diff -r 3          #比较你的本地代码和版本号为3的所有文件的不同。
svn diff -r 3 text.c   #比较你的本地代码和版本号为3的text.c文件的不同。
svn diff -r 5:6        #比较版本5和版本6之间所有文件的不同。
svn diff -r 5:6 text.c #比较版本5和版本6之间的text.c文件的变化。
svn diff -c 6 test.c    #比较版本5和版本6之间的text.c文件的变化。
```

### svn log -- 显示svn 的版本log，含作者、日期、路径等。

```
svn log         #什么都不加会显示所有版本commit的日志信息:版本、作者、日期、comment。
svn log -r 4:20 #只看版本4到版本20的日志信息，顺序显示。
svn log -r 20:5 #显示版本20到4之间的日志信息，逆序显示。
svn log test.c  #查看文件test.c的日志修改信息。
svn log -r 8 -v #显示版本8的详细修改日志，包括修改的所有文件列表信息。
svn log -r 8 -v -q   #显示版本8的详细提交日志，不包括comment。
svn log -v -r 88:866 #显示从版本88到版本866之间，当前代码目录下所有变更的详细信息 。
svn log -v dir  #查看目录的日志修改信息,需要加v。
svn log http://foo.com/svn/trunk/code/  #显示代码目录的日志信息。
```

---

## [SVN What do the result codes in SVN mean?](http://stackoverflow.com/questions/2034/what-do-the-result-codes-in-svn-mean)
```
	U: Working file was updated

	G: Changes on the repo were automatically merged into the working copy

	M: Working copy is modified

	C: This file conflicts with the version in the repo

	?: This file is not under version control

	!: This file is under version control but is missing or incomplete

	A: This file will be added to version control (after commit)

	A+: This file will be moved (after commit)

	D: This file will be deleted (after commit)

	S: This signifies that the file or directory has been switched from the path of the rest of the working copy (using svn switch) to a branch

	I: Ignored

	X: External definition

	~: Type changed

	R: Item has been replaced in your working copy. This means the file was scheduled for deletion, and then a new file with the same name was scheduled for addition in its place.

	L : Item is locked

	E: Item existed, as it would have been created, by an svn update.
```

```
	svn st 
	status (stat, st): 显示工作副本中目录与文件的状态。
	用法: status [PATH...]
	  未指定参数时，只显示本地修改的条目(没有网络访问)。
	  使用 -q 时，只显示本地修改条目的摘要信息。
	  使用 -u 时，增加工作版本和服务器上版本过期信息。
	  使用 -v 时，显示每个条目的完整版本信息。
	  输出的前七栏各占一个字符宽度:
	    第一栏: 表示一个项目是增加、删除，还是修改
	      “ ” 无修改
	      “A” 增加
	      “C” 冲突
	      “D” 删除
	      “I” 忽略
	      “M” 改变
	      “R” 替换
	      “X” 未纳入版本控制的目录，被外部引用的目录所创建
	      “?” 未纳入版本控制
	      “!” 该项目已遗失(被非 svn 命令删除)或不完整
	      “~” 版本控制下的项目与其它类型的项目重名
	    第二栏: 显示目录或文件的属性状态
	      “ ” 无修改
	      “C” 冲突
	      “M” 改变
	    第三栏: 工作副本目录是否被锁定
	      “ ” 未锁定
	      “L” 锁定
	    第四栏: 已调度的提交是否包含副本历史
	      “ ” 没有历史
	      “+” 包含历史
	    第五栏: 该条目相对其父目录是否已切换，或者是外部引用的文件
	      “ ” 正常
	      “S” 已切换
	      “X” 被外部引用创建的文件
	    第六栏: 版本库锁定标记
	      (没有 -u)
	      “ ” 没有锁定标记
	      “K” 存在锁定标记
	      (使用 -u)
	      “ ” 没有在版本库中锁定，没有锁定标记
	      “K” 在版本库中被锁定，存在锁定标记
	      “O” 在版本库中被锁定，锁定标记在一些其他工作副本中
	      “T” 在版本库中被锁定，存在锁定标记但已被窃取
	      “B” 没有在版本库中被锁定，存在锁定标记但已被破坏
	    第七栏: 项目冲突标记
	      “ ” 正常
	      “C” 树冲突
	    如果项目包含于树冲突之中，在项目状态行后会附加行，说明冲突的种类。
	  是否过期的信息出现的位置是第九栏(与 -u 并用时):
	      “*” 服务器上有更新版本
	      “ ” 工作副本是最新版的
	  剩余的栏位皆为变动宽度，并以空白隔开:
	    工作版本号(使用 -u 或 -v 时)
	    最后提交的版本与最后提交的作者(使用 -v 时)
	    工作副本路径总是最后一栏，所以它可以包含空白字符。
	  范例输出:
	    svn status wc
	     M     wc/bar.c
	    A  +   wc/qax.c
	    svn status -u wc
	     M           965    wc/bar.c
	           *     965    wc/foo.c
	    A  +         965    wc/qax.c
	    Status against revision:   981
	    svn status --show-updates --verbose wc
	     M           965       938 kfogel       wc/bar.c
	           *     965       922 sussman      wc/foo.c
	    A  +         965       687 joe          wc/qax.c
	                 965       687 joe          wc/zig.c
	    Status against revision:   981
	    svn status
	     M      wc/bar.c
	    !     C wc/qaz.c
	          >   local missing, incoming edit upon update
	    D       wc/qax.c
	有效选项:
	  -u [--show-updates]      : 显示更新信息
	  -v [--verbose]           : 打印附加信息
	  -N [--non-recursive]     : 过时；尝试 --depth=files 或 --depth=immediates
	  --depth ARG              : 受深度参数 ARG(“empty”，“files”，“immediates”，或“infinity”) 约束的操作
	  -q [--quiet]             : 不打印信息，或只打印概要信息
	  --no-ignore              : 忽略默认值和 svn:ignore 属性
	  --incremental            : 给予适合串联的输出
	  --xml                    : 输出为 XML
	  --ignore-externals       : 忽略外部项目
	  --changelist ARG         : 只能对修改列表 ARG 成员操作
	                             [aliases: --cl]
	全局选项:
	  --username ARG           : 指定用户名称 ARG
	  --password ARG           : 指定密码 ARG
	  --no-auth-cache          : 不要缓存用户认证令牌
	  --non-interactive        : 不要交互提示
	  --trust-server-cert      : 不提示的接受未知的 SSL 服务器证书(只用于选项 “--non-interactive”)
	  --config-dir ARG         : 从目录 ARG 读取用户配置文件
	  --config-option ARG      : 以下属格式设置用户配置选项：
	                                 FILE:SECTION:OPTION=[VALUE]
	                             例如：
                                 servers:global:http-library=serf
```
