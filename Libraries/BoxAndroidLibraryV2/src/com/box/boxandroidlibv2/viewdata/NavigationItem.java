package com.box.boxandroidlibv2.viewdata;


/**
 * 
 * This class will contain an item to display in navigation spinner..
 */
public final class NavigationItem {

    private String mName;
    private String mFolderId;

    public NavigationItem(final String name, final String id) {
        mName = name;
        mFolderId = id;
    }

    public String toString() {
        return mName;
    }

    public String getFolderId() {
        return mFolderId;
    }
}
