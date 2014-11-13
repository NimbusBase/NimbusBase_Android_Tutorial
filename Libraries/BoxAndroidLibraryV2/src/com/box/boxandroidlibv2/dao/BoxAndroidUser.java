package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxUser;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data class for user.
 */
public class BoxAndroidUser extends BoxUser implements Parcelable {

    public BoxAndroidUser() {
        super();
    }

    protected BoxAndroidUser(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidUser(BoxAndroidUser obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidUser(Map<String, Object> map) {
        super(map);
    }

    @JsonProperty(FIELD_ENTERPRISE)
    private void setEnterprise(BoxAndroidEnterprise enterprise) {
        put(FIELD_ENTERPRISE, enterprise);
    }

    @Override
    @JsonProperty(FIELD_ENTERPRISE)
    public BoxAndroidEnterprise getEnterprise() {
        return (BoxAndroidEnterprise) getValue(FIELD_ENTERPRISE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidUser> CREATOR = new Parcelable.Creator<BoxAndroidUser>() {

        @Override
        public BoxAndroidUser createFromParcel(Parcel source) {
            return new BoxAndroidUser(source);
        }

        @Override
        public BoxAndroidUser[] newArray(int size) {
            return new BoxAndroidUser[size];
        }
    };
}
