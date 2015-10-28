package org.eurstein.test.javaexample.testclass;

public class ThreadLocalTest {

	static String value = "value";

	public static void test() {
		value = "change value";
		// 如果ThreadLocal.set()进去的东西本来就是多个线程共享的同一个对象，那么多个线程的ThreadLocal.get()取得的还是这个共享对象本身，还是有并发访问问题。 
		// Thread1
		new Thread() {
			public void run() {
				ThreadLocal<String> tl1 = new ThreadLocal<String>();
				tl1.set(value);
				System.out.println(tl1.get());
			};
		}.start();
		
		// Thread2
		new Thread() {
			public void run() {
				ThreadLocal<String> tl2 = new ThreadLocal<String>();
				tl2.set(value);
				System.out.println(tl2.get());
			};
		}.start();
	}
}
