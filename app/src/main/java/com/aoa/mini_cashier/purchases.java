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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;
import com.aoa.mini_cashier.item_classes.policy_item_class;
import com.aoa.mini_cashier.item_classes.purchases_item_class;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class purchases extends AppCompatActivity {

    purchases.dialog_view_addtypes dva = new purchases.dialog_view_addtypes();

  public static   String[] arr_slery,arr_quantity_type;
    public Databases databases = new Databases(this);
   public static Dialog purchase,Date_Dialog;
    ListView list_purchases;
    Button add_purchases,add_policy,change_list_items;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    //String date_viewe= "asdf";
    TextView phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt,
            purchases_total,paid_total,pu_pa_total;
   public static String data_type,Text_date="0",Text_date_2="0",set_mony="",string_1="null",string_2;
   String s=" ";
    int date_place = 0 ;
    public static ArrayList<purchases_item_class> q_list = new ArrayList<>();

    public static ArrayList<policy_item_class> policy_list = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static Button add_tg_btn;
    public static double quantity_stored=1.0,quantity_stored_2=1.0;
    public static boolean chaeck_seve=false,chaeck_seve_2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        list_purchases= (ListView) findViewById(R.id.list_purchases);
        add_policy =(Button) findViewById(R.id.add_policy);
        add_purchases =(Button) findViewById(R.id.add_purchases);

         phone_resource_txt=findViewById(R.id.phone_resource_txt);
         mobile_resource_txt=findViewById(R.id.mobile_resource_txt);
         address_resource=findViewById(R.id.address_resource);
         name_resource_txt=findViewById(R.id.name_resource_txt);

        purchases_total = findViewById(R.id.purchases_total);
        paid_total = findViewById(R.id.paid_total);
        pu_pa_total = findViewById(R.id.pu_pa_total);

        /////////////////////////Date Picker///////////////////////////////////
        Intent data =getIntent();
        s=data.getExtras().getString("class");
        change_list_items=findViewById(R.id.change_list_items);

        if (s == null){s=" ";}
        if (s.equals("bills")){
            findViewById(R.id.add_purchases).setVisibility(View.GONE);
            findViewById(R.id.phone_resource_txt).setVisibility(View.GONE);
            findViewById(R.id.mobile_resource_txt).setVisibility(View.GONE);
            findViewById(R.id.address_resource).setVisibility(View.GONE);
            findViewById(R.id.name_resource_txt).setVisibility(View.GONE);
            findViewById(R.id.purchases_total).setVisibility(View.GONE);
            findViewById(R.id.paid_total).setVisibility(View.GONE);
            findViewById(R.id.pu_pa_total).setVisibility(View.GONE);
            findViewById(R.id.phone_number).setVisibility(View.GONE);
            findViewById(R.id.mobile_number).setVisibility(View.GONE);
            string_1=data.getExtras().getString("id_bills");
            s=data.getExtras().getString("name_agent");

            listShow_bills_1(string_1);
            findViewById(R.id.change_list_items).setOnClickListener(v -> {
                if (change_list_items.getText().toString().equals("قائمة المشتريات")){
                    change_list_items.setText("قائمة السندات");
                    q_list = new ArrayList<>();
                    ListAdupter ad = new ListAdupter(q_list);
                    list_purchases.setAdapter(ad);
                    listShow_bills_2();
                }else {
                    change_list_items.setText("قائمة المشتريات");
                    policy_list = new ArrayList<>();
                    ListAdupter2 ad = new ListAdupter2(policy_list);
                    list_purchases.setAdapter(ad);
                    listShow_bills_1(string_1);
                }
            });
            add_policy.setOnClickListener(v -> add_policy_data_2());
        }else {

        calendar = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        add_policy.setOnClickListener(v -> add_policy_data());

        add_purchases.setOnClickListener(v -> add_purchases_data());

        /////Intent

        //phone_resource_txt.setText(data.getExtras().getString("phone_resource"));
        //mobile_resource_txt.setText(data.getExtras().getString("mobile_resource"));
        address_resource.setText(data.getExtras().getString("address"));
        name_resource_txt.setText(data.getExtras().getString("name"));



        listShow_policy_2();

        findViewById(R.id.change_list_items).setOnClickListener(v -> {
            if (change_list_items.getText().toString().equals("قائمة المشتريات")){
                change_list_items.setText("قائمة السندات");
                q_list = new ArrayList<>();
                ListAdupter ad = new ListAdupter(q_list);
                list_purchases.setAdapter(ad);
                listShow_policy();
            }else {
                change_list_items.setText("قائمة المشتريات");
                policy_list = new ArrayList<>();
                ListAdupter2 ad = new ListAdupter2(policy_list);
                list_purchases.setAdapter(ad);
                listShow_policy_2();
            }
        });

        show();
        }
    }

    private void listShow_bills_1(String string_1) {
        int a=Integer.parseInt(string_1);

        String[] purchases=databases.get_one_products_bills(a);

        if (databases.get_one_products_bills_num(a)>=1) {
            q_list.clear();
            int i = 0;
            for (int j = 0; j < databases.get_one_products_bills_num(a); j++) {

                q_list.add(new purchases_item_class(purchases[i], purchases[i+1],purchases[i+2],purchases[i+3],purchases[i+4],purchases[i+5],
                        "",""));
                i += 6;

            }
            //////////////////////////////Add List Item//////////////////////////////////////
            purchases.ListAdupter ad = new purchases.ListAdupter(q_list);
            list_purchases.setAdapter(ad);
        }
    }

    private void listShow_bills_2() {

        int id_agent=databases.get_id_agent(s);
        String[] policy=databases.get_All_policy_agent(id_agent);
        findViewById(R.id.purchases_visbility).setVisibility(View.GONE);
        findViewById(R.id.policy_visbility).setVisibility(View.VISIBLE);
        if (databases.get_number_policy_agent(id_agent)>=1) {
            policy_list.clear();
            int i = 0;
            for (int j = 0; j < databases.get_number_policy_agent(id_agent); j++) {

                policy_list.add(new policy_item_class( policy[i + 3], policy[i], policy[i + 1], policy[i + 2]));
                i += 4;
            }
            //////////////////////////////Add List Item//////////////////////////////////////
            purchases.ListAdupter2 ad = new purchases.ListAdupter2(policy_list);
            list_purchases.setAdapter(ad);
        }
    }

    private void show() {
        int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());

        double[] total=databases.get_ALL_total_purchases(id_resource),total2;

        double v =0,vv=0,vvv=0;
        for (int i=0;i<databases.get_number_purchases_2(id_resource);i++){
            v +=total[i];
        }
        purchases_total.setText(MessageFormat.format("{0}",v));

        ////////////////////////////////////
        total=databases.get_ALL_paid_purchases(id_resource);
        total2=databases.get_ALL_type_policy(id_resource);


        for (int i=0;i<databases.get_number_purchases_2(id_resource);i++){
            vvv +=total[i];
        }
        for (int i=0;i<databases.get_number_policy(id_resource);i++){
            vv +=total2[i];
        }

        paid_total.setText(MessageFormat.format("{0}",vvv+vv));

        pu_pa_total.setText(MessageFormat.format("{0}",(vvv+vv)-v));///n  الاجمالي
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        show();
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

            ///////////////////////////////////////////////////////////////
            final Button date_paid=(Button) purchase.findViewById(R.id.date_paid);
            ///////////////////////////////////////////////////////////////
            date_paid.setText(date_format.format(calendar2.getTime()));
            Date_Dialog.dismiss();
        });
        Date_Dialog.show();
    }

    public void date_picker_2 () {
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

            final EditText add_date_sale=(EditText) purchase.findViewById(R.id.add_date_sale);
            final EditText add_date_ex=(EditText) purchase.findViewById(R.id.add_date_ex);

            calendar2.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
            if (date_place == 0) {
                add_date_ex.setText(date_format.format(calendar2.getTime()));
            } else {
                add_date_sale.setText(date_format.format(calendar2.getTime()));
            }
            Date_Dialog.dismiss();
        });
        Date_Dialog.show();
    }

    public void add_purchases_data () {

        //Dialog Customer Data viewer
        purchase = new Dialog(this);
        purchase.setContentView(R.layout.purchases_dialog);
        purchase.setTitle("إضافة مشتريات ");
        final AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);//add_tg_btn
        final EditText add_name_goods=(EditText) purchase.findViewById(R.id.add_name_goods);
        final EditText add_quantity=(EditText) purchase.findViewById(R.id.add_quantity);
        //final EditText add_extra_quantity=(EditText) purchase.findViewById(R.id.add_extra_quantity);
        final EditText add_date_sale=(EditText) purchase.findViewById(R.id.add_date_sale);
        final EditText add_date_ex=(EditText) purchase.findViewById(R.id.add_date_ex);
        final EditText editText=(EditText) purchase.findViewById(R.id.editText);////n      -سعر الشراء-بأعلى قيمة
        final EditText editText2=(EditText) purchase.findViewById(R.id.editText2);
        final EditText editText3=(EditText) purchase.findViewById(R.id.editText3);


        final SwitchMaterial purchases_dept_cash = (SwitchMaterial) purchase.findViewById(R.id.purchases_dept_cash);
        get_ALL_baracode();
        final Button add_barcode = (Button) purchase.findViewById(R.id.add_barcode2);
        final Button save_add_goods = (Button) purchase.findViewById(R.id.save_add_goods);//editText
        final Button dileg_q = (Button) purchase.findViewById(R.id.dileg_q);
          add_tg_btn = (Button) purchase.findViewById(R.id.add_tg_btn);
        final Button date_sale_btn = purchase.findViewById(R.id.date_show_sale);
        final Button date_ex_btn = purchase.findViewById(R.id.date_show_ex);

        add_barcode.setOnClickListener(v -> red_qr());

        purchases_dept_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchases_dept_cash.isChecked())
                {
                    purchases_dept_cash.setText("آجل");
                }
                else
                {
                    purchases_dept_cash.setText("نقد");
                }
            }
        });

        save_add_goods.setOnClickListener(v -> {
           // chaeck_seve_2 = chaeck_seve2_2();||chaeck_seve_2
            if ((chaeck_seve)&&quantity_stored_2>=1) {
                int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
                if (quantity_stored_2==0){
                    quantity_stored_2=1;
                }
                add_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format(quantity_stored*quantity_stored_2)));
                double total4 = Double.parseDouble(editText.getText().toString()) * Double.parseDouble(add_quantity.getText().toString());
                System.out.println("----------------------------------------------------"+total4+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                if ((editText2.getText().toString().length()<1&&editText3.getText().toString().length()>0)
                        ||(editText2.getText().toString().length()>0&&editText3.getText().toString().length()<1)){
                    if (editText2.getText().toString().length()<1){editText2.setError("أكمل ابيانات.");
                    }else {editText3.setError("أكمل ابيانات.");}
                }
                else if(editText2.getText().toString().length()<1&&editText3.getText().toString().length()<1){
                    editText2.setText("0");
                    editText3.setText("0");
                }

                if(editText2.getText().toString().length()>=1&&editText3.getText().toString().length()>=1) {
                    double total = Double.parseDouble(editText.getText().toString()) * Double.parseDouble(add_quantity.getText().toString());
                    double[] All_goods_double=databases.get_All_goods_for_barcod_Double(add_barcode_txt.getText().toString());
                    double q = All_goods_double[0];
                    q +=Double.parseDouble(add_quantity.getText().toString());
                    databases.get_update_quantity_in_goods(add_barcode_txt.getText().toString(),q);
                    databases.insert_purchases(
                            add_name_goods.getText().toString(),
                            add_barcode_txt.getText().toString(),
                            Double.parseDouble(editText.getText().toString()),
                            total,
                            Integer.parseInt(To_int(add_quantity.getText().toString())),
                            Integer.parseInt(editText2.getText().toString()),
                            add_date_ex.getText().toString(),
                            add_date_sale.getText().toString(),
                            id_resource,purchases_dept_cash.getText().toString());
                    insert_Data_quantity(add_barcode_txt.getText().toString());
                    chaeck_seve=false;
                    chaeck_seve_2=false;
                    show();
                    purchase.dismiss();
                    listShow_policy_2();
                }
            }
        });

        date_sale_btn.setOnClickListener(v -> {
            data_type="تاريخ الشراء";
            date_place = 1;
            date_picker_2();

        });

        date_ex_btn.setOnClickListener(v -> {
            data_type="تاريخ الإنتهاء";
            date_place = 0;
            date_picker_2();

        });

        dileg_q.setOnClickListener(v -> get_dileg_q());


        purchases.counter counter=new purchases.counter(3800,120);
        add_tg_btn.setOnClickListener(v -> {//cheack_1="1";

            if (add_barcode_txt.getText().toString().length()>0&&editText.getText().toString().length()>0){
                if (add_tg_btn.isEnabled()){
                    add_tg_btn.setEnabled(false);
                    counter.start();
                    set_mony=editText.getText().toString();
                }
                dva.show(getSupportFragmentManager(), "إضافة نوع");
            }

        });

        /*buy_or_restore.setOnClickListener(v -> {
            if(buy_or_restore.isChecked())
            {
                buy_or_restore.setText("آجل");
            }
            else {
                buy_or_restore.setText("نقد");
            }

        });*/
        purchase.show();
    }

    private boolean chaeck_seve2_2() {
        boolean b;
        EditText editText=(EditText) purchase.findViewById(R.id.editText);////n      -سعر الشراء-بأعلى قيمة
        AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);
         int a=databases.check_baracod(add_barcode_txt.getText().toString());

         if (add_barcode_txt.getText().toString().length()>0&&editText.getText().toString().length()>0&&
                 string_1.length()>0&&string_2.length()>0&&quantity_stored_2>=1) {
             b= a >= 1;
         }else b=false;

        return  b;
    }

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

    private void insert_Data_quantity(String barcode) {

        int id_g = databases.get_id_goods(barcode);
        int t=0, id_q;

        for (int i=0;i<=arr_slery.length-1;i++){
            if (arr_slery[i]!=null){
                t++;
            }
        }

        for (int r=0;r<=t;r++){
            if (r==1){
                id_q=databases.get_id_quantity_type(arr_quantity_type[0]);
                databases.get_update_quantity(id_g,id_q,Double.parseDouble(arr_slery[0]));
            }else if (r==2){
                id_q=databases.get_id_quantity_type(arr_quantity_type[1]);
                databases.get_update_quantity(id_g,id_q,Double.parseDouble(arr_slery[1]));
            }else if (r==3){
                id_q=databases.get_id_quantity_type(arr_quantity_type[2]);
                databases.get_update_quantity(id_g,id_q,Double.parseDouble(arr_slery[2]));
            }else if (r==4){
                id_q=databases.get_id_quantity_type(arr_quantity_type[3]);
                databases.get_update_quantity(id_g,id_q,Double.parseDouble(arr_slery[3]));
            }
        }

    }

    private void get_dileg_q() {

        Dialog customer_data;

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.agent_dialog);
        customer_data.setTitle("بيانات الكمية");
        final EditText add_extra_quantity_2 =customer_data.findViewById(R.id.add_extra_quantity_2);
        final EditText add_quantity_2 =customer_data.findViewById(R.id.add_quantity_2);
        final Button save_quantity_2 =customer_data.findViewById(R.id.save_quantity_2);

        add_quantity_2.setText("0");
        add_extra_quantity_2.setText("0");

        save_quantity_2.setOnClickListener(v -> {
            //save Data of customer

            if (add_extra_quantity_2.getText().toString().length()<1){
                add_extra_quantity_2.setText("0");
            }
            quantity_stored_2= Double.parseDouble(add_extra_quantity_2.getText().toString() +"d");
            if (add_quantity_2.getText().toString().length()<1){
                add_quantity_2.setText("0");
            }
            quantity_stored_2+= Double.parseDouble(add_quantity_2.getText().toString() +"d");

            customer_data.dismiss();
        });
        customer_data.show();
    }

    ///////////////n   يقوم بجلب كل الباركود ويقوم بتخزينها
    private void get_ALL_baracode() {

        String[] Allbaracod=databases.get_ALLbaracod();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Allbaracod);

        AutoCompleteTextView Text_barcode =
                purchase.findViewById(R.id.add_barcode_txt);

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

        String[] All_goods=databases.get_All_goods_for_barcod(item);
        //double[] All_goods_double=databases.get_All_goods_for_barcod_Double(item);

        final AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);
        final EditText add_name_goods=(EditText) purchase.findViewById(R.id.add_name_goods);
        //final EditText add_quantity=(EditText) purchase.findViewById(R.id.add_quantity);
       // final EditText add_extra_quantity=(EditText) purchase.findViewById(R.id.add_extra_quantity);
        final EditText add_date_sale=(EditText) purchase.findViewById(R.id.add_date_sale);
        final EditText add_date_ex=(EditText) purchase.findViewById(R.id.add_date_ex);
        final MaterialSpinner spinner=purchase.findViewById(R.id.spinner);

        if (!Text_date.equals("0")){
            add_barcode_txt.setText(Text_date);
            Text_date_2=Text_date;
            Text_date="0";
        }
        Text_date_2=item;

        add_name_goods.setText(All_goods[0]);
        //add_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format( All_goods_double[0])));//MaterialSpinner
        add_date_ex.setText(All_goods[1]);
        add_date_sale.setText(All_goods[2]);
        spinner.setText(All_goods[3]);


        string_1=All_goods[0];
        string_2=All_goods[3];


    }

    public void alter(String s){
        final AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);
        add_barcode_txt.setText(s);

        AlertDialog.Builder builder =new AlertDialog.Builder(purchases.this);/////////////////////////n        يوجد خطا هنا تاكد لا تنسسسسسس
        builder.setMessage("هل تريد إضافة المنتج جديد");
        //builder.setTitle("إضافة المنتج جديد");
        builder.setPositiveButton("نعم", (dialog, which) -> startActivity(new Intent(this,add_goods_db.class)));
        builder.setNegativeButton("لا", (dialog, which) -> {
            //////////////////b يتم فتح كلاس لاضافة لكي يقوم بعملية التعديل

        }).show();
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

    public void red_qr(){
        Intent intent=new Intent(this, ScanCodeActivity.class);
        intent.putExtra("page","purchases");
        //startActivity(intent);
        startActivityForResult(intent,3);

    }

    ///////n      يقوم بارسال تاكيد بانة تمت القراء باستخدام الكمايراء لكي يتم مل الليستة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String retern,key;
        int intent_key = 3;
        if (requestCode== intent_key){
            if (resultCode==RESULT_OK){

                assert data != null;
                key=data.getStringExtra("key");
                retern=data.getStringExtra("valu");

                if (key.equals("true")){
                    Text_date=retern;
                    Packing_for_goods(retern);

                }else {
                    alter(retern);
                }
            }
        }
    }

    public void add_policy_data () {

        //Dialog Customer Data viewer
        purchase = new Dialog(this);
        purchase.setContentView(R.layout.dialog_policy);
        purchase.setTitle("إضافة سند ");
         TextView resource_name=(TextView) purchase.findViewById(R.id.resource_name);
         EditText amount_policy=(EditText) purchase.findViewById(R.id.amount_policy);/////n  المبلغ
         Button date_paid=(Button) purchase.findViewById(R.id.date_paid);
         MultiAutoCompleteTextView note_txt=(MultiAutoCompleteTextView) purchase.findViewById(R.id.note_txt);/////n     ملاحظة


        resource_name.setText(name_resource_txt.getText().toString());
        final Button catch_btn=(Button) purchase.findViewById(R.id.catch_btn);
        final Button pure_btn = (Button) purchase.findViewById(R.id.pure_btn);

        Date date =new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
        date_paid.setText(sdf.format(date));

        date_paid.setOnClickListener(v -> date_picker());

        catch_btn.setOnClickListener(v -> {
            //قبض
            if (!TextUtils.isEmpty(resource_name.getText().toString())&&!TextUtils.isEmpty(amount_policy.getText().toString())&&
                    !TextUtils.isEmpty(note_txt.getText().toString())){

                int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
                double money = databases.get_money_box();
                boolean result = databases.insert_policy(
                        Double.parseDouble(amount_policy.getText().toString()),
                        date_paid.getText().toString(),
                        note_txt.getText().toString(),
                        "قبض",id_resource,0);
                databases.get_insert_money_box(money+Double.parseDouble(amount_policy.getText().toString()));

                if (result) {
                    Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    show();
                    purchase.dismiss();
                }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

            }

        });
        pure_btn.setOnClickListener(v -> {
            //صرف
            if (!TextUtils.isEmpty(resource_name.getText().toString())&&!TextUtils.isEmpty(amount_policy.getText().toString())&&
                    !TextUtils.isEmpty(note_txt.getText().toString())){
                double money = databases.get_money_box();
                int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
                boolean result = databases.insert_policy(
                        Double.parseDouble(amount_policy.getText().toString()),
                        date_paid.getText().toString(),
                        note_txt.getText().toString(),
                        "صرف",id_resource,0);
                databases.get_insert_money_box(money-Double.parseDouble(amount_policy.getText().toString()));
                if (result) {
                    Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    show();
                    purchase.dismiss();
                }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

            }
        });
        purchase.show();
    }

    public void add_policy_data_2 () {

        //Dialog Customer Data viewer
        purchase = new Dialog(this);
        purchase.setContentView(R.layout.dialog_policy);
        purchase.setTitle("إضافة سند ");
        TextView resource_name=(TextView) purchase.findViewById(R.id.resource_name);
        EditText amount_policy=(EditText) purchase.findViewById(R.id.amount_policy);/////n  المبلغ
        Button date_paid=(Button) purchase.findViewById(R.id.date_paid);
        MultiAutoCompleteTextView note_txt=(MultiAutoCompleteTextView) purchase.findViewById(R.id.note_txt);/////n     ملاحظة


        resource_name.setText(name_resource_txt.getText().toString());
        final Button catch_btn=(Button) purchase.findViewById(R.id.catch_btn);
        final Button pure_btn = (Button) purchase.findViewById(R.id.pure_btn);

        Date date =new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
        date_paid.setText(sdf.format(date));

        date_paid.setOnClickListener(v -> date_picker());

        catch_btn.setOnClickListener(v -> {
            //قبض
            if (!TextUtils.isEmpty(resource_name.getText().toString())&&!TextUtils.isEmpty(amount_policy.getText().toString())&&
                    !TextUtils.isEmpty(note_txt.getText().toString())){

                int id_agent=databases.get_id_agent(s);
                double money = databases.get_money_box();
                boolean result = databases.insert_policy(
                        Double.parseDouble(amount_policy.getText().toString()),
                        date_paid.getText().toString(),
                        note_txt.getText().toString(),
                        "قبض",0,id_agent);
                databases.get_insert_money_box(money+Double.parseDouble(amount_policy.getText().toString()));

                if (result) {
                    Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    show();
                    purchase.dismiss();
                }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();
            }

        });

        pure_btn.setOnClickListener(v -> {
            //صرف
            if (!TextUtils.isEmpty(resource_name.getText().toString())&&!TextUtils.isEmpty(amount_policy.getText().toString())&&
                    !TextUtils.isEmpty(note_txt.getText().toString())){
                double money = databases.get_money_box();
                int id_agent=databases.get_id_agent(s);
                boolean result = databases.insert_policy(
                        Double.parseDouble(amount_policy.getText().toString()),
                        date_paid.getText().toString(),
                        note_txt.getText().toString(),
                        "صرف",0,id_agent);
                databases.get_insert_money_box(money-Double.parseDouble(amount_policy.getText().toString()));
                if (result) {
                    Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    show();
                    purchase.dismiss();
                }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

            }
        });
        purchase.show();
    }

    public void listShow_policy(){
        int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
        String[] policy=databases.get_All_policy(id_resource);

        findViewById(R.id.purchases_visbility).setVisibility(View.GONE);
        findViewById(R.id.policy_visbility).setVisibility(View.VISIBLE);

        double[] policy_double =databases.get_All_policy_double(id_resource);
        if (databases.get_number_policy(id_resource)>=1) {
            policy_list.clear();
            int i = 0, g = 0;
            for (int j = 0; j < databases.get_number_policy(id_resource); j++) {

                policy_list.add(new policy_item_class(MessageFormat.format("{0}", policy_double[g]), policy[i], policy[i + 1], policy[i + 2]));
                i += 3;
                g += 1;
            }
            //////////////////////////////Add List Item//////////////////////////////////////
            purchases.ListAdupter2 ad = new purchases.ListAdupter2(policy_list);
            list_purchases.setAdapter(ad);

        }
    }

    public void listShow_policy_2(){
        int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
        String[] purchases=databases.get_All_purchases(id_resource);

        double[] purchases_double =databases.get_All_purchases_double(id_resource);

        if (databases.get_number_policy(id_resource)>=1) {
            q_list.clear();
            int i = 0, g = 0;
            for (int j = 0; j < databases.get_number_purchases_2(id_resource); j++) {

                q_list.add(new purchases_item_class(purchases[i + 1], purchases[i], MessageFormat.format("{0}", purchases_double[g]),
                        purchases[i + 2], MessageFormat.format("{0}", purchases_double[g + 1]),
                        purchases[i + 3], purchases[i + 5], purchases[i + 4]));

                System.out.println("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
                System.out.println("purchases[i+1] " + purchases[i + 1] + "\n purchases[i] " + purchases[i] + "\n purchases_double[g] " +
                        MessageFormat.format("{0}", purchases_double[g]) +
                        "\n purchases[i+2] " + purchases[i + 2] + "\n purchases_double[g+1] " + MessageFormat.format("{0}", purchases_double[g + 1]) +
                        "\n purchases[i+3] " + purchases[i + 3] + "\n purchases[i+5] " + purchases[i + 5] + "\n purchases[i+4] " + purchases[i + 4]);
                i += 6;
                g += 2;
            }
            //////////////////////////////Add List Item//////////////////////////////////////
            purchases.ListAdupter ad = new purchases.ListAdupter(q_list);
            list_purchases.setAdapter(ad);

        }
    }

    class ListAdupter extends BaseAdapter {
        ArrayList<purchases_item_class> list_item;
        ListAdupter(ArrayList<purchases_item_class> list_item){
            this.list_item = list_item ;
        }

        @Override
        public int getCount() {
            return list_item.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list_item.get(position).name;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.purchases_item,null);

            TextView purchases_item_barcode = (TextView) view.findViewById(R.id.purchases_item_barcode);

            TextView purchases_item_name = (TextView) view.findViewById(R.id.purchases_item_name);


            TextView purchases_item_sale = (TextView) view.findViewById(R.id.purchases_item_sale);
            TextView purchases_item_quintity = (TextView) view.findViewById(R.id.purchases_item_quintity);
            TextView purchases_item_total = (TextView) view.findViewById(R.id.purchases_item_total);
            TextView purchases_item_free_guintity = (TextView) view.findViewById(R.id.purchases_item_free_guintity);
            TextView purchases_item_date_purchase = (TextView) view.findViewById(R.id.purchases_item_date_purchase);
            TextView purchases_item_date_expare = (TextView) view.findViewById(R.id.purchases_item_date_expare);



            purchases_item_barcode.setText(list_item.get(i).barcode );
            purchases_item_name.setText(String.valueOf(list_item.get(i).name));
            purchases_item_sale.setText(String.valueOf(list_item.get(i).buy_price));
            purchases_item_quintity.setText(String.valueOf(list_item.get(i).quintity));
            purchases_item_total.setText(String.valueOf(list_item.get(i).total));
            purchases_item_free_guintity.setText(String.valueOf(list_item.get(i).free_quintity));
            purchases_item_date_purchase.setText(String.valueOf(list_item.get(i).date_purchase));
            purchases_item_date_expare.setText(String.valueOf(list_item.get(i).date_expare));

            return view;
        }
    }

    class ListAdupter2 extends BaseAdapter {
        ArrayList<policy_item_class> list_item;

        ListAdupter2(ArrayList<policy_item_class> list_item){
            this.list_item = list_item ;
        }

        @Override
        public int getCount() {
            return list_item.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list_item.get(position).sum;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.policy_item,null);

            TextView policy_sum = (TextView) view.findViewById(R.id.policy_sum);

            TextView policy_date = (TextView) view.findViewById(R.id.policy_date);

            TextView policy_note = (TextView) view.findViewById(R.id.policy_note);
            TextView policy_type = (TextView) view.findViewById(R.id.policy_type);

            policy_sum.setText(list_item.get(i).sum );
            policy_date.setText(String.valueOf(list_item.get(i).date));
            policy_note.setText(String.valueOf(list_item.get(i).note));
            policy_type.setText(String.valueOf(list_item.get(i).type));
            return view;
        }
    }

    ////////n     dialog_view_addtypes
    public static class dialog_view_addtypes extends AppCompatDialogFragment {

        @SuppressLint("StaticFieldLeak")
        public  EditText Text_q_type, Text_q_buy_price, Text_q_sale_price,Text_q_type_2,
                Text_q_quantity_2,Text_q_buy_price_2,Text_q_sale_price_2,Text_q_type_3,Text_q_quantity_3,
                Text_q_buy_price_3,Text_q_sale_price_3,Text_q_type_4,Text_q_quantity_4,Text_q_buy_price_4,Text_q_sale_price_4;
        RadioGroup radioGroup;
        Button  save;
        RadioButton primary_type,primary_type_2,primary_type_3,primary_type_4;
        androidx.appcompat.app.AlertDialog.Builder builder;
        boolean check_impot;



        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.types_goods, null);
            /////////////////////////////////////////////////////////////////quantity -> dialog
            save = view.findViewById(R.id.save_tg_add);


            primary_type=view.findViewById(R.id.primary_type);///////raduo
            Text_q_type=view.findViewById(R.id.q_type);
            Text_q_buy_price=view.findViewById(R.id.q_buy_price);
            Text_q_sale_price=view.findViewById(R.id.q_sale_price);
            primary_type.setEnabled(false);
            Text_q_type.setEnabled(false);
            Text_q_buy_price.setEnabled(false);
            Text_q_sale_price.setEnabled(false);

            primary_type_2=view.findViewById(R.id.primary_type_2);
            Text_q_type_2=view.findViewById(R.id.q_type_2);
            Text_q_quantity_2=view.findViewById(R.id.q_quantity_2);
            Text_q_buy_price_2=view.findViewById(R.id.q_buy_price_2);
            Text_q_sale_price_2=view.findViewById(R.id.q_sale_price_2);
            primary_type_2.setEnabled(false);
            Text_q_type_2.setEnabled(false);
            Text_q_quantity_2.setEnabled(false);
            Text_q_buy_price_2.setEnabled(false);
            Text_q_sale_price_2.setEnabled(false);

            primary_type_3=view.findViewById(R.id.primary_type_3);
            Text_q_type_3=view.findViewById(R.id.q_type_3);
            Text_q_quantity_3=view.findViewById(R.id.q_quantity_3);
            Text_q_buy_price_3=view.findViewById(R.id.q_buy_price_3);
            Text_q_sale_price_3=view.findViewById(R.id.q_sale_price_3);
            primary_type_3.setEnabled(false);
            Text_q_type_3.setEnabled(false);
            Text_q_quantity_3.setEnabled(false);
            Text_q_buy_price_3.setEnabled(false);
            Text_q_sale_price_3.setEnabled(false);

            primary_type_4=view.findViewById(R.id.primary_type_4);
            Text_q_type_4=view.findViewById(R.id.q_type_4);
            Text_q_quantity_4=view.findViewById(R.id.q_quantity_4);
            Text_q_buy_price_4=view.findViewById(R.id.q_buy_price_4);
            Text_q_sale_price_4=view.findViewById(R.id.q_sale_price_4);
            primary_type_4.setEnabled(false);
            Text_q_type_4.setEnabled(false);
            Text_q_quantity_4.setEnabled(false);
            Text_q_buy_price_4.setEnabled(false);
            Text_q_sale_price_4.setEnabled(false);

            //////////////////////////n   عملية الضغط على الرايديو بوتن  ////////////////////////////////////////////////////
            radioGroup=view.findViewById(R.id.radioGroup);
            radioGroup.setEnabled(false);
            ///////////////////////////////////////////////////////////////////////////////////////////////

            insert_into_qnuatity();

            //save Button on clicked
            save.setOnClickListener(v -> {

                if (check_impot_quantity()) {
                    chaeck_seve=true;
                    dismiss();
                }

            });

            builder.setView(view).setTitle("إضافة نوع");

            return builder.create();
        }

        ////////////n للتحقق من المدخلات
        private boolean check_impot_quantity() {
            arr_slery =new String[4];
            arr_quantity_type =new String[4];
            quantity_stored =1.0;

            if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())) {

                if (TextUtils.isEmpty(Text_q_buy_price.getEditableText().toString())||TextUtils.isEmpty(Text_q_sale_price.getEditableText().toString())){
                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else{
                    check_impot = true;
                    arr_slery[0]=Text_q_sale_price.getText().toString();
                    arr_quantity_type[0]=Text_q_type.getText().toString();
                    quantity_stored *=1;
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_2.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_2.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_2.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {
                    check_impot = true;
                    arr_slery[1]=Text_q_sale_price_2.getText().toString();
                    arr_quantity_type[1]=Text_q_type_2.getText().toString();
                    quantity_stored *=Double.parseDouble(Text_q_quantity_2.getText().toString());
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_3.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_3.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_3.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;

                    }else {
                    check_impot = true;
                    arr_slery[2]=Text_q_sale_price_3.getText().toString();
                    arr_quantity_type[2]=Text_q_type_3.getText().toString();
                    quantity_stored *=Double.parseDouble(Text_q_quantity_3.getText().toString());
                }
            }
            if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
                if (TextUtils.isEmpty(Text_q_quantity_4.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_4.getEditableText().toString())||
                        TextUtils.isEmpty(Text_q_sale_price_4.getEditableText().toString())){

                    Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                    check_impot = false;
                }else {
                    check_impot = true;
                    arr_slery[3]=Text_q_sale_price_4.getText().toString();
                    arr_quantity_type[3]=Text_q_type_4.getText().toString();
                    quantity_stored *=Double.parseDouble(Text_q_quantity_4.getText().toString());
                }
            }

            else if(TextUtils.isEmpty(Text_q_type.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())&&
                    TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())) {
                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                check_impot = false;
            }

            return check_impot;
        }

        /////////////////n     التعبئة في حقول الكمية
        private void insert_into_qnuatity() {

            Databases databases = new Databases(getActivity());
            int id = databases.get_id_goods(Text_date_2);
            int a=databases.read_Tname_q_type(id);
            String[] quantity=databases.get_ALLq_qnuatity(id);
            double[] quantity_Double=databases.get_ALLq_qnuatity_Double(id);////MessageFormat.format("{0}", quantity_Double[g]

            ////new DecimalFormat("#.000#").format(9999999999.123)
            double v=0.0;
            int i=1;

            for (int j=0;j<a;j++){////   String.format("%.3f", quantity_Double[2])
                if (i==1){
                    Text_q_type.setText(quantity[0]);
                    Text_q_buy_price.setText(String.valueOf(set_mony));///set_mony
                    Text_q_sale_price.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[2])));
                    set_rideou_cheack(1,quantity[0]);
                    Text_q_sale_price.setEnabled(true);

                    i+=1;
                }else if (i==2){
                    Text_q_type_2.setText(quantity[1]);
                    v=Double.parseDouble(set_mony)/Double.parseDouble(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[3])));
                    Text_q_quantity_2.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[3])));
                    Text_q_buy_price_2.setText(theack_aggen(new DecimalFormat("#.00#").format( v)));
                    Text_q_sale_price_2.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[5])));
                    set_rideou_cheack(2,quantity[1]);
                    Text_q_sale_price_2.setEnabled(true);
                    i+=1;
                }else if (i==3){
                    Text_q_type_3.setText(quantity[2]);
                    v= v /Double.parseDouble(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[6])));
                    Text_q_quantity_3.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[6])));
                    Text_q_buy_price_3.setText(theack_aggen(new DecimalFormat("#.00#").format(v)));
                    Text_q_sale_price_3.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[8])));
                    set_rideou_cheack(3,quantity[2]);
                    Text_q_sale_price_3.setEnabled(true);
                    i+=1;
                }else if (i==4){
                    Text_q_type_4.setText(quantity[3]);
                    v= v /Double.parseDouble(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[9])));
                    Text_q_quantity_4.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[9])));
                    Text_q_buy_price_4.setText(theack_aggen(new DecimalFormat("#.00#").format( v)));
                    Text_q_sale_price_4.setText(theack_aggen(new DecimalFormat("#.00#").format( quantity_Double[11])));
                    set_rideou_cheack(4,quantity[3]);
                    Text_q_sale_price_3.setEnabled(true);
                }

            }
        }

        public void set_rideou_cheack(int i,String number_q){
            Databases databases = new Databases(getActivity());
            int id = databases.get_id_goods(Text_date_2);

            int treu=databases.get_retern_cheack(id,number_q);

            if (i==1&&treu==1){
                primary_type.setChecked(true);
            }else if (i==2&&treu==1){
                primary_type_2.setChecked(true);
            }else if (i==3&&treu==1){
                primary_type_3.setChecked(true);
            }else if (i==4&&treu==1){
                primary_type_4.setChecked(true);
            }

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
            purchases.add_tg_btn.setEnabled(true);
        }
    }

}
