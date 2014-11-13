package com.box.boxandroidlibv2.dao;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxBase;
import com.box.boxjavalibv2.dao.BoxObject;
import com.box.boxjavalibv2.dao.IBoxParcelWrapper;
import com.box.boxjavalibv2.dao.IBoxParcelable;

/**
 * A parcel wrapper wrapping android parcel class.
 */
public class BoxParcel implements IBoxParcelWrapper {

    private final Parcel mParcel;

    public BoxParcel(Parcel parcel) {
        this.mParcel = parcel;
    }

    @Override
    public void writeString(String string) {
        mParcel.writeString(string);
    }

    @Override
    public String readString() {
        return mParcel.readString();
    }

    @Override
    public void writeLong(long value) {
        mParcel.writeLong(value);
    }

    @Override
    public long readLong() {
        return mParcel.readLong();
    }

    @Override
    public void writeBooleanArray(boolean[] val) {
        mParcel.writeBooleanArray(val);
    }

    @Override
    public void readBooleanArray(boolean[] val) {
        mParcel.readBooleanArray(val);
    }

    @Override
    public void writeInt(int val) {
        mParcel.writeInt(val);
    }

    @Override
    public int readInt() {
        return mParcel.readInt();
    }

    @Override
    public double readDouble() {
        return mParcel.readDouble();
    }

    @Override
    public void writeDouble(double value) {
        mParcel.writeDouble(value);
    }

    @Override
    public void writeParcelable(IBoxParcelable val, int flags) {
        if (val != null) {
            val.writeToParcel(this, flags);
        }
        else {
            this.writeBooleanArray(new boolean[] {false});
        }
    }

    @Override
    public void writeMap(Map<String, Object> map) {
        HashMap<String, Object> newMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (!(value instanceof BoxObject) || value instanceof Parcelable) {
                // Makes sure non parcelable BoxObject not getting into parcel
                newMap.put(entry.getKey(), value);
            }
        }
        mParcel.writeMap(newMap);
    }

    @Override
    public void readMap(Map<String, Object> map) {
        mParcel.readMap(map, BoxBase.class.getClassLoader());
    }

    @Override
    public boolean isNull() {
        boolean[] isNotNull = new boolean[1];
        mParcel.readBooleanArray(isNotNull);
        return !isNotNull[0];
    }

    @Override
    public void initParcel() {
        this.writeBooleanArray(new boolean[] {true});
    }
}
