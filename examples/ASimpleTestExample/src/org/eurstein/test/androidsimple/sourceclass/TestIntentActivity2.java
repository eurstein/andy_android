package org.eurstein.test.androidsimple.sourceclass;

import org.eurstein.test.androidexample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TestIntentActivity2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testintent2);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String name = bundle.getString("name");
			Log.i("TestIntentActivity2", "name = " + name);
		}

		// 向上一个Activity返回结果
		Intent intent = new Intent();
		// Intent intent = getIntent(); // 和上面是一样的
		Bundle bundle2 = new Bundle();
		bundle2.putString("name", "This is from ShowMsg!");
		intent.putExtras(bundle2);
		setResult(RESULT_OK, intent);
	}
}
