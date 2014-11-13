package com.box.boxandroidlibv2.dao;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.box.boxjavalibv2.dao.BoxFolder;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data class for folder.
 */
public class BoxAndroidFolder extends BoxFolder implements Parcelable {

    public BoxAndroidFolder() {
        super();
    }

    private BoxAndroidFolder(Parcel in) {
        super(new BoxParcel(in));
    }

    /**
     * Copy constructor, this does deep copy for all the fields.
     * 
     * @param obj
     */
    public BoxAndroidFolder(BoxAndroidFolder obj) {
        super(obj);
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
    @JsonProperty(FIELD_ITEM_COLLECTION)
    public BoxAndroidCollection getItemCollection() {
        return (BoxAndroidCollection) getValue(FIELD_ITEM_COLLECTION);
    }

    @JsonProperty(FIELD_ITEM_COLLECTION)
    protected void setItemCollection(BoxAndroidCollection itemCollection) {
        put(FIELD_ITEM_COLLECTION, itemCollection);
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
    @JsonProperty(FIELD_SHARED_LINK)
    public BoxAndroidSharedLink getSharedLink() {
        return (BoxAndroidSharedLink) getValue(FIELD_SHARED_LINK);
    }

    @JsonProperty(FIELD_SHARED_LINK)
    private void setSharedLink(BoxAndroidSharedLink sharedLink) {
        put(FIELD_SHARED_LINK, sharedLink);
    }

    @Override
    @JsonProperty(FIELD_PERMISSIONS)
    public BoxAndroidItemPermissions getPermissions() {
        return (BoxAndroidItemPermissions) getValue(FIELD_PERMISSIONS);
    }

    @JsonProperty(FIELD_PERMISSIONS)
    private void setPermissions(BoxAndroidItemPermissions permissions) {
        put(FIELD_PERMISSIONS, permissions);
    }

    /**
     * Instantiate the object from a map. Each entry in the map reflects to a field.
     * 
     * @param map
     */
    public BoxAndroidFolder(Map<String, Object> map) {
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

    public static final Parcelable.Creator<BoxAndroidFolder> CREATOR = new Parcelable.Creator<BoxAndroidFolder>() {

        @Override
        public BoxAndroidFolder createFromParcel(Parcel source) {
            return new BoxAndroidFolder(source);
        }

        @Override
        public BoxAndroidFolder[] newArray(int size) {
            return new BoxAndroidFolder[size];
        }
    };
}
