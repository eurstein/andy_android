package org.eurstein.test.androidsimple.sourceclass;

import android.os.Parcel;
import android.os.Parcelable;

public class TestParcelableObject implements Parcelable {

	public int intValue;
	public String strValue1;
	public String strValue2;
	public byte byteValue;
	public boolean boolValue1;
	public boolean boolValue2;
	public long longValue;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(intValue);
		dest.writeString(strValue1);
		dest.writeString(strValue2);
		dest.writeByte(byteValue);
		dest.writeByte((byte) (boolValue1 ? 1 : 0));
		dest.writeByte((byte) (boolValue2 ? 1 : 0));
		dest.writeLong(longValue);
	}

	public static final Parcelable.Creator<TestParcelableObject> CREATOR = new Creator<TestParcelableObject>() {
		@Override
		public TestParcelableObject createFromParcel(Parcel source) {
			TestParcelableObject object = new TestParcelableObject();
			object.intValue = source.readInt();
			object.strValue1 = source.readString();
			object.strValue2 = source.readString();
			object.byteValue = source.readByte();
			object.boolValue1 = source.readByte() != 0;
			object.boolValue2 = source.readByte() != 0;
			object.longValue = source.readLong();
			return object;
		}
		
		@Override
		public TestParcelableObject[] newArray(int size) {
			return null;
		}
	};
}
