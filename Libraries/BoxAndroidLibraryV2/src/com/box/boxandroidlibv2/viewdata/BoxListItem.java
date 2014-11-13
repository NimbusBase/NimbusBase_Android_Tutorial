package com.box.boxandroidlibv2.viewdata;

import java.util.concurrent.FutureTask;

import com.box.boxandroidlibv2.dao.BoxAndroidFolder;
import com.box.boxjavalibv2.dao.BoxItem;

/**
 * 
 * This class will contain an item or a task that will be displayed in a list.
 */
public final class BoxListItem {

    public static final int TYPE_BOX_FOLDER_ITEM = 0;
    public static final int TYPE_BOX_FILE_ITEM = 1;
    public static final int TYPE_FUTURE_TASK = 2;

    public static final int TOTAL_FILE_TYPES = 3;

    private BoxItem mBoxItem;
    private FutureTask<?> mTask;
    private int mType;
    private String mIdentifier;

    /**
     * Constructor.
     * 
     * @param boxItem
     *            box item that should be displayed to user.
     */
    public BoxListItem(BoxItem boxItem, final String identifier) {
        mBoxItem = boxItem;
        if (boxItem instanceof BoxAndroidFolder) {
            mType = TYPE_BOX_FOLDER_ITEM;
        }
        else {
            mType = TYPE_BOX_FILE_ITEM;
        }
        setIdentifier(identifier);
    }

    /**
     * Constructor.
     * 
     * @param task
     *            task that should be performed if this item is gotten from the list.
     */
    public BoxListItem(FutureTask<?> task, final String identifier) {
        mTask = task;
        mType = TYPE_FUTURE_TASK;
        setIdentifier(identifier);
    }

    /**
     * Set a future task for the
     * 
     * @param task
     */
    public void setTask(FutureTask<?> task) {
        mTask = task;

    }

    /**
     * 
     * @return the task set for this item.
     */
    public FutureTask<?> getTask() {
        return mTask;

    }

    /**
     * 
     * @return the box item used in construction of this item.
     */
    public BoxItem getBoxItem() {
        return mBoxItem;

    }

    /**
     * 
     * @return the future task used in construction of this item.
     */
    public int getType() {
        return mType;
    }

    /**
     * 
     * @return an identifier for the item (used as a performance enhancement).
     */
    public String getIdentifier() {
        return mIdentifier;
    }

    /**
     * 
     * @param identifier
     *            sets the identifier (used as a performance enhancement).
     */
    private void setIdentifier(final String identifier) {
        mIdentifier = identifier;
    }

}
