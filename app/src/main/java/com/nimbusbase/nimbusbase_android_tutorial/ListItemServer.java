package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.SwitchPreference;

/**
 * Created by Will on 11/10/14.
 */
public class ListItemServer extends SwitchPreference {

    public ListItemServer(Context context, String server)  {
        super(context);
        this.setTitle(server);
        this.setSwitchTextOff("Out");
        this.setSwitchTextOn("In");
    }
}
