package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Will on 11/10/14.
 */
public class MDLDatabaseManager extends SQLiteOpenHelper {

    private static final int
            sDatabaseVersion = 1;
    private static final String
            sDatabaseName = "NimbusBase_Android_Tutorial";

    public MDLDatabaseManager(Context context) {
        super(context, sDatabaseName, null, sDatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String[]
                tableCreateSQLs = {MDLUser.SQL_CREATE_TABLE,};
        for (final String sql : tableCreateSQLs) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
