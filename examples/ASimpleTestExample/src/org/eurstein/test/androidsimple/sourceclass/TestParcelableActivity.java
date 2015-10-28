
package org.eurstein.test.androidsimple.sourceclass;

import org.eurstein.test.androidexample.R;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestParcelableActivity extends Activity {

	private final String TAG = this.getClass().getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_testparcelable);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(mOnClickListener);

		TestParcelableObject p = (TestParcelableObject) getIntent().getParcelableExtra(
				"TestParcelableObject");

		String text = null;
		if (p != null) {
			text = "intValue:" + p.intValue + "\nstrValue1:" + p.strValue1 + "\nstrValue2:"
					+ p.strValue2 + "\nbyteValue:" + p.byteValue + "\nboolValue1:" + p.boolValue1
					+ "\nboolValue2:" + p.boolValue2 + "\nlongValue:" + p.longValue;
			AndyLog.i(TAG, text);
		} else {
			text = "TestParcelableObject p = null";
			AndyLog.i(TAG, text);
		}

		TextView v = (TextView) findViewById(R.id.textView1);
		v.setText(text);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
				case R.id.button1:
					Log.i(TAG, "SecondActivity button click");
					finish();
					break;

				default:
					break;
			}
		}
	};

}
