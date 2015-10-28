package org.eurstein.test.androidsimple.testclass;

import android.content.Context;
import android.content.Intent;
import org.eurstein.test.androidsimple.utils.AndyConstant;

/**
 * Created by Administrator on 2015/6/25.
 */
public class BroadcastTest {

    private final static String TAG = BroadcastTest.class.getSimpleName();

    public static void test(Context context) {
        Intent intent = new Intent();
        intent.setAction(AndyConstant.packageName+".action.RELATED"); // 固定的广播Action
        intent.putExtra("from", "com.android.test.example"); // 拉起者包名（比如手机管家），新版应用宝会将此字段上报统计
        intent.putExtra("via", "via1"); // via,透传参数，可不设置，新版应用宝直接上报统计
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); // 强制拉起停止状态应用。小米等手机不支持此参数，无法通过广播形式拉起
        context.sendBroadcast(intent);
    }
}
