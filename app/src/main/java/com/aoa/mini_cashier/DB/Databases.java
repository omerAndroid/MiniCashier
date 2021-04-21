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

public class Databases extends SQLiteOpenHelper {/// Databases_quantity

    static final String DBNAME="DB 0,1.db";///NOT NULL
    private final Context mContext;

    public Databases(@Nullable Context context) {
        super(context,DBNAME,null,2);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {////REAL  = DOUBLE + FLOAT    /////  NUMERIC = Boolean

        db.execSQL("CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT ,bracode TEXT unique,name_g TEXT unique,quantity_stored REAL,quantity_sold REAL" +
                ",expiration_date TEXT ,date_purchase TEXT ,id_d INTEGER)");

        db.execSQL("CREATE TABLE quantity(id INTEGER PRIMARY KEY AUTOINCREMENT ,number_q INTEGER,quantity_q INTEGER," +
                "default_q NUMERIC,purchase_price REAL,selling_price REAL," +
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

        db.execSQL("CREATE TABLE resource(id INTEGER PRIMARY KEY AUTOINCREMENT ,name_res TEXT,mobile INTEGER,phone INTEGER,address INTEGER)");
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
        db.execSQL("DROP TABLE IF EXISTS resource");

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

            }catch (Exception ignored){}
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

    public boolean insert_new_department(String add) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name_dep",add);

        long result=db.insert("department",null,contentValues);
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
    public boolean insert_goods(String bracode,String name,double quantity_stored,String expiration_date,
                                String date_purchase,int id_d){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("bracode",bracode);
        contentValues.put("name_g",name);
        contentValues.put("quantity_stored",quantity_stored);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);
        contentValues.put("id_d",id_d);

        long result=db.insert("goods",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    //////price عملية اضافةللكمية
    public boolean insert_quantity(int number_q, int quantity_q,boolean default_q,double purchase_price,
                                   double selling_price,int id_q,int id_g){
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
    /////////////n         يقوم بتحديث جدول الكميات بستخدام اسم الكمية القديم
    public boolean get_seve_ubdate_quantity(int id, String name_q, Double price,Double quantity_q,
                                            int id_g,Double purchase){
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
        while (!res.isAfterLast()){
            a=res.getInt(res.getColumnIndex("id"));
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
    //////n        جلب كل اسمماء الاقسام
    public String[] get_ALL_department(){
        int lenght=return_lenght_department();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[return_lenght_department()];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from department", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c = res.getString(res.getColumnIndex("name_dep"));
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ///////b يقوم بارجاع عدد الصفوف في الاقسام
    public int return_lenght_department(){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select name_dep from department",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a++;
            res.moveToNext();
        }
        return a;
    }
    //////n جلب رقم القسم
    public int get_id_department(String department){
        int i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from department where name_dep like '"+department+"'",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            i=res.getInt(res.getColumnIndex("id"));

            res.moveToNext();
        }
        return i ;

    }
    //////////////n       يقوم بجلب اسم القسم على حسب ال id
    public String get_name_department(int id){
        String i="";
        SQLiteDatabase db=this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res=db.rawQuery("select name_dep from department where id = "+id+" ",null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            i=res.getString(res.getColumnIndex("name_dep"));

            res.moveToNext();
        }
        return i ;
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
//                c = res.getString(res.getColumnIndex("quantity_stored"));
//                sat[i] = c;
//                i++;
                c = res.getString(res.getColumnIndex("expiration_date"));
                sat[i] = c;
                i++;
                c = res.getString(res.getColumnIndex("date_purchase"));
                sat[i] = c;
                i++;

                c = get_name_department(res.getInt(res.getColumnIndex("id_d")));///////////   *---*
                sat[i] = c;
                i++;
                     res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ////////n         جلب بيانات جدول البضاعة باستخدام الباركود الراجع double
    public double[] get_All_goods_for_barcod_Double(String bracode){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        double[] sat=new double[1];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods where bracode like '"+bracode+"'", null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                double c;

                c = res.getDouble(res.getColumnIndex("quantity_stored"));
                sat[i] = c;
                i++;

                res.moveToNext();
            }
        }else{
            sat=new double[1];
            sat[0]=0.0;}

        return sat;
    }
    ///////n             يقوم بتجديث جدول البضاعة باستخدام الباركود القديم له
    public boolean get_seve_ubdate_googs(String bracode,String name,double quantity_stored,
                                         String expiration_date,String date_purchase,int id_d,String old_baracod){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("bracode",bracode);
        contentValues.put("name_g",name);
        contentValues.put("quantity_stored",quantity_stored);
        contentValues.put("expiration_date",expiration_date);
        contentValues.put("date_purchase",date_purchase);
        contentValues.put("id_d",id_d);

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
    public String[] get_ALLq_qnuatity(int id){
        int lenght=read_Tname_q_type(id);
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[1*read_Tname_q_type(id)];

        if (lenght>0) {
            int i = 0;

            @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from quantity where id_g = "+id+" ", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String c;
                 c = get_name_quantity_type(res.getInt(res.getColumnIndex("id_q")));///////////   *---*
                sat[i] = c;
                i++;

//                c = res.getString(res.getColumnIndex("quantity_q"));
//                sat[i] = c;
//                i++;
//
//                c = res.getString(res.getColumnIndex("purchase_price"));///cشراء ---------- buy
//                sat[i] = c;
//                i++;
//
//                c = res.getString(res.getColumnIndex("selling_price"));///f   البيع   -------- sale
//                sat[i] = c;
//                i++;
                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }
    ///////n جلب من جدول الكميات كل الكميات لتعبئة الليستة *_*/////////////////  Double
    public double[] get_ALLq_qnuatity_Double(int id){
        int lenght=read_Tname_q_type(id);
        SQLiteDatabase db=this.getReadableDatabase();
        double[] sat=new double[3*read_Tname_q_type(id)];

        if (lenght>0) {
            int i = 0;

            @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from quantity where id_g = "+id+" ", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                double c;

                c = res.getDouble(res.getColumnIndex("quantity_q"));
                sat[i] = c;
                i++;

                c = res.getDouble(res.getColumnIndex("purchase_price"));///cشراء ---------- buy
                sat[i] = c;
                i++;

                c = res.getDouble(res.getColumnIndex("selling_price"));///f   البيع   -------- sale
                sat[i] = c;
                i++;
                res.moveToNext();
            }
        }else{
            sat=new double[1];
            sat[0]=0.0;}

        return sat;
    }
     //////////////n       يقوم بجلب اسم الكمية على حسب ال id
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
        String[] sat=new String[4*read_Tname()];

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
//                c = res.getString(res.getColumnIndex("quantity_stored"));
//                sat[i] = String.valueOf(Double.parseDouble(c));
//                i++;
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
     //////n                                               _Double
    public double[] get_All_goods_Double(){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        double[] sat=new double[read_Tname()];

        if (lenght>0) {
            int i = 0;
            Cursor res = db.rawQuery("select * from goods " , null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                double c;
//                c = res.getString(res.getColumnIndex("bracode"));
//                sat[i] = c;
//                i++;
//                c = res.getString(res.getColumnIndex("name_g"));
//                sat[i] = c;
//                i++;
                c = res.getDouble(res.getColumnIndex("quantity_stored"));
                sat[i] = c;
                i++;
//                c = res.getString(res.getColumnIndex("expiration_date"));
//                sat[i] = c;
//                i++;
//                c = res.getString(res.getColumnIndex("date_purchase"));
//                sat[i] = c;
//                i++;
                res.moveToNext();
            }
        }else{
            sat=new double[1];
            sat[0]=0.0;}

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

    public int get_one_goods(String name_goods){
        SQLiteDatabase db=this.getReadableDatabase();
        int a=0;
        Cursor res=db.rawQuery("select * from goods where name_g like '"+name_goods+"' ",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a=res.getInt(res.getColumnIndex("id"));
            res.moveToNext();
        }
        return a;
    }

    public String[] get_one_quantity(int id_g ){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        String[] sat=new String[1];

        if (lenght>0) {

            Cursor res  = db.rawQuery("select * from quantity where  id_g = "+id_g+" and default_q = "+1+" ",null);

            int i = 0;

            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String c;
//                c = res.getString(res.getColumnIndex("selling_price"));///////n   البيع
//                sat[i] = c;
//                i++;
                c = get_name_quantity_type(res.getInt(res.getColumnIndex("id_q")));///////////   *---*
                sat[0] = c;
                i++;

                res.moveToNext();
            }
        }else{
            sat=new String[1];
            sat[0]=" ";}

        return sat;
    }

    public double[] get_one_quantity_double(int id_g ){
        int lenght=read_Tname();
        SQLiteDatabase db=this.getReadableDatabase();
        double[] sat=new double[1];

        if (lenght>0) {

            Cursor res  = db.rawQuery("select * from quantity where  id_g = "+id_g+" and default_q = "+1+" ",null);

            int i = 0;

            res.moveToFirst();
            while (res.isAfterLast() == false) {
                double c;
                c = res.getDouble(res.getColumnIndex("selling_price"));///////n     البيع
                sat[0] = c;
                i++;

                res.moveToNext();
            }
        }else{
            sat=new double[1];
            sat[0]=0.0;}

        return sat;
    }

    public boolean get_delete_goods(String code){
        SQLiteDatabase db=this.getWritableDatabase();

        long result=db.delete("goods","bracode = ?",new String[]{code});
        if (result==-1)
            return false;
        else
            return true;
    }

    public boolean get_delete_quantity(String id){
        SQLiteDatabase db=this.getWritableDatabase();

        long result=db.delete("quantity","id_g = ?",new String[]{id});
        if (result==-1)
            return false;
        else
            return true;
    }

    public int get_retern_cheack(int id_g,String number_q){

        SQLiteDatabase db=this.getReadableDatabase();
        int id_q = get_check_quantity_type(number_q);
        int a=0;

      @SuppressLint("Recycle") Cursor res=db.rawQuery("select default_q from quantity where id_g = "+id_g+" and id_q = "+id_q+" ",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            a=res.getInt(res.getColumnIndex("default_q"));
            res.moveToNext();
        }
        return a;
    }



}

