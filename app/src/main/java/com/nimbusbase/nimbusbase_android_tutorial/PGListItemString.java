package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;

/**
 * Created by Will on 11/11/14.
 */
public class PGListItemString extends PGListItemAttribute {
    public PGListItemString(Context context, String name, String string) {
        super(context, name, string);

        setSummary(R.string.value_type_string);
    }
}
