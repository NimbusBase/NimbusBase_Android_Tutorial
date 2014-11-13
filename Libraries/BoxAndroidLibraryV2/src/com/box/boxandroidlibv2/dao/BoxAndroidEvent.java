package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxEvent;

/**
 * Data class for event.
 */
public class BoxAndroidEvent extends BoxEvent implements Parcelable {

    public BoxAndroidEvent() {
        super();
    }

    private BoxAndroidEvent(final Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidEvent(BoxAndroidEvent obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidEvent(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidEvent> CREATOR = new Parcelable.Creator<BoxAndroidEvent>() {

        @Override
        public BoxAndroidEvent createFromParcel(final Parcel source) {
            return new BoxAndroidEvent(source);
        }

        @Override
        public BoxAndroidEvent[] newArray(final int size) {
            return new BoxAndroidEvent[size];
        }
    };
}
