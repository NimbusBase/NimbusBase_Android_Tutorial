package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.EditTextPreference;

/**
 * Created by Will on 11/11/14.
 */
public class PGListItemAttribute extends EditTextPreference {
    public PGListItemAttribute(Context context, String name, Object value) {
        super(context);
        setTitle(value == null ? "" : value.toString());
        setDefaultValue(value == null ? null : value.toString());
        setDialogTitle("Input value for attribute '" + name + "'");

        setSelectable(false);
        setEnabled(false);
    }
}
