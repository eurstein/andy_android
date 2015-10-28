package org.eurstein.test.androidsimple.sourceclass;

import org.eurstein.test.androidexample.R;
import org.eurstein.test.androidsimple.MainActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class TestPendingIntentActivity extends Activity {

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testpendingintent);
		
		OnClickListener onClickListener = 	new OnClickListener() {
			@Override
			public void onClick(final View v) {
				new Thread() {
					@Override
					public void run() {
						super.run();
						testPendingIntent(v.getId());
					}
				}.start();
			}
		};
		
		// 通知1
		this.findViewById(R.id.button1).setOnClickListener(onClickListener);
		// 通知2
		this.findViewById(R.id.button2).setOnClickListener(onClickListener);
		// 更新通知1内容
		this.findViewById(R.id.button3).setOnClickListener(onClickListener);
		// 取消通知1
		this.findViewById(R.id.button4).setOnClickListener(onClickListener);
		// 取消通知2
		this.findViewById(R.id.button5).setOnClickListener(onClickListener);
		// 取消所有通知
		this.findViewById(R.id.button6).setOnClickListener(onClickListener);
	}

	private void testPendingIntent(int buttonId) {
		switch (buttonId) {
		case R.id.button1:
			showNotification(1, 1);
			break;
		case R.id.button2:
			showNotification(2, 2);
			break;
		case R.id.button3:
			showNotification(3, 1);
			break;
		case R.id.button4:
			cancelNotify(1, false);
			break;
		case R.id.button5:
			cancelNotify(2, false);
			break;
		case R.id.button6:
			cancelNotify(0, true);
			break;

		default:
			break;
		}
	}
	
	private void showNotification(int buttonId, int notifyId) {
		//Notification n = new Notification(R.drawable.ic_launcher, "TestPendingIntent通知", (long)(System.currentTimeMillis() + 2000));
		Intent notificationIntent = new Intent(this, MainActivity.class);

		Notification n = new Notification.Builder(this)
		.setContentTitle("ContentTitle" + buttonId)
		.setContentText("ContentText" + buttonId)
		.setOnlyAlertOnce(true) // 一次而已
		.setVibrate(new long[] {200, 200, 200, 200})
		.setSmallIcon(R.drawable.ic_launcher) // 落下前显示的图标
		.setSubText("subText" + buttonId) // 第二行内容
		.setContentInfo("ContentInfo" + buttonId) // 额外的内容，添加到了右下角
		.setShowWhen(true) // 是否允许setWhen
		.setWhen(System.currentTimeMillis()+5000) // 何时通知，延迟500ms
		.setTicker("Ticker" + buttonId, null)
//		.setDefaults(Notification.DEFAULT_SOUND)
//		.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
		.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.images))
		.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT))
		.build();
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(notifyId, n);
	}
	
	private void cancelNotify(int notifyId, boolean cancelAll) {
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		if (cancelAll) {
			nm.cancelAll();
		} else {
			nm.cancel(notifyId);
		}
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
}
