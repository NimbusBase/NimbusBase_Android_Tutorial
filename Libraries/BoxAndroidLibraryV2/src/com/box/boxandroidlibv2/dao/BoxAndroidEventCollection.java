package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxEventCollection;

public class BoxAndroidEventCollection extends BoxEventCollection implements Parcelable {

    public BoxAndroidEventCollection() {
        super();
    }

    public BoxAndroidEventCollection(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidEventCollection(BoxAndroidEventCollection obj) {
        super(obj);
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidEventCollection(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidEventCollection> CREATOR = new Parcelable.Creator<BoxAndroidEventCollection>() {

        @Override
        public BoxAndroidEventCollection createFromParcel(Parcel source) {
            return new BoxAndroidEventCollection(source);
        }

        @Override
        public BoxAndroidEventCollection[] newArray(int size) {
            return new BoxAndroidEventCollection[size];
        }
    };
}
