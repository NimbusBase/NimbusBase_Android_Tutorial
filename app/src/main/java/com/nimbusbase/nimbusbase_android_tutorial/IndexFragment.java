package com.nimbusbase.nimbusbase_android_tutorial;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Will on 11/7/14.
 */
public class IndexFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_index);
        final PreferenceScreen
                preferenceScreen = getPreferenceScreen();
        final PreferenceCategory
                serverCate = (PreferenceCategory) preferenceScreen.findPreference(getString(R.string.group_servers));
        serverCate.setOrderingAsAdded(true);
        for (final String server : new String[]{"Dropbox", "Box"}) {
            final ListItemServer
                    item = new ListItemServer(getActivity(), server);
            serverCate.addPreference(item);
            item.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    onServerItemStateChange((ListItemServer) preference, (Boolean) newValue);
                    return false;
                }
            });
        }
    }

    protected void onServerItemStateChange(ListItemServer item, Boolean newValue) {
        final int
                index = item.getOrder();
        final String
                server = null;
        if (newValue) {

        }
        else {

        }
    }

    protected void onServerStateChange(String server, int index, int newValue) {
        final ListItemServer
                item = (ListItemServer) getServerCategory().getPreference(index);
        if (true) {
            item.setChecked(true);
        }
        else if (true) {
            item.setChecked(false);
        }
    }

    private PreferenceCategory getServerCategory() {
        return (PreferenceCategory) getPreferenceScreen().findPreference(getString(R.string.group_servers));
    }
}
