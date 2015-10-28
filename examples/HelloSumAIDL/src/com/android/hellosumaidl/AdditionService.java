package com.android.hellosumaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * This class exposes the service to client
 */
public class AdditionService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
	    Log.d("AdditionService", "onBind");
		// TODO Auto-generated method stub
		return new IAdditionService.Stub() {
			/**
			 * Implement com.android.hellosumaidl.IAdditionService.add(int, int)
			 */
			
			@Override
			public int add(int value1, int value2) throws RemoteException {
				return value1 + value2;
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
