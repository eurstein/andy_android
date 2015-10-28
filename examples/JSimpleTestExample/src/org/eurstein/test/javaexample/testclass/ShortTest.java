
package org.eurstein.test.javaexample.testclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import jdk.nashorn.internal.runtime.FindProperty;

import org.eurstein.test.javaexample.utils.AndyLog;

public class ShortTest {
    private static final String TAG = "ShortTest";

    private static final String fileName = "test/1/DDDDD.txt";

    public static void test() {

        RandomAccessFile raf = null;

        try {
            int i = 0xF0F0; // 61680
            short s = -3856;

            byte b0 = (byte) 0xF0;
            byte b1 = (byte) 0xF0;
            AndyLog.i(TAG, "b0: " + b0);
            AndyLog.i(TAG, "b1: " + b1);
            AndyLog.i(TAG, "(int)b1: " + (int) b1);
            AndyLog.i(TAG, "b1 & 0xFF: " + (b1 & 0xFF));
            AndyLog.i(TAG, "0xF0: " + 0xF0);
            AndyLog.i(TAG, "0xF0F0: " + i);
            AndyLog.i(TAG, "((0xF0 << 8) | 0xF0)" + ((0xF0 << 8) | 0xF0));
            AndyLog.i(TAG, "((0xF0 << 8) + 0xF0)" + ((0xF0 << 8) + 0xF0));

            raf = new RandomAccessFile(fileName, "rw");

            raf.writeByte(b0); // f0
            raf.writeByte(b1); // f0
            raf.writeShort(i); // f0f0
            raf.writeShort(s); // f0f0

            raf.seek(0);
            int readUnsignedShort = raf.readUnsignedShort();
            AndyLog.i(TAG, "raf.readUnsignedShort(): " + readUnsignedShort); // 0xf0f0
                                                                             // -
                                                                             // 61680

            raf.seek(0);
            short readShort = raf.readShort();
            AndyLog.i(TAG, "raf.readShort(): " + readShort); // -3856

            DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
            AndyLog.i(TAG, "dis.readUnsignedShort(): " + dis.readUnsignedShort()); // 61680
            dis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
