title: git 

## git usage
[TOC]

## .gitignore, ~/.gitignore_global
man gitignore 

## ~/.gitconfig
git config --global --get user.name
git config --list
git config -l
git config --global -l
git config --global -e
git config --global --add user.name "Yu Guangzhen"
git config --global --replace user.name "andy"

## Configure your personal ignore file
http://www.programblings.com/2008/10/22/git-global-ignores/
I like to stick to conventions so I call my file .gitignore_global, and I put it in my home directory. But that’s up to you, really.
```
git config --global core.excludesfile ~/.gitignore_global
```
Note that there’s one little gotcha to be aware of. If you prefer to edit the .gitconfig file directly (or if you use a weird shell), git expects an absolute path. In the example above, bash converted the ~ shorthand to my home directory.
Now I just add ignore globs to it like any other project level (directory level, really) git ignore file.
```
echo .DS_Store >> ~/.gitignore
```
Once I’ve ignored all my favorite useless files, I can get cracking and never worry about them again.

## 初始化空git repository
git init

## 查看状态
git status

## 添加文件到git index
git add . // 添加当前目录下的所有文件和子目录

## 强制添加到git，对ignore的文件使用
git add -f ignorefile

## 仅删git index记录，不删文件
git rm -r --cached directory
git rm --cached file

## 删git index记录，且删文件
git rm -rf directory

## 提交
git commit -m "first commit"

## 查询log
git log -n // 查看n条记录
git log --pretty=oneline // 仅显示一行
git log -p // 显示变化内容
git log --name-status // 可以查看到那些文件有那个变动（A,M,...)
git log --stat // 可以查看到那些文件被影响
git show // 查看最近一次的变更内容

## 查看改变
git diff 版本号1 版本号2

## 克隆repository
git clone repository [directory]

## 查看远程分支
git remote -v
git remote show origin
git remote add origin ~/Desktop/ // 添加远程分支名
git remote set-url origin ~/workspace/Android/andy_android/ // 重新设置远程分支url

## 拉取远程分支内容到本地
git pull

## 推送本地修改到远程
git push // 此命令遇到non-bare repository时，将出如下错误，解决方法见下节内容
```
Andy:andy_android YuGuangzhen$ git push
Counting objects: 7, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (4/4), done.
Writing objects: 100% (4/4), 602 bytes | 0 bytes/s, done.
Total 4 (delta 3), reused 0 (delta 0)
remote: error: refusing to update checked out branch: refs/heads/master
remote: error: By default, updating the current branch in a non-bare repository
remote: error: is denied, because it will make the index and work tree inconsistent
remote: error: with what you pushed, and will require 'git reset --hard' to match
remote: error: the work tree to HEAD.
remote: error: 
remote: error: You can set 'receive.denyCurrentBranch' configuration variable to
remote: error: 'ignore' or 'warn' in the remote repository to allow pushing into
remote: error: its current branch; however, this is not recommended unless you
remote: error: arranged to update its work tree to match what you pushed in some
remote: error: other way.
remote: error: 
remote: error: To squelch this message and still keep the default behaviour, set
remote: error: 'receive.denyCurrentBranch' configuration variable to 'refuse'.
To /Users/YuGuangzhen/workspace/Android/andy_android/
 ! [remote rejected] master -> master (branch is currently checked out)
error: failed to push some refs to '/Users/YuGuangzhen/workspace/Android/andy_android/'

```

## 设置允许被push
git config receive.denyCurrentBranch ignore // 如此设置后，non-bare repository也可以被push，不推荐这样设置  此命令实际修改的是./.git/config文件

<!-- ??如果希望保留生产服务器上所做的改动,仅仅并入新配置项, 处理方法如下:
http://www.cnlvzi.com/index.php/Index/article/id/119

git stash

git pull

git stash pop

然后可以使用git diff -w +文件名 来确认代码自动合并的情况.


反过来,如果希望用代码库中的文件完全覆盖本地工作版本. 方法如下:

git reset --hard

git pull 


http://www.01happy.com/git-resolve-conflicts/

有时候使用Git工作得小心，特别是涉及到一些高级操作，甚至一些很小的操作，例如删除一个分支。其实没有必要，只要git库不删除，就可以恢复，因为git的历史记录是不可修改的，也就是说你不能更改任何已经发生的事情。你做的任何操作都只是在原来的操作上修改。也就是说，即使你删除了一个分支，修改了一个提交，或者强制重置，你仍然可以回滚这些操作。

工具/原料
git
方法/步骤
1
git reflog。reflog它会记录所有HEAD的历史，也就是说当你做 reset，checkout等操作的时候，这些操作会被记录在reflog中。
git reset -hard 的误操作的解决办法
2
如果我们要找回我们第二次commit，只需要做如下操作：
git reset --hard 98abc5a
3
所以，如果因为reset等操作丢失一个提交的时候，你总是可以把它找回来。



-->

## git utf-8

1. 使用git add添加要提交的文件的时候，如果文件名是中文，会显示形如274\232\350\256\256\346\200\273\347\273\223.png的乱码
```
git config --global core.quotepath false

```
2 .在MsysGit中，使用git log显示提交的中文log乱码
解决方法：
    1. 设置git gui的界面编码
    ```
    git config --global gui.encoding utf-8
    ```
    2. 设置 commit log 提交时使用 utf-8 编码，可避免服务器上乱码，同时与linux上的提交保持一致！
    ```
    git config --global i18n.commitencoding utf-8
    ```
    3. 使得在 $ git log 时将 utf-8 编码转换成 gbk 编码，解决Msys bash中git log 乱码。
    ```
    git config --global i18n.logoutputencoding gbk
    ```
    4. 使得 git log 可以正常显示中文（配合i18n.logoutputencoding = gbk)，在 /etc/profile 中添加：
    ``
    export LESSCHARSET=utf-8
    ```
3. 在MsysGit自带的bash中，使用ls命令查看中文文件名乱码。cygwin没有这个问题。
解决方案：
使用 ls --show-control-chars 命令来强制使用控制台字符编码显示文件名，即可查看中文文件名。
为了方便使用，可以编辑 /etc/git-completion.bash（在git安装目录下），新增一行 alias ls="ls --show-control-chars"

## MsysGit相关设置
1. 全局的.gitconfig 在`~`目录下面，可以进入git bash后，通过如下方法取得
```
cd ~
pwd
```
2. 配置全局.gitignore文件
```
git config --global core.excludesfile ~/.gitignore
```
3. 在MsysGit自带的bash中，使用ls命令查看中文文件名乱码
在git安装目录的/etc/git-completion.bash末尾加入下面代码
```
alias ls="ls --show-control-chars"
```

## git忽略mode的配置(old mode 100755 new mode 100644 让git忽略文件权限检查)
```
git config --global core.filemode false  // 当遇到设置此值不生效时，有可能是.git/config中把filemode设成了true，因此全局设置被覆盖
git config --add core.filemode false // 设置此值将覆盖全局设置
```

