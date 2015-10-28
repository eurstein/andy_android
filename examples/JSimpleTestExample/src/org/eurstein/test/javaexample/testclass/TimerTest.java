
package org.eurstein.test.javaexample.testclass;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

	// 测试timer的重复cancel功能
	// 结论：可以重复cancel多次，一旦cancel，将不能再schedule任务
	public static void testTimerCancel() {

		Timer t = new Timer();

		t.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("this is first schedule");
			}
		}, 500);

		t.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("this is second schedule");
			}
		}, 500);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		t.cancel();
		t.cancel();

		t.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("this is third schedule");
			}
		}, 500);

		// t.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// System.out.println("this is forth schedule");
		// }
		// }, 500);
		// t.cancel();
	}
}
