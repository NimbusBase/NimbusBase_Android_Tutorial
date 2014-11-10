package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 11/7/14.
 */
public class IndexFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView
                listView = getListView();



        final IndexListAdapter
                adapter =  new IndexListAdapter(getActivity(), new String[]{"Dropbox", "Box"});
        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    //region Event

    protected void onServerItemViewClick(View view, int index) {

    }

    protected void onPlaygroundItemViewClick(View view, int index) {

    }

    private class IndexListAdapter extends ArrayAdapter<UIListViewItem> {
        final static int
                ITEM_TYPE_SEPARATOR = 0,
                ITEM_TYPE_SERVER = 1,
                ITEM_TYPE_PLAYGROUND = 2;
        final static String
                TITLE_SERVERS = "Servers",
                TITLE_LOCAL_DATA = "Local Data",
                TITLE_PLAYGROUND = "Playground";

        int
                mRangeServerMin, mRangeServerMax,
                mRangePlaygroundMin, mRangePlaygroundMax;


        IndexListAdapter(Context context, String[] servers) {
            super(context, android.R.layout.simple_list_item_1);

            mRangeServerMin = 1;
            mRangeServerMax = mRangeServerMin += servers.length;
            mRangePlaygroundMin = mRangeServerMax + 1;
            mRangePlaygroundMax = mRangePlaygroundMin + 1;

            final List<UIListViewItem>
                    items = new ArrayList<UIListViewItem>();

            items.add(new UIListViewSeparatorItem(TITLE_SERVERS));

            for (final String name : servers) {
                final UIListViewServerItem
                        item = new UIListViewServerItem(name);
                items.add(item);
            }

            items.add(new UIListViewSeparatorItem(TITLE_LOCAL_DATA));

            items.add(new UIListViewPlaygroundItem(TITLE_PLAYGROUND));

            addAll(items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View
                    itemView = convertView != null ? convertView :
                    View.inflate(getActivity(), R.layout.list_item_normal, parent);



            return itemView;
        }

        @Override
        public int getItemViewType(int position) {
            if (mRangeServerMin <= position && mRangeServerMax > position)
                return ITEM_TYPE_SERVER;
            else if (mRangePlaygroundMin <= position && mRangePlaygroundMax > position)
                return ITEM_TYPE_PLAYGROUND;
            else
                return ITEM_TYPE_SEPARATOR;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        private void bindServerData(UIListViewServerItem item, View view) {
            if (item == null) return;
            if (view == null) return;

            final TextView
                    titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(item.name);
        }

        private void bindHeaderData(UIListViewSeparatorItem item, View view) {
            if (item == null) return;
            if (view == null) return;

            final TextView
                    titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(item.title);
        }
    }
}
