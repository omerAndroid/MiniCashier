package com.aoa.mini_cashier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.widget.Toast.makeText;

import com.aoa.mini_cashier.Class_Adupter.ListAdupter_quantity;
import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

   public Databases databases = new Databases(this);
    boolean check_impot,check_add;
    String data_type;
    ZXingScannerView scannerView;
    public  String old_baracod;
    int date_place = 0;
    @SuppressLint("StaticFieldLeak")
    public static EditText  Text_name_goods, Text_quantity,Text_quantity_box,Text_date_ex, Text_date_sale;///Text_barcode,
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView Text_barcode;
    @SuppressLint("StaticFieldLeak")
    public static Button add_tg_btn, date_sale_btn, date_ex_btn,save_add_goods,ubdate_btn,seve_ubdat_goods_btn,clear_all;
    private Dialog Date_Dialog;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    private final int intent_key=1;

    public String boolen_key;


    dialog_view_addtypes dva = new dialog_view_addtypes();

    ArrayList<list_item_qnuatitytype> q_list =new ArrayList<list_item_qnuatitytype>();
   public static ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_add_goods_db);
        //////////////////////////////////////////////////////////////////
        add_tg_btn = findViewById(R.id.add_tg_btn);
        date_sale_btn = findViewById(R.id.date_show_sale);
        date_ex_btn = findViewById(R.id.date_show_ex);
        save_add_goods= findViewById(R.id.save_add_goods);
        clear_all= findViewById(R.id.clear_all);
        ubdate_btn= findViewById(R.id.ubdate_btn);
        seve_ubdat_goods_btn= findViewById(R.id.seve_ubdat_goods_btn);

        /////////////////////////////////////////////////////////////////googs + quantity
        Text_barcode = (AutoCompleteTextView)findViewById(R.id.add_barcode_txt);

        Text_name_goods = findViewById(R.id.add_name_goods);
        Text_quantity = findViewById(R.id.add_quantity);
        Text_quantity_box= findViewById(R.id.add_quantity_box);
        Text_date_ex = findViewById(R.id.add_date_ex);////الانتهاء
        Text_date_sale = findViewById(R.id.add_date_sale);
        //////////////////////////b الادخال في مصفوفة الاختيارات للباركود/////////////////////////////////////////////
        get_ALL_baracode();
        /////////////////////////Date Picker///////////////////////////////////
        calendar = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

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


        //////////////////////////////Add List Item  for Camar//////////////////////////////////////set_true_ForList

        //////////////////////////////////////////////////////////////////////////////////

        add_tg_btn.setOnClickListener(v -> {
            //shaw quantity adder
            if(get_seve_goods()||check_add) {//||ubdate_btn.getText().toString().equals("تعديل")||seve_ubdat_goods_btn.getText().toString().equals("حفظ التعديل")
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
                de_Modification();});

        ///////n الضغط على عملية حفظ التعديل
        seve_ubdat_goods_btn.setOnClickListener(v -> {
            if (check_impot_googs()){
                Modification();
                seve_ubdate_googs(old_baracod);
                get_ALL_baracode();


                //m   إضافة الى ليستة الكميات
                q_list = new ArrayList<list_item_qnuatitytype>();
                listShow_qnuatitytype();

            }
        });

        clear_all.setOnClickListener(v -> {
            Text_barcode.setText("");
            Text_name_goods.setText("");
            Text_quantity.setText("");
            Text_quantity_box.setText("");
            Text_date_ex.setText("");
            Text_date_sale.setText("");

            clear_all.setVisibility(View.GONE);
            save_add_goods.setVisibility(View.VISIBLE);///visible      ظاهر
            ubdate_btn.setVisibility(View.GONE);///visible      ظاهر
            seve_ubdat_goods_btn.setVisibility(View.GONE);///visible      ظاهر
            Text_barcode.setEnabled(true);
            Text_name_goods.setEnabled(true);
            Text_quantity.setEnabled(true);
            date_sale_btn.setEnabled(true);
            date_ex_btn.setEnabled(true);
            Text_quantity_box.setEnabled(true);
        });


    }

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

        String[] All_goods=databases.get_All_goods_for_barcod(item);
            Text_name_goods.setText(All_goods[1]);
            Text_quantity.setText(All_goods[2]);
            Text_quantity_box.setText(All_goods[0]);
            Text_date_ex.setText(All_goods[3]);
            Text_date_sale.setText(All_goods[4]);
        
    }

    public void red_qr(View view) {
        Intent intent=new Intent(this,ScanCodeActivity.class);
        intent.putExtra("page","add_goods_db");
        //startActivity(intent);
        startActivityForResult(intent,intent_key);

    }

    public void seve_goods(View view) {
        get_seve_goods(); }

    private void seve_ubdate_googs(String old_baracod) {
        int check_baracod = databases.check_baracod(old_baracod);

        if (check_baracod >0) {
            boolean result = databases.get_seve_ubdate_googs(Text_barcode.getText().toString().trim(),Double.parseDouble(Text_quantity_box.getEditableText().toString()),
                    Text_name_goods.getEditableText().toString(),
                    Double.parseDouble(Text_quantity.getEditableText().toString()),
                    Text_date_ex.getEditableText().toString(),
                    Text_date_sale.getEditableText().toString(),old_baracod);

            if (result) {

                Toast.makeText(this, "OK ubdate  ok", Toast.LENGTH_SHORT).show();
                Modification();/////v  تعديل بعد عملية التعديل


            } else {

                Toast.makeText(this, "No ubdate", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean get_seve_goods() {

        check_add=false;
        boolean check;

        if (check_impot_googs()) {

            int check_baracod = databases.check_baracod(Text_barcode.getText().toString().trim());

            if (check_baracod == 0) {

                boolean result = databases.insert_goods(Text_barcode.getText().toString().trim(),Double.parseDouble(Text_quantity_box.getEditableText().toString()),
                        Text_name_goods.getEditableText().toString(),
                        Double.parseDouble(Text_quantity.getEditableText().toString()),
                        Text_date_ex.getEditableText().toString(),
                        Text_date_sale.getEditableText().toString());

                if (result) {
                    check_add=true;
                    check = true;
                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
                    Modification();/////v  تعديل بعد عملية الادخال
                    get_ALL_baracode();
                    //m   إضافة الى ليستة الكميات
                    q_list = new ArrayList<list_item_qnuatitytype>();
                    listShow_qnuatitytype();

                } else {

                    check = false;
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
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

        save_add_goods.setVisibility(View.GONE);///visible      ظاهر
        ubdate_btn.setVisibility(View.VISIBLE);///visible      ظاهر
        seve_ubdat_goods_btn.setVisibility(View.GONE);///visible      ظاهر
        Text_barcode.setEnabled(false);
        Text_name_goods.setEnabled(false);
        Text_quantity.setEnabled(false);
        date_sale_btn.setEnabled(false);
        date_ex_btn.setEnabled(false);
        Text_quantity_box.setEnabled(false);
        clear_all.setVisibility(View.VISIBLE);

    }
    ////// n فك التعديل
    public void de_Modification(){
        clear_all.setVisibility(View.GONE);

        save_add_goods.setVisibility(View.GONE);///visible      ظاهر
        ubdate_btn.setVisibility(View.GONE);///visible      ظاهر
        seve_ubdat_goods_btn.setVisibility(View.VISIBLE);///visible      ظاهر
        Text_barcode.setEnabled(true);
        Text_name_goods.setEnabled(true);
        Text_quantity.setEnabled(true);
        date_sale_btn.setEnabled(true);
        date_ex_btn.setEnabled(true);
        Text_quantity_box.setEnabled(true);
    }

    private boolean check_impot_googs() {
        if (TextUtils.isEmpty(Text_barcode.getText().toString().trim()) || TextUtils.isEmpty(Text_name_goods.getText().toString().trim())
                || TextUtils.isEmpty(Text_quantity.getText().toString().trim())|| TextUtils.isEmpty(Text_date_ex.getText().toString().trim())
                || TextUtils.isEmpty(Text_date_sale.getText().toString().trim())||TextUtils.isEmpty(Text_quantity_box.getText().toString().trim())) {

            //mEmail.setError("Email is Required.");

            Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
            // return;
            check_impot = false;
        } else {
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

        int i=0;
        for (int j=0;j<a;j++){

            q_list.add(new list_item_qnuatitytype(quantity[i],quantity[1+i],quantity[2+i],quantity[3+i]));

          i+=3;
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
                    q_list = new ArrayList<list_item_qnuatitytype>();
                    listShow_qnuatitytype();
                }
            }
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