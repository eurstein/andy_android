
package com.tencent.assistant.localres.localapk.loadapkservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

import com.qq.AppService.AstApp;
import com.tencent.assistant.utils.XLog;

/**
 * 标记正在处理的安装包，删除上次标记的坏包等功能
 * 
 * @author andygzyu, Created on 2015-1-27.
 */
public class BadApkMarkerOld {

    private static final String TAG = BadApkMarkerOld.class.getSimpleName();

    /* 记录坏包路径的持久化文件 */
    private String badApkRecordFile;
    /* 正在处理的包路径 */
    private String processingApkPath;

    public BadApkMarkerOld(Context context) {
        XLog.d(TAG, "BadApkMarker context: " + context);
        badApkRecordFile = AstApp.self().getFilesDir() + "/lastBadApk";
        XLog.d(TAG, "BadApkMarker badApkRecordFile: " + badApkRecordFile);
    }

    public void setProcessingApk(String apkPath) {
        processingApkPath = apkPath;
    }
    
    /**
     * 记录坏包路径
     * 
     * @return
     */
    public boolean markProcessingApkBad() {
        DataOutputStream dos = null;
        try {
            File f = new File(badApkRecordFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
                    badApkRecordFile)));
            dos.writeUTF(processingApkPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    
    /**
     * 删除坏包，清除记录
     */
    public void deleteBadApkAndCleanMark() {
        XLog.d(TAG, "deleteBadApkAndCleanMark 0");
        String badApkPath = getLastBadApkPath();
        XLog.d(TAG, "deleteBadApkAndCleanMark 1 badapk: " + badApkPath);
        if (badApkPath != null && !TextUtils.isEmpty(badApkPath)) {
            XLog.d(TAG, "deleteBadApkAndCleanMark 2");
            File f = new File(badApkPath);
            if (f.exists()) {
                XLog.d(TAG, "deleteBadApkAndCleanMark 3");
                f.delete();
            }
            XLog.d(TAG, "deleteBadApkAndCleanMark 4");
            cleanMark();
        }
    }
    
    /**
     * 取上一次运行时遇到的坏包路径
     * 
     * @return
     */
    private String getLastBadApkPath() {
        File f = new File(badApkRecordFile);
        if (!f.exists()) {
            return null;
        }
        String lastProcessingApk = null;
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(badApkRecordFile)));
            lastProcessingApk = dis.readUTF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lastProcessingApk;
    }

    /**
     * 清除坏包记录
     */
    private void cleanMark() {
        XLog.d(TAG, "cleanProcessingApkMark markFilePath: " + badApkRecordFile);
        File f = new File(badApkRecordFile);
        if (f.exists()) {
            f.delete();
        }
    }
}
