
package org.eurstein.test.javaexample.testclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.text.html.HTML.Tag;

import org.eurstein.test.javaexample.utils.AndyLog;

public class ListTest {

    private static final String TAG = "ListTest";

    // http://my.oschina.net/leoson/blog/103291
    //    CopyOnWriteArrayList，因何而存在？
    //    ArrayList的一个线程安全的变体，其所有可变操作（add、set等）都是通过对底层数组进行一次新的复制来实现的，代价昂贵。
    //    CopyOnWriteArrayList，是因”并发”而生。
    //    CopyOnWriteArrayList，改了其中的某对象的某个值，

    // 对于CopyOnWriteArrayList来说，直接进行list.add和list.remove是不会出错的，但是使用迭代器add和remove会报错
    public static int testCopyOnWriteArrayListRemove() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");

        try {
            Iterator<String> iter = list.iterator();
            while (iter.hasNext()) {
                String str = iter.next();
                System.out.println(str);
                iter.remove(); // 会报错 UnsupportedOperationException
                System.out.println("list.size() = " + list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (String str : list) {
                System.out.println(str);
                list.remove(str); // 不会报错
                System.out.println("list.size() = " + list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 简单来说对于ArrayList，使用迭代器进行add/remove操作是不会报错的
    public static int testArrayListRemove() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");

        try {
            Iterator<String> iter = list.iterator();
            while (iter.hasNext()) {
                String str = iter.next();
                System.out.println(str);
                iter.remove(); // 不会报错
                System.out.println("list.size() = " + list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            for (String str : list) {
//                System.out.println(str);
//                list.remove(str); // 会报错
//                System.out.println("list.size() = " + list.size());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return 0;
    }

    public static void testIndexOf() {
        String strs[] = {
                "a", "b", "c"
        };
        List<Integer> list_i = new ArrayList<Integer>();
        List<String> list_s = new ArrayList<String>();
        for (int i = 0; i < strs.length; i++) {
            list_i.add(strs[i].hashCode());
            list_s.add(strs[i]);
        }

        AndyLog.i(TAG, "list_i.indexOf(\"b\".hashCode()): " + list_i.indexOf("b".hashCode()));
        AndyLog.i(TAG, "list_s.indexOf(\"b\"): " + list_s.indexOf("b"));
        AndyLog.i(TAG, "list_i.indexOf(\"c\".hashCode()): " + list_i.indexOf("c".hashCode()));
        AndyLog.i(TAG, "list_s.indexOf(\"c\"): " + list_s.indexOf("c"));
        AndyLog.i(TAG, "list_i.indexOf(\"x\".hashCode()): " + list_i.indexOf("x".hashCode()));
        AndyLog.i(TAG, "list_s.indexOf(\"x\"): " + list_s.indexOf("x"));

    }

    // list中的元素对象可以是null
    public static void testNullElement() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("s1");
        list.add("s2");
        list.add(null);
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            AndyLog.i(TAG, s);
        }
    }

    public static void testIterateOverNullList() {
        ArrayList<String> list = null;
        for (String item : list) { // 会报空指针错误NullPointerException
            AndyLog.d(TAG, "item: " + item);
        }
    }

    public static void testIterateOverEmptyList() {
        ArrayList<String> list = new ArrayList<String>();
        for (String item : list) { // 会报空指针错误NullPointerException
            AndyLog.d(TAG, "item: " + item);
        }
    }
    
    // 
    public static void testListElementInnerValue() {
        ArrayList<testObject> list = new ArrayList<ListTest.testObject>();
        testObject o = new testObject(1, "andy");
        AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString());
        list.add(o);
        ArrayList<testObject> newlist = new ArrayList<ListTest.testObject>(list);
        {
            o = list.get(0);
            AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString() + " list");
        }
        {
            o = newlist.get(0);
            AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString() + " newlist");
        }
        // 改变o对象内容后,所有包含o对象的list元素都会改变，即list保存的是同一份o对象，他们的指针是同一个
        o.id = 2;
        o.name = "eurstein";
        AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString());
        {
            o = list.get(0);
            AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString() + " list");
        }
        {
            o = newlist.get(0);
            AndyLog.i(TAG, "o.identityHashCode: " + System.identityHashCode(o) + " o.hashCode: " + o.hashCode() + " " + o.toString() + " newlist");
        }
    }

    public static void testAddAll() {
        ArrayList<String> list = new ArrayList<String>();
        AndyLog.i(TAG, "list.size: " + list.size());
        
        ArrayList<String> newlist = new ArrayList<String>(list);
        newlist.addAll(list);
        AndyLog.i(TAG, "newlist.size: " + newlist.size());
    }
    
    private static class testObject {
        public int id;
        public String name;
        
        public testObject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return "id: " + id + " name: " + name;
        }
    }
}
