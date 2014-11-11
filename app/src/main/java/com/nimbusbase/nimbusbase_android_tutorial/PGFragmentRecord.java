package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;

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

    protected PGRecordBasic
            mRecord;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ActionBar
                actionBar = getActivity().getActionBar();
        if (actionBar != null && mRecord != null)
                actionBar.setTitle(mRecord.getTitle());

        final SQLiteDatabase
                database = mSQLiteOpenHelper.getReadableDatabase();
        try {
            final Cursor
                    cursor = database.query(mTableName, null, "_ROWID_ == ?", new String[]{mRecordID.toString()}, null, null, null);
            final PGRecordBasic
                    record = new PGRecordBasic(cursor);
            if (!record.equals(mRecord)) {
                this.mRecord = record;
                reload(record);
            }
        }
        catch (Exception e) {
        }
        finally {
            database.close();
        }
    }

    protected void reload(PGRecordBasic record) {

    }
}
