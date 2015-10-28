
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
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;

public class Main2Activity extends Activity {

    public int testInt = 0;
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        registerReceiver();
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
        // 绑定Service
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
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
//        Intent it = new Intent(this, Main2Activity.class);
//        startActivity(it);
//        sendMsgToService(MyService.MSG_SAY_HELLO);
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
        }

        public void onServiceDisconnected(ComponentName className) {
            AndyLog.d(TAG, "onServiceDisconnected");
            mService = null;
            mBound = false;
        }
    };

    public void sendMsgToService(int what) {
        if (!mBound)
            return;
        AndyLog.d(TAG, "sendMsgToService " + what);
        // 向Service发送一个Message
        Message msg = Message.obtain(null, what, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Service -> Activity
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
}
