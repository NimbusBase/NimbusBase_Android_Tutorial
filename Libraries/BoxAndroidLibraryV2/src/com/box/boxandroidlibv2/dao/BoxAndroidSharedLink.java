package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxSharedLink;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data class for shared link.
 */
public class BoxAndroidSharedLink extends BoxSharedLink implements Parcelable {

    public BoxAndroidSharedLink() {
        super();
    }

    private BoxAndroidSharedLink(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidSharedLink(BoxAndroidSharedLink obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidSharedLink(Map<String, Object> map) {
        super(map);
    }

    @Override
    @JsonProperty(FIELD_PERMISSIONS)
    public BoxAndroidSharedLinkPermissions getPermissions() {
        return (BoxAndroidSharedLinkPermissions) getValue(FIELD_PERMISSIONS);
    }

    @JsonProperty(FIELD_PERMISSIONS)
    private void setPermissions(final BoxAndroidSharedLinkPermissions permissionsEntity) {
        put(FIELD_PERMISSIONS, permissionsEntity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidSharedLink> CREATOR = new Parcelable.Creator<BoxAndroidSharedLink>() {

        @Override
        public BoxAndroidSharedLink createFromParcel(Parcel source) {
            return new BoxAndroidSharedLink(source);
        }

        @Override
        public BoxAndroidSharedLink[] newArray(int size) {
            return new BoxAndroidSharedLink[size];
        }
    };
}
