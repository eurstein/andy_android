
package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

public class StringTest {

    private static final String TAG = "StringTest";

    public static void test() {
        lastIndexOf();
    }

    private static void lastIndexOf() {
        String patchPath = "/sdcard/tencent/tassistant/a.b.c.d_00_1.patch";
        String newGenApkPath = patchPath.substring(0, patchPath.lastIndexOf(".")) + ".apk";
        AndyLog.i(TAG, "patchPath: " + patchPath);
        AndyLog.i(TAG, "newGenApkPath: " + newGenApkPath);
    }

    public static void testStringPlus() {

        String hostPackageName = null;
        int hostVersionCode = 0;
        String path = "tmast://download?hostpname=" + hostPackageName + "&hostversion="
                + hostVersionCode;
        path += "abc" + "def" + "ghi";
        System.out.println(path);
    }

    public static void testValueOf() {
        // test valueOf
        int i = Integer.valueOf("966");
        System.out.println(i);
        // i = Integer.parseInt("66,66");
        i = Integer.parseInt("66,66".replace(",", ""));
        System.out.println(i);


        i = Integer.parseInt(")2604000");
        AndyLog.d(TAG, "Integer.parseInt(\")2604000\") = " + i);
    }

    public static void testEquals() {
        // 结论： equals比较内容，==比较指针
        String a = "abcdefg";
        String b = new String("abcdefg");

        System.out.println("a: " + a.toString() + "\nb: " + b.toString());

        System.out.println("a.toString(): " + a.toString() + "\nb.toString(): " + b.toString());

        System.out.println("a.equals(b): " + a.equals(b));

        System.out.println("(a == b): " + (a == b));

        System.out.println("b.equals(null) " + b.equals(null));
    }

    /**
     * 字符过滤，过滤掉特殊字符
     * 
     * @param text
     * @return
     */
    public static void testFilter(String text) {
        System.out.println("filter\nbefore: " + text + "\nafter: " + filter(text));
    }

    private static String filter(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 0x20 && chars[i] != '/' && chars[i] != '_' && chars[i] != '&'
                    && chars[i] != '|'
                    && chars[i] != '-') {
                result.append(chars[i]);
            }
        }
        return result.toString();
    }

    public static void testStringBufferClear() {
        StringBuffer sb = new StringBuffer();

        sb.append("1234567890");
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());
        sb.setLength(0);
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());

        sb.append("1234567890");
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());
        sb.delete(0, sb.length());
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());

        sb.append("1234567890");
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());
        sb.delete(0, sb.length() - 2);
        AndyLog.d(TAG, "testStringBufferClear " + sb.length() + "-" + sb.toString());
    }

    public static void testBuildNo() {
        BUILD_NO = null;
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());

        BUILD_NO = "";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());


        BUILD_NO = "1";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());


        BUILD_NO = "12";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());


        BUILD_NO = "123";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());


        BUILD_NO = "1234";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());


        BUILD_NO = "12345";
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
        AndyLog.d(TAG, "BUILD_NO " + BUILD_NO + " getBuildNo: " + getBuildNo());
    }

    private static String BUILD_NO = "1";
    public static String getBuildNo() {
        if (BUILD_NO == null || BUILD_NO.contains("BuildNo")) {
            return "0000";
        } else {
            if (BUILD_NO.length() != 4) {
                // 重新矫正成规则的4位数
                if (BUILD_NO.length() <= 0) {
                    BUILD_NO = "0000";
                } else if (BUILD_NO.length() == 1) {
                    BUILD_NO = "000" + BUILD_NO;
                } else if (BUILD_NO.length() == 2) {
                    BUILD_NO = "00" + BUILD_NO;
                } else if (BUILD_NO.length() == 3) {
                    BUILD_NO = "0" + BUILD_NO;
                } else {
                    BUILD_NO = BUILD_NO.substring(BUILD_NO.length() - 4, BUILD_NO.length());
                }
            }

            return BUILD_NO;
        }
    }
}
