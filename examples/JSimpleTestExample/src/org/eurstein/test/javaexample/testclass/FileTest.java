
package org.eurstein.test.javaexample.testclass;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.eurstein.test.javaexample.utils.AndyLog;

public class FileTest {

    private static final String TAG = FileTest.class.getSimpleName();

    private static String fileName = "filetest.txt";

    // 测试RandomAccessFile(File file, String mode)新建文件功能
    public static void testFile() {

        File file = new File("test/1/DDDDD");
        // 删除测试文件
        file.delete();
        // 删除测试文件夹
        File dir = new File("test/1");
        dir.delete();

        System.out.println("file.exist(): " + file.exists());
        System.out.println("file.length(): " + file.length());

        File testfile1 = new File("noparent");
        System.out.println("new File(\"noparent\")'s parent = " + testfile1.getParent());

        // 即使testfile不存在，只有它有父目录（test/1)，依然可以去的他的parent目录
        File parent = file.getParentFile();
        System.out.println("new File(\"test/1/DDDDD\")'s parent = " + parent);

        System.out.println("parent.exist(): " + parent.exists());

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        RandomAccessFile mAccessFile = null;
        try {
            mAccessFile = new RandomAccessFile(file, "rw"); // testfile不存在，但可以正常写文件
            mAccessFile.setLength(100);
            mAccessFile.writeUTF("test file");
            System.out.println("file.exist(): " + file.exists());
            System.out.println("file.length(): " + file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } finally {
            try {
                if (mAccessFile != null) {
                    mAccessFile.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // private static void testCreateDirectory(String directory) {
    // File dir = new File(directory);
    // if (!dir.exists()) {
    // dir.mkdirs();
    // }
    // }
    //
    //
    // private static FileChannel getFileReadChannel() {
    // if (fileReadChannel == null) {
    // File f = getFile();
    // FileInputStream fin = null;
    // try {
    // fin = new FileInputStream(f);
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // fileReadChannel = fin.getChannel();
    // try {
    // System.out.println("postion: " + fileReadChannel.position());
    // System.out.println("length: " + f.length());
    //
    // byte[] b = new byte[1];
    // ByteBuffer bb = ByteBuffer.wrap(b);
    // System.out.println("read: " + fileReadChannel.read(bb));
    // System.out.println("read: " + fileReadChannel.read(bb));
    // System.out.println("read: " + fileReadChannel.read(bb));
    //
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // return fileReadChannel;
    // }
    //
    // private static FileChannel getFileWriteChannel() {
    // if (fileWriteChannel == null) {
    // File f = getFile();
    // FileOutputStream fout = null;
    // try {
    // fout = new FileOutputStream(f);
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // fileWriteChannel = fout.getChannel();
    // }
    // return fileWriteChannel;
    // }
    //
    // private static RandomAccessFile getRandomAccessFile() {
    // if (rf == null) {
    // try {
    // rf = new RandomAccessFile(fileName, "rw");
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // return rf;
    // }
    //
    // private static void testReadToByteArray(byte[] b) {
    // ByteBuffer bb = ByteBuffer.wrap(b);
    // try {
    // int count = getFileReadChannel().read(bb);
    // System.out.println("read count = " + count);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // // bb.position(0);
    // // short a = bb.getShort();
    // // System.out.println("bb = " + bb + "a = " + a);
    // }

    private static File getFile() {
        File f = new File(fileName + "1");
        // System.out.println("1." + f.getAbsoluteFile());
        // System.out.println("2." + f.getAbsolutePath());
        // try {
        // System.out.println("3." + f.getCanonicalFile());
        // } catch (IOException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        // System.out.println("4." + f.getName());
        // System.out.println("5." + f.getParent());
        // System.out.println("6." + f.getPath());

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return f;
    }

    // 结论：DataOutputStream是重新写文件的，以最后一次写入为准
    public static void testRewriteFile() {
        try {
            {
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                        new FileOutputStream(fileName)));
                dos.writeUTF("11111111111111111");
                dos.close();
            }
            {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(
                        new FileInputStream(fileName)));
                AndyLog.d(TAG, "dis.available: " + dis.available());
                if (dis.available() > 0) {
                    AndyLog.d(TAG, dis.readUTF());
                }
                dis.close();
            }
            { // 将覆盖之前的内容
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                        new FileOutputStream(fileName)));
                dos.writeUTF("22222222");
                dos.close();
            }
            {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(
                        new FileInputStream(fileName)));
                AndyLog.d(TAG, "dis.available: " + dis.available());
                if (dis.available() > 0) {
                    AndyLog.d(TAG, dis.readUTF());
                }
                dis.close();
            }
            // {
            // DataOutputStream dos = new DataOutputStream(new
            // BufferedOutputStream(
            // new FileOutputStream(fileName)));
            // dos.writeUTF("333");
            // dos.close();
            // }
            { // 此语句快执行完后，文件内容将被清空
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                        new FileOutputStream(fileName)));
                dos.close();
            }
            {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(
                        new FileInputStream(fileName)));
                AndyLog.d(TAG, "dis.available: " + dis.available());
                if (dis.available() > 0) {
                    AndyLog.d(TAG, dis.readUTF());
                }
                dis.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void testWriteToFile() {
        writeToFile("test", "路径是文件夹，能否写");
        writeToFile("test/1.txt", "路径是有效的                                                             第一次写");
        AndyLog.d(TAG, "第一次写的内容是： " + readToString(new File("test/1.txt")));
        writeToFile("test/1.txt", "\n文件已存在，再次写入");
        AndyLog.d(TAG, "第二次写完之后的内容是： " + readToString(new File("test/1.txt")));
    }

    // 结论： 覆盖写，相当于删旧文件，再写新文件，原有内容将全部被覆盖； 追加写将在文件尾部追加写入内容
    private static void writeToFile(String path, String content) {
        if (path == null || path.length() <= 0) {
        }
        if (content == null || content.length() <= 0) {
        }
        FileOutputStream fos = null;
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                AndyLog.d(TAG, "writeToFile file exists");
            }
//            fos = new FileOutputStream(f, true); // 文件末尾追加写
//            fos = new FileOutputStream(f, false); // 覆盖写，相当于删旧文件，再写新文件，原有内容将全部被覆盖
            fos = new FileOutputStream(f); // 默认是覆盖写
            fos.write(content.getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static String readToString(File file) {
        byte[] filecontent = new byte[(int)file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(filecontent);
            return new String(filecontent, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
