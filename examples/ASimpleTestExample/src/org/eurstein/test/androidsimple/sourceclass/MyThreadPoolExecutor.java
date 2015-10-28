package org.eurstein.test.androidsimple.sourceclass;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import org.eurstein.test.androidsimple.utils.AndyLog;

public class MyThreadPoolExecutor extends My1ThreadPoolExecutor {
	private final String TAG = "MyThreadPoolExecutor";
	
	public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
	             Executors.defaultThreadFactory(), new AbortPolicy());
	}
 
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		AndyLog.i(TAG, "afterExecute thread.id: " + Thread.currentThread().getId());
		super.afterExecute(r, t);

		if (!(r instanceof MyFutureTask) || t != null) {
			return;
		}

		Object result = null;
		try {
            result = ((Future<?>) r).get();
            AndyLog.i(TAG, "result" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
 	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		AndyLog.i(TAG, "beforeExecute thread.id: " + t.getId());
 		super.beforeExecute(t, r);
  	}
}
