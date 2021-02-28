package com.aoa.mini_cashier.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

public class Databases extends SQLiteOpenHelper {

    static final String DBNAME="DB 0,1.db";///
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Databases(@Nullable Context context) {
        super(context,DBNAME,null,1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_g TEXT,quantity INTEGER,expiration_date TEXT , date_purchase TEXT)");

        db.execSQL("CREATE TABLE quantity(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_q TEXT,price INTEGER,id_g INTEGER ,purchase INTEGER," +
                "FOREIGN KEY(id_g) REFERENCES goods(id))");

        db.execSQL("CREATE TABLE agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_agent TEXT,address TEXT,email TEXT , password INTEGER)");

        db.execSQL("CREATE TABLE phone_agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,phone INTEGER,id_agent INTEGER,FOREIGN KEY(id_agent) REFERENCES agent(id))");

        db.execSQL("CREATE TABLE bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,date_bills TEXT,type_bills TEXT,total INTEGER,discount INTEGER,paid INTEGER," +
                "remaining INTEGER,id_agent INTEGER,recipient TEXT,FOREIGN KEY(id_agent) REFERENCES agent(id))");

        db.execSQL("CREATE TABLE products_bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_prod TEXT,selling_price INTEGER,purchase_price INTEGER,quantity INTEGER,id_bills INTEGER," +
                "FOREIGN KEY(id_bills) REFERENCES bills(id))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS goods");
        db.execSQL("DROP TABLE IF EXISTS quantity");
        db.execSQL("DROP TABLE IF EXISTS agent");
        db.execSQL("DROP TABLE IF EXISTS phone_agent");
        db.execSQL("DROP TABLE IF EXISTS bills");
        db.execSQL("DROP TABLE IF EXISTS products_bills");
        onCreate(db);
    }
}
