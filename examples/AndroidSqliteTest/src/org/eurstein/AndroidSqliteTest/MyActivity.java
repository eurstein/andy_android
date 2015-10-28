package org.eurstein.AndroidSqliteTest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.eurstein.sqlite.SqliteHelper;
import org.eurstein.sqlite.SqliteTest;
import org.eurstein.utils.AndyLog;

public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initButtonEvent();
    }

    private void initButtonEvent() {
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(onClickListener);
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int viewId = v.getId();
            new Thread() {
                @Override
                public void run() {
                    switch (viewId) {
                        case R.id.button1:
                            SqliteTest.testWriteDataBase();
                            AndyLog.d(TAG, "button1 click");
                            break;
                        case R.id.button2:
                            SqliteTest.testGetTypes();
//                            SqliteTest.testGetIds();
//                            SqliteTest.testGetDatas();
//                            SqliteTest.testGetIds();
//                            SqliteTest.testDelete();
                            SqliteTest.testDelete((byte)1);
                            AndyLog.d(TAG, "button2 click");
                            break;
                        default:
                            break;
                    }
                }
            }.start();
        }
    };
}
