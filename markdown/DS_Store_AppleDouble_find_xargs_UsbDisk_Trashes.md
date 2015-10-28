## find pathto -name “.DS_Store” -delete

```
Remove existing files from the repository:

find . -name .DS_Store -print0 | xargs -0 git rm -f --ignore-unmatch
Add the line

.DS_Store
to the file .gitignore, which can be found at the top level of your repository (or created if it isn't there already). Then

git add .gitignore
git commit -m '.DS_Store banished!'
```

## U盘禁止苹果回收站
```
mdutil -i off /Volumes/yourUSBstick
cd /Volumes/yourUSBstick
rm -rf .{,_.}{fseventsd,Spotlight-V*,Trashes}
mkdir .fseventsd
touch .fseventsd/no_log .metadata_never_index .Trashes
cd -
```

## 拷贝文件到U盘时，发现出来一堆`._.DS_Store`和`._*`文件(Apple double "._*" files) 
**结论:** 向U盘拷贝文件时，不能阻止`._*`文件的产生。但是可以使用`dot_clean -m path`命令删除path目录下所有的`._*`文件
详见：http://en.wikipedia.org/wiki/AppleSingle_and_AppleDouble_formats
```
Unwanted "._" files can be removed using dot_clean -m
```

初步观察被拷贝的文件夹中含有`.DS_Store`时，在目标U盘中将会出现`._.DS_Store`
```
当从mac复制文件到其他系统的时候，你会发现，目录下面多了一堆._开头的同名文件，很让人纠结，找了资料，说这是【 “Apple Double” 的文件系統處理機制】，虽然可以被删除，但很麻烦。

找到的原文是这么说的：

XML/HTML代码
Before Mac OS X, the Mac OS used ‘forked’ files, which have two components: a data fork and a resource fork. The Mac OS Standard (HFS) and Mac OS Extended (HFS Plus) disk formats support forked files. When you move these types of files to other disk formats, the resource fork can be lost.  
  
With Mac OS X, there is a mechanism called “Apple Double” that allows the system to work with disk formats that do not have a forked file feature, such as remote NFS, SMB, WebDAV directories, or local UFS volumes. Apple Double does this by converting the file into two separate files. The first new file keeps the original name and contains the data fork of the original file. The second new file has the name of the original file prefixed by a “._ ” and contains the resource fork of the original file. If you see both files, the ._ file can be safely ignored. Sometimes when deleting a file, the ._ component will not be deleted. If this occurs you can safely delete the ._ file.   
好吧，我当然是没辙的，所以就只能用find . -name "._*"|xargs rm这样的方式来删除了。这也是参考文章中的办法： OSX 下 DOT_ 前綴文件

不过还是有一点小问题，那就是如果文件夹中有空格，其实在查询的时候是会被加上“\”的，也就导致在删除 的时候这个反斜杠变成了转义符。所以，这个目录还是先mv一下，改个名吧
其实可以试试 find . -name "._*" -print0 | xargs -0 rm
```

http://craig.is/archiving/removing-apple-double-files-in-mac-os-x/23
```
From The Archives
Removing Apple Double "._" files in Mac OS X
created May 24, 2010 at 1:43 am
When I work locally on projects I like to use symlinks for all of my shared libraries. It makes it way easier than having to manage a million different versions of the same files.

Recently I was creating a tarball (.tar) archive to deploy somewhere using the -h option to follow symlinks like so:

tar cvfh code.tar code
I noticed that many of the symlinked files that were copied into the archive ended up being transformed into apple double files. Of course, this is quite annoying, and I wanted to get rid of them. I did some googling and found the answer buried somewhere on the internets so I thought I'd make it easier to find.

All you have to do is add the following to your ~/.bash_profile:

# for Tiger OS 10.4 or earlier
export COPY_EXTENDED_ATTRIBUTES_DISABLE/span>=true

# for Leopard OS 10.5 and later
export COPYFILE_DISABLE=true
That's all.
```

1. 在根目录下创建文件.metadata_never_index，不管用

2. http://apple.stackexchange.com/questions/6707/how-to-stop-os-x-from-writing-spotlight-and-trash-files-to-memory-cards-and-usb
```
When plugging a USB stick into a Mac, OS X creates a number of hidden files on the stick, including a Spotlight index and Trash folder.

Example from the terminal for a USB stick "Untitled":

$ ls -a /Volumes/Untitled
.Spotlight-V100
.Trashes
._.Trashes
.disk
.fseventsd
It even does this on the xD memory card for my camera, so after having copied my pictures and deleted them from the card, the card is still full.

Is it possible to turn this off for USB and memory cards, so OS X either writes these files to the primary disk or doesn't write them at all?

osx photos usb spotlight trash
shareimprove this question
edited Jul 28 '11 at 21:22

abby hairboat♦
1,26452555  
asked Jan 20 '11 at 17:15

j-g-faustus
408146
        
Sidenote: A journaled filesystem automatically generates these files. While indexing isn't integral to journaling, fseventd, for example, is. Though an HFS+ scheme has its benefits (IE, lack of fragmentation), you've pointed out one of its drawbacks. If you format the drive as exFAT or FAT32, it would both be compatible and solve your issue. The current solutions provided are all, essentially, different methods to unjournal a journaled format. –  njboot May 10 '14 at 10:30
2       
Unfortunately, this has nothing to do with journaling, rather it is a "feature" of the Mac. There are several processes that write these files, including Spotlight and Finder. This behavior happens on any filesystem, not just HFS+. (Yes, it also writes the invisibles to FAT and FAT32 volumes.) –  quickthyme Jul 28 '14 at 22:45
add a comment
13 Answers
activeoldestvotes
up vote
14
down vote
accepted
As I know you have 2 choices :

TinkerTool (free)
alt text

BlueHarvest (commercial)
alt text

shareimprove this answer
edited Dec 6 '12 at 11:53

sorin
4,6392680149    
answered Jan 20 '11 at 17:49

Am1rr3zA
7,65083775
        
OK, thanks. Was kind of hoping that there would be a built-in setting somewhere, but at least there's a solution available. –  j-g-faustus Jan 20 '11 at 18:08
        
@j-g: Tinkertool shows the system settings in a GUI interface as opposed to CLI, which can be harder to use for a lot of people. –  Philip Regan Jan 20 '11 at 18:28
1       
@Philip: Unfortunately Tinkertool only deals with .DS_Store files according to their detail page, which presumably means that this is the only hidden file that can be disabled through the CLI - the other files need custom tools. But BlueHarvest looks nice, and does what I need. (Found some more details on the issue here and here) –  j-g-faustus Jan 20 '11 at 18:47 
1       
outdate! later answers are better –  qarma Aug 19 '13 at 9:46
        
@j-g-faustus Not only Tinkertool only deals with .DS_store files but it does so just for network fileysystems –  nhed Apr 14 '14 at 16:23
show 1 more comment
up vote
40
down vote
For just a particular mounted volume - like a flash drive called yourUSBstick in this example - these commands will remove existing cruft, stop Spotlight indexing now and in the future, stop the related fsevents logging, and disable the Trash feature.

mdutil -i off /Volumes/yourUSBstick
cd /Volumes/yourUSBstick
rm -rf .{,_.}{fseventsd,Spotlight-V*,Trashes}
mkdir .fseventsd
touch .fseventsd/no_log .metadata_never_index .Trashes
cd -
Other unfamiliar stuff you may still see you probably want to keep, like Apple double "._*" files and other Apple DS cruft relating to icons and window placement.

shareimprove this answer
answered Jan 28 '11 at 22:06

Metaxis
500134
2       
While this may disable indexing, those files and directories will still be present on the volume (which is the annoying part in the first place), and if you delete them .Spotlight* and .fseventd will come back. In fact, .metadata_never_index is one more entry in the file listing than the usual cruft. –  ShreevatsaR May 21 '11 at 8:02 
        
+1, Actually, I came up with he idea of touching trashes myself and went here for a better solution, but having seen this one couldn't resist +1 it. It solves the real problem — stopping car audio from playing trashed files ;) –  Michael Krelin - hacker Feb 1 '12 at 19:33
        
+1 knew this existed but always have to find it when I need it. This is useful if you have more access to the USB drive than to the OSX system. –  Matthew May 30 '14 at 13:00
        
Thanks, I didn't know about "cd -". I've still been pushing pushd/popd :). –  studgeek Dec 6 '14 at 16:16
add a comment
up vote
12
down vote
Another way to deal with (just the) spotlight files, is to add that volume to your Spotlight exclude list. Plug the device in, and go to the Spotlight prefpane in System Preferences. Select the Privacy tab. Now drag that volume from your desktop up into the privacy list.. or use the + button at the bottom to add it. No more spotlight indexing will happen on that volume.

shareimprove this answer
answered Jan 20 '11 at 20:49

Harv
3,341521
        
Thanks, I'll try that. –  j-g-faustus Jan 21 '11 at 8:16
add a comment
up vote
12
down vote
To keep Spotlight from indexing non system volumes, add /Volumes to the Privacy list in System Preferences > Spotlight.

/Volumes is the point in the file system where all non-system disks are mounted by default.

enter image description here

shareimprove this answer
answered Jan 20 '12 at 13:48

Miles Leacy
95247
        
impossible to add /Volumes in 10.8; it's possible to add individual volumes one at a time though. I suspect it's similar to mdutil -i off /Volumes/xxx –  qarma Aug 19 '13 at 9:47
3       
@qarma It is very possible to add /Volumes in 10.8 or later. Simply open a Finder window, press Shift+Command+G to bring up the "Go to folder..." window, type /Volumes, and then drag the little folder icon at the top of the Finder window (next to the word "Volumes") into the list in the screenshot above –  Chris Mukherjee Nov 20 '14 at 20:01 
        
I'll have to try that out... –  qarma Nov 21 '14 at 9:02
add a comment
up vote
10
down vote
Insert the USB drive.
Navigate to Macintosh HD > Applications > Utilities and open Terminal.
At the Terminal prompt, type the following command, replacing path_to_volume with the real path:

sudo mdutil -i off /path_to_volume

Press return.

If prompted for a password, type your admin password, then press return. You will receive the response:

/path_to_volume/: Indexing disabled for volume. in Mac OS X 10.4 or

/path_to_volume: Indexing disabled. under Mac OS X 10.5 or later.

Spotlight will immediately cease to index the specified volume.

If you are using Mac OS X 10.5 or later, skip to step 9.

At the Terminal prompt, type the following command, again substituting the correct path:

sudo mdutil -E /path_to_volume and press return

If prompted for a password, type your admin password, then press return. You will receive the response:

/path_to_volume/: Volume index removed.

At the Terminal prompt, type exit then press return.
Quit Terminal.
Thanks to thexlab.com, their troubleshooting Mac OS X e-books, and their website for the detailed explanation of why other methods sort of work.

shareimprove this answer
edited Feb 8 '12 at 20:18

gentmatt
25.6k21107215   
answered Feb 8 '12 at 20:05

Chainsaw
10112
1       
Note sudo asks for the current user's password –  Mark Feb 16 '13 at 8:21
add a comment
up vote
4
down vote
I use the MacOS Terminal command line to list and delete all these files and folders before ejecting the device from the desktop. For some files, you may have to sudo the /bin/rm command.

shareimprove this answer
answered Jan 20 '11 at 20:20

hotpaw2
3,38021021
        
That's what I do too. Although it would be even better if I didn't need to, so I'm investigating the alternatives. –  j-g-faustus Jan 20 '11 at 20:26
        
By the way, the device is found somewhere at /Volumes/<name> –  DerMike Jan 21 '11 at 11:31
add a comment
up vote
1
down vote
An easy way to stop my car audio trying to read hidden Mac OS files is to remove them in Windows OS. Simply copy your MP3 music to the USB stick from iTunes. Swap the stick into Windows OS and select view hidden files from folder options. This will then allow you to delete every single hidden file that your trusty Mac placed on your USB stick including those pesky .trashes files. Finally a use for Windows OS!

shareimprove this answer
answered Apr 20 '13 at 23:19

MacMad
111
        
+1 It is a bit tedious, but sure, why not, it does get the job done haha. –  Vladimir Dec 10 '14 at 6:08 
add a comment
up vote
1
down vote
Actually touching the .Trashes file will be the best way to solve your main problem since .Trashes is now a file instead of a folder. This means that Apple can't relocate the files to the .Trashes folder when you delete them and your drive is no longer full.

Another option is to hit Cmd-Opt-Shift-Backspace to force Finder to empty the .Trashes content on the card before you eject it.

The first method is really the best as the second affects all Trash contents on all drives.

However, it seems from your post that you are more worried about the pollution of the drive by the various dot files. If you follow the steps mentioned above, you'll save your disk space, but there will be a minimum of dot files created.

shareimprove this answer
answered Aug 15 '13 at 16:27

Timpraetor
512
add a comment
up vote
1
down vote
I use Clean Eject (free) and a custom Automator Service (still private) so I can assign a hotkey to clean & eject a volume using the app.

shareimprove this answer
answered Jan 9 '14 at 10:20

matt
243110
        
Without access to the Automator Service you mentioned this answer isn't actually very helpful. Can you share the service as well? –  patrix♦ Jan 9 '14 at 10:54
        
The answer is absolutely helpful - you can use the app without the Automator action. If you want to add a special shortcut key for Clean Eject then you have lots of options: Alfred, Keyboard Maestro. The Automator action is not essential, but is useful. I will upload it when I can to: gingerbeardman.com/services –  matt Jan 10 '14 at 12:35 
        
The service I use to Clean Eject selected finder volumes is now uploaded to by site (see above for link). –  matt Jan 10 '14 at 12:56
        
Thanks for the upload, makes the answer much more complete. –  patrix♦ Jan 10 '14 at 13:25
add a comment
up vote
1
down vote
I ended up using a free app "Hidden Cleaner". My car's MP3 player was trying to read .(MP3filename).mp3 (hollow, empty mp3 files) as well. Go to Macintosh HD in Devices section on the Finder left hand menu and drag your USB drive and drop onto the Hidden Cleaner app. It will cleanup the hollow files and leave the real MP3s and will eject your USB.

Note: That is not a permanent solution. You need to do above everytime you copy files. I don't mind though.

shareimprove this answer
edited Feb 28 '14 at 14:57

patrix♦
24.1k104576 
answered Feb 28 '14 at 13:57

Uygar Y
1113
        
Another similar program: Eject for Windows www011.upp.so-net.ne.jp/decafish/EjectForWindows/… –  Vinicius Pinto Oct 6 '14 at 1:57
add a comment
up vote
1
down vote
This should really be a comment, but I don't have enough points to actually comment so I had to write a separate answer.

If a moderator could move this to the right location (or somehow give me 50 reputation so that I could comment) I would really appreciate it. :)

Anyway here's what I wanted to say:

~ ~ ~ ~ ~ ~ ~

@ Miles Leacy's post

and @ qarma's comment:

No, this is still possible even in OSX 10.9, but you need to do a few extra steps now:

1) In Finder click Go then click Go To Folder...

2) Type /Volumes and click Go.

3) A Finder window will open, and it should say Volumes at the top.

This is the most important step:

4) Next to where it says Volumes at the top of the Finder window, there is a tiny blue folder icon. Click and drag this icon left into your Favorites panel.

5) Now you will have access to your Volumes folder anywhere, including in Spotlight settings like Miles Leacy suggested. (Whenever you need it, just click on the Favorites link to select it.)

Hope this helps,

Best,

Vlad :)

~ ~ ~

How to add the Volumes folder to your Favorites so that you can access it in the Spotlight settings

~ ~ ~

What it looks like after adding the Volumes folder to the Spotlight exceptions list:

Notice in the background you can see my post in Safari. ;)

What it looks like after adding the Volumes folder to the Spotlight exceptions list

shareimprove this answer
edited Apr 8 '14 at 23:48

answered Apr 8 '14 at 23:43

Vladimir
36716
add a comment
up vote
1
down vote
Old question, but I, finally, discovered Asepsis. This is an open source utility that solves this age-old problem by confining all the .DS_STORE directories in one place, by default /usr/local/.dscage
After installation, and a reboot, no more .DS_STORE on USB drives, with the advantage (for some of us) of not having to disable indexing on external drives. Since version 1.4 it also supports OS X Mavericks.

shareimprove this answer
answered May 10 '14 at 8:31

Carlo
21328
        
Cool! I wonder how this program will handle a system crash or extreme CPU load. In other words, I wonder if a disycronization will bet automatically handled, or will need to do that your selves? Automaticlly is good because we don't need to worry about it, but it also means that the program will have to do a lot more checking. –  Vladimir Nov 22 '14 at 7:41
add a comment
up vote
-2
down vote
it can't work.

no way to resolve this problem.

shareimprove this answer
```

## 禁用或启用自动生成 (这条命令只能禁止网络文件系统的生成，对本地无效的-_-)
打开 “终端” ，复制粘贴下面的命令，回车执行，重启Mac即可生效。

1. 禁止.DS_store生成：
```
defaults write com.apple.desktopservices DSDontWriteNetworkStores -bool TRUE
```

2. 恢复.DS_store生成：
```
defaults delete com.apple.desktopservices DSDontWriteNetworkStores
```

## "find -print | grep name" 和 "find | grep name"的区别

```
你這樣用是沒差別的，下面使用就有差別了....

find /path -type f -exec rm {} \;

find /path -type f -print -exec rm {} \;

第一個例子表示找到文檔後刪除之而沒任何訊息，後者表示會顯示找到結果並且刪除文檔。
```

## xargs是一条Unix和类Unix操作系统的常用命令。它的作用是将参数列表转换成小块分段传递给其他命令，以避免参数列表过长的问题。

例如，下面的命令：
```
rm `find /path -type f`
```
如果path目录下文件过多就会因为“参数列表过长”而报错无法执行。但改用xargs以后，问题即获解决。
```
find /path -type f -print0 | xargs -0 rm
```
本例中xargs将find产生的长串文件列表拆散成多个子串，然后对每个子串调用rm。这样要比如下使用find命令效率高的多。
```
find /path -type f -exec rm '{}' \;
```
上面这条命令会对每个文件调用"rm"命令。当然使用新版的"find"也可以得到和"xargs"命令同样的效果：
```
find /path -type f -exec rm '{}' +
```
xargs的作用一般等同于大多数Unix shell中的反引号，但更加灵活易用，并可以正确处理输入中有空格等特殊字符的情况。对于经常产生大量输出的命令如find、locate和grep来说非常有用。

## find -print0 | xargs -0 中0的作用

1. find -print0 // 让 find 在打印出一个文件名之后接着输出一个 NULL 字符 ('\0') 而不是换行符
2. xargs -0 // -0 表示当sdtin含有特殊字元时候，将其当成一般字符，像/'空格等。然后再告诉 xargs 也用 NULL 字符来作为记录的分隔符. 这就是 find 的 -print0 和 xargs 的 -0 的来历吧.

    包含空白的檔案名稱

    假設我們的目錄中有一些檔名包含空白字元：
    ```
    touch "G T Wang.c"
    ```
    在這種狀況下如果用上面 find 與 xargs 的方式會無法將其刪除，原因在於當我們執行
    ```
    find . -name "*.c" | xargs rm -f
    ```
    的時候，xargs 所產生的指令為
    ```
    rm -f ./G T Wang.c
    ```
    因為檔名包含空白，所以這會會造成 rm 指令無法正確刪除該檔案。

    **這個時候我們可以將 find 指令加上 -print0 參數，另外將 xargs 指令加上 -0 參數，改成這樣**
    ```
    find . -name "*.c" -print0 | xargs -0 rm -rf
    ```
    如此一來，即可正確處理包含空白字元的檔案名稱。

3. find中的-print0和xargs中-0的奥妙  
    让 find 在打印出一个文件名之后接着输出一个 NULL 字符 ('\0') 而不是换行符, 然后再告诉 xargs 也用 NULL 字符来作为记录的分隔符. 这就是 find 的 -print0 和 xargs 的 -0 的来历吧.
    ```
    find中的-print0和xargs中-0的奥妙  
    http://blog.163.com/laser_meng@126/blog/static/16972784420117102638257/

    2011-08-10 14:48:04|  分类： 系统命令 |  标签：find  xargs   |举报|字号 订阅
    默认情况下, find 每输出一个文件名, 后面都会接着输出一个换行符 ('\n'), 因此我们看到的 find 的输出都是一行一行的:
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; ls -l
    total 0
    -rw-r--r-- 1 root root 0 2010-08-02 18:09 file1.log
    -rw-r--r-- 1 root root 0 2010-08-02 18:09 file2.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log'
    ./file2.log
    ./file1.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; bye

    比如我想把所有的 .log 文件删掉, 可以这样配合 xargs 一起用:
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log'
    ./file2.log
    ./file1.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log' | xargs rm
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log'
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; bye

    嗯, 不错, find+xargs 真的很强大. 然而:
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; ls -l
    total 0
    -rw-r--r-- 1 root root 0 2010-08-02 18:12 file 1.log
    -rw-r--r-- 1 root root 0 2010-08-02 18:12 file 2.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log'
    ./file 1.log
    ./file 2.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log' | xargs rm
    rm: cannot remove `./file': No such file or directory
    rm: cannot remove `1.log': No such file or directory
    rm: cannot remove `./file': No such file or directory
    rm: cannot remove `2.log': No such file or directory
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; bye

    原因其实很简单, xargs 默认是以空白字符 (空格, TAB, 换行符) 来分割记录的, 因此文件名 ./file 1.log 被解释成了两个记录 ./file 和 1.log, 不幸的是 rm 找不到这两个文件.

    为了解决此类问题, 聪明的人想出了一个办法, 让 find 在打印出一个文件名之后接着输出一个 NULL 字符 ('\0') 而不是换行符, 然后再告诉 xargs 也用 NULL 字符来作为记录的分隔符. 这就是 find 的 -print0 和 xargs 的 -0 的来历吧.
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; ls -l
    total 0
    -rw-r--r-- 1 root root 0 2010-08-02 18:12 file 1.log
    -rw-r--r-- 1 root root 0 2010-08-02 18:12 file 2.log
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log' -print0 | hd
               0  1  2  3   4  5  6  7   8  9  A  B   C  D  E  F  |0123456789ABCDEF|
    --------+--+--+--+--+---+--+--+--+---+--+--+--+---+--+--+--+--+----------------|
    00000000: 2e 2f 66 69  6c 65 20 31  2e 6c 6f 67  00 2e 2f 66  |./file 1.log../f|
    00000010: 69 6c 65 20  32 2e 6c 6f  67 00                     |ile 2.log.      |
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log' -print0 | xargs -0 rm
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; find -name '*.log'
    -(dearvoid@LinuxEden:Forum)-(~/tmp/find)-
    [bash-4.1.5] ; bye

    你可能要问了, 为什么要选 '\0' 而不是其他字符做分隔符呢? 这个也容易理解: 一般的编程语言中都用 '\0' 来作为字符串的结束标志, 文件的路径名中不可能包含 '\0' 字符.
    ```