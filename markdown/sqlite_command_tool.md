title: SQlite命令行工具的使用

# SQlite命令行工具的使用
[TOC]

http://blog.csdn.net/michaelpp/article/details/5697521

最近使用sqlite 较多，这东西现在是越用越着迷，不管是工作或者个人自娱自乐写东西，只要有数据存储 首先就想到了sqlite ，我一直对C/S模式的数据存储很反感，记得在知道sqlite 之前的很长一段时间里一直用的是xml来存数我的数据，我喜欢将数据存储在单一文件 里，我可以随时把数据带在身上，而且它们兼容性够好，拿着数据文件到那里都能用，哪怕是在嵌入式系统里照样玩儿的转…

## SQLite特性
1. ACID事务
2. 零配置 – 无需安装和管理配置
3. 储存在单一磁盘文件中的一个完整的数据库
4. 数据库文件可以在不同字节顺序的机器间自由的共享
5. 支持数据库大小至2TB
6. 足够小, 大致3万行C代码, 250K
7. 比一些流行的数据库在大部分普通数据库操作要快
8. 简单, 轻松的API
9. 包含TCL绑定, 同时通过Wrapper支持其他语言的绑定
10. 良好注释的源代码, 并且有着90%以上的测试覆盖率
11. 独立: 没有额外依赖
12. Source完全的Open, 你可以用于任何用途, 包括出售它
13. 支持多种开发语言,C, PHP, Perl, Java, ASP.NET,Python

## Sqlite安装 
现在各大Linux发型版均有现成的软件包可供安装，而且大部份系统都是自带有的，想确认系统里有没有运行下
$sqlite3 
非Linux系统到：http://www.sqlite.org/download.html 下 载安装
接下来就是sqlite命令行工具的使用

打开或创建数据库 
$sqlite3 test.db3 
这样就能打开或者创建一个新的数据库文件
$sqlite3 test.db3
SQLite version 3.6.23
Enter ".help" for instructions
Enter SQL statements terminated with a ";"
sqlite> 
它看起来就是这个样子

## SQlite内置命令 
它除了能执行SQL语句以外还提供一组内置的命令，它们是以点. 开始，比如说查看帮助信息就是 **.help** 退 出是 **.exit** 跟 **.quit**

## 创建表 
在命令行里大部份的SQL语句它都是支持的，现在来新建两个表
```
sqlite>create table Artists (
--->ArtistID INTEGER PRIMARY KEY,
--->ArtistName TEXT); 
sqlite 对SQL语句大小写不敏感，所以大写小写随便
sqlite>create table CDs (
--->CDID INTEGER PRIMARY KEY,
--->ArtistID INTEGER NOT NULL,
--->Title TEXT NOT NULL,
--->Date TEXT); 
```

***这里注意，除了INTEGER PRIMARY KEY其它字段是都可以是无类型的，也就是不关声明什么或者不声明，这个字段是可以存储任何数据的。***

## 插入数据 
```
sqlite>insert into Artists (ArtistID,ArtistName) values (NULL,'Peter Gabriel');
sqlite>insert into Artists (ArtistID,ArtistName) values (NULL,'Bruce Hornsby');
sqlite>insert into Artists (ArtistID,ArtistName) values (NULL,'Lyle Lovett');
sqlite>insert into Artists (ArtistID,ArtistName) values (NULL,'Beach Boys'); 
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,'So','1984');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,'Us','1992');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,2,'The Way It Is','1986');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,2,'Scenes from the Southside','1990');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,'Security','1990');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,3,'Joshua Judges Ruth','1992');
sqlite>insert into CDs (CDID,ArtistID,Title,Date) values (NULL,4,'Pet Sounds','1966'); 
```
sqlite是支持导入sql文件的，只要使用内置命令**.read** 即可,比如说我们将以上的命令建成一个sql文件， 命名为insert_table.sql
```
insert into Artists (ArtistID,ArtistName) values (NULL,’Peter Gabriel’);
insert into Artists (ArtistID,ArtistName) values (NULL,’Bruce Hornsby’);
insert into Artists (ArtistID,ArtistName) values (NULL,’Lyle Lovett’);
insert into Artists (ArtistID,ArtistName) values (NULL,’Beach Boys’);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,’So’,'1984′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,’Us’,'1992′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,2,’The Way It Is’,'1986′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,2,’Scenes from the Southside’,'1990′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,1,’Security’,'1990′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,3,’Joshua Judges Ruth’,'1992′);
insert into CDs (CDID,ArtistID,Title,Date) values (NULL,4,’Pet Sounds’,'1966′);
```
接着在命令行里运行
```
sqlite>.read insert_table.sql
```

## 表查询 
来看看现在这两张表里都有那些内容，执行
```
sqlite>select * from Artists; 
```
和
```
sqlite>select * from CDs; 
```
如果要同时看表头，请在运行查询语句前打开headers选项
```
sqlite>.headers ON 
```
输出结果看起来应该是这样子

```
ArtisID|ArtistName
1 |Peter Gabriel
2 |Bruce Hornsby
3 |Lyle Lovett
4 |Beach Boys

和


CDID|ArtisID|Title |Date
1 |1 |So |1984
2 |1 |Us |1992
3 |2 |The Way It Is |1986
4 |2 |Scenes from the Southside|1990
5 |1 |Security |1990
6 |3 |Joshua Judges Ruth |1992
7 |4 |Pet Sounds |1966
```
其它的一些查询语句
```
sqlite>SELECT Title AS AlbumName FROM CDs;

sqlite>SELECT Title FROM CDs WHERE Date>=1990 ORDER BY Title;

sqlite>SELECT Date FROM CDs;

sqlite>SELECT DISTINCT Date FROM CDs;

sqlite>SELECT Title FROM CDs GROUP BY ArtistID;
```
## 多表查询 
执行
```
sqlite>SELECT t1.ArtistName,CDs.Title FROM Artists t1, CDs WHERE t1.ArtistID=CDs.ArtistID 
```
得到的结果
```
ArtistName |Title
Peter Gabriel|So
Peter Gabriel|Us
Peter Gabriel|Security
Bruce Hornsby|The Way It Is
Bruce Hornsby|Scenes from the Southside
Lyle Lovett |Joshua Judge Ruth
Beach Boys |Pet Sounds
```

## 更新字段 
插入一条数据
```
sqlite>insert into Artists (ArtistID,ArtistName) values (NULL,'Supernatural'); 
```
如果要更改歌手名字为Santana
```
sqlite>UPDATE Artists SET ArtistName ='Santana' WHERE ArtistID=5; 
```
即可

## 删除字段 
首先执行
```
sqlite>select * FROM CDs WHERE Title LIKE 'Super%'; 
```
看看是不是想要删除的数据，是的话执行
```
sqlite>DELETE FROM CDs WHERE Title LIKE 'Super%'; 
```
再运行
```
sqlite>select * FROM CDs WHERE Title LIKE 'Super%'; 
```
看看是不是已经删除了？

如果嫌上面的命令行不够直观高效，而你又非常喜欢多用鼠标，那么推荐你安装SQLite Manager 这个Firefox扩展程序，它真的非常方便。
