package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxEmailAlias;

/**
 * Data class for Email alias.
 */
public class BoxAndroidEmailAlias extends BoxEmailAlias implements Parcelable {

    public BoxAndroidEmailAlias() {
        super();
    }

    private BoxAndroidEmailAlias(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidEmailAlias(BoxAndroidEmailAlias obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidEmailAlias(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidEmailAlias> CREATOR = new Parcelable.Creator<BoxAndroidEmailAlias>() {

        @Override
        public BoxAndroidEmailAlias createFromParcel(Parcel source) {
            return new BoxAndroidEmailAlias(source);
        }

        @Override
        public BoxAndroidEmailAlias[] newArray(int size) {
            return new BoxAndroidEmailAlias[size];
        }
    };

}
