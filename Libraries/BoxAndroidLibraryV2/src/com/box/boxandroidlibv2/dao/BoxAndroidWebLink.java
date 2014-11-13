package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxWebLink;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Web link.
 */
public class BoxAndroidWebLink extends BoxWebLink implements Parcelable {

    public BoxAndroidWebLink() {
        super();
    }

    private BoxAndroidWebLink(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidWebLink(BoxAndroidWebLink obj) {
        super(obj);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidWebLink(Map<String, Object> map) {
        super(map);
    }

    @Override
    @JsonProperty(FIELD_PARENT)
    public BoxAndroidFolder getParent() {
        return (BoxAndroidFolder) getValue(FIELD_PARENT);
    }

    @JsonProperty(FIELD_PARENT)
    private void setParent(BoxAndroidFolder folder) {
        put(FIELD_PARENT, folder);
    }

    @Override
    @JsonProperty(FIELD_SHARED_LINK)
    public BoxAndroidSharedLink getSharedLink() {
        return (BoxAndroidSharedLink) getValue(FIELD_SHARED_LINK);
    }

    @JsonProperty(FIELD_SHARED_LINK)
    private void setSharedLink(BoxAndroidSharedLink sharedLink) {
        put(FIELD_SHARED_LINK, sharedLink);
    }

    @Override
    @JsonProperty(FIELD_PATH_COLLECTION)
    public BoxAndroidCollection getPathCollection() {
        return (BoxAndroidCollection) getValue(FIELD_PATH_COLLECTION);
    }

    @JsonProperty(FIELD_PATH_COLLECTION)
    private void setPathCollection(BoxAndroidCollection pathCollection) {
        put(FIELD_PATH_COLLECTION, pathCollection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(new BoxParcel(dest), flags);
    }

    public static final Parcelable.Creator<BoxAndroidWebLink> CREATOR = new Parcelable.Creator<BoxAndroidWebLink>() {

        @Override
        public BoxAndroidWebLink createFromParcel(Parcel source) {
            return new BoxAndroidWebLink(source);
        }

        @Override
        public BoxAndroidWebLink[] newArray(int size) {
            return new BoxAndroidWebLink[size];
        }
    };
}
