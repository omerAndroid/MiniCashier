package com.aoa.mini_cashier.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Databases extends SQLiteOpenHelper {/// hello AAB

    static final String DBNAME="DB 0,1.db";///NOT NULL
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Databases(@Nullable Context context) {
        super(context,DBNAME,null,1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {////REAL  = DOUBLE + FLOAT
        db.execSQL("CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT ,barcod TEXT,name_g TEXT,quantity REAL,quantity_box REAL ,expiration_date TEXT ," +
                " date_purchase TEXT)");

        db.execSQL("CREATE TABLE quantity(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_q TEXT , price REAL ,quantity_q TEXT , id_g INTEGER ,purchase REAL," +
                "FOREIGN KEY(id_g) REFERENCES goods(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_agent TEXT,address TEXT,email TEXT , password INTEGER)");

        db.execSQL("CREATE TABLE phone_agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,phone INTEGER,id_agent INTEGER," +
                "FOREIGN KEY(id_agent) REFERENCES agent(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,date_bills TEXT,type_bills TEXT,total INTEGER,discount INTEGER,paid INTEGER," +
                "remaining INTEGER,id_agent INTEGER,recipient TEXT,FOREIGN KEY(id_agent) REFERENCES agent(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE products_bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_prod TEXT,selling_price INTEGER,purchase_price INTEGER,quantity INTEGER,id_bills INTEGER," +
                "FOREIGN KEY(id_bills) REFERENCES bills(id) ON UPDATE CASCADE ON DELETE CASCADE)");


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


    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(DBNAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public int check_baracod(String baracod){ ///  E عملية التاكد من الباركود اذا كان موجود من قبل او لا
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        Cursor res=db.rawQuery("select * from goods where barcod like '"+baracod+"' ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            a++;
            res.moveToNext();
        }
        return a;
    }


    public boolean insert_goods(String barcod, String name,Double quantity,Double quantity_box,String expiration_date,String date_purchase){//price عملية اللاضافةللبضاعة
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);

        long result=db.insert("goods",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
}

