package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Will on 11/11/14.
 */
public class PGFragmentRecordNew extends PGFragmentRecord {

    public static PGFragmentRecordNew newInstance(String tableName, HashMap<String, Integer> attrTypesByName) {
        final PGFragmentRecordNew
                fragment = new PGFragmentRecordNew();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
        bundle.putSerializable(sAttrTypesByNameKey, attrTypesByName);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ActionBar
                actionBar = getActivity().getActionBar();
        if (actionBar != null)
            actionBar.setTitle("New " + mTableName);

        reload(mAttrTypesByName, new HashMap<String, Object>(0));

        for (final PGListItemAttribute attrItem : getAllAttributeItems()) {
            attrItem.setEnabled(true);
            attrItem.setSelectable(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pg_menu_record_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_save_new_record == item.getItemId()) {
            onSaveClick(item);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    protected void onSaveClick(MenuItem item) {
        final List<PGListItemAttribute>
                attrItems = getAllAttributeItems();
        final ContentValues
                contentValues = contentValuesFromAttributeItems(attrItems);
        if (saveRecord(contentValues)) {
            getFragmentManager().popBackStack();
        }
    }

    protected boolean saveRecord(ContentValues contentValues) {
        final SQLiteDatabase
                database = mSQLiteOpenHelper.getWritableDatabase();

        boolean success =
                database.insert(mTableName, null, contentValues) > 0;

        database.close();

        return success;
    }
}
