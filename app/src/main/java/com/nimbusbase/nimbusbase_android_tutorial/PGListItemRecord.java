package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.preference.Preference;
import android.view.View;

/**
 * Created by Will on 11/10/14.
 */
public class PGListItemRecord extends Preference implements View.OnLongClickListener {
    public PGListItemRecord(Context context, PGRecord record) {
        super(context);
        setTitle(record.getTitle());
        setSummary(record.getSummary());
    }

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
}
