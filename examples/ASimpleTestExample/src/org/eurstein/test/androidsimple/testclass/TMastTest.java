package org.eurstein.test.androidsimple.testclass;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import org.eurstein.test.androidsimple.utils.AndyConstant;

/**
 * Created by Administrator on 2015/6/25.
 */
public class TMastTest {

    private final static String TAG = TMastTest.class.getSimpleName();

    public static void testTmast(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("tmast://www.baidu.com"));
        context.startActivity(intent);
    }

    public static void testService(Context context) {
        Intent intent = new Intent();
        intent.setAction(AndyConstant.packageName + ".SDKService"); // 固定Service的Action
        intent.putExtra("from", "com.android.test.example"); // 拉起者包名（比如手机管家），新版应用宝会将此字段上报统计
        intent.putExtra("via", "via1"); // via,透传参数，可不设置，新版应用宝直接上报统计
        context.startService(intent);
    }

    public static void testService1(Context context) {
        Intent intent = new Intent();
        intent.setAction(AndyConstant.packageName + ".SDKService"); // 固定的广播Action
        intent.putExtra("from", "com.android.test.example"); // 拉起者包名（比如手机管家），新版应用宝会将此字段上报统计
        intent.putExtra("via", "via1"); // via,透传参数，可不设置，新版应用宝直接上报统计
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //  创建ServiceConnection对象
    private static ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // 获得AIDL服务对象
        }
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
        }
    };

}
