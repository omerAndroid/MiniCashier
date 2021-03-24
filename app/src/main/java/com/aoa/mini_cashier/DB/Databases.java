package com.aoa.mini_cashier.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Databases extends SQLiteOpenHelper {/// hello AAB

    static final String DBNAME="DB 0,1.db";///NOT NULL
    private Context mContext;

    public Databases(@Nullable Context context) {
        super(context,DBNAME,null,1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {////REAL  = DOUBLE + FLOAT
        db.execSQL("CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT ,barcod TEXT,quantity_box REAL ,name_g TEXT,quantity REAL,expiration_date TEXT ," +/////
                " date_purchase TEXT)");

        db.execSQL("CREATE TABLE quantity(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_q TEXT , price REAL ,quantity_q REAL ,id_g INTEGER ,purchase REAL," +
                "FOREIGN KEY(id_g) REFERENCES goods(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_agent TEXT,address TEXT,email TEXT , password INTEGER)");

        db.execSQL("CREATE TABLE phone_agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,phone INTEGER,id_agent INTEGER," +
                "FOREIGN KEY(id_agent) REFERENCES agent(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,date_bills TEXT,type_bills TEXT,total INTEGER,discount INTEGER,paid INTEGER," +
                "remaining INTEGER,id_agent INTEGER,recipient TEXT,FOREIGN KEY(id_agent) REFERENCES agent(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE products_bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_prod TEXT,selling_price INTEGER,purchase_price INTEGER," +
                "quantity INTEGER,id_bills INTEGER," +
                "FOREIGN KEY(id_bills) REFERENCES bills(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE quantity_type(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_type TEXT unique)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS goods");
        db.execSQL("DROP TABLE IF EXISTS quantity");
        db.execSQL("DROP TABLE IF EXISTS agent");
        db.execSQL("DROP TABLE IF EXISTS phone_agent");
        db.execSQL("DROP TABLE IF EXISTS bills");
        db.execSQL("DROP TABLE IF EXISTS products_bills");
        db.execSQL("DROP TABLE IF EXISTS quantity_type");

        onCreate(db);
    }

    public void insert_quantity_type()
    {
        String [] add = {"كرتون","درزن","حبة"};
        for(int i=0;i<3;i++)
        {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name_type", add[i]);

                long result = db.insert("quantity_type", null, contentValues);
            }catch (Exception e){}
        }
    }

    public ArrayList Show_quantity_type()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from quantity_type  ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            arrayList.add(res.getString(1));
            res.moveToNext();
        }

        return arrayList;
    }
    //////b انشاء نسخة احتاياطية
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
    ///  E عملية التاكد من الباركود اذا كان موجود من قبل او لا
    public int check_baracod(String baracod){
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
    //price عملية اللاضافةللبضاعة
    public boolean insert_goods(String barcod,String name,Double quantity,String expiration_date,String date_purchase){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("barcod",barcod);
        contentValues.put("name_g",name);
        contentValues.put("quantity",quantity);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);

        long result=db.insert("goods",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    //////price عملية اضافةللكمية
    public boolean insert_quantity(String name_q, double price,double quantity_q,int id_g,double purchase){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name_q",name_q);
        contentValues.put("price",price);
        contentValues.put("quantity_q",quantity_q);
        contentValues.put("id_g",id_g);
        contentValues.put("purchase",purchase);


        long result=db.insert("quantity",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    //////n جلب رقم البضاعة
    public int get_id_goods(String barcod){
        int i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from goods where barcod like '"+barcod+"'",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
             i=res.getInt(res.getColumnIndex("id"));

            res.moveToNext();
        }
        return i ;

    }
    //////n        جلب كل الباركود
    public String[] get_ALLbaracod(){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[read_Tname()];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c = res.getString(res.getColumnIndex("barcod"));
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    //////n        جلب كل اسماء المنتجات
    public String[] get_ALLname_g(){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[read_Tname()];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c = res.getString(res.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ///////b يقوم بارجاع عدد الصفوف في المصفوفه
    private int read_Tname(){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select barcod from goods",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a++;
            res.moveToNext();
        }
        return a;
    }
    ////////n       جلب بيانات جدول البضاعة باستخدام الباركود
    public String[] get_All_goods_for_barcod(String barcod){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[5];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods where barcod like '"+barcod+"'", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c;
                c = res.getString(res.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("quantity"));
                sat[i] = String.valueOf(Double.parseDouble(c));
                i++;
                c = res.getString(res.getColumnIndex("expiration_date"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("date_purchase"));
                sat[i] = c;
                i++;
                     res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ///////n             يقوم بتجديث جدول البضاعة باستخدام الباركود القديم له
    public boolean get_seve_ubdate_googs(String barcod, String name,Double quantity,String expiration_date,String date_purchase,String old_baracod){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("barcod",barcod);
        contentValues.put("name_g",name);
        contentValues.put("quantity",quantity);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);

        long result=db.update("goods",contentValues,"barcod= ?",new String[]{old_baracod});
        if (result==-1)
            return false;
        else
            return true;
    }


    public int get_id_quantity(String name , int id_g){
        int i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from quantity where name_q like '"+name+"' and id_g =="+id_g,null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            i=res.getInt(res.getColumnIndex("id"));

            res.moveToNext();
        }
        return i ;

    }
     /////////////n                    يقوم بتحديث جدول الكميات بستخدام اسم الكمية القديم
    public boolean get_seve_ubdate_quantity(int id, String name_q, Double price,Double quantity_q,int id_g,Double purchase){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name_q",name_q);
        contentValues.put("price",price);
        contentValues.put("quantity_q",quantity_q);
        contentValues.put("id_g",id_g);
        contentValues.put("purchase",purchase);

        long result=db.update("quantity",contentValues,"id = ?",new String[]{String.valueOf(id)});
        if (result==-1)
            return false;
        else
            return true;
    }

    public boolean get_delete_quantity(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        long result=db.delete("quantity","id = ?",new String[]{String.valueOf(id)});
        if (result==-1)
            return false;
        else
            return true;
    }
    ///////n جلب من جدول الكميات كل الكميات لتعبئة الليستة *_*////////////////////////////////////
    public String[] get_ALLq_qnuatitytype(int id){
        int lenght=read_Tname_q_type(id);
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[4*read_Tname_q_type(id)];

        if (lenght>0) {
            int i = 0;

            @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from quantity where id_g = "+id+" ", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String c;
                 c = res.getString(res.getColumnIndex("name_q"));
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("quantity_q"));
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("purchase"));///cشراء ---------- buy
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("price"));///f   البيع   -------- sale
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ///////b يقوم بارجاع عدد الصفوف في المصفوفه
    public int read_Tname_q_type(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        ///Cursor res=db.rawQuery("select name_q from quantity where id_g = id ",null);
        //("goods",contentValues,"barcod= ?",new String[]{old_baracod});
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select name_q from quantity where id_g = "+id+" ",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a++;
            res.moveToNext();
        }
        return a;
    }

    public String[] get_All_goods(){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[5];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods " , null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c;
                c = res.getString(res.getColumnIndex("barcod"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("quantity"));
                sat[i] = String.valueOf(Double.parseDouble(c));
                i++;
                c = res.getString(res.getColumnIndex("expiration_date"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("date_purchase"));
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    public boolean get_delete_goods(String code){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        long result=db.delete("goods","barcod = ?",new String[]{code});
        if (result==-1)
            return false;
        else
            return true;
    }
}

