package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by Will on 11/10/14.
 */
public class PGFragmentRecord extends PreferenceFragment {

    private static String
            sTableNameKey = "table_name",
            sRecordIDKey = "record_id";

    protected String
            mTableName;
    protected Long
            mRecordID;

    protected SQLiteOpenHelper
            mSQLiteOpenHelper;

    public static PGFragmentRecord newInstance(String tableName, Long recordID) {
        final PGFragmentRecord
                fragment = new PGFragmentRecord();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
        bundle.putLong(sRecordIDKey, recordID);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        final Bundle
                bundle = getArguments();
        this.mTableName = bundle.getString(sTableNameKey);
        this.mRecordID = bundle.getLong(sRecordIDKey);

        this.mSQLiteOpenHelper = new MDLDatabaseManager(getActivity());

        getPreferenceManager().createPreferenceScreen(getActivity());
    }
}
