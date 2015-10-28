
package org.eurstein.test.androidsimple.sourceclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eurstein.test.androidexample.R;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LayoutPerformanceAdapter extends BaseAdapter {

    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public LayoutPerformanceAdapter(Context context) {
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 15; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("a", i + "");
            list.add(map);
        }

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Map<String, Object> map = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_testlayoutperformance, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
        textView.setText((String) map.get("a"));
        AndyLog.i("p", "getview被执行");
        return convertView;
    }

}
