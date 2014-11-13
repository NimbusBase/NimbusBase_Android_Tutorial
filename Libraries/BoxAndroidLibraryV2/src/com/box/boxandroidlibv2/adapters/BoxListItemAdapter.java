package com.box.boxandroidlibv2.adapters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.box.boxandroidlibv2.R;
import com.box.boxandroidlibv2.dao.BoxAndroidCollection;
import com.box.boxandroidlibv2.dao.BoxAndroidFile;
import com.box.boxandroidlibv2.dao.BoxAndroidFolder;
import com.box.boxandroidlibv2.manager.ThumbnailManager;
import com.box.boxandroidlibv2.viewdata.BoxListItem;
import com.box.boxjavalibv2.dao.BoxItem;
import com.box.boxjavalibv2.dao.BoxTypedObject;

/**
 * An adapter that displays files and folders for a user to select.
 * 
 * @author dcung
 * 
 */
public class BoxListItemAdapter extends ArrayAdapter<BoxListItem> {

    /** Used to quickly find the position of where an item was inserted within this adapter. */
    private final HashMap<String, Integer> positionInsertedMap = new HashMap<String, Integer>();
    /** Map of positions to ViewHolders. */
    private final Map<Integer, ViewHolder> viewHolderMap = new ConcurrentHashMap<Integer, ViewHolder>();
    private ThumbnailManager mThumbnailManager;

    /**
     * Construct a new adapter.
     * 
     * @param context
     *            current context.
     */
    public BoxListItemAdapter(final Activity context, ThumbnailManager manager) {
        super(context, 0);
        mThumbnailManager = manager;
    }

    @Override
    public synchronized View getView(final int position, final View convertView, final ViewGroup parent) {
        viewHolderMap.remove(position);
        final View row;
        final ViewHolder holder;
        final BoxListItem listItem = this.getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            if (this.getItemViewType(position) == BoxListItem.TYPE_BOX_FOLDER_ITEM) {
                row = LayoutInflater.from(getContext()).inflate(R.layout.boxandroidlibv2_box_folder_list_item, parent, false);
            }
            else {
                row = LayoutInflater.from(getContext()).inflate(R.layout.boxandroidlibv2_box_list_item, parent, false);
            }
            row.setTag(holder);
            holder.setIconView((ImageView) row.findViewById(R.id.icon));
            holder.setNameView((TextView) row.findViewById(R.id.name));
            holder.setDescriptionView((TextView) row.findViewById(R.id.metaline_description));
            holder.mSpinner = (ProgressBar) row.findViewById(R.id.spinner);
        }
        else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.getIconView();
        holder.setBoxItem(listItem);
        update(holder, listItem);
        viewHolderMap.put(position, holder);
        return row;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return BoxListItem.TOTAL_FILE_TYPES;
    }

    /**
     * Sets views with data from given box item.
     * 
     * @param holder
     *            The holder containing views to update.
     * @param listItem
     *            the box to display.
     */
    protected void update(final ViewHolder holder, final BoxListItem listItem) {
        if (listItem.getType() == BoxListItem.TYPE_FUTURE_TASK) {
            if (!listItem.getTask().isDone()) {
                holder.mSpinner.setVisibility(View.VISIBLE);
                holder.getIconView().setVisibility(View.GONE);
                holder.getNameView().setText(getContext().getResources().getString(R.string.boxandroidlibv2_Please_wait));
            }
            return;
        }
        else if (listItem.getType() == BoxListItem.TYPE_BOX_FILE_ITEM || listItem.getType() == BoxListItem.TYPE_BOX_FOLDER_ITEM) {
            holder.mSpinner.setVisibility(View.GONE);
            holder.getIconView().setVisibility(View.VISIBLE);
            BoxItem boxItem = listItem.getBoxItem();
            // sets the drawable for the given box item.
            mThumbnailManager.setThumbnailIntoView(holder.getIconView(), boxItem);
            holder.getNameView().setText(boxItem.getName());
            if (boxItem.getSize() != null && holder.getDescriptionView() != null) {
                holder.getDescriptionView().setText(localFileSizeToDisplay(boxItem.getSize()));
            }
        }

    }

    /**
     * Java version of routine to turn a long into a short user readable string.
     * 
     * This routine is used if the JNI native C version is not available.
     * 
     * @param numSize
     *            the number of bytes in the file.
     * @return String Short human readable String e.g. 2.5 MB
     */
    private static String localFileSizeToDisplay(final double numSize) {
        final int constKB = 1024;
        final int constMB = constKB * constKB;
        final int constGB = constMB * constKB;
        final double floatKB = 1024.0f;
        final double floatMB = floatKB * floatKB;
        final double floatGB = floatMB * floatKB;
        final String BYTES = "B";
        String textSize = "0 bytes";
        String strSize = Double.toString(numSize);
        double size;

        if (numSize < constKB) {
            textSize = strSize + " " + BYTES;
        }
        else if ((numSize >= constKB) && (numSize < constMB)) {
            size = numSize / floatKB;
            textSize = String.format(Locale.ENGLISH, "%4.1f KB", size);
        }
        else if ((numSize >= constMB) && (numSize < constGB)) {
            size = numSize / floatMB;
            textSize = String.format(Locale.ENGLISH, "%4.1f MB", size);
        }
        else if (numSize >= constGB) {
            size = numSize / floatGB;
            textSize = String.format(Locale.ENGLISH, "%4.1f GB", size);
        }
        return textSize;
    }

    public void set(BoxAndroidCollection collection) {
        this.clear();
        this.add(collection);

    }

    public void add(final BoxAndroidCollection collection) {
        setNotifyOnChange(false);
        for (BoxTypedObject o : collection.getEntries()) {
            if (o instanceof BoxAndroidFile) {
                add(new BoxListItem((BoxAndroidFile) o, o.getId()));
            }
            else if (o instanceof BoxAndroidFolder) {
                add(new BoxListItem((BoxAndroidFolder) o, o.getId()));
            }
        }
        setNotifyOnChange(true);

    }

    @Override
    public void addAll(BoxListItem... items) {
        for (BoxListItem item : items) {
            add(item);
        }
    }

    @Override
    public void addAll(Collection<? extends BoxListItem> collection) {
        for (BoxListItem item : collection) {
            add(item);
        }
    }

    @Override
    public synchronized void add(final BoxListItem listItem) {
        positionInsertedMap.put(listItem.getIdentifier(), positionInsertedMap.size());
        super.add(listItem);
    }

    @Override
    public synchronized void remove(final BoxListItem listItem) {
        Integer removalPos = positionInsertedMap.remove(listItem.getIdentifier());
        if (removalPos != null) {
            if (removalPos < positionInsertedMap.size()) {
                for (String key : positionInsertedMap.keySet()) {
                    Integer potentiallyOutdatedPos = positionInsertedMap.get(key);
                    if (potentiallyOutdatedPos > removalPos) {
                        positionInsertedMap.put(key, potentiallyOutdatedPos - 1);
                    }
                }
            }
        }
        super.remove(listItem);
    }

    /**
     * Update a single item based on given identifier.
     * 
     * @param identifier
     *            identifier of item to update.
     */
    public synchronized void update(final String identifier) {
        Integer positionInserted = positionInsertedMap.get(identifier);
        if (positionInserted != null) {
            ViewHolder holder = viewHolderMap.get(positionInserted);
            if (holder != null && holder.mBoxListItem != null) {
                this.update(holder, holder.mBoxListItem);
            }

        }

    }

    /**
     * Remove an item based on a given identifier
     * 
     * @param identifier
     *            remove an item from this adapter with given identifier.
     */
    public void remove(final String identifier) {
        Integer positionInserted = positionInsertedMap.get(identifier);
        if (positionInserted != null) {
            remove(this.getItem(positionInserted));
        }

    }

    @Override
    public void clear() {
        positionInsertedMap.clear();
        super.clear();
    }

    /**
     * A ViewHolder to keep references to children views.
     */
    public static class ViewHolder {

        /** The box list item to be displayed to the user. */
        private BoxListItem mBoxListItem;
        private ImageView icon;
        private TextView name;
        private TextView description;
        public ProgressBar mSpinner;

        /** Constructor. */
        public ViewHolder() {

        }

        /**
         * @return the BoxListItem
         */
        public BoxListItem getBoxListItem() {
            return mBoxListItem;
        }

        /**
         * @param boxListItem
         *            the BoxListItem to set
         */
        public void setBoxItem(final BoxListItem boxListItem) {
            this.mBoxListItem = boxListItem;
        }

        /**
         * @return the icon
         */
        public ImageView getIconView() {
            return icon;
        }

        /**
         * @param icon
         *            the icon to set
         */
        public void setIconView(final ImageView icon) {
            this.icon = icon;
        }

        /**
         * @return the name
         */
        public TextView getNameView() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setNameView(final TextView name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public TextView getDescriptionView() {
            return description;
        }

        /**
         * @param description
         *            the description to set
         */
        public void setDescriptionView(final TextView description) {
            this.description = description;
        }

    }

}
