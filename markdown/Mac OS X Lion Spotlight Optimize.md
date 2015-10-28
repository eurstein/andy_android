title: Mac OS X Lion Spotlight 优化指南

##Mac OS X Lion Spotlight 优化指南
[TOC]

http://bigasp.com/archives/516

## 基本优化方案：减少需要索引的内容
根据上面的介绍，那么我们可以想到，最简单的优化就是减少需要索引的内容。

1. 首先，在Spotlight的设置中选择我们需要索引的内容。
打开系统偏好设置->Spotlight，在搜索结果这个Tab栏内，我们把所有我们不关心的内容全部勾选掉。
我只勾选了：应用程序，系统偏好设置，通讯录和音乐。这些对我来说够用了

2. 然后，将我们不需要索引的文件夹都加入Spotlight的黑名单。
在系统偏好设置->Spotlight中的隐私Tab中，我们可以选择那些目录是Spotlight不用去索引的，我们可以利用这个目录来大大减少我们需要索引的文件量。
打开Finder，点击菜单中的前往->前往文件夹，转到根目录/， 将文件夹中除了应用程序和用户的目录，全部放入黑名单。

3. 需要注意的是：其实用户目录也可以放入黑名单，Spotlight中显示的音乐，联系人和邮件，其实是靠和这些软件数据库直接联系来建立索引的，所以和文件夹的索引没有太大的联系，也可以放入黑名单。这里看大家的喜好，我全放进去了。

4. 另外还需要注意的是：这里需要在Finder里面显示所有隐藏文件，因为一些和系统相关的目录，如/usr，其实都没有索引的必要，都可以放入黑名单。如果显示文件的方法不会的话，大家可以去Google一下。
```
这里光显示隐藏文件还是不够的，可以在点完加号浏览/选择文件的时候，直接按command+shift+g，然后输入"/bin"即可
```

## 索引服务
1. 关闭所有磁盘的索引服务
```
sudo mdutil -a -i off
```
2. 只打开主硬盘的索引服务，我的主硬盘被挂载到/Volumes/Macintosh HD目录下（默认设置），大家可以根据具体情况修改。
```
sudo mdutil -i on "/Volumes/Macintosh HD"
```
3. 如果上面的指令执行错误，可以使用如下命令来恢复对所有磁盘的索引。
```
sudo mdutil -a -i on
```
好，到此我们已经完成了第一步优化了。

此时大家可以看到Spotlight在重建索引了，在我的机器上，原来完全建立一次索引需要2小时，现在只需要几分钟了。

## 替换优化方案 I：替换Spotlight的前端部分
OK，在完成基本优化方案之后，既然我们已经将Spotlight阉割成了这般田地。那我们干脆换掉Spotlight使用其他的软件替代好了。
这里有一个不错的免费软件推荐：Alfred。大家也可以直接在App Store中搜索安装即可，过程就不赘述了。

1. 隐藏任务栏中的Spotlight图标
这个方案问题的关键在于：既然Spotlight都不需要了，我们也就不需要显示它了。
关闭它的方法很简单，在终端里面输入如下指令即可：
```
sudo mv /System/Library/CoreServices/Search.bundle /System/Library/CoreServices/Search2.bundle
ps aux | grep SystemUIServer | grep -v grep | awk '{print $2}' | xargs kill
```
现在我们就会发现桌面上的Spotlight图标不见了，这一步优化也就宣告完成了。

2. 当然想要恢复也很简单，输入如下命令即可恢复：
```
sudo mv /System/Library/CoreServices/Search2.bundle /System/Library/CoreServices/Search.bundle
ps aux | grep SystemUIServer | grep -v grep | awk '{print $2}' | xargs kill
```

## 替换优化方案 II：替换整个Spotlight
完全停止Spotlight的意思是不仅仅关闭掉Spotlight的桌面搜索，还停止掉Spotlight的后台索引服务。
**所以友情提醒：前方有怪兽！将Spotlight关闭之后，依赖于Spotlight的索引服务的软件将无法正常运行，如Alfred将无法显示搜索结果（依赖于Spotlight索引服务mds），请小心处理，如果引起诡异的问题，与笔者无关。**

这个方案是我暂时最喜欢的，因为在系统中Spotlight的索引服务还是随时在运行的，所以它依然会占用我们的系统资源，而我们又无法控制，完全替换他，才是我们最好的选择。
好，让我们开始吧。

1. 完全停止Spotlight后台索引
在完成替换优化方案I之后，我们可以输入如下命令来完全停止Spotlight的索引。
```
sudo launchctl unload -w /System/Library/LaunchDaemons/com.apple.metadata.mds.plist
```
在输入完这条指令之后，大家可以查看一下当前系统内是否还存在mds和mdsworker的进程。他们应该都退出了。
至此，Spotlight算是基本被我们给干掉了。

2. 当然，如果我们后悔了，我们也可以通过如下命令来恢复。
```
sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.metadata.mds.plist
```
恢复完成之后，不要忘了检查基本优化方案里面的内容哦，因为停止后台索引服务后，索引工作也停止了。

安装替代软件
那在没有后台索引服务的情况下，我们还能不能享受到和Spotlight这种类似的功能呢？
这里再推荐一个小软件：QuickSilver。
这款软件里面会建立自己的索引，虽然就功能而言，不及Alfred强大，但也已经相差无几，对于图片，视频的索引是否有必要，个人还是持保留态度的，原因相信大家都懂的，所以基本上对我来说已经够用了。
在这个软件里面，我们可以自己设定软件的索引范围，或者禁止它自动更新索引，这样，就可以让我们最大程度上的控制这个后台服务了。

总结
到此为止，整个Spotlight的优化算是完成了，我可怜的mbp温度终于降低一些了，而且也不会因为大量文件操作导致温度飙升了。总的来说，折腾还是有些效果的，希望能对大家也有些帮助吧。
