package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import static android.widget.Toast.makeText;

import com.aoa.mini_cashier.Class_Adupter.ListAdupter_quantity;
import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

   public Databases databases = new Databases(this);
   public static boolean  check_impot,check_add ,ceack_save=false,ceack_ubdate=false,ceack_seve_ubdat=false,ceack_save_quantity=false;
    String data_type;
    ZXingScannerView scannerView;
    public  String old_baracod,old_baracod_2;
    int date_place = 0;
    @SuppressLint("StaticFieldLeak")
    public static EditText  Text_name_goods, Text_quantity,Text_date_ex, Text_date_sale,Text_extra_quantity;///Text_barcode,
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView Text_barcode;
    @SuppressLint("StaticFieldLeak")
    public static Button add_tg_btn, date_sale_btn, date_ex_btn,save_add_goods,ubdate_btn,seve_ubdat_goods_btn,clear_all;
    private Dialog Date_Dialog;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    private final int intent_key=1;

    public String boolen_key,department_item="";
   public static com.jaredrummler.materialspinner.MaterialSpinner spinner;

    dialog_view_addtypes dva = new dialog_view_addtypes();

   public static ArrayList<list_item_qnuatitytype> q_list = new ArrayList<>();


   ///////n        له علاقة بكلاس الدايلق


   public static float quantity_stored=1;


   @SuppressLint("StaticFieldLeak")
   public static ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_add_goods_db);

        list = findViewById(R.id.list_quantity);
        //////////////////////////////////////////////////////////////////
        add_tg_btn = findViewById(R.id.add_tg_btn);
        date_sale_btn = findViewById(R.id.date_show_sale);
        date_ex_btn = findViewById(R.id.date_show_ex);
        save_add_goods= findViewById(R.id.save_add_goods);
        clear_all= findViewById(R.id.clear_all);
        ubdate_btn= findViewById(R.id.ubdate_btn);
        seve_ubdat_goods_btn= findViewById(R.id.seve_ubdat_goods_btn);

        /////////////////////////////////////////////////////////////////googs
        Text_barcode = (AutoCompleteTextView)findViewById(R.id.add_barcode_txt);
        Text_name_goods = findViewById(R.id.add_name_goods);
        Text_quantity = findViewById(R.id.add_quantity);
        Text_extra_quantity= findViewById(R.id.add_extra_quantity);
        Text_date_ex = findViewById(R.id.add_date_ex);////الانتهاء
        Text_date_sale = findViewById(R.id.add_date_sale);
        spinner = findViewById(R.id.spinner);
        //////////////////////////b   الادخال في مصفوفة الاختيارات للباركود  /////////////////////////////////////////////

        get_ALL_baracode();
        get_ALL_department();
        Text_extra_quantity.setText("0");

        /////////////////////////Date Picker///////////////////////////////////
        calendar = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        //////////b       يقوم بارجاع من كلاس التعديل
        try {
            Bundle b = getIntent().getExtras();
            String code = b.getString("barcode");
            int key = b.getInt("key");
            if (key == 1) {
                Text_barcode.setText(code);
                Packing_for_goods(code);
                de_Modification();
                listShow_qnuatitytype();
            }
        }catch (Exception ignored){}

        date_ex_btn.setOnClickListener(v -> {
            data_type="تاريخ الإنتهاء";
            date_place = 0;
            date_picker();
        });

        date_sale_btn.setOnClickListener(v -> {
            data_type="تاريخ الشراء";
            date_place = 1;
            date_picker();
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
//        list.setOnItemClickListener((parent, view, position, id) -> {
//            if(Text_barcode.isEnabled())
//            {
//                TextView buy = view.findViewById(R.id.q_buy_item);
//                TextView sale = view.findViewById(R.id.q_sale_item);
//                TextView name = view.findViewById(R.id.q_name_item);
//                TextView quantity = view.findViewById(R.id.q_quantity_item);
//
//                dva.buy_price = buy.getText().toString();
//                dva.sale_price = sale.getText().toString();
//                dva.name_type = name.getText().toString();
//                dva.quantity = quantity.getText().toString();
//                dva.check_work=true;
//
//                dva.show(getSupportFragmentManager(), "إضافة نوع");
//            }
//        });

        /////////////////////////////////n حفظ الكميات لاول مره فقط /////////////////////////////////////////////



         //////////////n          عند الضغط على الاقسام
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) ->{
            department_item=item;
            Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();});
        //////////////////////////////////////////////////////////////////////////////////department

        add_tg_btn.setOnClickListener(v -> {
            //shaw quantity adder
            if(get_seve_fast()||check_add) {
                if (!Text_barcode.getText().toString().trim().isEmpty()){
                        dva.show(getSupportFragmentManager(), "إضافة نوع");
                }else {
                    Toast.makeText(add_goods_db.this, "أدخل الباركود", Toast.LENGTH_SHORT).show();
                }
             }
        });

        ///////n الضغط على عملية التعديل
        ubdate_btn.setOnClickListener(v -> {
                old_baracod=Text_barcode.getText().toString().trim();
                de_Modification();
            ceack_ubdate=true;
            add_tg_btn.setEnabled(true);
        });

        ///////n الضغط على عملية حفظ التعديل
        seve_ubdat_goods_btn.setOnClickListener(v -> {
            get_seve_ubdat_goods_btn();
        });

        clear_all.setOnClickListener(v -> {
            Text_barcode.setText("");
            Text_name_goods.setText("");
            Text_quantity.setText("");
            Text_date_ex.setText("");
            Text_date_sale.setText("");
            spinner.setText("");
            spinner.setText("الاقسام");
            save_add_goods.setVisibility(View.VISIBLE);
            clear_all.setVisibility(View.GONE);
            ubdate_btn.setVisibility(View.GONE);///visible      ظاهر
            seve_ubdat_goods_btn.setVisibility(View.GONE);///VISIBLE      ظاهر
            Text_barcode.setEnabled(true);
            Text_name_goods.setEnabled(true);
            Text_quantity.setEnabled(true);
            date_sale_btn.setEnabled(true);
            date_ex_btn.setEnabled(true);
            add_tg_btn.setEnabled(true);
            Text_extra_quantity.setEnabled(true);
            spinner.setEnabled(true);

            listShow_qnuatitytype();
        });

    }

    ///////////////n   يقوم بجلب كل الباركود ويقوم بتخزينها
    private void get_ALL_department() {

        String[] ALL_department=databases.get_ALL_department();

        for (int i=0;i<databases.return_lenght_department();i++){
            add_goods_db.spinner.setItems(ALL_department[i]);
        }

    }
    ////////////n      عند الضغط على حفظ التغييرات ينتقل الى هنا
     public void get_seve_ubdat_goods_btn(){
         if (check_impot_googs()){
             Modification();
             seve_ubdate_googs(old_baracod);
             get_ALL_baracode();
             add_tg_btn.setEnabled(false);
             ceack_seve_ubdat=true;
             //m   إضافة الى ليستة الكميات
             q_list = new ArrayList<>();
             listShow_qnuatitytype();


         }
    }
    ///////////////n   يقوم بجلب كل الباركود ويقوم بتخزينها
    private void get_ALL_baracode() {

        String[] Allbaracod=databases.get_ALLbaracod();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Allbaracod);

        AutoCompleteTextView Text_barcode = (AutoCompleteTextView)
                findViewById(R.id.add_barcode_txt);

        Text_barcode.setAdapter(adapter);
        Text_barcode.setThreshold(1);//will start working from first

        Text_barcode.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات
            Packing_for_goods(item);

        });
        
    }

    //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات
    private void Packing_for_goods(String item) {
        Modification();
        add_tg_btn.setEnabled(false);
        String[] All_goods=databases.get_All_goods_for_barcod(item);
            Text_name_goods.setText(All_goods[0]);
            Text_quantity.setText(All_goods[1]);
            Text_date_ex.setText(All_goods[2]);
            Text_date_sale.setText(All_goods[3]);
        spinner.setText(All_goods[4]);
        listShow_qnuatitytype();
        
    }
    //////////////n        عند الضغط على الكاميرا ينتقل مبارشرة الى كلاس الكاميره
    public void red_qr(View view) {
        Intent intent=new Intent(this,ScanCodeActivity.class);
        intent.putExtra("page","add_goods_db");
        //startActivity(intent);
        startActivityForResult(intent,intent_key);

    }

    public void seve_goods(View view) {
        get_seve_goods(); }
      //////////n        يقوم بحفظ التعديلات
    private boolean seve_ubdate_googs(String old_baracod) {
        boolean check=false;
        int check_baracod = databases.check_baracod(old_baracod);///department_item

        int id_d=databases.get_id_department(department_item);

        if (check_baracod >0) {
            boolean result = databases.get_seve_ubdate_googs(Text_barcode.getText().toString().trim(),///Float.parseFloat(Text_quantity.getText().toString() + "f" ) + Float.parseFloat(Text_extra_quantity.getText().toString() + "f")
                    Text_name_goods.getEditableText().toString(),
                    Float.parseFloat(Text_quantity.getText().toString() + "f" ) + Float.parseFloat(Text_extra_quantity.getText().toString() + "f"),
                    Text_date_ex.getEditableText().toString(),
                    Text_date_sale.getEditableText().toString(),id_d,old_baracod);

            if (result) {
                   check=true;
                Toast.makeText(this, "OK ubdate  ok", Toast.LENGTH_SHORT).show();
                Modification();/////v  تعديل بعد عملية التعديل


            } else {
                check=false;
                Toast.makeText(this, "No ubdate", Toast.LENGTH_SHORT).show();
            }
        }
        return check;
    }

    public boolean get_seve_goods() {

        check_add=false;
        boolean check;

        if (check_impot_googs()) {

            int check_baracod = databases.check_baracod(old_baracod_2);//old_baracod_2

            if (check_baracod >= 0) {

                int id = databases.get_id_goods(old_baracod_2);//old_baracod_2
                int a = databases.read_Tname_q_type(id);

                if (a>0){

                    boolean result = seve_ubdate_googs(old_baracod_2);

                    if (result) {
                        check_add=true;
                        check = true;
                        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
                        Modification();/////v  تعديل بعد عملية الادخال
                        get_ALL_baracode();
                        add_tg_btn.setEnabled(false);
                        //m   إضافة الى ليستة الكميات
                        q_list = new ArrayList<>();
                        listShow_qnuatitytype();

                        ceack_save=true;

                    } else {

                        check = false;
                        Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this, "أدخل كمية للبضاعة", Toast.LENGTH_SHORT).show();
                    check = false;
                }
            } else {
                check_add=true;
                check = false;
            }

        } else {
            check = false;
        }

        return check;
    }

    /////////n عملية اضافة سريعة للبضائع للتخلص من بعض المشاكل
    public boolean get_seve_fast(){
        check_add=false;
        boolean check;

        if (check_impot_googs()) {

            int id_d=databases.get_id_department(department_item);
            int check_baracod = databases.check_baracod(Text_barcode.getText().toString().trim());

            if (check_baracod == 0) {

                    boolean result = databases.insert_goods(Text_barcode.getText().toString().trim(),
                            Text_name_goods.getEditableText().toString(),
                            Float.parseFloat(Text_quantity.getText().toString() + "f" ),
                            Text_date_ex.getEditableText().toString(),
                            Text_date_sale.getEditableText().toString(),id_d);

                    if (result) {
                        check_add=true;
                        check = true;
                        old_baracod_2 = Text_barcode.getText().toString().trim();

//                        Modification();/////v  تعديل بعد عملية الادخال
//                        get_ALL_baracode();
//                        //save_add_goods.setVisibility(View.VISIBLE);
//                        save_add_goods.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, "يوجد باركود مشابة أو اسم منتج مشابه", Toast.LENGTH_SHORT).show();
                        check = false;
                    }


            } else {
                check_add=true;
                check = false;
            }

        } else {
            check = false;
        }

        return check;
    }

    public void Modification() {
        save_add_goods.setVisibility(View.GONE);
        ubdate_btn.setVisibility(View.VISIBLE);///visible      ظاهر
        seve_ubdat_goods_btn.setVisibility(View.GONE);///visible      ظاهر
        Text_barcode.setEnabled(false);
        Text_name_goods.setEnabled(false);
        Text_quantity.setEnabled(false);
        Text_extra_quantity.setEnabled(false);
        date_sale_btn.setEnabled(false);
        date_ex_btn.setEnabled(false);
        clear_all.setVisibility(View.VISIBLE);
        spinner.setEnabled(false);
    }
    ////// n فك التعديل
    public void de_Modification(){
        clear_all.setVisibility(View.GONE);

        ubdate_btn.setVisibility(View.GONE);///visible      ظاهر
        seve_ubdat_goods_btn.setVisibility(View.VISIBLE);///visible      ظاهر
        Text_barcode.setEnabled(true);
        Text_name_goods.setEnabled(true);
        Text_quantity.setEnabled(true);
        date_sale_btn.setEnabled(true);
        date_ex_btn.setEnabled(true);
        Text_extra_quantity.setEnabled(true);
        spinner.setEnabled(true);
    }

    private boolean check_impot_googs() {

        if (TextUtils.isEmpty(Text_barcode.getText().toString().trim()) || TextUtils.isEmpty(Text_name_goods.getText().toString().trim())
                || TextUtils.isEmpty(Text_quantity.getText().toString().trim())|| TextUtils.isEmpty(Text_extra_quantity.getText().toString().trim())
                || TextUtils.isEmpty(Text_date_sale.getText().toString().trim())|| TextUtils.isEmpty(Text_date_ex.getText().toString().trim())||
                TextUtils.isEmpty(department_item)) {

            //mEmail.setError("Email is Required.");

            Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
            // return;
            check_impot = false;
        }else {
                check_impot = true;
        }
        return check_impot;
    }

    public void listShow_qnuatitytype(){

        int id = databases.get_id_goods(Text_barcode.getText().toString().trim());
        int a=databases.read_Tname_q_type(id);
        //Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();
        String[] quantity=databases.get_ALLq_qnuatitytype(id);
       // makeText(this, String.valueOf(g.length), Toast.LENGTH_SHORT).show();
        q_list.clear();
        int i=0;
        for (int j=0;j<a;j++){

            q_list.add(new list_item_qnuatitytype(quantity[i],quantity[1+i],quantity[2+i],quantity[3+i]));
          i+=4;
        }


        //////////////////////////////Add List Item//////////////////////////////////////
         list = (ListView) findViewById(R.id.list_quantity);

        ListAdupter_quantity ad = new ListAdupter_quantity(this,q_list);
        list.setAdapter(ad);


    }

    public void date_picker () {
            //Dialog Date viewer
            Date_Dialog = new Dialog(this);
            Date_Dialog.setContentView(R.layout.dailog_date);
            final DatePicker picker = (DatePicker) Date_Dialog.findViewById(R.id.calendar_View);
            Button date_viewer = (Button) Date_Dialog.findViewById(R.id.date_veiwer);

            picker.setMinDate(calendar.getTimeInMillis());

            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.YEAR, 5);
            picker.setMaxDate(calendar1.getTimeInMillis());

            date_viewer.setOnClickListener(v -> {
                //Get Date to Edit Text
                final Calendar calendar2 = Calendar.getInstance();

                calendar2.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                if (date_place == 0) {
                    Text_date_ex.setText(date_format.format(calendar2.getTime()));
                } else {
                    Text_date_sale.setText(date_format.format(calendar2.getTime()));
                }
                Date_Dialog.dismiss();
            });
            Date_Dialog.show();
        }
     ///////n      يقوم بارسال تاكيد بانة تمت القراء باستخدام الكمايراء لكي يتم مل الليستة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==intent_key){
            if (resultCode==RESULT_OK){
                boolen_key=data.getStringExtra("page");

                makeText(this, boolen_key, Toast.LENGTH_SHORT).show();
                if (boolen_key.equals("true")){
                    q_list = new ArrayList<>();
                    add_tg_btn.setEnabled(false);
                    listShow_qnuatitytype();
                }
            }
        }
    }

    ///////////////////////n       عملية الرجوع
     public boolean onKeyDown(int keyCode, KeyEvent event){/////old_baracod_2
        if (keyCode==KeyEvent.KEYCODE_BACK){

            AlertDialog.Builder bu = new AlertDialog.Builder(this);

            int idd = databases.get_id_goods(old_baracod_2);
            int a = databases.read_Tname_q_type(idd);
            String s=String.valueOf(idd);

            if (a==0&&databases.get_id_goods(old_baracod_2)>0){

                bu.setMessage("لم يتم استكمال بيانات المنتج بعد ، هل تريد استكمالها \n " +
                        "ملاحظة اذا لم تستكمل سيتم الحذف !")
                        .setTitle("تنبية لم  يتم استكمال بيانات المنتج")
                        .setPositiveButton("نعم", (dialog, id) -> {
                        })
                        .setNegativeButton("لا", (dialog, id) -> {
                            databases.get_delete_goods(old_baracod_2);
                            finish();
                        }).show();
            }else if (a>0&&databases.get_id_goods(old_baracod_2)>0&&ceack_save_quantity||ceack_ubdate){

                if (!ceack_ubdate&&!ceack_save) {
                    bu.setMessage("لم يتم حفظ بيانات المنتج بعد ، هل تريد حفظها \n " +
                            "ملاحظة اذا لم تحفظها سيتم الحذف !")
                            .setTitle("تنبية لم  يتم جفظ بيانات المنتج")
                            .setPositiveButton("نعم", (dialog, id) -> {
                                get_seve_goods();
                                add_tg_btn.setEnabled(true);
                                finish();
                            })
                            .setNegativeButton("لا", (dialog, id) -> {
                                databases.get_delete_goods(old_baracod_2);
                                databases.get_delete_quantity(s);
                                finish();
                            }).show();
                }else  if (!ceack_seve_ubdat&&ceack_ubdate){
                    bu.setMessage("لم يتم حفظ تعديل بيانات المنتج بعد ، هل تريد حفظ التعديل \n " +
                            "ملاحظة اذا لم تحفظ التعديل فلن يتم تعديلة !")
                            .setTitle("تنبية لم  يتم جفظ بيانات المنتج")
                            .setPositiveButton("نعم", (dialog, id) -> {
                                get_seve_ubdat_goods_btn();
                                finish();
                            })
                            .setNegativeButton("لا", (dialog, id) -> {
                                finish();
                            }).show();
                }else {
                    finish();
                }

            }else {
                finish();
            }
            ceack_save=false;
            ceack_ubdate=false;
            ceack_seve_ubdat=false;
            ceack_save_quantity=false;
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            makeText(this, "111111111111", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyLongPress(keyCode,event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            makeText(this, "222222222222222", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            makeText(this, "333333333333", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }



    ////////n     dialog_view_addtypes
    public static class dialog_view_addtypes extends AppCompatDialogFragment {

        @SuppressLint("StaticFieldLeak")
        public  EditText  Text_q_type, Text_q_buy_price, Text_q_sale_price,Text_q_type_2,
                Text_q_quantity_2,Text_q_buy_price_2,Text_q_sale_price_2,Text_q_type_3,Text_q_quantity_3,
                Text_q_buy_price_3,Text_q_sale_price_3,Text_q_type_4,Text_q_quantity_4,Text_q_buy_price_4,Text_q_sale_price_4;
        public  String name_type,quantity,buy_price,sale_price;
        RadioGroup radioGroup;
        public  boolean check_work=false,check_insert_Data=false,isChecked_1=false,isChecked_2=false,isChecked_3=false,isChecked_4=false;
        Button  save,del;
        int id_quantity;
        public  String old_q_type,add_q_type;

        androidx.appcompat.app.AlertDialog.Builder builder;
        boolean check_impot;

        RadioButton radioButton;

        int sum_q;

        int size_impout;

        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            try {

                builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                Databases databases = new Databases(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.types_goods, null);
                /////////////////////////////////////////////////////////////////quantity -> dialog
                save = view.findViewById(R.id.save_tg_add);
                del = view.findViewById(R.id.delete_tg_add);

                //primary_type=view.findViewById(R.id.primary_type);
                Text_q_type=view.findViewById(R.id.q_type);
                Text_q_buy_price=view.findViewById(R.id.q_buy_price);
                Text_q_sale_price=view.findViewById(R.id.q_sale_price);

                //  primary_type_2=view.findViewById(R.id.primary_type_2);
                Text_q_type_2=view.findViewById(R.id.q_type_2);
                Text_q_quantity_2=view.findViewById(R.id.q_quantity_2);
                Text_q_buy_price_2=view.findViewById(R.id.q_buy_price_2);
                Text_q_sale_price_2=view.findViewById(R.id.q_sale_price_2);

                // primary_type_3=view.findViewById(R.id.primary_type_3);
                Text_q_type_3=view.findViewById(R.id.q_type_3);
                Text_q_quantity_3=view.findViewById(R.id.q_quantity_3);
                Text_q_buy_price_3=view.findViewById(R.id.q_buy_price_3);
                Text_q_sale_price_3=view.findViewById(R.id.q_sale_price_3);

                //  primary_type_4=view.findViewById(R.id.primary_type_4);
                Text_q_type_4=view.findViewById(R.id.q_type_4);
                Text_q_quantity_4=view.findViewById(R.id.q_quantity_4);
                Text_q_buy_price_4=view.findViewById(R.id.q_buy_price_4);
                Text_q_sale_price_4=view.findViewById(R.id.q_sale_price_4);

                //////////////////////////n   عملية الضغط على الرايديو بوتن  ////////////////////////////////////////////////////
                radioGroup=view.findViewById(R.id.radioGroup);
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    radioButton=view.findViewById(checkedId);
                    switch (radioButton.getId()){
                        case R.id.primary_type:{
                            isChecked_1=true;
                        }
                        break;
                        case R.id.primary_type_2:{
                            isChecked_2=true;
                        }
                        break;
                        case R.id.primary_type_3:{
                            isChecked_3=true;
                        }
                        break;
                        case R.id.primary_type_4:{
                            isChecked_4=true;
                        }
                        break;
                    }
                });




        /*databases.insert_quantity_type();

        ArrayList<String> add_spinner = databases.Show_quantity_type();
        add_q_type = add_spinner.get(0);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,add_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Text_q_type.setAdapter(adapter);*/
///////////////////////////////////////////////////////////////////////////////////////////////


                if(check_work)
                {
                    del.setVisibility(View.VISIBLE);
                    add_q_type = name_type;
                    // Text_q_quantity.setText(quantity);
                    Text_q_buy_price.setText(buy_price);
                    Text_q_sale_price.setText(sale_price);
                    save.setText("تعديل");
                    id_quantity=databases.get_id_quantity(name_type,databases.get_id_goods(Text_barcode.getText().toString().trim()));
                }
                else {
                    save.setText("حفظ");
                    del.setVisibility(View.GONE);

                }



                ///////////////////////////////////////////////////////////////////////////////////////
        /*Text_q_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add_q_type= add_spinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
                //////////////////////////////////////////////////////////////////////////////

                //save Button on clicked
                save.setOnClickListener(v -> {

                    if (check_impot_quantity()) {

                        //old_q_type=add_q_type;

                        ////// m  يتم اضافة الى جدول الكمية
                        if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
                            // جلب رقم البضاعة
                            //int id = databases.get_id_goods(Text_barcode.getText().toString().trim());

                            if (save.getText().toString().equals("حفظ")){

                                if (!isChecked_1&&!isChecked_2&&!isChecked_3&&!isChecked_4){
                                    Toast.makeText(getActivity(), "حدد احد الخيارات التي بالاسفل", Toast.LENGTH_SHORT).show();
                                }else {
                                    boolean result =insert_Data();
                                    if (result) {
                                        /////////m     يقوم بادخال الكمية المخزونة
                                        //databases.insert_fast_to_quantity_stored_in_goods(quantity_stored,Text_barcode.getText().toString().trim());
                                        /////////m     يقوم لارسال القيمة الى العرض
                                        quantity_stored = quantity_stored * Float.parseFloat(Text_quantity.getText().toString() + "f" );
                                        quantity_stored+=Float.parseFloat(Text_extra_quantity.getText().toString() + "f");
                                        Text_quantity.setText(Float.toString(quantity_stored));

                                        ceack_save_quantity=true;
                                        dismiss();
                                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
//                        boolean result2 = databases.get_seve_ubdate_quantity(id_quantity,add_q_type, Double.parseDouble(Text_q_buy_price.getText().toString().trim()),
//                                Double.parseDouble(Text_q_quantity.getText().toString().trim()), id, Double.parseDouble(Text_q_sale_price.getText().toString().trim()));

                                if (true) {
                                    dismiss();
                                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                                    ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
                                } else{
                                    Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }//
                    }

                });

                del.setOnClickListener(v -> {
                    boolean result2 = databases.get_delete_quantity(id_quantity);
                    if (result2) {
                        dismiss();
                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                        ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
                    } else{
                        Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                    }

                });



                builder.setView(view)
                        .setTitle("إضافة نوع");
        /*
                .setPositiveButton("حفظ", (dialogInterface, i) -> {

                });*/


            }catch (Exception e){
                //////
            }
            return builder.create();
        }


        public boolean insert_Data(){
            Databases databases = new Databases(getActivity());
            int id_g = databases.get_id_goods(Text_barcode.getText().toString().trim());
            int id_q,number_type=0;
            check_insert_Data=false;
            if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())){
                number_type=1;
                id_q=databases.get_id_quantity_type(Text_q_type.getEditableText().toString());

                check_insert_Data= databases.insert_quantity(number_type,1,isChecked_1,Float.parseFloat(Text_q_buy_price.getText().toString() + "f" ),
                        Float.parseFloat(Text_q_sale_price.getText().toString() + "f" ),id_q,id_g);

                quantity_stored *=1;
            }
            if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                number_type=2;
                id_q=databases.get_id_quantity_type(Text_q_type_2.getEditableText().toString());

                check_insert_Data=databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_2.getText().toString()),isChecked_2,
                        Float.parseFloat(Text_q_buy_price_2.getText().toString() + "f" ), Float.parseFloat(Text_q_sale_price_2.getText().toString() + "f" ),id_q,id_g);

                quantity_stored *=Float.parseFloat(Text_q_quantity_2.getText().toString()+"f");
            }
            if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                number_type=3;
                id_q=databases.get_id_quantity_type(Text_q_type_3.getEditableText().toString());
                check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_3.getText().toString()),isChecked_3,
                        Float.parseFloat(Text_q_buy_price_3.getText().toString() + "f" ), Float.parseFloat(Text_q_sale_price_3.getText().toString() + "f" ),id_q,id_g);

                quantity_stored *=Float.parseFloat(Text_q_quantity_3.getText().toString()+"f");
            }
            if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                number_type=4;
                id_q=databases.get_id_quantity_type(Text_q_type_4.getEditableText().toString());
                check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_4.getText().toString()),isChecked_4,
                        Float.parseFloat(Text_q_buy_price_4.getText().toString() + "f" ), Float.parseFloat(Text_q_sale_price_4.getText().toString() + "f" ),id_q,id_g);

                quantity_stored *=Float.parseFloat(Text_q_quantity_4.getText().toString()+"f");
            }

            return check_insert_Data;

        }


        private boolean check_impot_quantity() {
            size_impout=0;
            sum_q=0;
            if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())) {

                if (TextUtils.isEmpty(Text_q_buy_price.getEditableText().toString())||TextUtils.isEmpty(Text_q_sale_price.getEditableText().toString())){
                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {

                    sum_q+=1;
                    if (!checked_quantity_type(Text_q_type.getText().toString().trim())){
                        check_impot = AlertDialog_show(Text_q_type.getEditableText().toString());
                    }else {check_impot = true;}
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_2.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_2.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_2.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {

                    sum_q+=1;
                    if (!checked_quantity_type(Text_q_type_2.getText().toString().trim())){
                        check_impot = AlertDialog_show(Text_q_type_2.getEditableText().toString());
                    }else {check_impot = true;}
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_3.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_3.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_3.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {

                    sum_q+=1;
                    if (!checked_quantity_type(Text_q_type_3.getText().toString().trim())){
                        check_impot = AlertDialog_show(Text_q_type_3.getEditableText().toString());
                    }else {check_impot = true;}
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_4.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_4.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_4.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {

                    sum_q+=1;
                    if (!checked_quantity_type(Text_q_type_4.getText().toString().trim())){
                        check_impot = AlertDialog_show(Text_q_type_4.getEditableText().toString());
                    }else {check_impot = true;}
                }
            }

            else if(TextUtils.isEmpty(Text_q_type.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())&&
                    TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())) {
                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                check_impot = false;
            }

            return check_impot;
        }

        ////////////////////n       اشعار تنبية اذا لم يكن نوع الكمية مخزون من قبل ويريد اضافتة
        boolean b=false;
        public boolean AlertDialog_show(String new_type){
            b=false;
            Databases databases = new Databases(getActivity());
            androidx.appcompat.app.AlertDialog.Builder bu = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            bu.setMessage("هل تريد حفظ اسم الكمية الجديدة"+new_type)
                    .setTitle(R.string.eree)
                    .setPositiveButton("نعم", (dialog, id) -> {
                        databases.insert_new_quantity_type(new_type);
                        b=true;
                    })
                    .setNegativeButton("لا", (dialog, id) ->{
                        b=false;
                    }).show();
            return b;
        }
        //////////////////n         عملية التاكد بان اسم الكمية مسجل من قبل
        public boolean checked_quantity_type(String s){
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            int number;
            boolean cheack;

            Databases databases = new Databases(getActivity());

            number=databases.get_check_quantity_type(s);

            cheack= number > 0;

            return cheack;
        }


    }
}

/*
String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }


 */