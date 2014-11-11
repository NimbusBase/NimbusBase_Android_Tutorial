package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Looper;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nimbusbase.nimbusbase.Base;
import com.nimbusbase.nimbusbase.Server;
import com.nimbusbase.nimbusbase.promise.Callback;
import com.nimbusbase.nimbusbase.promise.Promise;
import com.nimbusbase.nimbusbase.promise.Response;

import org.apache.http.auth.AuthState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.logging.Handler;

/**
 * Created by Will on 11/7/14.
 */
public class IndexFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        final Base
            base = null;

        bindEvents(base);
        initiatePreferenceScreen(base, R.xml.fragment_index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ActionBar
                actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.app_name);
   }

    protected void bindEvents(Base base) {
        final Server[]
                servers = base.getServers();
        for (int index = 0; index < servers.length; index ++) {
            final Server
                    server = servers[index];
            final PropertyChangeSupport
                    support = server.propertyChangeSupport;
            final int
                    innerIndex = index;
            support.addPropertyChangeListener(
                    Server.Property.authState,
                    new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent event) {
                            final Server
                                    innerServer = (Server) event.getSource();
                            onServerStateChange(innerServer, innerIndex, (Server.AuthState) event.getNewValue(), innerServer.isInitialized());
                        }
                    }
            );
            support.addPropertyChangeListener(
                    Server.Property.isInitialized,
                    new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent event) {
                            final Server
                                    innerServer = (Server) event.getSource();
                            onServerStateChange(innerServer, innerIndex, innerServer.getAuthState(), (Boolean) event.getNewValue());
                        }
                    }
            );
        }
    }

    protected PreferenceScreen initiatePreferenceScreen(Base base, int preferencesResID) {
        addPreferencesFromResource(preferencesResID);
        final PreferenceScreen
                preferenceScreen = getPreferenceScreen();

        final PreferenceCategory
                serverCate = getServerCategory(preferenceScreen);
        serverCate.setOrderingAsAdded(true);

        for (final Server server : base.getServers()) {
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
            item.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    onServerItemClick((ListItemServer) preference);
                    return true;
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

    protected void onServerItemClick(ListItemServer item) {
        final Server
                server = item.getServer();
        if (server.isSynchronizing()) {
            server.getRunningSync().cancel();
        }
        else if (server.canSynchronize()) {
            startSyncOnServer(server);
        }
    }

    protected void onServerItemStateChange(ListItemServer item, Boolean newValue) {
        final Server
                server = item.getServer();
        final Server.AuthState
                authState = server.getAuthState();
        if (newValue && Server.AuthState.Out == authState) {
            server.authorize(getActivity());
        }
        else if (!newValue && Server.AuthState.In == authState) {
            server.signOut();
        }
    }

    protected void onServerStateChange(Server server, int index, Server.AuthState authState, boolean initialized) {
        final ListItemServer
                item = (ListItemServer) getServerCategory(getPreferenceScreen()).getPreference(index);
        if (Server.AuthState.In == authState) {
            item.setChecked(true);
        }
        else if (Server.AuthState.Out == authState) {
            item.setChecked(false);
        }
    }

    protected void startSyncOnServer(Server server) {
        final Base
                base = null;
        final int
                index = Arrays.asList(base.getServers()).indexOf(server);

        final Promise
                promise = server.synchronize(null);
        promise
                .onProgress(new Callback.ProgressListener() {
                    @Override
                    public void onProgress(double v) {
                        onServerSyncProgress(index, (float) v);
                    }
                })
                .onAlways(new Callback.AlwaysListener() {
                    @Override
                    public void onAlways(Response response) {
                        onServerSyncEnd(index, response);
                    }
                });
    }

    protected void onServerSyncEnd(int index, Response response) {

    }

    protected void onServerSyncProgress(int index, float progress) {

    }

    protected boolean onPlaygroundItemClick(Preference item) {
        final PGFragmentTable
                fragment = PGFragmentTable.newInstance(MDLUser.ENTITY_NAME);
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
