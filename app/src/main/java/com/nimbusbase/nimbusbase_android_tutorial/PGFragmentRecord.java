package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Will on 11/11/14.
 */
public class PGFragmentRecord extends PreferenceFragment {

    protected static String
            sTableNameKey = "table_name",
            sAttrTypesByNameKey = "attr_types_by_name";

    protected String
            mTableName;
    protected Map<String, Integer>
            mAttrTypesByName;

    protected SQLiteOpenHelper
            mSQLiteOpenHelper;

    public static PGFragmentRecordExist newInstance(String tableName, HashMap<String, Integer> attrTypesByName) {
        final PGFragmentRecordExist
                fragment = new PGFragmentRecordExist();
        final Bundle
                bundle = new Bundle();
        bundle.putString(sTableNameKey, tableName);
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
        this.mAttrTypesByName = (Map<String, Integer>) bundle.getSerializable(sAttrTypesByNameKey);

        this.mSQLiteOpenHelper = new MDLDatabaseManager(getActivity());

        final PreferenceScreen
                preferenceScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        setPreferenceScreen(preferenceScreen);
        preferenceScreen.setOrderingAsAdded(true);
    }

    protected void reload(Map<String, Integer> typesByAttrName, Map<String, Object> valuesByAttrName) {
               final PreferenceScreen
                preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        final Activity
                context = getActivity();

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

    protected boolean validateValueOfAttributeName(String attrName, Object value) {
        return true;
    }
}
