package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxCollaboration;

/**
 * Data class for Collaboration.
 */
public class BoxAndroidCollaboration extends BoxCollaboration implements Parcelable {

    public BoxAndroidCollaboration() {
        super();
    }

    private BoxAndroidCollaboration(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidCollaboration(BoxAndroidCollaboration obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidCollaboration(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidCollaboration> CREATOR = new Parcelable.Creator<BoxAndroidCollaboration>() {

        @Override
        public BoxAndroidCollaboration createFromParcel(Parcel source) {
            return new BoxAndroidCollaboration(source);
        }

        @Override
        public BoxAndroidCollaboration[] newArray(int size) {
            return new BoxAndroidCollaboration[size];
        }
    };
}
