
package org.eurstein.test.androidsimple;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.eurstein.test.androidexample.R;
import org.eurstein.test.androidsimple.testclass.*;
import org.eurstein.test.androidsimple.utils.AndyLog;

// public protected private abstract static final transient volatile synchronized native strictfp

public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity" + " andygzyu";

    private void startTestInBackThread() {
//
//        QuaTest quaTest = new QuaTest();
//        quaTest.startTest(getApplicationContext());
//
//        ApkFileTest apkFileTest = new ApkFileTest();
//        apkFileTest.testCrc();
//        apkFileTest.testMd5();
//        apkFileTest.test();
//
//        MakeABugTest.test(); // 不能跨进程trycatch
//
//        TimeTest.testTime();
//        TimeTest.testCalendar();
//        TimeTest.testContentResolver(this);
//        TimeTest.testDate();
//
//        MainThreadTest.test(this);
//
//        ForeachTest.test();
//
//        MD5Test.test();
//
//        ParcelableObjectTest.test(this);
//
//        IntentActivityTest.test(this);
//
//        PendingintentTest.test(this);
//
//        LayoutPerformanceTest.test(this);
//
//        Toast.makeText(getApplicationContext(), null, Toast.LENGTH_LONG).show();
//
//        PackageManagerTest.test(getApplicationContext());
//        ThreadPoolExecutorTest.getInstance().startTask("test" + System.currentTimeMillis());
//
//        PackageInstallListenerTest.getInstance().register(getApplicationContext());
//        PackageInstallListenerTest.getInstance()
//                .register(this); // 使用不同的Context多次注册，会导致PackageInstallReceiver多次接受到同一个应用安装的回调
//
//        BundleTest.test();
//        SignatureGetTest.test(getApplicationContext());
//        DataOutputStreamTest.test();
//        TMastTest.testService(this);

//        ApkFileTest.testzip("/sdcard/local/apkinstall/cmbchina_new.apk");

//        ApkFileTest.testzip("/sdcard/local/apkinstall/cmbchina_origin.apk");
//        ApkFileTest.testzip("/sdcard/local/apkinstall/cmbchina_4.4.apk");
    }

    private void startTestInMainThread() {
        AndyLog.d(TAG, "current thread is " + Thread.currentThread().getName());
    }

    private void button2OnClicked() {
        BroadcastTest.test(this);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                final int vid = v.getId();
                new Thread() {
                    public void run() {
                        switch (vid) {
                            case R.id.button1:
                                AndyLog.i(TAG, "button1 click");
                                startTestInBackThread();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        startTestInMainThread();
                                    }
                                });
                                break;
                            case R.id.button2:
                                AndyLog.i(TAG, "button2 click");
                                button2OnClicked();
                                break;
                            default:
                                break;
                        }
                    }
                }.start();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(onClickListener);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            return rootView;
        }
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exitByDoubleClick();
            return true;

        }
        return false;
    }

    private Toast toast = null;
    private long lastBackClickTime = 0;

    private void exitByDoubleClick() {
        try {
            long cTime = System.currentTimeMillis();
            if (cTime - lastBackClickTime > 2000) {
                if (toast != null) {
                    toast.cancel();
                }
                lastBackClickTime = cTime;
                (toast = Toast.makeText(this, "再按一次退出程序",
                        Toast.LENGTH_SHORT)).show();
            } else {
                if (toast != null) {
                    toast.cancel();
                }
                exit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exit() {
        finish();
        // 延迟半秒杀进程
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                AndyLog.i("andygzyu",
                        "kill process in main called.last create time");
                Process.killProcess(Process.myPid());
            }
        }, 800);
    }
}
