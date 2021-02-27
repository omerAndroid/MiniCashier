package com.aoa.mini_cashier.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

public class Databases extends SQLiteOpenHelper {

    static final String DBNAME="DB0,1.db";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Databases(Context context) {
        super(context,DBNAME,null,1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
