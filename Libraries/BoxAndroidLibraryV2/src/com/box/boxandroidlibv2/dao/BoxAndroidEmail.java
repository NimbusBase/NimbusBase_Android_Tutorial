package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxEmail;

/**
 * Data class for Email.
 */
public class BoxAndroidEmail extends BoxEmail implements Parcelable {

    public BoxAndroidEmail() {
        super();
    }

    private BoxAndroidEmail(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidEmail(BoxAndroidEmail obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidEmail(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidEmail> CREATOR = new Parcelable.Creator<BoxAndroidEmail>() {

        @Override
        public BoxAndroidEmail createFromParcel(Parcel source) {
            return new BoxAndroidEmail(source);
        }

        @Override
        public BoxAndroidEmail[] newArray(int size) {
            return new BoxAndroidEmail[size];
        }
    };
}
