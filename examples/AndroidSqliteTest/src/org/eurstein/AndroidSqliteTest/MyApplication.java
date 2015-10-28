package org.eurstein.AndroidSqliteTest;

import android.app.Application;

/**
 * Created by andygzyu on 2015/9/11.
 */
public class MyApplication extends Application {

    private static MyApplication _app;

    @Override
    public void onCreate() {
        super.onCreate();
        _app = this;
    }

    public static MyApplication self() {
        return _app;
    }
}
