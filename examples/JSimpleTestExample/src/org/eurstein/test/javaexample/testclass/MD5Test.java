package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.sourceclass.MD5;
import org.eurstein.test.javaexample.sourceclass.MD5New;
import org.eurstein.test.javaexample.utils.AndyLog;

import java.io.File;
import java.util.Locale;

/**
 * Created by YuGuangzhen on 15/3/20.
 */
public class MD5Test {

    private static final String TAG = "MD5Test";

    public static void test() {
        File f = new File("filetest.txt");

        AndyLog.d(TAG, "MD5.fileToMD5(\"filetest.txt\")" + MD5.fileToMD5("filetest.txt"));
        AndyLog.d(TAG, "MD5.getFileMD5(f)" + MD5.getFileMD5(f));;
        AndyLog.d(TAG, "MD5.getFileMD5old(f)" + MD5.getFileMD5old(f));;
        AndyLog.d(TAG, "MD5New.getFileMD5(f)" + MD5New.getFileMD5(f));
        AndyLog.d(TAG, "MD5New.toMD5(\"1\")" + MD5New.toMD5("1"));
    }

    public static void test1() {
        String e = "china";
        AndyLog.i(TAG, e + " MD5:" + MD5New.toMD5(e));

        e = "中国";
        AndyLog.i(TAG, e + " MD5:" + MD5New.toMD5(e));
    }
}
