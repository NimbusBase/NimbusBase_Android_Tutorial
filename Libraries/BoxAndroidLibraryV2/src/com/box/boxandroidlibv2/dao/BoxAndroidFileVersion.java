package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxFileVersion;

/**
 * Data class for file version.
 */
public class BoxAndroidFileVersion extends BoxFileVersion implements Parcelable {

    public BoxAndroidFileVersion() {
        super();
    }

    private BoxAndroidFileVersion(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidFileVersion(BoxAndroidFileVersion obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidFileVersion(Map<String, Object> map) {
        super(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidFileVersion> CREATOR = new Parcelable.Creator<BoxAndroidFileVersion>() {

        @Override
        public BoxAndroidFileVersion createFromParcel(Parcel source) {
            return new BoxAndroidFileVersion(source);
        }

        @Override
        public BoxAndroidFileVersion[] newArray(int size) {
            return new BoxAndroidFileVersion[size];
        }
    };
}
