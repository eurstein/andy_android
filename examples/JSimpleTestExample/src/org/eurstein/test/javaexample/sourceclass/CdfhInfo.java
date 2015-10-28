package org.eurstein.test.javaexample.sourceclass;

public class CdfhInfo {
	//压缩方法
	public short compressionMethod;
	//压缩后的大小
	public int compressedSize;
	//local file header 的相对位移：从“文件出现的第一个磁盘开始”到“local file header的起点”的字节数
	public int offset; // 对应的local file header的偏移
}
