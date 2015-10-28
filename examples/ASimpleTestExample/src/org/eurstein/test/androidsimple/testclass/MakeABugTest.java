
package org.eurstein.test.androidsimple.testclass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MakeABugTest {

	private static final String TAG = "MakeABugTest";
	
	// 测试单例类（为了测试方便，直接定义成内部类）
	private static class BugSingleton {

		private static BugSingleton instance = null;

		private BugSingleton() {

		}

		public static synchronized BugSingleton getInstance() {
			if (instance == null) {
				instance = new BugSingleton();
			}
			return instance;
		}

		Handler handler = new Handler(); // 单例成员 埋坑

	}
	
	// 一次运行输出：
	//	10-28 21:52:39.310: I/MainActivity(10367): button1 click
	//	10-28 21:52:39.312: W/System.err(10367): java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
	//	10-28 21:52:39.312: W/System.err(10367): 	at android.os.Handler.<init>(Handler.java:200)
	//	10-28 21:52:39.312: W/System.err(10367): 	at android.os.Handler.<init>(Handler.java:114)
	//	10-28 21:52:39.312: W/System.err(10367): 	at org.eurstein.test.androidsimple.testclass.MakeABugTest$2.run(MakeABugTest.java:71)
	//	10-28 21:52:39.313: I/MakeABugTest(10367): hideComplexBug task start at pool-1-thread-1
	//	10-28 21:52:39.319: I/MakeABugTest(10367): hideComplexBug task start at main
	//	10-28 21:52:39.322: I/MakeABugTest(10367): 成功获得BugSingleton单例对象...
	//	10-28 21:52:39.322: I/MakeABugTest(10367): hideComplexBug task end at main
	public static void test() {
		// MakeABugTest.simpleBug();
		// MakeABugTest.complexBug();
		MakeABugTest.hideSimpleBug();
		MakeABugTest.hideComplexBug();
	}

	// 程序崩溃退出
	private static void simpleBug() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				Handler handler = new Handler(); // 异常，无捕获，程序退出
				// get the handler to do something...
			}
		}.start();
	}

	// 程序不退出，打印异常调用栈，逻辑可能出错
	private static void hideSimpleBug() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Handler handler = new Handler(); // 此处异常
					// get the handler to do something...
					Log.i(TAG, "成功创建handler...");
				} catch (Exception e) {
					e.printStackTrace(); // 捕获异常，打印异常调用栈
				}
			}
		}.start();
	}

	// 程序崩溃退出
	private static void complexBug() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				BugSingleton.getInstance();
			}
		}.start();
	}

	// 程序不退出，LogCat也无任何异常信息，task任务异常结束(超隐蔽的BUG产生~)
	private static void hideComplexBug() {
		
		Runnable task = new Runnable() {

			@Override
			public void run() {
				Log.i(TAG, "hideComplexBug task start at " + Thread.currentThread().getName());
				BugSingleton.getInstance(); // 此处崩溃，task异常结束
				Log.i(TAG, "成功获得BugSingleton单例对象...");
				Log.i(TAG, "hideComplexBug task end at " + Thread.currentThread().getName());
			}
		};
		
		// 非主线程，且无Looper，task执行失败，且LogCat无任何出错信息输出(BUG出现~)
		ExecutorService executors = Executors.newFixedThreadPool(5); // ExecutorService藏坑
		executors.submit(task);
		
		// 主线程，task执行成功，在主线程中能正常初始化BugSingleton单例对象，导致此BUG更具隐蔽性
		Handler mainHandle = new Handler(Looper.getMainLooper());
		mainHandle.post(task);
	}
	
}
