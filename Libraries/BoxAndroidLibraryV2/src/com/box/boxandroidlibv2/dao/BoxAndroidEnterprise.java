package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxEnterprise;

public class BoxAndroidEnterprise extends BoxEnterprise implements Parcelable {

    public BoxAndroidEnterprise() {
        super();
    }

    private BoxAndroidEnterprise(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidEnterprise(Map<String, Object> map) {
        super(map);
    }

    public BoxAndroidEnterprise(BoxEnterprise obj) {
        super(obj);
    }

    public BoxAndroidEnterprise(BoxAndroidEnterprise obj) {
        super(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidEnterprise> CREATOR = new Parcelable.Creator<BoxAndroidEnterprise>() {

        @Override
        public BoxAndroidEnterprise createFromParcel(Parcel source) {
            return new BoxAndroidEnterprise(source);
        }

        @Override
        public BoxAndroidEnterprise[] newArray(int size) {
            return new BoxAndroidEnterprise[size];
        }
    };
}
