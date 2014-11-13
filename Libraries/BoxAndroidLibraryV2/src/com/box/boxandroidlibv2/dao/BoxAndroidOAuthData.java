package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxOAuthToken;

/**
 * Data used for OAuth.
 */
public class BoxAndroidOAuthData extends BoxOAuthToken implements Parcelable {

    public BoxAndroidOAuthData() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param in
     *            parcel
     * @throws BoxNullObjectException
     *             exception
     */
    private BoxAndroidOAuthData(final Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidOAuthData(BoxOAuthToken obj) {
        super(obj);
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidOAuthData(BoxAndroidOAuthData obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidOAuthData(Map<String, Object> map) {
        super(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidOAuthData> CREATOR = new Parcelable.Creator<BoxAndroidOAuthData>() {

        @Override
        public BoxAndroidOAuthData createFromParcel(final Parcel source) {
            return new BoxAndroidOAuthData(source);
        }

        @Override
        public BoxAndroidOAuthData[] newArray(final int size) {
            return new BoxAndroidOAuthData[size];
        }
    };
}
