package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxGroup;

/**
 * Data class for group.
 */
public class BoxAndroidGroup extends BoxGroup implements Parcelable {

    public BoxAndroidGroup() {
        super();
    }

    private BoxAndroidGroup(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidGroup(BoxAndroidGroup obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidGroup(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidGroup> CREATOR = new Parcelable.Creator<BoxAndroidGroup>() {

        @Override
        public BoxAndroidGroup createFromParcel(Parcel source) {
            return new BoxAndroidGroup(source);
        }

        @Override
        public BoxAndroidGroup[] newArray(int size) {
            return new BoxAndroidGroup[size];
        }
    };
}
