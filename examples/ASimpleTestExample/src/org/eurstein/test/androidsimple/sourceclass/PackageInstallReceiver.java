package org.eurstein.test.androidsimple.sourceclass;

import java.util.ArrayList;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class PackageInstallReceiver extends BroadcastReceiver {
	protected final String TAG = PackageInstallReceiver.class.getName();
	
	public static final int PACKAGE_ADDED = 1;
	public static final int PACKAGE_REMOVED = 2;
	public static final int PACKAGE_REPLACED = 3;

	protected boolean isRegisterReceiver = false;
	
	private static PackageInstallReceiver mInstance = null;
	
	public static synchronized PackageInstallReceiver getInstance() {
		if(mInstance == null) {
			mInstance = new PackageInstallReceiver();
		}
		return mInstance;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		AndyLog.d(TAG, "onReceive >> " + intent.getAction());

		String intentPkgNameString = intent.getDataString();
		if (TextUtils.isEmpty(intentPkgNameString)) {
			AndyLog.e(TAG, "onReceive intentPkgNameString == null ");
			return;
		}

		String packageName = "";
		String values[] = intentPkgNameString.split(":");
		if (values.length == 2) {
			packageName = values[1];
		} else {
			AndyLog.e(TAG,
					"onReceive packageName == null " + intent.getDataString());
			return;
		}
		
		Message msg = Message.obtain();
		msg.obj = packageName;
		
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			AndyLog.d(TAG, "ACTION_PACKAGE_REMOVED >> " + intentPkgNameString);
			msg.what = PACKAGE_REMOVED;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
			AndyLog.d(TAG, "ACTION_PACKAGE_REPLACED >> " + intentPkgNameString);
			msg.what = PACKAGE_REPLACED;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			AndyLog.d(TAG, "ACTION_PACKAGE_ADDED >> " + intentPkgNameString);
			msg.what = PACKAGE_ADDED;
		}
		
		mPackageInstallHandler.sendMessage(msg);
	}
	
	public void register(Context context) {
		// 注册安装监听
	    // 使用不同的Context多次注册，会导致多次接受到同一个应用安装的回调;统一使用getApplicationContext()注册将不会
	    // PS.注意：同一个应用内不同的进程有不同的getApplicationContext()
	    AndyLog.i(TAG, "context: " + System.identityHashCode(context) + " context.getApplicationContext(): " + System.identityHashCode(context.getApplicationContext()));
        context = context == null ? null : context.getApplicationContext();
		if (context != null) {
			IntentFilter installFilter = new IntentFilter();
			installFilter.addDataScheme("package");
			installFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
			installFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			installFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
			context.registerReceiver(this, installFilter);
			isRegisterReceiver = true;
		}
	}
	
	public void unregister(Context context) {
		if (context == null) {
			return;
		}
		if (isRegisterReceiver) {
			context.unregisterReceiver(this);
			isRegisterReceiver = false;
		}
	}
	
	private final Handler mPackageInstallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			notifyPackageChanged((String)msg.obj, msg.what);
		}
	};
	
	private void notifyPackageChanged(String packageName, int packageAction) {
		for(IPackageInstallObserver ob : mObs) {
			ob.onPackageChanged(packageName, packageAction);
		}
	}
	
	ArrayList<IPackageInstallObserver> mObs = new ArrayList<PackageInstallReceiver.IPackageInstallObserver>();
	public static interface IPackageInstallObserver {
		public void onPackageChanged(String packageName, int packageAction);
	}
	
	public void addObserver(IPackageInstallObserver ob) {
		if (!mObs.contains(ob) && ob != null) {
			mObs.add(ob);
		}
	}
	
	public void removeObserver(IPackageInstallObserver ob) {
		if (ob != null) {
			mObs.remove(ob);
		}
	}
}
