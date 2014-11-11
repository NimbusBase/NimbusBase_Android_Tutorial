package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.text.InputType;

/**
 * Created by Will on 11/11/14.
 */
public class PGListItemInteger extends PGListItemAttribute {
    public PGListItemInteger(Context context, String name, Long integer) {
        super(context, name, integer);
        getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        setSummary(R.string.value_type_integer);
    }
}
