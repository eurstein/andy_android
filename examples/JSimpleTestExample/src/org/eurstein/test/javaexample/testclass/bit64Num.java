package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

import java.math.BigInteger;

/**
 * Created by andygzyu on 2015/9/24.
 */
public class bit64Num {

    private static final String TAG = bit64Num.class.getSimpleName();

    public static void test() {
        {
            String s = "9923372036854775807"; //
            BigInteger bigInteger = new BigInteger(s);
            long start = System.currentTimeMillis();
            BigInteger bigInteger_4096 = null;
            for (int i = 0; i < 100000; i++) {
                bigInteger_4096 = bigInteger.divide(BigInteger.valueOf(4096l));
            }
            long end1 = System.currentTimeMillis();
            BigInteger bigInteger_12 = null;
            for (int i = 0; i < 100000; i++) {
                bigInteger_12 = bigInteger.shiftRight(12);
            }
            long end2 = System.currentTimeMillis();
            int bigInteger_12_mod = bigInteger_12.mod(BigInteger.valueOf(100)).intValue();

            AndyLog.d(TAG, "bigInteger=" + bigInteger + " bigInteger_4096=" + bigInteger_4096 + " bigInteger_12=" + bigInteger_12 + " bigInteger_12_mod=" + bigInteger_12_mod + " cost " + (end1-start) + ":" + (end2-end1));
        }
        {
            String maxLong = "9223372036854775807"; // Long.MAX_VALUE
            BigInteger bigInteger = new BigInteger(maxLong);
            BigInteger bigInteger_4096 = bigInteger.divide(BigInteger.valueOf(4096l));
            BigInteger bigInteger_12 = bigInteger.shiftRight(12);
            AndyLog.d(TAG, "bigInteger=" + bigInteger + " bigInteger_4096=" + bigInteger_4096 + " bigInteger_12=" + bigInteger_12);

            Long l = Long.parseLong(maxLong);
            long l_4096 = l/4096;
            long l_12 = l >> 12;
            AndyLog.d(TAG, "l=" + l + " l_4096=" + l_4096 + " l_12=" + l_12);
        }
    }
}
