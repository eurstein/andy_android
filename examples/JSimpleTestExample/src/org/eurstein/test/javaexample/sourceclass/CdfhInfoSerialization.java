
package org.eurstein.test.javaexample.sourceclass;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CdfhInfoSerialization {

	private static final String TAG = "CdfhInfoSerialization";
	
	public static boolean save(LinkedHashMap<Long, CdfhInfo> cdfhInfoMap, String path) {
		long start = System.currentTimeMillis();
		if (cdfhInfoMap == null || cdfhInfoMap.size() <= 0) {
			System.out.println("save - param is wrong. cdfhInfoMap: " + cdfhInfoMap + " path: " + path);
			return false;
		}
		DataOutputStream dos = null;
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path))); // 用BufferedOutputStream和不用耗时差别很大
			dos.writeInt(cdfhInfoMap.size());
			for (CdfhInfo cdfhInfo : cdfhInfoMap.values()) {
				dos.writeShort(cdfhInfo.compressionMethod);
				dos.writeInt(cdfhInfo.compressedSize);
				dos.writeInt(cdfhInfo.offset);
			}
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
		long end = System.currentTimeMillis();
		System.out.println("CdfhInfoUtils - save cost: " + (end - start));
		return true;
	}

	public static HashMap<Long, CdfhInfo> load(String path) {
		long start = System.currentTimeMillis();
		HashMap<Long, CdfhInfo> map = null;
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
			map = new LinkedHashMap<Long, CdfhInfo>();
			long size = dis.readInt();
			for (long i = 0; i < size; i++) {
				CdfhInfo cdfhInfo = new CdfhInfo();
				cdfhInfo.compressionMethod = dis.readShort();
				cdfhInfo.compressedSize = dis.readInt();
				cdfhInfo.offset = dis.readInt();
				map.put(i, cdfhInfo);
			}
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
		long end = System.currentTimeMillis();
		System.out.println("CdfhInfoUtils - load cost: " + (end - start));
		return map;
	}
}
