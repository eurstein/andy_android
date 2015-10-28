
package com.techblogon.serviceexample;

import java.lang.ref.WeakReference;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.ExceptionCatcher.NativeExceptionHandler;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private static MyService mSelf = null;
    
    /**
     * 当Client绑定Service的时候，通过这个方法返回一个IBinder，Client用这个IBinder创建出的Messenger，
     * 就可以与Service的Handler进行通信了
     */
    @Override
    public IBinder onBind(Intent arg0) {
        AndyLog.d(TAG, "onBind appcontext: " + System.identityHashCode(getApplicationContext())
                + " Thread: " + Thread.currentThread().getName() + "-"
                + System.identityHashCode(Thread.currentThread()) + "-"
                + Thread.currentThread().hashCode());
//        Toast.makeText(this, "Binding", Toast.LENGTH_LONG).show();
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        AndyLog.d(TAG, "onCreate");
        init();
    }

    @Override
    public void onStart(Intent intent, int startId) {
//        Toast.makeText(this, "MyService Started", Toast.LENGTH_LONG).show();
        AndyLog.d(TAG, "onStart");
        // PackageManagerTest.test(getApplicationContext());
        AndyLog.i(TAG, "onStart success");
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        AndyLog.d(TAG, "onDestroy");
    }

    public static MyService self() {
        return mSelf;
    }
    
    private void init() {
        AndyLog.d(TAG, "onCreate 1");
        NativeExceptionHandler.init();
//        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        
        ExceptionCatcher.getInstance().init(getApplicationContext());
        
        // 防止扫描文件时，listFiles()在主线程中调用发生crash
//        new Thread() {
//            public void run() {
//                Looper.prepare();
                mMessenger = new Messenger(new IncomingHandler(MyService.self()));
//                Looper.loop();
//            };
//        }.start();
    }

    // ------------------- 接受Client发来的消息 --------------------------
    /**
     * 用于Handler里的消息类型
     */
    static final int MSG_SAY_HELLO = 1;
    static final int MSG_GET_PACKAGEINFO = 2;
    static final int MSG_TEST_JAVA_EXCEPTION = 3;
    static final int MSG_TEST_JAVA_EXCEPTION_WITHOUT_CATCH = 4;

    /**
     * 在Service处理Activity传过来消息的Handler
     */
    static class IncomingHandler extends Handler {

        WeakReference<MyService> mService;

        IncomingHandler(MyService service) {
            mService = new WeakReference<MyService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            AndyLog.d(TAG, "handleMessage: " + " Thread: " + Thread.currentThread().getName() + "-"
                    + System.identityHashCode(Thread.currentThread()) + "-"
                    + Thread.currentThread().hashCode());

            if (mService.get() == null) {
                return;
            }
            switch (msg.what) {
                case MSG_SAY_HELLO:
//                    Toast.makeText(mService.get().getApplicationContext(), "hello!",
//                            Toast.LENGTH_SHORT).show();
                    mService.get().sendBroadcast();

                    Bundle bundle = new Bundle();
                    bundle.putString("s", "Hello!");
                    int what = MSG_SAY_HELLO;
                    Message message = Message.obtain(null, what, 0, 0);
                    message.setData(bundle);
                    mService.get().replyMsgToClient(msg.replyTo, message);

                    // 测试Messenger的串行处理，结论:退出此函数即可以处理下一个msg
                    new Thread() {
                        public void run() {
                            try {
                                // mService.get().sendBroadcast();
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            AndyLog.d(TAG, "slept 1000");
                        };
                    }.start();
                    break;
                case MSG_GET_PACKAGEINFO:
                    // ListFilesTest.scanFile("/sdcard/UCDownloads");

//                    Handler mainHandler = new Handler(Looper.getMainLooper());
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
                            // TODO Auto-generated method stub
                            PackageManagerTest.test(mService.get().getApplicationContext());
//                        }
//                    });
                    
                    break;
                case MSG_TEST_JAVA_EXCEPTION:
                    // 制造一个空指针异常，但是在ExceptionCatcher中处理
                    StringBuilder sb = null;
                    sb.append(1);
                    break;
                case MSG_TEST_JAVA_EXCEPTION_WITHOUT_CATCH:
                    // 制造一个数组越界异常，且ExceptionCatcher不处理
                    String[] strArray = new String[2];
                    strArray[2] = "IndexOutOfBounds";
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 这个Messenger可以关联到Service里的Handler，Activity用这个对象发送Message给Service，
     * Service通过Handler进行处理。
     */
    private Messenger mMessenger = null;//new Messenger(new IncomingHandler(this));

    // ---------------------- Service向Client发广播 ----------------------
    private Intent intent = new Intent("com.techblogon.serviceexample.MyService.RECEIVER");

    private void sendBroadcast() {
        // 发送Action为com.techblogon.serviceexample.MyService.RECEIVER的广播
        AndyLog.d(TAG, "sendBroadcast");
        intent.putExtra("progress", 2);
        sendBroadcast(intent);
    }

    // ---------------------- Service向Client回消息 ----------------------
    public void replyMsgToClient(Messenger messenger, Message msg) {
        AndyLog.d(TAG, "sendMsgToClient");
        // 向Client回复一个Message
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
