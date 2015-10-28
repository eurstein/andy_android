
package com.techblogon.serviceexample;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    public int testInt = 0;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        registerReceiver();
        // //“mMessenger = new Messenger(new
        // Handler(handlerThread.getLooper())”需要在有Looper的线程创建
        // // 方法1.
        // new Thread() {
        // public void run() {
        // HandlerThread handlerThread = new HandlerThread("handlerThread");
        // handlerThread.start();
        // mMessenger = new Messenger(new Handler(handlerThread.getLooper()) {
        // @Override
        // public void handleMessage(Message msg) {
        // AndyLog.d(TAG, "handleMessage: " + " Thread: " +
        // Thread.currentThread().getName() + "-"
        // + System.identityHashCode(Thread.currentThread()) + "-"
        // + Thread.currentThread().hashCode());
        //
        // // if (mService.get() == null) {
        // // return;
        // // }
        // switch (msg.what) {
        // case MSG_SAY_HELLO:
        // // Toast.makeText(mService.get().getApplicationContext(),
        // // "hello!",
        // // Toast.LENGTH_SHORT).show();
        // String hello = msg.getData().getString("s");
        // AndyLog.i(TAG, "reply msg: " + hello);
        // break;
        // case MSG_GET_PACKAGEINFO:
        // // PackageManagerTest.test(mService.get().getApplicationContext());
        // break;
        // case MSG_TEST_JAVA_EXCEPTION:
        // // 制造一个空指针异常，但是在ExceptionCatcher中处理
        // // StringBuilder sb = null;
        // // sb.append(1);
        // break;
        // case MSG_TEST_JAVA_EXCEPTION_WITHOUT_CATCH:
        // // // 制造一个数组越界异常，且ExceptionCatcher不处理
        // // String[] strArray = new String[2];
        // // strArray[2] = "IndexOutOfBounds";
        // // break;
        // default:
        // super.handleMessage(msg);
        // break;
        // }
        // }
        // });
        // };
        // }.start();
        // //方法2.
        // new Thread() {
        // public void run() {
        // Looper.prepare();
        // mMessenger = new Messenger(new receiverHandler());
        // Looper.loop();
        // };
        // }.start();
        // 方法3.
        new Thread() {
            public void run() {
                mMessenger = new Messenger(new receiverHandler(Looper.getMainLooper()));
            };
        }.start();
    }

    // start the service
    public void onClickStartServie(View V) {
        testInt = 1;
        AndyLog.i(
                TAG,
                "onClickStartServie appcontext: "
                        + System.identityHashCode(getApplicationContext()));
        // start the service from here //MyService is your service class name
        // try {
        // startService(new Intent(this, MyService.class));
        // } catch (Exception e) {
        // // TODO: handle exception
        // e.printStackTrace();
        // AndyLog.i(TAG, "startService crash");
        // }
        bindService();
    }

    private void bindService() {
        // 绑定Service
        bindService(new Intent(getApplicationContext(), MyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
    
    // Stop the started service
    public void onClickStopService(View V) {
        // Stop the running service from here//MyService is your service class
        // name
        // Service will only stop if it is already running.
        // stopService(new Intent(this, MyService.class));
        // 解绑
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    // Say hello to service
    public void onClickSayHelloToService(View v) {
        sendMsgToService(MyService.MSG_SAY_HELLO);
    }
    
    private void testListFiles() {
        // new Thread() {
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // super.run();
        // Looper.prepare();
        // ListFilesTest.scanFile("/sdcard/UCDownloads");
        // Looper.loop();
        // }
        // }.start();
    }
    
    private void toMain2Activity() {
        // Intent it = new Intent(this, Main2Activity.class);
        // startActivity(it);
    }
    
    // Get packageinfo frome service
    public void onClickGetPackageInfoFromeService(View v) {
        sendMsgToService(MyService.MSG_GET_PACKAGEINFO);
    }

    // Test java exception in service
    public void onClickTestJavaExceptionInService(View v) {
        sendMsgToService(MyService.MSG_TEST_JAVA_EXCEPTION);
    }

    // Test java exception without catch in service
    public void onClickTestJavaExceptionWithoutCatchInService(View v) {
        sendMsgToService(MyService.MSG_TEST_JAVA_EXCEPTION_WITHOUT_CATCH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // --------------------- 向Service发消息 --------------------------
    /** 向Service发送Message的Messenger对象 */
    Messenger mService = null;

    /** 判断有没有绑定Service */
    boolean mBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            AndyLog.d(TAG, "onServiceConnected");
            // Activity已经绑定了Service
            // 通过参数service来创建Messenger对象，这个对象可以向Service发送Message，与Service进行通信
            mService = new Messenger(service);
            mBound = true;
            onClickGetPackageInfoFromeService(null);
        }

        public void onServiceDisconnected(ComponentName className) {
            AndyLog.d(TAG, "onServiceDisconnected");
            mService = null;
            mBound = false;
            bindService();
            
            StringBuilder sb = null;
            sb.append(1);
        }
    };

    public void sendMsgToService(int what) {
        if (!mBound)
            return;
        AndyLog.d(TAG, "sendMsgToService " + what);
        // 向Service发送一个Message
        Message msg = Message.obtain(null, what, 0, 0); 
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // ----------------------- 广播方式接受Service消息 -----------------------
    /**
     * 动态注册广播接收器
     */
    public void registerReceiver() {
        MyReceiver msgReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.techblogon.serviceexample.MyService.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);
    }

    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            AndyLog.d(TAG, "onReceive");
            // 拿到进度，更新UI
            int progress = intent.getIntExtra("progress", 0);
            AndyLog.i(TAG, "onReceive, progress: " + progress);
        }
    }

    // --------------------- Messenger方式接受Service消息 ---------------------
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
    static class receiverHandler extends Handler {

        public receiverHandler(Looper looper) {
            super(looper);
        }

        // WeakReference<MyService> mService;

        // receiverHandler(MyService service) {
        // mService = new WeakReference<MyService>(service);
        // }

        @Override
        public void handleMessage(Message msg) {
            AndyLog.d(TAG, "handleMessage: " + " Thread: " + Thread.currentThread().getName() + "-"
                    + System.identityHashCode(Thread.currentThread()) + "-"
                    + Thread.currentThread().hashCode());

            // if (mService.get() == null) {
            // return;
            // }
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    // Toast.makeText(mService.get().getApplicationContext(),
                    // "hello!",
                    // Toast.LENGTH_SHORT).show();
                    String hello = msg.getData().getString("s");
                    AndyLog.i(TAG, "reply msg: " + hello);
                    break;
                case MSG_GET_PACKAGEINFO:
                    // PackageManagerTest.test(mService.get().getApplicationContext());
                    break;
                case MSG_TEST_JAVA_EXCEPTION:
                    // 制造一个空指针异常，但是在ExceptionCatcher中处理
                    // StringBuilder sb = null;
                    // sb.append(1);
                    break;
                case MSG_TEST_JAVA_EXCEPTION_WITHOUT_CATCH:
                    // // 制造一个数组越界异常，且ExceptionCatcher不处理
                    // String[] strArray = new String[2];
                    // strArray[2] = "IndexOutOfBounds";
                    // break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    /**
     * 这个Messenger可以关联到Service里的Handler，Activity用这个对象发送Message给Service，
     * Service通过Handler进行处理。
     */
    private Messenger mMessenger;

}
