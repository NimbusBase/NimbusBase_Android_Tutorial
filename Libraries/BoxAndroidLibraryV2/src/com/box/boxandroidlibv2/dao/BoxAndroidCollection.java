package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxCollection;

public class BoxAndroidCollection extends BoxCollection implements Parcelable {

    public BoxAndroidCollection() {
        super();
    }

    private BoxAndroidCollection(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidCollection(BoxAndroidCollection obj) {
        super(obj);
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidCollection(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidCollection> CREATOR = new Parcelable.Creator<BoxAndroidCollection>() {

        @Override
        public BoxAndroidCollection createFromParcel(Parcel source) {
            return new BoxAndroidCollection(source);
        }

        @Override
        public BoxAndroidCollection[] newArray(int size) {
            return new BoxAndroidCollection[size];
        }
    };
}
