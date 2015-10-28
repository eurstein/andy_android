
package org.eurstein.test.javaexample.testclass;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.eurstein.test.javaexample.sourceclass.DownloadingFileInputStream;
import org.eurstein.test.javaexample.utils.AndyLog;

public class BufferedInputStreamTest {

    // 总结
    // 下面方法返回的readLen可能小于fileName.length，从而导致意想不到的结果出现
    // int readLen = dis.read(fileName, 0, fileName.length);
    // 这是因为BufferedInputStream的实现导致的，假如available为0时，那么即使没有读满fileName.length，也会返回
    //
    // java中的BufferedInputStream相关实现
    // // if not closed but no bytes available, return
    // InputStream input = in;
    // if (input != null && input.available() <= 0)
    // return n;
    //
    // android中BufferedInputStream类的相关实现
    // if (localIn.available() == 0) {
    // return byteCount - required;
    // }
    //
    // 解决方法：
    // DownloadingFileInputStream重写available方法，用返回值标示是否还有可读数据，返回值由
    // long result = Math.min(avaliableLen - mFileChannel.position(), Integer.MAX_VALUE);
    // 改为
    // long result = Math.min(totalLen - mFileChannel.position(), Integer.MAX_VALUE);
    // 如此,BufferedInputStream的循环将会继续往下走，在下一次循环时调用DownloadingFileInputStream类的read函数将会被阻塞掉

    private static final String TAG = "BufferedInputStreamTest";

    private static final String testfile = "test/testfile";

    public static void test() {

        final long length = new File(testfile).length();

        AndyLog.i(TAG, "length: " + length);

        final int BUFFERSIZE = 8192;

        final long totalLen = BUFFERSIZE * 2 + 20;

        final DownloadingFileInputStream dfis = new DownloadingFileInputStream(testfile);

        new Thread() {
            public void run() {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(dfis));

                try {
                    long count = 0;
                    while (count < totalLen) {
                        count += 20;
                        if (count == 8200) {
                            Thread.sleep(10);
                        }
                        byte[] fileName = new byte[20];
                        int readLen = dis.read(fileName, 0, fileName.length);
                        String fileNameStr = new String(fileName, "UTF-8");
                        AndyLog.i(TAG, "count: " + count + " fileName: " + fileNameStr
                                + " readLen: " + readLen + " fileNameLen: " + fileNameStr.length());

                        Thread.sleep(5);
                    }

                    dis.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();

        new Thread() {
            public void run() {
                RandomAccessFile accessFile = null;
                FileDescriptor fd = null;
                try {
                    accessFile = new RandomAccessFile(testfile, "rw");
                    fd = accessFile.getFD();
                    accessFile.setLength(0);
                    fd.sync();
                    accessFile.setLength(totalLen);
                    fd.sync();
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                byte[] b = new byte[BUFFERSIZE];
                ByteBuffer bb = ByteBuffer.wrap(b);
                for (int i = 0; i < b.length; i++) {
                    bb.put((byte) 49);
                }

                while (dfis.avaliableLen < totalLen) {
                    long limit = Math.min(dfis.avaliableLen + BUFFERSIZE, totalLen);

                    try {
                        accessFile.seek(dfis.avaliableLen);
                        accessFile.write(b, 0, (int) (limit - dfis.avaliableLen));
                        fd.sync();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    dfis.onStreamLengthChange(limit, totalLen);
                    AndyLog.i(TAG, "limit: " + limit);

                    try {
                        Thread.sleep(5000);
                        // dfis.close();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                // try {
                // accessFile.close();
                // } catch (IOException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
            };
        }.start();

    }
}
