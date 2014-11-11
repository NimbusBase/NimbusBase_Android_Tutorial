package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.EditTextPreference;

/**
 * Created by Will on 11/11/14.
 */
public class PGListItemAttribute extends EditTextPreference {

    protected final String
        mAttributeName;

    public PGListItemAttribute(Context context, String name, Object value) {
        super(context);
        this.mAttributeName = name;

        final String
                displayValue = value == null ? "" : value.toString();
        setText(displayValue);
        setTitle(displayValue);

        setDefaultValue(value == null ? null : value.toString());
        setDialogTitle("Input value for attribute '" + name + "'");

        setSelectable(false);
        setEnabled(false);
    }

    public String getAttributeName() {
        return mAttributeName;
    }
}
