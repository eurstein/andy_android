package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.utils.AndyLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by andygzyu on 2015/3/23.
 */
public class DataOutputStreamTest {

    private static final String TAG = DataOutputStreamTest.class.getSimpleName();

    public static void test() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < 1000*1000; i++) {
            try {
                dos.write(new byte[1000]); // 内存将被写爆而 OOM crash！
            } catch (IOException e) {
                e.printStackTrace();
                AndyLog.d(TAG, "dos.write " + e.getMessage());
            }
        }
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
