
package org.eurstein.test.javaexample.testclass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.eurstein.test.javaexample.utils.AndyLog;

public class URLEncodeDecodeTest {
	
	public static final String TAG = "URLEncodeDecodeTest";

    public static void test() {
        String urlStr;
        String originalStr;
        try {
            urlStr = URLEncoder.encode("this is 汉字", "UTF-8");
            originalStr = URLDecoder.decode(urlStr, "UTF-8");
            AndyLog.i(TAG, "urlStr: " + urlStr);
            AndyLog.i(TAG, "originalStr: " + originalStr);
            return;
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        } finally {
            System.out.println("finally!");
        }
    }
}
