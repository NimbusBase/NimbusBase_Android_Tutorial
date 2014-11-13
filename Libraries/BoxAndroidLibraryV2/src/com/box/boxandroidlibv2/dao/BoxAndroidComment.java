package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxComment;

/**
 * Data class for Comment.
 */
public class BoxAndroidComment extends BoxComment implements Parcelable {

    public BoxAndroidComment() {
        super();
    }

    private BoxAndroidComment(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidComment(BoxAndroidComment obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidComment(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidComment> CREATOR = new Parcelable.Creator<BoxAndroidComment>() {

        @Override
        public BoxAndroidComment createFromParcel(Parcel source) {
            return new BoxAndroidComment(source);
        }

        @Override
        public BoxAndroidComment[] newArray(int size) {
            return new BoxAndroidComment[size];
        }
    };
}
