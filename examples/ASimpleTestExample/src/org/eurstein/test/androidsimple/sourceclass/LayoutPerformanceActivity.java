
package org.eurstein.test.androidsimple.sourceclass;

import org.eurstein.test.androidexample.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class LayoutPerformanceActivity extends Activity {

    private ListView listView;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testlayoutperformance);
        
        listView = (ListView) findViewById(R.id.listView1);
        LayoutPerformanceAdapter adapter = new LayoutPerformanceAdapter(this);
        listView.setAdapter(adapter);
    }
}
