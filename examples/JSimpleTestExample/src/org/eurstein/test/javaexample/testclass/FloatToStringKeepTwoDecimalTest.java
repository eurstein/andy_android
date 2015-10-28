package org.eurstein.test.javaexample.testclass;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eurstein.test.javaexample.utils.AndyLog;

public class FloatToStringKeepTwoDecimalTest {

    private static final String TAG = "FloatToStringKeepTwoDecimalTest";
    
    public static void test() {
        AndyLog.d(TAG, formatToString(1111.200891, 2));
        AndyLog.d(TAG, formatToString(1111.200891, 3));
        AndyLog.d(TAG, formatToString(1111.200891, 4));
        AndyLog.d(TAG, formatToString(1111, 2));
        AndyLog.d(TAG, formatToString(1111, 3));
        AndyLog.d(TAG, formatToString(-1111.200891, 4));

        testDoubleToString(0.027891);
        testDoubleToString(0.0);
        testDoubleToString(-1111.227891);
        testDoubleToString(1111.200891);
//        testFloatToString(0.027891f);
    }


    /**
     * 计算double型数值按指定精度四舍五入后的字符串，若小数位不足，末尾补零
     * 此函数比直接使用DecimalFormat高效，且不会发生奇怪的native crash
     *
     * @param d 需要转成String的double型数值
     * @param precision 精确度，保留小数的位数
     * @return 返回四舍五入后，保留precision位小数的字符串，如果小数位不够，则末尾补零
     */
    public static String formatToString(double d, int precision) {
        StringBuilder get_double_string = new StringBuilder();
        if (precision <= 0) {
            get_double_string.append((int)Math.round(d));
            return get_double_string.toString();
        }
        get_double_string.append((int)d);
        get_double_string.append(".");
        double zoom = 1.0;
        for (int i = 0; i < precision; i++) {
            zoom = zoom * 10;
        }
        double get_double = Math.round((d-(int)d)*zoom)/zoom;
        get_double = Math.abs(get_double);
        for (int i = 0; i < precision; i++) {
            get_double = get_double * 10;
            get_double_string.append(((int)(get_double))%10);
        }
        return get_double_string.toString();
    }

    public static void testDoubleToString(double d) {
        System.out.println(String.format("%.2f", d));
        
        double get_double = 0.0;
        String get_double_string = null;

        get_double = ((int)(d*100))/100;
        AndyLog.i(TAG, "((int)(d*100))/100: " + get_double);
        
        get_double = ((int)(d*100))/100.0;
        AndyLog.i(TAG, "((int)(d*100))/100.0: " + get_double);
        
        //方案一:
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            get_double_string = formatToString(d, 4);
        }
        AndyLog.i(TAG, "方案一(Math.round): " + get_double_string + " - " + get_double + " do 10000 times cost: " + (System.currentTimeMillis() - start));
        
        //方案二:
        start = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < 10000; i++) {
            get_double_string = df.format(d);
        }
        get_double = Double.parseDouble(get_double_string);
        AndyLog.i(TAG, "方案二(DecimalFormat): " + get_double_string + " - " + get_double + " do 10000 times cost: " + (System.currentTimeMillis() - start));

        //方案三:
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            get_double_string = String.format("%.2f", d);
        }
        get_double = Double.parseDouble(get_double_string);
        AndyLog.i(TAG, "方案三(String.format): " + get_double_string + " - " + get_double + " do 10000 times cost: " + (System.currentTimeMillis() - start));

        //方案四:
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            BigDecimal bd = new BigDecimal(d);
            BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            get_double_string = bd2.toString();
        }
        get_double = Double.parseDouble(get_double_string);
        AndyLog.i(TAG, "方案四(BigDecimal): " + get_double_string + " - " + get_double + " do 10000 times cost: " + (System.currentTimeMillis() - start));
    }
    
    public static void testFloatToString(float d) {
        System.out.println(String.format("%.2f", d));
        
        double get_double = 0.0;

        get_double =((int)(d*100))/100;
        AndyLog.i(TAG, "" + get_double);
        
        //方案一:
        get_double = (double)(Math.round(d*100)/100.0);
        AndyLog.i(TAG, "" + get_double);
        //方案二:
        DecimalFormat df = new DecimalFormat("#.##");
        get_double = Double.parseDouble(df.format(d));
        AndyLog.i(TAG, "" + get_double);

        //方案三:
        get_double = Double.parseDouble(String.format("%.2f",d));
        AndyLog.i(TAG, "" + get_double);

        //方案四:
        BigDecimal bd = new BigDecimal(d);
        BigDecimal bd2 = bd.setScale(2,BigDecimal  .ROUND_HALF_UP);
        get_double = Double.parseDouble(bd2.toString());
        AndyLog.i(TAG, "" + get_double);
    }
}
