
package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class MainThreadTest {

	private static final String TAG = "MainThreadTest";
	
	public static void test(final Context context) {
		boolean b = false;
		if (b) {
			Handler mainHandler = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 1) {
						Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
					}
				}
			};
			Message msg = mainHandler.obtainMessage(1, 2, 3, "给主线程发送消息了");
			mainHandler.sendMessage(msg);
			AndyLog.i(TAG, "");
		} else {
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
