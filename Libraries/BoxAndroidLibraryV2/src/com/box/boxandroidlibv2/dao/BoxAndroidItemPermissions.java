package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxItemPermissions;

public class BoxAndroidItemPermissions extends BoxItemPermissions implements Parcelable {

    public BoxAndroidItemPermissions() {
        super();
    }

    private BoxAndroidItemPermissions(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidItemPermissions(BoxAndroidItemPermissions obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidItemPermissions(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidItemPermissions> CREATOR = new Parcelable.Creator<BoxAndroidItemPermissions>() {

        @Override
        public BoxAndroidItemPermissions createFromParcel(Parcel source) {
            return new BoxAndroidItemPermissions(source);
        }

        @Override
        public BoxAndroidItemPermissions[] newArray(int size) {
            return new BoxAndroidItemPermissions[size];
        }
    };

}
