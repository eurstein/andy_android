
package org.eurstein.test.javaexample.testclass;

// 一次运行结果：
// Exception err: 0
// oneBigTryCatch cost:    3
// manySmallTryCatch cost: 2
// oneBigTryCatch cost:    0
// manySmallTryCatch cost: 0
//
// Exception err: -2
// oneBigTryCatch cost:    70
// manySmallTryCatch cost: 64
// oneBigTryCatch cost:    62
// manySmallTryCatch cost: 62
//
// 结果：
// 1. 不管是否有异常，oneBigTryCatch 和 manySmallTryCatch 耗时相当
// 2. 运行时有异常的耗时 远大于 无异常发生的耗时
//
// 结论: 
// 1. trycatch不耗时，抛异常才耗时
// 2. 多个连续的trycatch 和 一个巨大的trycatch耗时相当？？

public class TryCatchTest {

    public static void test() {
        long time = 100000; // 调用次数

        // 运行无异常
        int err = 0;
        System.out.println("Exception err: " + err);
        testOneBigTryCatch(err, time);
        testManySmallTryCatch(err, time);
        testOneBigTryCatch(err, time);
        testManySmallTryCatch(err, time);

        System.out.println("");

        // 运行有异常
        err = -2;
        System.out.println("Exception err: " + err);
        testOneBigTryCatch(err, time);
        testManySmallTryCatch(err, time);
        testOneBigTryCatch(err, time);
        testManySmallTryCatch(err, time);
    }

    private static void testOneBigTryCatch(int err, long time) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            oneBigTryCatch(err);
        }
        long end = System.currentTimeMillis();
        System.out.println("oneBigTryCatch cost:\t" + (end - start));
    }

    private static void testManySmallTryCatch(int err, long time) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            manySmallTryCatch(err);
        }
        long end = System.currentTimeMillis();
        System.out.println("manySmallTryCatch cost:\t" + (end - start));
    }

    private static int oneBigTryCatch(int a) {
        int errorCode = 0;
        try {
            errorCode = -1;
            if (a == -1) {
                throw new Exception("a == -1, error");
            }

            errorCode = -2;
            if (a == -2) {
                throw new Exception("a == -2, error");
            }

            errorCode = -3;
            if (a == -3) {
                throw new Exception("a == -3, error");
            }

            errorCode = 0;
        } catch (Exception e) {
            // System.out.println("catch errorCode = " + errorCode);
        } finally {
            // System.out.println("finally errorCode = " + errorCode);
        }
        return errorCode;
    }

    private static int manySmallTryCatch(int a) {
        int errorCode = 0;
        try {
            if (a == -1) {
                throw new Exception("a == -1, error");
            }
        } catch (Exception e) {
            // System.out.println("catch errorCode = " + errorCode);
            errorCode = -1;
            return errorCode;
        }

        try {
            if (a == -2) {
                throw new Exception("a == -2, error");
            }
        } catch (Exception e) {
            // System.out.println("catch errorCode = " + errorCode);
            errorCode = -2;
            return errorCode;
        }

        try {
            if (a == -3) {
                throw new Exception("a == -3, error");
            }
        } catch (Exception e) {
            // System.out.println("catch errorCode = " + errorCode);
            errorCode = -3;
            return errorCode;
        }

        return errorCode;
    }

}
