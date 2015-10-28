package org.eurstein.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import org.eurstein.AndroidSqliteTest.MyApplication;
import org.eurstein.utils.AndyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andygzyu on 2015/9/11.
 */
public class STTable {
    private static final String TAG = STTable.class.getSimpleName();

    private static final String TABLE_NAME = "st_data";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS st_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, data BLOB, data_length INTEGER);";

    private static final String SQL_INSERT = "INSERT INTO st_data (type, data, data_length) values (?, ?, ?)";

    private static final long RECORD_MAX_SIZE = 1 * 1024 * 1024; // 超过1M，读取时会异常IllegalStateException

    private static final long CURSOR_WINDOW_MAX_SIZE = 2 * 1024 * 1024; // CursorWindow size 2097152 bytes

    private static volatile STTable _instance;

    public synchronized static STTable getInstance() {
        if (_instance == null) {
            _instance = new STTable();
        }
        return _instance;
    }

    public String createTableSQL() {
        return SQL_CREATE;
    }

    public boolean save(byte type, byte[] data) {
        AndyLog.d(TAG, "save data");

        if (data == null) {
            AndyLog.d(TAG, "save data == null");
            return false;
        }

        if (data.length <= 0) {
            AndyLog.w(TAG, "save data.length = " + data.length);
        }

        if (data.length > RECORD_MAX_SIZE) {
            AndyLog.w(TAG, "save data > 1M (" + data.length + ")");
        }

        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("data", data);
        values.put("data_length", data.length);
        final SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getWritableDatabase();
        return db.insert(TABLE_NAME, null, values) > 0;
    }

    public boolean save(byte type, List<byte[]> datas) {
        AndyLog.d(TAG, "save datas");

        final SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getWritableDatabase();

        db.beginTransaction();
        try {
            SQLiteStatement statement = db.compileStatement(SQL_INSERT);

            for (byte[] data : datas) {
                if (data == null || data.length > RECORD_MAX_SIZE) {
                    continue;
                }
                statement.bindLong(1, type);
                statement.bindBlob(2, data);
                statement.bindLong(3, data.length);
                statement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public List<Integer> getAllType() {
        AndyLog.d(TAG, "getAllType");

        List<Integer> typeList = new ArrayList<Integer>();
        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select distinct type from " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    typeList.add(type);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return typeList;
    }

    public List<Long> getAllIds() {
        AndyLog.d(TAG, "getAllIds");

        List<Long> ids = new ArrayList<Long>();
        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select distinct _id from " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    ids.add(id);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }

    public List<Long> getIds(byte type, int count) {
        AndyLog.d(TAG, "getIds, type: " + type + " count: " + count);

        List<Long> ids = new ArrayList<Long>();
        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[] {"_id"}, "type = ?", new String[] {Byte.toString(type)}, null, null, "_id asc", "0," + count);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    ids.add(id);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }

    public static class DataWrapper{
        public List<Long> idList;
        public List<byte[]> dataList;
    }

    public DataWrapper getDatas(byte type, int count) {
        AndyLog.d(TAG, "getDatas");

        DataWrapper result = new DataWrapper();
        result.dataList = new ArrayList<byte[]>();
        result.idList = new ArrayList<Long>();

        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getReadableDatabase();
        List<Long> invalidIds = null;
        Cursor cursor = null;
        long size = 0;
        try {
            cursor = db.query(TABLE_NAME, null, "type = ?", new String[] {Byte.toString(type)}, null, null, "_id asc", "0," + count);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    byte[] data = cursor.getBlob(cursor.getColumnIndex("data"));
                    long dataLength = cursor.getLong(cursor.getColumnIndex("data_length"));
                    if (data.length == 0 || (dataLength > 0 && (Math.abs(data.length-dataLength) > 100 || data.length > RECORD_MAX_SIZE))) {
                        if (invalidIds == null) {
                            invalidIds = new ArrayList<>();
                        }
                        invalidIds.add(id);
                    } else {
                        size += data.length;
                        result.idList.add(id);
                        result.dataList.add(data);
                        if (size >= CURSOR_WINDOW_MAX_SIZE) {
                            AndyLog.d(TAG, "size:" + size + " > CURSOR_WINDOW_MAX_SIZE");
                            break;
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (invalidIds != null && invalidIds.size() > 0) {
            AndyLog.d(TAG, "getDatas invalidIds.size()=" + invalidIds.size() + " " + invalidIds);
            delete(invalidIds);
        }
        return result;
    }

    /**
     * 分两步走，方便清理异常的数据（数据超大的记录）
     * 1. 取符合条件的记录ids
     * 2. 根据id取数据，假如取数据失败，那么将对应记录删除（有一定可能性误删，后续要观察统计数据）
     *
     * @param type
     * @param count
     * @return
     */
    public DataWrapper getDatas1(byte type, int count) {
        DataWrapper result = new DataWrapper();
        result.dataList = new ArrayList<>();
        result.idList = new ArrayList<>();

        List<Long> ids = getIds(type, count);
        if (ids == null || ids.size() <= 0) {
            return result;
        }

        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getReadableDatabase();
        List<Long> invalidIds = null;
        Cursor cursor = null;
        long size = 0;
        for (Long id : ids) {
            try {
                cursor = db.rawQuery(
                        "select data, data_length from " + TABLE_NAME + " where _id = ?",
                        new String[] { Long.toString(id) });
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        byte[] data = cursor.getBlob(cursor.getColumnIndex("data"));
                        long dataLength = cursor.getLong(cursor.getColumnIndex("data_length"));
                        if (data.length == 0 || (dataLength > 0 &&
                                (Math.abs(data.length - dataLength) > 100 ||
                                        data.length > RECORD_MAX_SIZE))) {
                            if (invalidIds == null) {
                                invalidIds = new ArrayList<>();
                            }
                            invalidIds.add(id);
                        } else {
                            result.idList.add(id);
                            result.dataList.add(data);
                            size += data.length;
                            if (size >= CURSOR_WINDOW_MAX_SIZE) {
                                AndyLog.w(TAG, "xxxxxxx size:" + size + " > CURSOR_WINDOW_MAX_SIZE");
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } catch (IllegalStateException e) {
                AndyLog.w(TAG, "xxxxxx id: " + id);
                // 异常数据，记录并删除
                if (invalidIds == null) {
                    invalidIds = new ArrayList<>();
                }
                invalidIds.add(id);
                e.printStackTrace();
            } catch (Throwable throwable) {
                AndyLog.w(TAG, "sssss id: " + id);
                throwable.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (invalidIds != null && invalidIds.size() > 0) {
            AndyLog.d(TAG, "getDatas invalidIds.size()=" + invalidIds.size() + " " + invalidIds);
            delete(invalidIds);
        }
        return result;
    }

    public boolean delete(List<Long> ids) {
        AndyLog.d(TAG, "delete");

        if (ids == null || ids.size() == 0) {
            return false;
        }
        StringBuffer sb = new StringBuffer("(");
        for(Long id : ids){
            sb.append(id);
            sb.append(",");
        }
        if(sb.length() > 1){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(")");
        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getWritableDatabase();
        int number = db.delete(TABLE_NAME, "_id in " + sb.toString(), null);
        return number > 0;
    }

    public synchronized boolean delete(byte type) {
        AndyLog.d(TAG, "delete type:" + type);
        SQLiteDatabase db = SqliteHelper.getInstance(MyApplication.self()).getWritableDatabase();
        db.delete(TABLE_NAME, "type = ?", new String[] {Byte.toString(type)});
        return true;
    }
}
