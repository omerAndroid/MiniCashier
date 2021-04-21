package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;
import static android.widget.Toast.makeText;

import com.aoa.mini_cashier.Class_Adupter.ListAdupter_quantity;
import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

   public Databases databases = new Databases(this);
   public static boolean  check_impot,check_add ,ceack_save=false,ceack_ubdate=false,ceack_seve_ubdat=false,ceack_save_quantity=false,ceack_save_ALL=false;
    String data_type;
    ZXingScannerView scannerView;
    public  String old_baracod,old_baracod_2;
    int date_place = 0;
    @SuppressLint("StaticFieldLeak")
    public static EditText  Text_name_goods,Text_quantity,Text_extra_quantity,Text_date_ex,Text_date_sale;///
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView Text_barcode;
    @SuppressLint("StaticFieldLeak")
    public static Button add_tg_btn, date_sale_btn, date_ex_btn,save_add_goods,ubdate_btn,seve_ubdat_goods_btn,clear_all,dileg_q;
    private Dialog Date_Dialog;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    private final int intent_key=1;

    Dialog customer_data;
    public static String boolen_key,department_item="";
   @SuppressLint("StaticFieldLeak")
   public static com.jaredrummler.materialspinner.MaterialSpinner spinner;

    dialog_view_addtypes dva = new dialog_view_addtypes();

   public static ArrayList<list_item_qnuatitytype> q_list = new ArrayList<>();


   ///////n        له علاقة بكلاس الدايلق


   public static double quantity_stored=1.0,quantity_stored_2=1.0;

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
        Text_barcode = findViewById(R.id.add_barcode_txt);
        Text_name_goods = findViewById(R.id.add_name_goods);
        Text_quantity = findViewById(R.id.add_quantity);
        Text_extra_quantity= findViewById(R.id.add_extra_quantity);
        Text_date_ex = findViewById(R.id.add_date_ex);////الانتهاء
        Text_date_sale = findViewById(R.id.add_date_sale);
        spinner = findViewById(R.id.spinner);

        dileg_q = findViewById(R.id.dileg_q);

        //////////////////////////b   الادخال في مصفوفة الاختيارات للباركود  /////////////////////////////////////////////

        get_ALL_baracode();
        get_ALL_department();
        Text_extra_quantity.setText("0");
        department_item="";
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
                Modification();
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

        dileg_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_dileg_q();

            }
        });


        /////////////////////////////////n حفظ الكميات لاول مره فقط /////////////////////////////////////////////



         //////////////n          عند الضغط على الاقسام
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) ->{
            department_item=item;
            //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
        });

        //////////////////////////////////////////////////////////////////////////////////department
        counter counter=new counter(3800,120);
        add_tg_btn.setOnClickListener(v -> {
            if (add_tg_btn.isEnabled()){
                add_tg_btn.setEnabled(false);
                counter.start();
            }
            //shaw quantity adder
            if(get_seve_fast()||check_add&&!department_item.equals("الاقسام")) {
                if (!Text_barcode.getText().toString().trim().isEmpty()){
                    ceack_save_ALL=true;
                    dva.show(getSupportFragmentManager(), "إضافة نوع");

                }else {
                    add_tg_btn.setEnabled(true);
                    Toast.makeText(add_goods_db.this, "أدخل الباركود", Toast.LENGTH_SHORT).show();
                }
             }else add_tg_btn.setEnabled(true);
        });

        ///////n الضغط على عملية التعديل
        ubdate_btn.setOnClickListener(v -> {
                old_baracod=Text_barcode.getText().toString().trim();
                de_Modification();
            ceack_ubdate=true;
            ceack_save_ALL=true;
            add_tg_btn.setEnabled(true);
            dva.check_work=true;///////n  كلاس الكمية

        });

        ///////n الضغط على عملية حفظ التعديل
        seve_ubdat_goods_btn.setOnClickListener(v ->
                get_seve_ubdat_goods_btn());

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
         //   Text_quantity.setEnabled(true);
            date_sale_btn.setEnabled(true);
            date_ex_btn.setEnabled(true);
            add_tg_btn.setEnabled(true);
          //  Text_extra_quantity.setEnabled(true);
            spinner.setEnabled(true);
            department_item="";
            listShow_qnuatitytype();
        });

    }

    //////n     اضافة كمية
    private void get_dileg_q() {

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.agent_dialog);
        customer_data.setTitle("بيانات الكمية");
        final EditText add_extra_quantity_2 =customer_data.findViewById(R.id.add_extra_quantity_2);
        final EditText add_quantity_2 =customer_data.findViewById(R.id.add_quantity_2);
        final Button save_quantity_2 =customer_data.findViewById(R.id.save_quantity_2);

        save_quantity_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save Data of customer

                quantity_stored_2= Double.parseDouble(add_extra_quantity_2.getText().toString() +"d");
                quantity_stored_2+= Double.parseDouble(add_quantity_2.getText().toString() +"d");



                customer_data.dismiss();
            }
        });
        customer_data.show();
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
             ceack_save_ALL=false;
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

        AutoCompleteTextView Text_barcode =
                findViewById(R.id.add_barcode_txt);

        Text_barcode.setAdapter(adapter);
        Text_barcode.setThreshold(1);//will start working from first

        Text_barcode.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            //Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات
            Packing_for_goods(item);

        });
        
    }
    //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات
    private void Packing_for_goods(String item) {
        Modification();
        add_tg_btn.setEnabled(false);
        String[] All_goods=databases.get_All_goods_for_barcod(item);
        double[] All_goods_double=databases.get_All_goods_for_barcod_Double(item);
            Text_name_goods.setText(All_goods[0]);
            Text_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format( All_goods_double[0])));
            Text_date_ex.setText(All_goods[1]);
            Text_date_sale.setText(All_goods[2]);
            spinner.setText(All_goods[3]);

           department_item=All_goods[3];

                listShow_qnuatitytype();
        
    }
    /////////////n      للتحويل الرقم من دبل الى انتجر
    public String To_int(@NonNull String s){
        String[] parts = s.split("\\.");
        String part1 ,v  ;
        if (s.contains(".")) {

            part1 = parts[0];
            v=part1;
        }else {
            v=s;
        }
        return v;
    }
    /////////////////n     خوارزمية تساعد لعملية عرض وادخال الارقام
    public String theack_aggen(@NonNull String s){
        StringBuilder ss= new StringBuilder();

        boolean b=false;
        for (int i = 0; i<= s.length()-1; i++){
            if (String.valueOf(s.charAt(i)).equals("٠")){
                ss.append("0");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٩")){
                ss.append("9");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("١")){
                ss.append("1");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٢")){
                ss.append("2");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٣")){
                ss.append("3");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٤")){
                ss.append("4");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٥")){
                ss.append("5");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                ss.append("6");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٧")){
                ss.append("7");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٨")){
                ss.append("8");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٫")){
                ss.append(".");
                b=true;
            }
        }

        if (b){
            return ss.toString();
        }else {
            return s;
        }

    }
    //////////////n        عند الضغط على الكاميرا ينتقل مبارشرة الى كلاس الكاميره
    public void red_qr(View view) {
        Intent intent=new Intent(this,ScanCodeActivity.class);
        intent.putExtra("page","add_goods_db");
        //startActivity(intent);
        startActivityForResult(intent,intent_key);

    }

    public void seve_goods(View view) {
        Text_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format(quantity_stored*quantity_stored_2)));
        get_seve_goods(); }
      //////////n        يقوم بحفظ التعديلات
    private boolean seve_ubdate_googs(String old_baracod) {
        boolean check=false;
        int check_baracod = databases.check_baracod(old_baracod);///department_item   + Double.parseDouble(Text_extra_quantity.getText().toString() )

        int id_d=databases.get_id_department(department_item);

        if (check_baracod >0) {
            boolean result = databases.get_seve_ubdate_googs(Text_barcode.getText().toString().trim(),///Float.parseFloat(Text_quantity.getText().toString() + "f" ) + Float.parseFloat(Text_extra_quantity.getText().toString() + "f")
                    Text_name_goods.getEditableText().toString(),
                    Double.parseDouble(Text_quantity.getText().toString()) + Double.parseDouble(Text_extra_quantity.getText().toString() ),
                    Text_date_ex.getEditableText().toString(),
                    Text_date_sale.getEditableText().toString(),id_d,old_baracod);

            if (result) {
                   check=true;
                //Toast.makeText(this, "OK ubdate  ok", Toast.LENGTH_SHORT).show();
                Modification();/////v  تعديل بعد عملية التعديل


            } else {
                check=false;
               // Toast.makeText(this, "No ubdate", Toast.LENGTH_SHORT).show();
            }
        }
        return check;
    }

    public void get_seve_goods() {

        check_add=false;
       // boolean check;

        if (check_impot_googs()) {

            int check_baracod = databases.check_baracod(old_baracod_2);//old_baracod_2

            if (check_baracod >= 0) {

                int id = databases.get_id_goods(old_baracod_2);//old_baracod_2
                int a = databases.read_Tname_q_type(id);

                if (a>0){

                    boolean result = seve_ubdate_googs(old_baracod_2);

                    if (result) {
                        ceack_save_ALL=false;
                        check_add=true;
                       // Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
                        Modification();/////v  تعديل بعد عملية الادخال
                        get_ALL_baracode();
                        add_tg_btn.setEnabled(false);
                        //m   إضافة الى ليستة الكميات
                        q_list = new ArrayList<>();
                        listShow_qnuatitytype();

                        ceack_save=true;

                    } else {

                      //  Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this, "أدخل كمية للبضاعة", Toast.LENGTH_SHORT).show();
                }
            } else {
                check_add=true;
            }

        }

        //return check;
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
                            Double.parseDouble(Text_quantity.getText().toString() ),
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
                        //Toast.makeText(this, "يوجد باركود مشابة أو اسم منتج مشابه", Toast.LENGTH_SHORT).show();
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
       // Text_quantity.setEnabled(false);
       // Text_extra_quantity.setEnabled(false);
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
     //   Text_quantity.setEnabled(true);
        date_sale_btn.setEnabled(true);
        date_ex_btn.setEnabled(true);
        Text_extra_quantity.setEnabled(true);
        spinner.setEnabled(true);
    }

    private boolean check_impot_googs() {

        if (TextUtils.isEmpty(Text_barcode.getText().toString().trim()) || TextUtils.isEmpty(Text_name_goods.getText().toString().trim())
                || TextUtils.isEmpty(Text_quantity.getText().toString().trim())|| TextUtils.isEmpty(Text_extra_quantity.getText().toString().trim())
                || TextUtils.isEmpty(Text_date_sale.getText().toString().trim())|| TextUtils.isEmpty(Text_date_ex.getText().toString().trim())||
                TextUtils.isEmpty(department_item)|| department_item.equals("الاقسام")) {

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
        //Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();///get_ALLq_qnuatity_Double
        String[] quantity=databases.get_ALLq_qnuatity(id);
        double[] quantity_Double=databases.get_ALLq_qnuatity_Double(id);

       // makeText(this, String.valueOf(g.length), Toast.LENGTH_SHORT).show();
        q_list.clear();
        int i=0,g=0;
        for (int j=0;j<a;j++){

            q_list.add(new list_item_qnuatitytype(quantity[i],MessageFormat.format("{0}", quantity_Double[g]),
                    MessageFormat.format("{0}", quantity_Double[g+1]),MessageFormat.format("{0}", quantity_Double[g+2])));
          i+=1;
          g+=3;
        }


        //////////////////////////////Add List Item//////////////////////////////////////
         list = findViewById(R.id.list_quantity);

        ListAdupter_quantity ad = new ListAdupter_quantity(this,q_list);
        list.setAdapter(ad);


    }

    public void date_picker () {
            //Dialog Date viewer
            Date_Dialog = new Dialog(this);
            Date_Dialog.setContentView(R.layout.dailog_date);
            final DatePicker picker =  Date_Dialog.findViewById(R.id.calendar_View);
            Button date_viewer =  Date_Dialog.findViewById(R.id.date_veiwer);

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
                assert data != null;
                boolen_key=data.getStringExtra("page");

               // makeText(this, boolen_key, Toast.LENGTH_SHORT).show();
                if (boolen_key.equals("true")){
                    q_list = new ArrayList<>();
                    add_tg_btn.setEnabled(false);
                    department_item=spinner.getText().toString();
                    listShow_qnuatitytype();
                }
            }
        }
    }

    ///////////////////////n       عملية الرجوع
    @Override
     public boolean onKeyDown(int keyCode, KeyEvent event){/////old_baracod_2
        if ((keyCode==KeyEvent.KEYCODE_BACK)){

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
                            .setNegativeButton("لا", (dialog, id) -> finish()).show();
                }else {
                    finish();
                }

            }else if (!ceack_save_ALL){
                finish();
            }else {
                makeText(getApplication(), "قم بعملية الحفظ اولا", Toast.LENGTH_SHORT).show();
            }

            ceack_save=false;
            ceack_ubdate=false;
            ceack_seve_ubdat=false;
            ceack_save_quantity=false;
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }



    ////////n     dialog_view_addtypes
    public static class dialog_view_addtypes extends AppCompatDialogFragment {

        @SuppressLint("StaticFieldLeak")
        public  EditText Text_q_type, Text_q_buy_price, Text_q_sale_price,Text_q_type_2,
                Text_q_quantity_2,Text_q_buy_price_2,Text_q_sale_price_2,Text_q_type_3,Text_q_quantity_3,
                Text_q_buy_price_3,Text_q_sale_price_3,Text_q_type_4,Text_q_quantity_4,Text_q_buy_price_4,Text_q_sale_price_4;
        //public  String name_type,quantity,buy_price,sale_price;
        RadioGroup radioGroup;
        public  boolean check_work=false,check_insert_Data=false,isChecked_1=false,isChecked_2=false,isChecked_3=false,isChecked_4=false;
        Button  save,clear_1,clear_2,clear_3,clear_4;
       // int id_quantity;
      //  public  String old_q_type,add_q_type;
        RadioButton primary_type,primary_type_2,primary_type_3,primary_type_4;
        androidx.appcompat.app.AlertDialog.Builder builder;
        boolean check_impot;

        RadioButton radioButton;

        int sum_q;

        int size_impout;

        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

                builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                Databases databases = new Databases(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.types_goods, null);
                /////////////////////////////////////////////////////////////////quantity -> dialog
                save = view.findViewById(R.id.save_tg_add);
                clear_1 = view.findViewById(R.id.clear_1);
                clear_2 = view.findViewById(R.id.clear_2);
                clear_3 = view.findViewById(R.id.clear_3);
                clear_4 = view.findViewById(R.id.clear_4);


               primary_type=view.findViewById(R.id.primary_type);
                Text_q_type=view.findViewById(R.id.q_type);
                Text_q_buy_price=view.findViewById(R.id.q_buy_price);
                Text_q_sale_price=view.findViewById(R.id.q_sale_price);

                primary_type_2=view.findViewById(R.id.primary_type_2);
                Text_q_type_2=view.findViewById(R.id.q_type_2);
                Text_q_quantity_2=view.findViewById(R.id.q_quantity_2);
                Text_q_buy_price_2=view.findViewById(R.id.q_buy_price_2);
                Text_q_sale_price_2=view.findViewById(R.id.q_sale_price_2);

                primary_type_3=view.findViewById(R.id.primary_type_3);
                Text_q_type_3=view.findViewById(R.id.q_type_3);
                Text_q_quantity_3=view.findViewById(R.id.q_quantity_3);
                Text_q_buy_price_3=view.findViewById(R.id.q_buy_price_3);
                Text_q_sale_price_3=view.findViewById(R.id.q_sale_price_3);

                primary_type_4=view.findViewById(R.id.primary_type_4);
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

               ///////////////////////////////////////////////////////////////////////////////////////////////

                if(check_work) {
                    insert_into_qnuatity();
                    setVisibility_clear();
                    save.setText("حفظ التعديل");
                }else {
                    save.setText("حفظ");
                }

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
                                    if (!return_cheeked_radio()) {
                                        Toast.makeText(getActivity(), "حدد احد الخيارات التي أدخلتها ", Toast.LENGTH_SHORT).show();
                                    }else {

                                        boolean result = insert_Data();
                                        if (result) {
                                            /////////m     يقوم بادخال الكمية المخزونة
                                            //databases.insert_fast_to_quantity_stored_in_goods(quantity_stored,Text_barcode.getText().toString().trim());
                                            /////////m     يقوم لارسال القيمة الى العرض

                                           // quantity_stored = quantity_stored * Double.parseDouble(Text_quantity.getText().toString());
                                           // quantity_stored += Double.parseDouble(Text_extra_quantity.getText().toString());
                                           // Text_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format(quantity_stored)));

                                            ceack_save_quantity = true;
                                            dismiss();
                                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }
                            else
                            {
                                if (!return_cheeked_radio()) {
                                        Toast.makeText(getActivity(), "حدد احد الخيارات التي أدخلتها ", Toast.LENGTH_SHORT).show();
                                }else {
                                    /////////////n     يقوم بحذف الكميات القديمة ويضيف الجديدة
                                    int idd = databases.get_id_goods(Text_barcode.getText().toString().trim());
                                    String s=String.valueOf(idd);
                                    databases.get_delete_quantity(s);

                                        boolean result = insert_Data();

                                        if (result) {

                                            /////////m     يقوم لارسال القيمة الى العرض
//                                            quantity_stored = quantity_stored * Double.parseDouble(Text_quantity.getText().toString() +"d");
//                                            quantity_stored += Double.parseDouble(Text_extra_quantity.getText().toString() +"d");
                                            //Text_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format(quantity_stored)));

                                            ceack_save_quantity = true;
                                            dismiss();
                                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            }
                        }//
                    }

                });

                clear_1.setOnClickListener(v -> opreshen_in_clear(1));
                clear_2.setOnClickListener(v -> opreshen_in_clear(2));
                clear_3.setOnClickListener(v -> opreshen_in_clear(3));
                clear_4.setOnClickListener(v -> opreshen_in_clear(4));

            builder.setView(view).setTitle("إضافة نوع");

            return builder.create();
        }

        public void setVisibility_clear(){
            clear_1.setVisibility(View.VISIBLE);
            clear_2.setVisibility(View.VISIBLE);
            clear_3.setVisibility(View.VISIBLE);
            clear_4.setVisibility(View.VISIBLE);
        }
        /////////n     عملية الادخال في قتعدة البيانات
        public boolean insert_Data(){
            Databases databases = new Databases(getActivity());
            int id_g = databases.get_id_goods(Text_barcode.getText().toString().trim());
            int id_q,number_type;
            check_insert_Data=false;
            quantity_stored =1.0;
            if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())){
                number_type=1;
                id_q=databases.get_id_quantity_type(Text_q_type.getEditableText().toString());

                check_insert_Data= databases.insert_quantity(number_type,1,isChecked_1,Double.parseDouble(Text_q_buy_price.getText().toString()  ),
                        Double.parseDouble(Text_q_sale_price.getText().toString()  ),id_q,id_g);

                quantity_stored *=1;
            }
            if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                number_type=2;
                id_q=databases.get_id_quantity_type(Text_q_type_2.getEditableText().toString());

                check_insert_Data=databases.insert_quantity(number_type,Integer.parseInt(To_int(Text_q_quantity_2.getText().toString())),isChecked_2,
                        Double.parseDouble(Text_q_buy_price_2.getText().toString()), Double.parseDouble(Text_q_sale_price_2.getText().toString() ),id_q,id_g);

                quantity_stored *=Double.parseDouble(Text_q_quantity_2.getText().toString());
            }
            if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                number_type=3;
                id_q=databases.get_id_quantity_type(Text_q_type_3.getEditableText().toString());
                check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(To_int(Text_q_quantity_3.getText().toString())),isChecked_3,
                        Double.parseDouble(Text_q_buy_price_3.getText().toString()  ), Double.parseDouble(Text_q_sale_price_3.getText().toString()),id_q,id_g);

                quantity_stored *=Double.parseDouble(Text_q_quantity_3.getText().toString());
            }
            if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                number_type=4;
                id_q=databases.get_id_quantity_type(Text_q_type_4.getEditableText().toString());
                check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(To_int(Text_q_quantity_4.getText().toString())),isChecked_4,
                        Double.parseDouble(Text_q_buy_price_4.getText().toString()  ), Double.parseDouble(Text_q_sale_price_4.getText().toString()),id_q,id_g);

                quantity_stored *=Double.parseDouble(Text_q_quantity_4.getText().toString());
            }
            return check_insert_Data;
        }


        ////////////n للتحقق من المدخلات
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
                    .setNegativeButton("لا", (dialog, id) -> b=false).show();
            return b;
        }
        //////////////////n         عملية التاكد بان اسم الكمية مسجل من قبل
        public boolean checked_quantity_type(String s){
            //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            int number;
            boolean cheack;

            Databases databases = new Databases(getActivity());

            number=databases.get_check_quantity_type(s);

            cheack= number > 0;

            return cheack;
        }
         /////////n     للتحقق انه ضغط راديوا فية بيانات
        public boolean return_cheeked_radio(){
             b=false;
            if (primary_type.isChecked()&&!TextUtils.isEmpty(Text_q_type.getEditableText().toString())){
                b=true;
            }else if (primary_type_2.isChecked()&&!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                b=true;
            }else if (primary_type_3.isChecked()&&!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                b=true;
            }else if (primary_type_4.isChecked()&&!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                b=true;
            }

//            Toast.makeText(getContext(), String.valueOf(b), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), String.valueOf(!TextUtils.isEmpty(Text_q_type.getEditableText().toString())), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), String.valueOf(isChecked_1), Toast.LENGTH_SHORT).show();

            return b;
        }


        /////////////////n     خوارزمية تساعد لعملية عرض وادخال الارقام
        public String To_double(String s){

            StringBuilder ss= new StringBuilder();
            String nu=s;
            for (int i=0;i<=nu.length()-1;i++){
                if (String.valueOf(nu.charAt(i)).equals("٠")){
                    ss.append("0");
                }else if(String.valueOf(nu.charAt(i)).equals("٩")){
                    ss.append("9");
                }else if(String.valueOf(nu.charAt(i)).equals("١")){
                    ss.append("1");
                }else if(String.valueOf(nu.charAt(i)).equals("٢")){
                    ss.append("2");
                }else if(String.valueOf(nu.charAt(i)).equals("٣")){
                    ss.append("3");
                }else if(String.valueOf(nu.charAt(i)).equals("٤")){
                    ss.append("4");
                }else if(String.valueOf(nu.charAt(i)).equals("٥")){
                    ss.append("5");
                }else if(String.valueOf(nu.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                    ss.append("6");
                }else if(String.valueOf(nu.charAt(i)).equals("٧")){
                    ss.append("7");
                }else if(String.valueOf(nu.charAt(i)).equals("٨")){
                    ss.append("8");
                }else if(String.valueOf(nu.charAt(i)).equals("٫")){
                    ss.append(".");
                }
            }
            s= ss.toString();
            String[] parts = s.split("\\.");
            String part1 ,v  ;   //
            String part2 ;   //
            StringBuilder text_1= new StringBuilder();
            StringBuilder text_2= new StringBuilder(".");///////////////////************************
            double d1,d2;
            DecimalFormat df ;
            if (s.contains(".")) {

                part1 = parts[0];
                part2 = parts[1];

                int size1=part1.length();
                int size2=part2.length();

                part2 ="0.";
                part2 += parts[1];

                for (int i=1;i<=size2;i++){
                    text_2.append("0");
                }

                for (int i=1;i<=size1;i++){
                    text_1.append("0");
                }

                String arr;
                arr=parts[1];

                if (String.valueOf(arr.charAt(0)).equals("0")&&String.valueOf(arr.charAt(1)).equals("0")){
                    text_2= new StringBuilder(".");
                    text_2.append("0");
                }

                d1=Double.parseDouble(part1);
                d2=Double.parseDouble(part2);

                d2=d1+d2;

                String bb= text_1.toString() +text_2;

                System.out.println("this is before formatting: "+d2);
                df = new DecimalFormat(bb);

                System.out.println("Value: " + df.format(d2));
                v=df.format(d2);

            }else {
                String par=s+".0";
                v= To_double(par);
            }
            return v ;
        }
        public String theack_aggen(String s){
            StringBuilder ss= new StringBuilder();

            boolean b=false;
            for (int i = 0; i<= s.length()-1; i++){
                if (String.valueOf(s.charAt(i)).equals("٠")){
                    ss.append("0");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٩")){
                    ss.append("9");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("١")){
                    ss.append("1");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٢")){
                    ss.append("2");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٣")){
                    ss.append("3");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٤")){
                    ss.append("4");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٥")){
                    ss.append("5");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                    ss.append("6");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٧")){
                    ss.append("7");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٨")){
                    ss.append("8");
                    b=true;
                }else if(String.valueOf(s.charAt(i)).equals("٫")){
                    ss.append(".");
                    b=true;
                }
            }

            if (b){
                return ss.toString();
            }else {
                return s;
            }

        }
        /////////////n      للتحويل الرقم من دبل الى انتجر
        public String To_int(String s){
            String[] parts = s.split("\\.");
            String part1 ,v  ;
            if (s.contains(".")) {

                part1 = parts[0];
                v=part1;
            }else {
                v=s;
            }
            return v;
        }
        /////////////////n     التعبئة في حقول الكمية
        private void insert_into_qnuatity() {

            Databases databases = new Databases(getActivity());
            int id = databases.get_id_goods(Text_barcode.getText().toString().trim());
            int a=databases.read_Tname_q_type(id);
            String[] quantity=databases.get_ALLq_qnuatity(id);
            double[] quantity_Double=databases.get_ALLq_qnuatity_Double(id);////MessageFormat.format("{0}", quantity_Double[g]

            ////new DecimalFormat("#.000#").format(9999999999.123)

            int i=1;
            for (int j=0;j<a;j++){////   String.format("%.3f", quantity_Double[2])
                if (i==1){
                    Text_q_type.setText(quantity[0]);
                    Text_q_buy_price.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[1])));
                    Text_q_sale_price.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[2])));
                    set_rideou_cheack(1,quantity[0]);
                    i+=1;
                }else if (i==2){
                    Text_q_type_2.setText(quantity[1]);
                    Text_q_quantity_2.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[3])));
                    Text_q_buy_price_2.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[4])));
                    Text_q_sale_price_2.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[5])));
                    set_rideou_cheack(2,quantity[1]);
                    i+=1;
                }else if (i==3){
                    Text_q_type_3.setText(quantity[2]);
                    Text_q_quantity_3.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[6])));
                    Text_q_buy_price_3.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[7])));
                    Text_q_sale_price_3.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[8])));
                    set_rideou_cheack(3,quantity[2]);
                    i+=1;
                }else if (i==4){
                    Text_q_type_4.setText(quantity[3]);
                    Text_q_quantity_4.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[9])));
                    Text_q_buy_price_4.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[10])));
                    Text_q_sale_price_4.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[11])));
                    set_rideou_cheack(4,quantity[3]);
                }

            }
        }
        ////////////////n     عند الضغط على عملية الحذف
        public void opreshen_in_clear(int i){
            if (i==1){
                Text_q_type.setText("");
                Text_q_buy_price.setText("");
                Text_q_sale_price.setText("");
                if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                    Text_q_type.setText(Text_q_type_2.getText().toString());
                    Text_q_buy_price.setText(Text_q_buy_price_2.getText().toString());
                    Text_q_sale_price.setText(Text_q_sale_price_2.getText().toString());
                    Text_q_type_2.setText("");
                    Text_q_quantity_2.setText("");
                    Text_q_buy_price_2.setText("");
                    Text_q_sale_price_2.setText("");
                }
            }
            if (i==2){
                Text_q_type_2.setText("");
                Text_q_quantity_2.setText("");
                Text_q_buy_price_2.setText("");
                Text_q_sale_price_2.setText("");
                if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                    Text_q_type_2.setText(Text_q_type_3.getText().toString());
                    Text_q_quantity_2.setText(Text_q_quantity_3.getText().toString());
                    Text_q_buy_price_2.setText(Text_q_buy_price_3.getText().toString());
                    Text_q_sale_price_2.setText(Text_q_sale_price_3.getText().toString());
                    Text_q_type_3.setText("");
                    Text_q_quantity_3.setText("");
                    Text_q_buy_price_3.setText("");
                    Text_q_sale_price_3.setText("");
                }
            }
            if (i==3){
                Text_q_type_3.setText("");
                Text_q_quantity_3.setText("");
                Text_q_buy_price_3.setText("");
                Text_q_sale_price_3.setText("");
                if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                    Text_q_type_3.setText(Text_q_type_4.getText().toString());
                    Text_q_quantity_3.setText(Text_q_quantity_4.getText().toString());
                    Text_q_buy_price_3.setText(Text_q_buy_price_4.getText().toString());
                    Text_q_sale_price_3.setText(Text_q_sale_price_4.getText().toString());
                    Text_q_type_4.setText("");
                    Text_q_quantity_4.setText("");
                    Text_q_buy_price_4.setText("");
                    Text_q_sale_price_4.setText("");
                }
            }
            if (i==4){
                Text_q_type_4.setText("");
                Text_q_quantity_4.setText("");
                Text_q_buy_price_4.setText("");
                Text_q_sale_price_4.setText("");
            }
        }

        public void set_rideou_cheack(int i,String number_q){
            Databases databases = new Databases(getActivity());
            int id = databases.get_id_goods(Text_barcode.getText().toString().trim());

            int treu=databases.get_retern_cheack(id,number_q);

            if (i==1&&treu==1){
                primary_type.setChecked(true);
                isChecked_1=true;
            }else if (i==2&&treu==1){
                primary_type_2.setChecked(true);
                isChecked_2=true;
            }else if (i==3&&treu==1){
                primary_type_3.setChecked(true);
                isChecked_3=true;
            }else if (i==4&&treu==1){
                primary_type_4.setChecked(true);
                isChecked_4=true;
            }

        }

    }

    //////////////n        كلاس مهمتة اعطى فترة معينة بين كل ضغطة
    public static class counter extends CountDownTimer {


        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            add_tg_btn.setEnabled(true);
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