package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxGroupMembership;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BoxAndroidGroupMembership extends BoxGroupMembership implements Parcelable {

    public BoxAndroidGroupMembership() {
        super();
    }

    private BoxAndroidGroupMembership(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidGroupMembership(BoxAndroidGroupMembership obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidGroupMembership(Map<String, Object> map) {
        super(map);
    }

    @Override
    @JsonProperty(FIELD_USER)
    public BoxAndroidUser getUser() {
        return (BoxAndroidUser) getValue(FIELD_USER);
    }

    /**
     * Setter. This is only used by {@see <a href="http://jackson.codehaus.org">Jackson JSON processer</a>}.
     * 
     * @param createdBy
     *            the created_by to set
     */
    @JsonProperty(FIELD_USER)
    private void setUser(BoxAndroidUser user) {
        put(FIELD_USER, user);
    }

    @Override
    @JsonProperty(FIELD_GROUP)
    public BoxAndroidGroup getGroup() {
        return (BoxAndroidGroup) getValue(FIELD_GROUP);
    }

    /**
     * Setter. This is only used by {@see <a href="http://jackson.codehaus.org">Jackson JSON processer</a>}.
     * 
     * @param createdBy
     *            the created_by to set
     */
    @JsonProperty(FIELD_GROUP)
    private void setGroup(BoxAndroidGroup group) {
        put(FIELD_GROUP, group);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidGroupMembership> CREATOR = new Parcelable.Creator<BoxAndroidGroupMembership>() {

        @Override
        public BoxAndroidGroupMembership createFromParcel(Parcel source) {
            return new BoxAndroidGroupMembership(source);
        }

        @Override
        public BoxAndroidGroupMembership[] newArray(int size) {
            return new BoxAndroidGroupMembership[size];
        }
    };

}
