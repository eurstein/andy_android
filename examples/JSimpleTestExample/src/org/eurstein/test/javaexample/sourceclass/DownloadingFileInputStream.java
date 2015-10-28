
package org.eurstein.test.javaexample.sourceclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;

import org.eurstein.test.javaexample.utils.AndyLog;

public class DownloadingFileInputStream extends InputStream {

    public static final String TAG = "DownloadingFileInputStream";

    // private int UNINITIALIZED = -1;

    private final Object mLock = new Object();

    private FileChannel mFileChannel = null;

    private String mPatchPath = null;

    /**
     * 文件总长度 The total length of this PatchInputStream, which never changes.
     */
    long totalLen = 0;

    /**
     * 有效数据长度 <code>avaliableLen - 1</code> is the last element that can be read
     * or written. avaliableLen must be no less than zero and no greater than
     * <code>totalLen</code> .
     */
    public long avaliableLen = 0;

    // /**
    // * 读/写操作的当前下标 The current position of this PatchInputStream. Position is
    // * always no less than zero and no greater than <code>avaliableLen</code>.
    // */
    // long position = 0;

    public DownloadingFileInputStream(String patchPath) {
        super();
        if (patchPath == null || patchPath.length() == 0) {
            throw new InvalidParameterException("patchPath is null.");
        }
        mPatchPath = patchPath;
    }

    private boolean initFileChannel() {
        boolean bSucceed = true;
        File f = new File(mPatchPath);
        try {
            mFileChannel = new FileInputStream(f).getChannel();
            avaliableLen = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bSucceed = false;
        }

        return bSucceed;
    }

    /**
     * 通知patch文件长度变更
     * 
     * @param url patch文件对应的url
     * @param patchPath patch文件路径
     * @param availableLength 当前可用数据长度
     * @param totalLength 数据总长度
     */
    public void onStreamLengthChange(long availableLength, long totalLength) {

        AndyLog.i(TAG, "onStreamLengthChange availableLength: " + availableLength
                + " totalLength: "
                + totalLength);

        // assert availableLength >= 0;
        // assert totalLength > 0;

        synchronized (mLock) {
            if (totalLen == 0 && initFileChannel()) {
                totalLen = totalLength;
            }
            avaliableLen = availableLength;
            mLock.notifyAll();
        }
    }

    @Override
    /**
     * read() 方法，这个方法 从输入流中读取数据的下一个字节。
     * 返回 0 到 255 范围内的 int 字节值。
     * 如果因为已经到达流末尾而没有可用的字节，则返回值 -1 。
     */
    public synchronized int read() throws IOException {
        if (avaliableLen == 0) {
            // 流未初始化，等待数据流
            doWait();
        } else if (avaliableLen == Long.MAX_VALUE) {
            // 在流初始化之前已经关闭
            throw new ClosedChannelException();
        }

        if (mFileChannel.position() >= totalLen) {
            // 已经到达流末尾没有可用的字节
            return -1;
        }

        while (avaliableLen <= mFileChannel.position()) {
            // 数据未到达，等待数据
            doWait();
        }

        byte[] b = new byte[1];
        ByteBuffer bb = ByteBuffer.wrap(b);
        int count = mFileChannel.read(bb);
        // assert count == 1;

        return b[0] & 0xFF;
    }

    @Override
    public int available() throws IOException {
        if (mFileChannel == null) {
            return 0;
        }
        // long result = Math.min(avaliableLen - mFileChannel.position(),
        // Integer.MAX_VALUE);
        long result = Math.min(totalLen - mFileChannel.position(), Integer.MAX_VALUE);
        return (int) result;
    }

    @Override
    public void close() throws IOException {
        synchronized (mLock) {
            if (mFileChannel != null) {
                mFileChannel.close();
            }
            avaliableLen = Long.MAX_VALUE;
            mLock.notifyAll();
        }
        super.close();
    }

    @Override
    public void mark(int readlimit) {
        // 不支持，空实现
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized int read(byte[] buffer, int byteOffset, int byteCount)
            throws IOException {
        if ((byteOffset | byteCount) < 0 || byteOffset > buffer.length
                || buffer.length - byteOffset < byteCount) {
            throw new IndexOutOfBoundsException("length=" + buffer.length + "; regionStart="
                    + byteOffset + "; regionLength=" + byteCount);
        }

        if (avaliableLen == 0) {
            // 流未初始化，等待数据流
            doWait();
        } else if (avaliableLen == Long.MAX_VALUE) {
            // 在流初始化之前已经关闭
            throw new ClosedChannelException();
        }

        if (mFileChannel.position() >= totalLen) {
            // 已经到达流末尾没有可用的字节
            return -1;
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, byteOffset, byteCount);

        // 数据不足，读到末尾结束
        byteCount = (int) Math.min(byteCount, totalLen - mFileChannel.position());

        while (byteCount > avaliableLen - mFileChannel.position()) {
            doWait();
        }

        int readcount = mFileChannel.read(byteBuffer);
        // assert byteCount == readcount;

        return readcount;
    }

    @Override
    /**
     * 跳过和丢弃此输入流中数据的n个字节
     * @param	byteCount 要跳过的字节
     * @return	跳过的实际字节数
     * 
     * @Throws	IOException: 如果流不支持搜索，或者发生其他 I/O 错误
     */
    public synchronized long skip(long byteCount) throws IOException {
        if (byteCount <= 0) {
            return 0;
        }

        if (avaliableLen == 0) {
            // 流未初始化，等待数据流
            doWait();
        } else if (avaliableLen == Long.MAX_VALUE) {
            // 在流初始化之前已经关闭
            throw new ClosedChannelException();
        }

        long result = Math.min(totalLen - mFileChannel.position(), byteCount);
        while (result > avaliableLen - mFileChannel.position()) {
            doWait();
        }
        // position += result;
        long position = mFileChannel.position() + result;
        mFileChannel.position(position);

        return result;
    }

    /**
     * 等待，恢复时检查流是否被close过，加入被close过则抛ClosedChannelException异常
     * 
     * @throws ClosedChannelException
     * @throws InstantiationException
     */
    private void doWait() throws IOException {
        synchronized (mLock) {
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            if (avaliableLen == Long.MAX_VALUE) {
                throw new ClosedChannelException();
            }

            if (mFileChannel == null) {
                if (avaliableLen > 0 && avaliableLen < Long.MAX_VALUE) {
                    throw new FileNotFoundException("filename:" + mPatchPath);
                } else {
                    throw new ClosedChannelException();
                }
            }
        }
    }

    /**
     * 返回patch的路径
     * 
     * @return mPatchPath
     */
    public String getPatchPath() {
        return mPatchPath;
    }

    /**
     * 文件是否下载完成
     * 
     * @return
     */
    public boolean isDownloadComplete() {
        return totalLen > 0 && totalLen == avaliableLen;
    }
}
