package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    protected boolean
            mEditing;

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
        setHasOptionsMenu(true);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pg_menu_record, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_edit == item.getItemId()) {
            final boolean
                    targetValue = !mEditing,
                    success = onEditingStateChanged(targetValue);
            if (success) {
                this.mEditing = targetValue;
                item.setTitle(targetValue ? R.string.action_update : R.string.action_edit);
            }
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
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
                    attrItem = new PGListItemString(context, attrName, (String) value);
                    break;
                case Cursor.FIELD_TYPE_BLOB:    // rare case: blob values in non blob column
                    attrItem = new PGListItemBlob(context, attrName, (byte[]) value);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    attrItem = new PGListItemInteger(context, attrName, (Long) value);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    attrItem = new PGListItemFloat(context, attrName, (Float) value);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                default:
                    attrItem = null;
                    break;
            }

            if (attrItem != null) {
                attrItem.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        final PGListItemAttribute
                                attrItem = (PGListItemAttribute) preference;
                        final boolean
                                valid = validateValueOfAttributeName(attrItem.getAttributeName(), newValue);
                        if (valid)
                            preference.setTitle(newValue.toString());
                        return valid;
                    }
                });
                category.addPreference(attrItem);
            }
        }
    }

    protected boolean onEditingStateChanged(boolean editing) {
        if (editing) {
            final List<PGListItemAttribute>
                    attrItems = getAllAttributeItems();
            for (final PGListItemAttribute item : attrItems) {
                item.setEnabled(true);
                item.setSelectable(true);
            }

            return true;
        }
        else {
            final List<PGListItemAttribute>
                    attrItems = getAllAttributeItems();

            final ContentValues
                    contentValues = contentValuesFromAttributeItems(attrItems);
            if (!updateRecord(contentValues))
                return false;

            for (final PGListItemAttribute item : getAllAttributeItems()) {
                item.setEnabled(false);
                item.setSelectable(false);
            }

            return true;
        }
    }

    protected List<PGListItemAttribute> getAllAttributeItems() {
        final PreferenceScreen
                root = getPreferenceScreen();
        List<PGListItemAttribute>
                items = new ArrayList<PGListItemAttribute>(mAttrTypesByName.size());
        for (int index = 0; index < root.getPreferenceCount(); index++) {
            final PreferenceCategory
                    category = (PreferenceCategory) root.getPreference(index);
            final PGListItemAttribute
                    item = (PGListItemAttribute) category.getPreference(0);
            items.add(item);
        }

        return items;
    }

    protected ContentValues contentValuesFromAttributeItems(List<PGListItemAttribute> attributeItems) {
        final ContentValues
                contentValues = new ContentValues(attributeItems.size());
        for (final PGListItemAttribute item : getAllAttributeItems()) {
            final String
                    attrName = item.getAttributeName();
            final String
                    value = item.getText();
            final int
                    type = mAttrTypesByName.get(attrName);

            try {
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        contentValues.put(attrName, value);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        contentValues.put(attrName, Integer.valueOf(value));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        contentValues.put(attrName, Float.valueOf(value));
                        break;
                }
            }
            catch (Exception ignored) {
            }
        }

        return contentValues;
    }

    protected boolean updateRecord(ContentValues contentValues) {
        final SQLiteDatabase
                database = mSQLiteOpenHelper.getWritableDatabase();

        boolean success =
                database.update(mTableName, contentValues, "_ROWID_ == ?", new String[]{mRecordID.toString(),}) > 0;

        database.close();

        return success;
    }

    protected boolean validateValueOfAttributeName(String attrName, Object value) {
        return true;
    }
}
