package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.Preference;

/**
 * Created by Will on 11/10/14.
 */
public class PGListItemRecord extends Preference {
    public PGListItemRecord(Context context, PGRecord record) {
        super(context);
        setTitle(record.getTitle());
    }
}
