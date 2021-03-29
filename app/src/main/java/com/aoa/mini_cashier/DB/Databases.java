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
        super(context,DBNAME,null,2);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {////REAL  = DOUBLE + FLOAT    /////  NUMERIC = Boolean

        db.execSQL("CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT ,bracode TEXT unique,name_g TEXT unique,quantity_stored REAL,quantity_sold REAL" +
                ",expiration_date TEXT ," +
                " date_purchase TEXT)");

        db.execSQL("CREATE TABLE quantity(id INTEGER PRIMARY KEY AUTOINCREMENT ,number_q INTEGER,quantity_q REAL,default_q NUMERIC,purchase_price REAL,selling_price REAL," +
                "id_q INTEGER,id_g INTEGER," +
                "FOREIGN KEY(id_g) REFERENCES goods(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(id_q) REFERENCES quantity_type(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE quantity_type(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_type TEXT unique)");

        db.execSQL("CREATE TABLE department(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_dep TEXT unique)");

        db.execSQL("CREATE TABLE agent(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_agent TEXT unique,address TEXT,mobile INTEGER,telephone INTEGER,email TEXT," +
                "password INTEGER)");

        db.execSQL("CREATE TABLE paid_type(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_type TEXT)");

        db.execSQL("CREATE TABLE bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,date_bills TEXT,id_p INTEGER,total REAL,paid REAL,discount REAL," +
                "id_agent INTEGER,recipient TEXT," +
                "FOREIGN KEY(id_agent) REFERENCES agent(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY(id_p) REFERENCES paid_type(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE products_bills(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_prod TEXT,purchase_price REAL,selling_price REAL," +
                "quantity REAL,id_bills INTEGER," +
                "FOREIGN KEY(id_bills) REFERENCES bills(id) ON UPDATE CASCADE ON DELETE CASCADE)");
 }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS goods");
        db.execSQL("DROP TABLE IF EXISTS quantity");
        db.execSQL("DROP TABLE IF EXISTS agent");
        db.execSQL("DROP TABLE IF EXISTS bills");
        db.execSQL("DROP TABLE IF EXISTS products_bills");
        db.execSQL("DROP TABLE IF EXISTS quantity_type");
        db.execSQL("DROP TABLE IF EXISTS department");
        db.execSQL("DROP TABLE IF EXISTS paid_type");

        onCreate(db);
    }

    //////////////n فها خطاء
    public void insert_quantity_type() {
        String [] add = {"كرتون","درزن","حبة"};
        for(int i=0;i<3-1;i++)
        {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name_type", add[i]);

                long result = db.insert("quantity_type", null, contentValues);

            }catch (Exception e){}
        }
    }
     ////////////////n        عملية اضافة نوع كمية جديدة
    public boolean insert_new_quantity_type(String add) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name_type",add);

        long result=db.insert("quantity_type",null,contentValues);
        return result != -1;

    }


    public ArrayList Show_quantity_type() {
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
    public int check_baracod(String bracode){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        Cursor res=db.rawQuery("select * from goods where bracode like '"+bracode+"' ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            a++;
            res.moveToNext();
        }
        return a;
    }
    //price عملية اللاضافةللبضاعة
    public boolean insert_goods(String bracode,String name,float quantity_stored,String expiration_date,String date_purchase){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("bracode",bracode);
        contentValues.put("name_g",name);
        contentValues.put("quantity_stored",quantity_stored);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);

        long result=db.insert("goods",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    //////price عملية اضافةللكمية
    public boolean insert_quantity(int number_q, float quantity_q,boolean default_q,float purchase_price,float selling_price,int id_q,int id_g){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("number_q",number_q);
        contentValues.put("quantity_q",quantity_q);
        contentValues.put("default_q",default_q);
        contentValues.put("purchase_price",purchase_price);
        contentValues.put("selling_price",selling_price);
        contentValues.put("id_q",id_q);
        contentValues.put("id_g",id_g);

        long result=db.insert("quantity",null,contentValues);

        if (result==-1){

            Toast.makeText(mContext, "insert_quantity ERRE *~* ", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
    ////////////////n        عملية اضافة سريعة للمخزون
    public boolean insert_fast_to_quantity_stored_in_goods(float add,String bracode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity_stored",add);

        long result=db.update("goods",contentValues,"bracode= ?",new String[]{bracode});

        return result != -1;

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
    //////n جلب رقم البضاعة
    public int get_id_goods(String bracode){
        int i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from goods where bracode like '"+bracode+"'",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
             i=res.getInt(res.getColumnIndex("id"));

            res.moveToNext();
        }
        return i ;

    }
    ///////////////n     id نوع الكمية جلب
    public int get_id_quantity_type(String name_type){

        int i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from quantity_type where name_type like '"+name_type+"'",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            i=res.getInt(res.getColumnIndex("id"));

            res.moveToNext();
        }
        return i ;
    }
    ///  E عملية التاكد من اسم البضاعة اذا كان موجود من قبل او لا
    public int get_check_quantity_type(String name_type){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        Cursor res=db.rawQuery("select * from quantity_type where name_type like '"+name_type+"' ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            a++;
            res.moveToNext();
        }
        return a;
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
                String c = res.getString(res.getColumnIndex("bracode"));
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
    public int read_Tname(){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select bracode from goods",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a++;
            res.moveToNext();
        }
        return a;
    }
    ////////n       جلب بيانات جدول البضاعة باستخدام الباركود
    public String[] get_All_goods_for_barcod(String bracode){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[5];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods where bracode like '"+bracode+"'", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c;
                c = res.getString(res.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("quantity_stored"));
                sat[i] = c;
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
    public boolean get_seve_ubdate_googs(String bracode,String name,float quantity_stored,String expiration_date,String date_purchase,String old_baracod){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("bracode",bracode);
        contentValues.put("name_g",name);
        contentValues.put("quantity_stored",quantity_stored);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);

        long result=db.update("goods",contentValues,"bracode= ?",new String[]{old_baracod});
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
                 c = get_name_quantity_type(res.getInt(res.getColumnIndex("id_q")));///////////   *---*
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("quantity_q"));
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("purchase_price"));///cشراء ---------- buy
                sat[i] = c;
                i++;

                c = res.getString(res.getColumnIndex("selling_price"));///f   البيع   -------- sale
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }

    public String get_name_quantity_type(int id){
        String i="";
        SQLiteDatabase db=this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select name_type from quantity_type where id = "+id+" ",null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            i=res.getString(res.getColumnIndex("name_type"));

            res.moveToNext();
        }
        return i ;
    }

    ///////b يقوم بارجاع عدد الصفوف في المصفوفه
    public int read_Tname_q_type(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        ///Cursor res=db.rawQuery("select name_q from quantity where id_g = id ",null);
        //("goods",contentValues,"barcod= ?",new String[]{old_baracod});
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select number_q from quantity where id_g = "+id+" ",null);
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
        String[] sat=new String[5*read_Tname()];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods " , null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c;
                c = res.getString(res.getColumnIndex("bracode"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("quantity_stored"));
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

    Cursor res1;
    public String[] get_one_goods(String searsh ,String key){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[5];


        if (lenght>0) {

            if (key.equals("Allbaracod")){
                res1 = db.rawQuery("select * from goods where bracode like '"+searsh+"'",null);

            }else {
                res1 = db.rawQuery("select * from goods where name_g like '"+searsh+"'",null);
            }

            int i = 0;

            res1.moveToFirst();
            while (res1.isAfterLast() == false) {
                String c;
                c = res1.getString(res1.getColumnIndex("bracode"));
                sat[i] = c;
                i++;
                c = res1.getString(res1.getColumnIndex("name_g"));
                sat[i] = c;
                i++;
                c = res1.getString(res1.getColumnIndex("quantity_stored"));
                sat[i] = String.valueOf(Double.parseDouble(c));
                i++;
                c = res1.getString(res1.getColumnIndex("expiration_date"));
                sat[i] = c;
                i++;
                c = res1.getString(res1.getColumnIndex("date_purchase"));
                sat[i] = c;
                i++;
                res1.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }

    public boolean get_delete_goods(String code){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();


        long result=db.delete("goods","bracode = ?",new String[]{code});
        if (result==-1)
            return false;
        else
            return true;
    }



}

