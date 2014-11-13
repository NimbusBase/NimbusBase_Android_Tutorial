package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxSharedLinkPermissions;

public class BoxAndroidSharedLinkPermissions extends BoxSharedLinkPermissions implements Parcelable {

    public BoxAndroidSharedLinkPermissions() {
        super();
    }

    public BoxAndroidSharedLinkPermissions(Map<String, Object> map) {
        super(map);
    }

    private BoxAndroidSharedLinkPermissions(Parcel in) {
        super(new BoxParcel(in));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidSharedLinkPermissions> CREATOR = new Parcelable.Creator<BoxAndroidSharedLinkPermissions>() {

        @Override
        public BoxAndroidSharedLinkPermissions createFromParcel(Parcel source) {
            return new BoxAndroidSharedLinkPermissions(source);
        }

        @Override
        public BoxAndroidSharedLinkPermissions[] newArray(int size) {
            return new BoxAndroidSharedLinkPermissions[size];
        }
    };
}
