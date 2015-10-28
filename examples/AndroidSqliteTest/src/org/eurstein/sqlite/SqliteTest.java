package org.eurstein.sqlite;

import org.eurstein.utils.AndyLog;

import java.util.List;
import java.util.Stack;

/**
 * Created by andygzyu on 2015/9/11.
 */
public class SqliteTest {

    private static final String TAG = SqliteTest.class.getSimpleName();

    // 测试写数据
    public static void testWriteDataBase() {
        //recordLog(STConst.ST_USER_ACTION,null);
//        for (byte i = 0; i < 10; i++) {
//            STTable.getInstance().save(i, new byte[1*1024*1024/4]); // 0.25M
//        }
//
//        for (byte i = 0; i < 100; i++) {
//            STTable.getInstance().save(i, new byte[1*1024*1024]); // 1M
//        }

        for (byte i = 0; i < 10; i++) {
            STTable.getInstance().save(i, new byte[i*1024*1024]); // 2M
        }
    }

    public static void testGetTypes() {
        List<Integer> types = STTable.getInstance().getAllType();
        StringBuilder stringBuilder = new StringBuilder("types {");
        for (Integer type : types) {
            stringBuilder.append(type).append(", ");
        }
        stringBuilder.append("}");
        AndyLog.d(TAG, "testGetTypes, all types " + stringBuilder.toString());
    }

    public static List<Long> testGetIds() {
        List<Long> ids = STTable.getInstance().getAllIds();
        StringBuilder stringBuilder = new StringBuilder("ids {");
        for (Long id : ids) {
            stringBuilder.append(id).append(", ");
        }
        stringBuilder.append("}");
        AndyLog.d(TAG, "testGetIds, all ids " + stringBuilder.toString());
        return ids;
    }

    public static void testDelete() {
        List<Long> ids = testGetIds();

        ids.remove(0);
        STTable.getInstance().delete(ids);

        testGetIds();
    }

    public static void testDelete(byte type) {
        STTable.getInstance().delete(type);
    }

    // 测试取数据
    public static void testGetDatas() {
        for (byte type = 0; type < 10; type++) {
            while (true) {
                STTable.DataWrapper dataWrapper = STTable.getInstance().getDatas1(type, 100);
                if (dataWrapper != null && dataWrapper.idList != null) {
                    if (dataWrapper.dataList.size() > 0) {
                        if (dataWrapper.idList.size() == dataWrapper.dataList.size()) {
                            AndyLog.d(TAG, "testGetDatas, type: " + type + " dataWrapper.idList.size(): " + dataWrapper.idList.size());
                            for (int i = 0; i < dataWrapper.idList.size(); i++) {
                                AndyLog.d(TAG, "testGetDatas count: " + i + " id: " + dataWrapper.idList.get(i) + " data length: " + dataWrapper.dataList.get(i).length);
                            }
                            STTable.getInstance().delete(dataWrapper.idList);
                        } else {
                            AndyLog.e(TAG, "testGetDatas, dataWrapper.idList.size():" + dataWrapper.idList.size()+ " != dataWrapper.dataList.size():" + dataWrapper.dataList.size());
                        }
                    } else {
                        AndyLog.d(TAG, "testGetDatas, dataWrapper.dataList.size()=0");
                        break;
                    }

                } else {
                    AndyLog.d(TAG, "testGetDatas, dataWrapper: " + dataWrapper);
                    break;
                }
            }
        }
    }
}
