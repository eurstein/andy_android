package org.eurstein.test.androidsimple.sourceclass;

import java.io.File;

import org.eurstein.test.androidexample.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import org.eurstein.test.androidsimple.utils.AndyConstant;

public class TestIntentActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testintent1);

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(final View v) {
				new Thread() {
					@Override
					public void run() {
						super.run();
						switch (v.getId()) {
						case R.id.button1:
							jumpActivityWithoutParam();
							break;
						case R.id.button2:
							jumpActivityWithParam();
							break;
						case R.id.button3:
							jumpNextActivityForResult();
							break;
						// case R.id.button4:
						//
						// break;
						case R.id.button5:
							showWebBrowser();
							break;
						case R.id.button6:
							showMap();
							break;
						case R.id.button7:
							getPathPlan();
							break;
						case R.id.button8:
							showDialers();
							break;
						case R.id.button9:
							startCall();
							break;
						case R.id.button10:
							showSMS();
							break;
						case R.id.button11:
							sendSMS();
							break;
						case R.id.button12:
							sendMMS();
							break;
						case R.id.button13:
							sendEmail1();
							break;
						case R.id.button14:
							sendEmail2();
							break;
						case R.id.button15:
							sendEmail3();
							break;
						case R.id.button16:
							sendEmail4();
							break;
						case R.id.button17:
							playMusic();
							break;
						case R.id.button18:
							playMusic2();
							break;
						case R.id.button19:
							searchAppInMarket();
							break;
						case R.id.button20:
							showAppInMarket();
							break;
						case R.id.button21:
							uninstallApp();
							break;
						default:
							break;
						}
					}
				}.start();
			}
		};
		findViewById(R.id.button1).setOnClickListener(onClickListener);
		findViewById(R.id.button2).setOnClickListener(onClickListener);
		findViewById(R.id.button3).setOnClickListener(onClickListener);
		findViewById(R.id.button4).setOnClickListener(onClickListener);
		findViewById(R.id.button5).setOnClickListener(onClickListener);
		findViewById(R.id.button6).setOnClickListener(onClickListener);
		findViewById(R.id.button7).setOnClickListener(onClickListener);
		findViewById(R.id.button8).setOnClickListener(onClickListener);
		findViewById(R.id.button9).setOnClickListener(onClickListener);
		findViewById(R.id.button10).setOnClickListener(onClickListener);
		findViewById(R.id.button11).setOnClickListener(onClickListener);
		findViewById(R.id.button12).setOnClickListener(onClickListener);
		findViewById(R.id.button13).setOnClickListener(onClickListener);
		findViewById(R.id.button14).setOnClickListener(onClickListener);
		findViewById(R.id.button15).setOnClickListener(onClickListener);
		findViewById(R.id.button16).setOnClickListener(onClickListener);
		findViewById(R.id.button17).setOnClickListener(onClickListener);
		findViewById(R.id.button18).setOnClickListener(onClickListener);
		findViewById(R.id.button19).setOnClickListener(onClickListener);
		findViewById(R.id.button20).setOnClickListener(onClickListener);
		findViewById(R.id.button21).setOnClickListener(onClickListener);
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

	// 无参数Activity跳转
	private void jumpActivityWithoutParam() {
		Intent it = new Intent(this, TestIntentActivity2.class);
		startActivity(it);
	}

	// 向下一个Activity传递数据（使用Bundle和Intent.putExtras）
	private void jumpActivityWithParam() {
		Intent it = new Intent(this, TestIntentActivity2.class);
		Bundle bundle = new Bundle();
		bundle.putString("name", "This is from MainActivity!");
		it.putExtras(bundle); // it.putExtra(“test”, "shuju”);
		startActivity(it); // startActivityForResult(it,REQUEST_CODE);
	}

	// 向上一个Activity返回结果（使用setResult，针对startActivityForResult(it,REQUEST_CODE)启动的Activity）
	Intent mIt = null;
	int REQUEST_CODE = 1;

	private void jumpNextActivityForResult() {
		mIt = new Intent(this, TestIntentActivity2.class);
		startActivityForResult(mIt, REQUEST_CODE);
	}

	// 回调上一个Activity的结果处理函数（onActivityResult）
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mIt == data) {
			Log.i("MainActivity", "mIt == data");
		} else {
			Log.i("MainActivity", "mIt != data");
		}
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_CANCELED)
				Log.i("MainActivity", "cancle");
			else if (resultCode == RESULT_OK) {
				String temp = null;
				Bundle bundle = data.getExtras();
				if (bundle != null)
					temp = bundle.getString("name");
				Log.i("MainActivity", "retrun msg, name = " + temp);
			}
		}
	}

	// 显示网页
	private void showWebBrowser() {
		Uri uri = Uri.parse("http://bing.com");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}

	// 显示地图
	private void showMap() {
		Uri uri = Uri.parse("geo:38.899533,-77.036476");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
		// 4. //其他 geo URI 範例
		// 5. //geo:latitude,longitude
		// 6. //geo:latitude,longitude?z=zoom
		// 7. //geo:0,0?q=my+street+address
		// 8. //geo:0,0?q=business+near+city
		// 9. //google.streetview:cbll=lat,lng&cbp=1,yaw,,pitch,zoom&mz=mapZoom
	}

	// 路径规划
	private void getPathPlan() {
		// Uri uri =
		// Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
		// 4. //where startLat, startLng, endLat, endLng are a long with 6
		// decimals
		// like: 50.123456
		Uri uri = Uri
				.parse("http://maps.google.com/maps?f=d&saddr=50.123456%2050.123456&daddr=59.123456%2059.123456&hl=en");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}

	private void showDialers() {
		Uri uri = Uri.parse("tel:13246751881");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}

	private void startCall() {
		try {
			Uri uri = Uri.parse("tel:13246751881");
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			startActivity(it);
			// <uses-permission android:name="android.permission.CALL_PHONE"/>
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "startCall crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void showSMS() {
		try {
			Uri uri = Uri.parse("smsto:10086");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			it.putExtra("sms_body", "The SMS text");
			it.setType("vnd.android-dir/mms-sms");
			startActivity(it);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "showSMS crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendSMS() {
		try {
			Uri uri = Uri.parse("smsto:10086");
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body", "10086");
			startActivity(it);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendSMS crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendMMS() {
		try {
			Uri uri = Uri.parse("content://media/external/images/media/23");
			Intent it = new Intent(Intent.ACTION_SEND);
			it.putExtra("sms_body", "some text");
			it.putExtra(Intent.EXTRA_STREAM, uri);
			it.setType("image/png");
			startActivity(it);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendMMS crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail1() {
		try {
			Uri uri = Uri.parse("mailto:eurstein@gmail.com");
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(it);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendEmail1 crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail2() {
		try {
			Intent it = new Intent(Intent.ACTION_SEND);
			it.putExtra(Intent.EXTRA_EMAIL, "eurstein@gmail.com");
			it.putExtra(Intent.EXTRA_TEXT, "The email body text");
			it.setType("text/plain");
			startActivity(Intent.createChooser(it, "Choose Email Client"));			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendEmail2 crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail3() {
		try {			
			Intent it = new Intent(Intent.ACTION_SEND);
			String[] tos = { "me@abc.com" };
			String[] ccs = { "you@abc.com" };
			it.putExtra(Intent.EXTRA_EMAIL, tos);
			it.putExtra(Intent.EXTRA_CC, ccs);
			it.putExtra(Intent.EXTRA_TEXT, "The email body text");
			it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
			it.setType("message/rfc822");
			startActivity(Intent.createChooser(it, "Choose Email Client"));
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendEmail3 crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail4() {
		try {
			Intent it = new Intent(Intent.ACTION_SEND);
			it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
			String path = Environment.getExternalStorageDirectory().getPath() + "/pingfanzhilu.mp3";
			File file = new File(path);
			it.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			it.setType("audio/mp3");
			startActivity(Intent.createChooser(it, "Choose Email Client"));
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "sendEmail4 crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void playMusic() {
		try {
//		Uri uri = Uri.parse("file:///sdcard/pingfanzhilu.mp3");
			String path = Environment.getExternalStorageDirectory().getPath() + "/pingfanzhilu.mp3";
			File file = new File(path);
			String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
			String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(file), mimetype);
			//it.setType("audio/mp3");
			startActivity(it);	
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "playMusic crash!" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void playMusic2() {
		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "playMusic2 crash!" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void searchAppInMarket() {
		try {
			Uri uri = Uri.parse("market://search?q=pname:com.google.android.music");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			// where pkg_name is the full package path for an application			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "searchAppInMarket crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void showAppInMarket() {
		try {
			Uri uri = Uri.parse("market://details?id=462");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			// where app_id is the application ID, find the ID
			// by clicking on your application on Market home
			// page, and notice the ID from the address bar			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "showAppInMarket crash!", Toast.LENGTH_SHORT).show();
		}
	}

	private void uninstallApp() {
		try {
			Uri uri = Uri.fromParts("package", AndyConstant.packageName, null);
			Intent it = new Intent(Intent.ACTION_DELETE, uri);
			startActivity(it);			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "uninstallApp crash!", Toast.LENGTH_SHORT).show();
		}
	}
}
