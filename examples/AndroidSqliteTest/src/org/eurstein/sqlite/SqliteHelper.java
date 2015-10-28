package org.eurstein.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import org.eurstein.utils.AndyLog;

/**
 * Created by andygzyu on 2015/9/11.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = SqliteHelper.class.getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "android_sqlite_test.db";

    private volatile static SqliteHelper _instance;

    public static synchronized SqliteHelper getInstance(Context context) {
        if (_instance == null) {
            _instance = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        }
        return _instance;
    }

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db = super.getWritableDatabase();
        while (db.isDbLockedByCurrentThread()) {
            AndyLog.w(TAG, "getWritableDatabase db.isDbLockedByCurrentThread() = true");
            SystemClock.sleep(10l);
        }
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        final SQLiteDatabase db = super.getReadableDatabase();
        while (db.isDbLockedByCurrentThread()) {
            AndyLog.w(TAG, "getReadableDatabase db.isDbLockedByCurrentThread() = true");
            SystemClock.sleep(10l);
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AndyLog.d(TAG, "onCreate");
        db.execSQL(STTable.getInstance().createTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AndyLog.d(TAG, "onUpgrade");
        db.execSQL(STTable.getInstance().createTableSQL());
    }


}
