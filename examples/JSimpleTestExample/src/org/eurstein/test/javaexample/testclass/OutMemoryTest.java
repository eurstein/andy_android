package org.eurstein.test.javaexample.testclass;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class OutMemoryTest {

	public static void testOutOfMemory() {
		try {
			runOutOfMemory();
		} catch (Throwable e) { // runOutOfMemory中已经catch了 OutOfMemoryError 异常所以此处接受不到 OutOfMemoryError 异常
			// TODO: handle exception
			System.out.println("testOutOfMemory " + e);
		}
	}

	private static final int MEGABYTE = (1024 * 1024);

	private static void runOutOfMemory() throws IOException {
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		for (int i = 1; i <= 3; i++) {
			try {
				byte[] bytes = new byte[Integer.MAX_VALUE]; // 申请太多内存，将抛 OutOfMemoryError 异常
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
				e.printStackTrace();
				long maxMemory = heapUsage.getMax() / MEGABYTE;
				long usedMemory = heapUsage.getUsed() / MEGABYTE;
				System.out.println(i + " : Memory Use :" + usedMemory + "M/" + maxMemory + "M");
			}
		}
	}
}
