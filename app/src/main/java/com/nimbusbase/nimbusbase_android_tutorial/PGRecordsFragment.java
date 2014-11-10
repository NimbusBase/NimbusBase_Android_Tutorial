package com.nimbusbase.nimbusbase_android_tutorial;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;

import java.util.List;

/**
 * Created by Will on 11/10/14.
 */
public class PGRecordsFragment extends PreferenceFragment {

    private static String
        sTableNameKey = "table_name";

    protected String
            mTableName;
    protected SQLiteOpenHelper
            mSQLiteOpenHelper;
    protected List<MDLUser>
            mRecords;

    public static PGRecordsFragment newInstance(String tableName) {
        final PGRecordsFragment
                fragment = new PGRecordsFragment();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
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

        this.mSQLiteOpenHelper = new MDLDatabaseManager(getActivity());

        addPreferencesFromResource(R.xml.fragment_pg_records);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SQLiteDatabase
                database = mSQLiteOpenHelper.getReadableDatabase();
        final List<MDLUser>
                records = MDLUser.fetchAll(database);
        database.close();

        if (!records.equals(mRecords)) {
            this.mRecords = records;
            final PreferenceScreen
                    preferenceScreen = getPreferenceScreen();
            preferenceScreen.removeAll();
            for (final MDLUser user : records) {
                final PGListItemRecord
                        item = new PGListItemRecord(getActivity(), user);
                preferenceScreen.setOrderingAsAdded(true);
                preferenceScreen.addPreference(item);
            }
        }
   }
}
