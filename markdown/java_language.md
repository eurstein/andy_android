==
SoftReference, WeakReference 以及 PhantomReference 的特性及用法

SoftReference的特点是它的一个实例保存对一个Java对象的软引用，该软引用的存在不妨碍垃圾收集线程对该Java对象的回收。也就是说，一旦SoftReference保存了对一个Java对象的软引用后，在垃圾线程对这个Java对象回收前，SoftReference类所提供的get()方法返回Java对象的强引用。另外，一旦垃圾线程回收该Java对象之后，get()方法将返回null。


另外值得注意的是，其实 SoftReference, WeakReference 以及 PhantomReference 的构造函数都可以接收一个 ReferenceQueue 对象。当 SoftReference 以及 WeakReference 被清空的同时，也就是 Java 垃圾回收器准备对它们所指向的对象进行回收时，调用对象的 finalize() 方法之前，它们自身会被加入到这个 ReferenceQueue 对象中，此时可以通过 ReferenceQueue 的 poll() 方法取到它们。而 PhantomReference 只有当 Java 垃圾回收器对其所指向的对象真正进行回收时，会将其加入到这个 ReferenceQueue 对象中，这样就可以追综对象的销毁情况。 

==
ArrayList,LinkedList

1.ArrayList是实现了基于动态数组的数据结构，LinkedList基于链表的数据结构。
2.对于随机访问get和set，ArrayList觉得优于LinkedList，因为LinkedList要移动指针。
3.对于新增和删除操作add和remove，LinedList比较占优势，因为ArrayList要移动数据。
4.查找操作indexOf,lastIndexOf,contains等，两者差不多。
5.随机查找指定节点的操作get，ArrayList速度要快于LinkedList.

==
JDK泛型中的问号

http://www.blogjava.net/dreamstone/archive/2011/06/01/99195.html

==
public int checkQQDownloaderInstalled() throws Exception

把 Exception 看成 一块 石头 
throws Exception 就是把石头丢出去 
try catch 就是拿个网兜在那里接石头 
throws Exception是写在方法后面的吧？属于契约式编程，就是告诉编译器本方法可能会抛出该类型异常，由方法的调用者去处理。try catch就不用说了就是那么个意思。 


==
The different LogCat methods are:

Log.v(); // Verbose
Log.d(); // Debug
Log.i(); // Info
Log.w(); // Warning
Log.e(); // Error

==
instanceof
instanceof是Java的一个二元操作符，和==，>，<是同一类东东。由于它是由字母组成的，所以也是Java的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回boolean类型的数据。举个例子：

　　String s = "I AM an Object!";
　　boolean isObject = s instanceof Object;


==
Activity & task & affinity

task就好像是能包含很多activity的栈。 


==
java synchronized详解

 一、当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。

 二、然而，当一个线程访问object的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。

 三、尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。

 四、第三个例子同样适用其它同步代码块。也就是说，当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。

 五、以上规则对其它对象锁同样适用.

 ==
 和接口有关的匿名类
  public Handler mHandler=new Handler()  
    {  
        public void handleMessage(Message msg)  
        {  
            switch(msg.what)  
            {  
            case 1:  
                button.setText(R.string.text2);  
                break;  
            default:  
                break;        
            }  
            super.handleMessage(msg);  
        }  
    };  

