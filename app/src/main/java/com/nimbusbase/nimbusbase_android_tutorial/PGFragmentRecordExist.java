package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Will on 11/10/14.
 */
public class PGFragmentRecordExist extends PGFragmentRecord {

    protected static String
            sRecordIDKey = "record_id";

    protected Long
            mRecordID;

    protected PGRecordBasic
            mRecord;

    protected boolean
            mEditing;

    public static PGFragmentRecordExist newInstance(String tableName, Long recordID, HashMap<String, Integer> attrTypesByName) {
        final PGFragmentRecordExist
                fragment = new PGFragmentRecordExist();
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
        final Bundle
                bundle = getArguments();
        this.mRecordID = bundle.getLong(sRecordIDKey);
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
            if (cursor.moveToFirst()) {
                final PGRecordBasic
                        record = new PGRecordBasic(cursor);
                if (!record.equals(mRecord)) {
                    this.mRecord = record;
                    reload(mAttrTypesByName, record.getValuesByAttrName());

                    if (actionBar != null)
                        actionBar.setTitle(record.getTitle());
                }
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
        inflater.inflate(R.menu.pg_menu_record_exist, menu);
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

    protected boolean updateRecord(ContentValues contentValues) {
        final SQLiteDatabase
                database = mSQLiteOpenHelper.getWritableDatabase();

        boolean success =
                database.update(mTableName, contentValues, "_ROWID_ == ?", new String[]{mRecordID.toString(),}) > 0;

        database.close();

        return success;
    }
}
