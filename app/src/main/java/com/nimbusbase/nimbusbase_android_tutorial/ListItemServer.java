package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.SwitchPreference;

import com.nimbusbase.nimbusbase.Server;

/**
 * Created by Will on 11/10/14.
 */
public class ListItemServer extends SwitchPreference {

    protected final Server
        mServer;

    public ListItemServer(Context context, Server server)  {
        super(context);
        this.mServer = server;
        setTitle(server.getCloud());
        setSwitchTextOff("Out");
        setSwitchTextOn("In");
    }

    public Server getServer() {
        return mServer;
    }

    @Override
    protected void onClick() {
    }
}
