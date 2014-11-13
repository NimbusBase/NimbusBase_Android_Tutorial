package com.box.boxandroidlibv2.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.box.boxandroidlibv2.R;
import com.box.boxandroidlibv2.viewdata.NavigationItem;

/**
 * Adapter for navigation items.
 * 
 */
public class NavigationListAdapter extends ArrayAdapter<NavigationItem> {

    /** Layout inflater. */
    private final LayoutInflater mInflater;

    /**
     * Constructor based on array of NavigationTreeItems.
     * 
     * @param activity
     *            current activity.
     * @param navigationItems
     *            List of NavigationTreeItem.
     */
    public NavigationListAdapter(final Activity activity, final ArrayList<NavigationItem> navigationItems) {
        super(activity, 0);
        mInflater = LayoutInflater.from(activity);
        setNavigationList(navigationItems);
    }

    /**
     * Set the list of NavigationTreeItems.
     * 
     * @param navigationItems
     *            List of NavigationTreeItem.
     */
    public void setNavigationList(final ArrayList<NavigationItem> navigationItems) {
        clear();
        setNotifyOnChange(false);
        for (NavigationItem treeItem : navigationItems) {
            this.add(treeItem);
        }
        setNotifyOnChange(true);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(final int position) {
        return true;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View v = mInflater.inflate(R.layout.boxandroidlibv2_navigation_item_folder, parent, false);
        if (v == null) {
            return null;
        }

        TextView tv = (TextView) v.findViewById(R.id.textView_navigationItem);
        NavigationItem item = getItem(position);
        tv.setText(item.toString());
        return v;
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {

        View v = mInflater.inflate(R.layout.boxandroidlibv2_navigation_dropdown_item_folder, parent, false);
        if (v == null) {
            return null;
        }

        TextView tv = (TextView) v.findViewById(R.id.textView_navigationItem);
        NavigationItem item = getItem(position);
        tv.setText(item.toString());
        return v;
    }
}
