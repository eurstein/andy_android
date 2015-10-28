
package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

public class TryCatchOtherThreadException {

    private static final String TAG = "TryCatchOtherThreadException";

    public static void test() {
        for (int i = 0; i < 10; i++) {
            AndyLog.i(TAG, "test a " + i);
            tryCatchSelfThreadException();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            AndyLog.i(TAG, "test b " + i);
            tryCatchOtherThreadException();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            AndyLog.i(TAG, "test c " + i); 
        }
    }

    // 其他线程异常，当前线程不能捕获其他线程的异常，虽然其他线程异常了，但是当前线程依然可以继续执行
    private static void tryCatchOtherThreadException() {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    StringBuilder sb = null;
                    sb.append(1); // 制造一个空指针异常
                }
            }.start();
        } catch (Exception e) {
//            e.printStackTrace();
            AndyLog.i(TAG, "tryCatchOtherThreadException is catched");
        }
        AndyLog.i(TAG, "tryCatchOtherThreadException end");
    }

    // 线程异常，在线程内部可以捕获，程序不会崩溃退出
    private static void tryCatchSelfThreadException() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    StringBuilder sb = null;
                    sb.append(1); // 制造一个空指针异常
                } catch (Exception e) {
//                    e.printStackTrace();
                    AndyLog.i(TAG, "tryCatchSelfThreadException is catched");
                }
                AndyLog.i(TAG, "tryCatchOtherThreadException run end");
            }
        }.start();
    }

}
