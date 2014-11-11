package com.nimbusbase.nimbusbase_android_tutorial;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 11/11/14.
 */
public class PGRecordBasic implements PGRecord {

    protected final Map<String, Object>
            mValuesByAttrName;
    protected final Map<String, Integer>
            mTypesByAttrName;

    public PGRecordBasic(Cursor cursor) {
        final int
                attrCount = cursor.getColumnCount();
        final Map<String, Object>
                attributes = new HashMap<String, Object>(attrCount);
        final Map<String, Integer>
                types = new HashMap<String, Integer>(attrCount);
        for (int index = 0; index < attrCount; index++) {
            final String
                    key = cursor.getColumnName(index);
            final int
                    type = cursor.getType(index);
            Object value;
            switch (type) {
                case Cursor.FIELD_TYPE_STRING:
                    value = cursor.getString(index);
                    break;
                case Cursor.FIELD_TYPE_BLOB:    // rare case: blob values in non blob column
                    value = cursor.getBlob(index);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    value = cursor.getLong(index);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    value = cursor.getDouble(index);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                default:
                    value = null;
                    break;
            }

            attributes.put(key, value);
            types.put(key, type);
        }

        this.mValuesByAttrName = attributes;
        this.mTypesByAttrName = types;
    }

    public Map<String, Integer> getTypesByAttrName() {
        return mTypesByAttrName;
    }

    public Map<String, Object> getValuesByAttrName() {
        return mValuesByAttrName;
    }

    @Override
    public String getTitle() {
        final String[]
                possibleKeys = {"title", "name"};
        String title = null;
        for (final String key : possibleKeys) {
            Object value = mValuesByAttrName.get(key);
            if (value != null) {
                title = value.toString();
                break;
            }
        }
        return title != null ? title : toString();
    }

    @Override
    public String getSummary() {
        return toString();
    }
}
