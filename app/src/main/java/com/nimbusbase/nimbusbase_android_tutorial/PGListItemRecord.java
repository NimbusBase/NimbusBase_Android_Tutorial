package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.Preference;

/**
 * Created by Will on 11/10/14.
 */
public class PGListItemRecord extends Preference {

    protected final PGRecord
            mRecord;

    public PGListItemRecord(Context context, PGRecord record) {
        super(context);
        this.mRecord = record;
        setTitle(record.getTitle());
        setSummary(record.getSummary());
    }

    public PGRecord getRecord() {
        return mRecord;
    }
/*
    protected OnPreferenceLongClickListener
            mOnPreferenceLongClickListener;

    public void setOnPreferenceLongClickListener(OnPreferenceLongClickListener onPreferenceLongClickListener) {
        mOnPreferenceLongClickListener = onPreferenceLongClickListener;
    }

    public interface OnPreferenceLongClickListener {
        boolean onPreferenceLongClick(Preference preference);
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnPreferenceLongClickListener != null && mOnPreferenceLongClickListener.onPreferenceLongClick(this);
    }
    */
}
