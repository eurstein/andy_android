
package org.eurstein.test.androidsimple.testclass;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.eurstein.test.androidsimple.sourceclass.MyCallable;
import org.eurstein.test.androidsimple.sourceclass.MyFutureTask;
import org.eurstein.test.androidsimple.sourceclass.MyThreadPoolExecutor;
import org.eurstein.test.androidsimple.utils.AndyLog;

public class ThreadPoolExecutorTest {

    private final static String TAG = "ThreadPoolExecutorTest";

    private static ThreadPoolExecutorTest mInstance;

    private final ConcurrentHashMap<String, MyFutureTask> mMyFutureTaskFutures = new ConcurrentHashMap<String, MyFutureTask>();

    private MyThreadPoolExecutor mThreadPool = new MyThreadPoolExecutor(5, 20, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private ThreadPoolExecutorTest() {
    }

    public static ThreadPoolExecutorTest getInstance() {
        if (mInstance == null) {
            mInstance = new ThreadPoolExecutorTest();
            AndyLog.i(TAG, "this: " + mInstance);
        }
        return mInstance;
    }

    public void test() {

    }

    public int startTask(String taskId) {
        int ret = 0;
//        MyFutureTask future = mMyFutureTaskFutures.get(taskId);
//        if (future == null)
//        {
            MyCallable myCallable = new MyCallable();
//
            MyFutureTask myFutureTask = new MyFutureTask(myCallable);
//
////            MyFutureTask tmpTask = mMyFutureTaskFutures.putIfAbsent(taskId, myFutureTask);
//            if (tmpTask != null) {
//                ret = 1;
//            } else {
                try {
                    mThreadPool.execute(myFutureTask);
                    ret = 2;
                } catch (RejectedExecutionException e) {
                    ret = 3;
                }
//            }
//        } else {
//            ret = 4;
//        }

        AndyLog.i(TAG, "1");
        return ret;
    }
}
