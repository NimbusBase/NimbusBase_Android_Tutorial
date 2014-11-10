package com.nimbusbase.nimbusbase_android_tutorial;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Will on 11/10/14.
 */
public class PGRecordsFragment extends PreferenceFragment {

    private static String
        sTableNameKey = "table_name";

    public static PGRecordsFragment newInstance(String tableName) {
        final PGRecordsFragment
                fragment = new PGRecordsFragment();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
        fragment.setArguments(bundle);

        return fragment;
    }


}
