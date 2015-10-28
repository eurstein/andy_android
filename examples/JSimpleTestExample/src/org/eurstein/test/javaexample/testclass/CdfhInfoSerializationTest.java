package org.eurstein.test.javaexample.testclass;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.eurstein.test.javaexample.sourceclass.CdfhInfo;
import org.eurstein.test.javaexample.sourceclass.CdfhInfoSerialization;

public class CdfhInfoSerializationTest {

	public static void test() {
		
		LinkedHashMap<Long, CdfhInfo> map = new LinkedHashMap<Long, CdfhInfo>();
		for (int i = 0; i < 1000/*00*/; i++) {
			CdfhInfo cdfhInfo = new CdfhInfo();
			cdfhInfo.compressionMethod = (short)(i%3);
			cdfhInfo.compressedSize = i * i * 1000;
			cdfhInfo.offset = i*(i%9);
			map.put((long)i, cdfhInfo);
		}
		
		String path = "cdfhinfo.txt";
		CdfhInfoSerialization.save(map, path);
		HashMap<Long, CdfhInfo> retmap = CdfhInfoSerialization.load(path);
		if (map.size() == retmap.size()) {
			for (long key : retmap.keySet()) {
				CdfhInfo v1 = map.get(key);
				CdfhInfo v2 = retmap.get(key);
				if (v1.compressionMethod == v2.compressionMethod && v1.compressedSize == v2.compressedSize && v1.offset == v2.offset) {
					continue;
				} else {
					System.out.println("map not equal retmap! key: " + key);
				}
			}
			System.out.println("map equal retmap!");
		} else {
			System.out.println("map not equal retmap! map.size: " + map.size() + " retmap.size: " + retmap.size());
		}
	}
}
