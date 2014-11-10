package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.FragmentTransaction;
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
        setRetainInstance(true);

        initiatePreferenceScreen(R.xml.fragment_index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
   }

    protected PreferenceScreen initiatePreferenceScreen(int preferencesResID) {
        addPreferencesFromResource(preferencesResID);
        final PreferenceScreen
                preferenceScreen = getPreferenceScreen();

        final PreferenceCategory
                serverCate = getServerCategory(preferenceScreen);
        serverCate.setOrderingAsAdded(true);
        for (final String server : new String[]{"Dropbox", "Box"}) {
            final ListItemServer
                    item = new ListItemServer(getActivity(), server);
            item.setSummary(R.string.auth_state_out);
            item.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    onServerItemStateChange((ListItemServer) preference, (Boolean) newValue);
                    return false;
                }
            });
            serverCate.addPreference(item);
        }

        final PreferenceCategory
                databaseCate = getDatabaseCategory(preferenceScreen);
        final PreferenceScreen
                playgroundItem = (PreferenceScreen) databaseCate.findPreference(getString(R.string.item_playground));
        playgroundItem.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return onPlaygroundItemClick(preference);
            }
        });

        return preferenceScreen;
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

    protected void onServerStateChange(String server, int index, int authState, boolean initialized) {
        final ListItemServer
                item = (ListItemServer) getServerCategory(getPreferenceScreen()).getPreference(index);
        if (true) {
            item.setChecked(true);
        }
        else if (true) {
            item.setChecked(false);
        }
    }

    protected boolean onPlaygroundItemClick(Preference item) {
        final PGRecordsFragment
                fragment = PGRecordsFragment.newInstance(MDLUser.ENTITY_NAME);
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    private PreferenceCategory getServerCategory(PreferenceScreen preferenceScreen) {
        return (PreferenceCategory) preferenceScreen.findPreference(getString(R.string.group_servers));
    }

    private PreferenceCategory getDatabaseCategory(PreferenceScreen preferenceScreen) {
        return (PreferenceCategory) preferenceScreen.findPreference(getString(R.string.group_database));
    }
}
