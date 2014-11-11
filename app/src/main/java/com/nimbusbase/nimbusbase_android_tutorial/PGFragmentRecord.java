package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 11/10/14.
 */
public class PGFragmentRecord extends PreferenceFragment {

    private static String
            sTableNameKey = "table_name",
            sRecordIDKey = "record_id",
            sAttrTypesByNameKey = "attr_types_by_name";

    protected String
            mTableName;
    protected Long
            mRecordID;
    protected Map<String, Integer>
            mAttrTypesByName;

    protected PGRecordBasic
            mRecord;

    protected SQLiteOpenHelper
            mSQLiteOpenHelper;

    public static PGFragmentRecord newInstance(String tableName, Long recordID, HashMap<String, Integer> attrTypesByName) {
        final PGFragmentRecord
                fragment = new PGFragmentRecord();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
        bundle.putLong(sRecordIDKey, recordID);
        bundle.putSerializable(sAttrTypesByNameKey, attrTypesByName);
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
        this.mAttrTypesByName = (Map<String, Integer>) bundle.getSerializable(sAttrTypesByNameKey);

        this.mSQLiteOpenHelper = new MDLDatabaseManager(getActivity());

        final PreferenceScreen
                preferenceScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        setPreferenceScreen(preferenceScreen);
        preferenceScreen.setOrderingAsAdded(true);
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
            cursor.moveToFirst();
            final PGRecordBasic
                    record = new PGRecordBasic(cursor);
            if (!record.equals(mRecord)) {
                this.mRecord = record;
                reload(record);

                if (actionBar != null)
                    actionBar.setTitle(record.getTitle());
            }
        }
        catch (SQLiteException e) {
        }
        finally {
            database.close();
        }
    }

    protected void reload(PGRecordBasic record) {
        final PreferenceScreen
                preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        final Activity
                context = getActivity();

        final Map<String, Object>
                valuesByAttrName = record.getValuesByAttrName();
        final Map<String, Integer>
                typesByAttrName = mAttrTypesByName;
        final String[]
                attrNames = typesByAttrName.keySet().toArray(new String[typesByAttrName.size()]);
        for (final String attrName : attrNames) {
            final PreferenceCategory
                    category = new PreferenceCategory(context);
            category.setTitle(attrName);
            preferenceScreen.addPreference(category);

            final Object
                    value = valuesByAttrName.get(attrName);
            final int
                    type = typesByAttrName.get(attrName);
            PGListItemAttribute attrItem = null;
            switch (type) {
                case Cursor.FIELD_TYPE_STRING:
                    attrItem = new PGListIntegerString(context, attrName, (String) value);
                    break;
                case Cursor.FIELD_TYPE_BLOB:    // rare case: blob values in non blob column
                    attrItem = new PGListItemBlob(context, attrName, (byte[]) value);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    attrItem = new PGListItemInteger(context, attrName, (Integer) value);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    attrItem = new PGListItemFloat(context, attrName, (Float) value);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                default:
                    attrItem = null;
                    break;
            }

            category.addPreference(attrItem);
        }
    }
}
