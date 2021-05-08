package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;
import com.aoa.mini_cashier.item_classes.policy_item_class;
import com.aoa.mini_cashier.item_classes.purchases_item_class;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class purchases extends AppCompatActivity {

    public Databases databases = new Databases(this);
    Dialog purchase,Date_Dialog;
    ListView list_purchases;
    Button add_purchases,add_policy,change_list_items;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    //String date_viewe= "asdf";
    TextView phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt;
    String data_type,Text_date="0";
    int date_place = 0;
    public static ArrayList<purchases_item_class> q_list = new ArrayList<>();

    public static ArrayList<policy_item_class> policy_list = new ArrayList<>();

    public static double quantity_stored=1.0,quantity_stored_2=1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        list_purchases= (ListView) findViewById(R.id.list_purchases);
        add_policy =(Button) findViewById(R.id.add_policy);
        add_purchases =(Button) findViewById(R.id.add_purchases);

//        ListAdupter ad = new ListAdupter(q_list);
//        list_purchases.setAdapter(ad);



         phone_resource_txt=findViewById(R.id.phone_resource_txt);
         mobile_resource_txt=findViewById(R.id.mobile_resource_txt);
         address_resource=findViewById(R.id.address_resource);
         name_resource_txt=findViewById(R.id.name_resource_txt);

        /////////////////////////Date Picker///////////////////////////////////
        calendar = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        add_policy.setOnClickListener(v -> add_policy_data());

        add_purchases.setOnClickListener(v -> add_purchases_data());

        /////Intent
        Intent data =getIntent();///phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt;data.getExtras().getString("page");
        //phone_resource_txt.setText(data.getExtras().getString("phone_resource"));
        //mobile_resource_txt.setText(data.getExtras().getString("mobile_resource"));
        address_resource.setText(data.getExtras().getString("address"));
        name_resource_txt.setText(data.getExtras().getString("name"));

        change_list_items=findViewById(R.id.change_list_items);///
        findViewById(R.id.change_list_items).setOnClickListener(v -> {
            if (change_list_items.getText().toString().equals("قائمة المشتريات")){
                change_list_items.setText("قائمة السندات");
                listShow_policy();
            }else {
                change_list_items.setText("قائمة المشتريات");

                policy_list = new ArrayList<>();
                ListAdupter2 ad = new ListAdupter2(policy_list);
                list_purchases.setAdapter(ad);

            }
        });
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
        final AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);
        final EditText add_name_goods=(EditText) purchase.findViewById(R.id.add_name_goods);
        final EditText add_quantity=(EditText) purchase.findViewById(R.id.add_quantity);
        final EditText add_extra_quantity=(EditText) purchase.findViewById(R.id.add_extra_quantity);
        final EditText add_date_sale=(EditText) purchase.findViewById(R.id.add_date_sale);
        final EditText add_date_ex=(EditText) purchase.findViewById(R.id.add_date_ex);
        get_ALL_baracode();
        final Button add_barcode = (Button) purchase.findViewById(R.id.add_barcode2);
        final Button save_add_goods = (Button) purchase.findViewById(R.id.save_add_goods);//dileg_q
        final Button dileg_q = (Button) purchase.findViewById(R.id.dileg_q);

        final Button date_sale_btn = purchase.findViewById(R.id.date_show_sale);
        final Button date_ex_btn = purchase.findViewById(R.id.date_show_ex);

        add_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red_qr();
            }
        });

        save_add_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save Data of customer
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

        dileg_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_dileg_q();
            }
        });
        purchase.show();
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

        save_quantity_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
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
        double[] All_goods_double=databases.get_All_goods_for_barcod_Double(item);

        final AutoCompleteTextView add_barcode_txt=(AutoCompleteTextView) purchase.findViewById(R.id.add_barcode_txt);
        final EditText add_name_goods=(EditText) purchase.findViewById(R.id.add_name_goods);
        final EditText add_quantity=(EditText) purchase.findViewById(R.id.add_quantity);
        final EditText add_extra_quantity=(EditText) purchase.findViewById(R.id.add_extra_quantity);
        final EditText add_date_sale=(EditText) purchase.findViewById(R.id.add_date_sale);
        final EditText add_date_ex=(EditText) purchase.findViewById(R.id.add_date_ex);
        final MaterialSpinner spinner=purchase.findViewById(R.id.spinner);

        if (!Text_date.equals("0")){
            add_barcode_txt.setText(Text_date);
            Text_date="0";
        }

        add_name_goods.setText(All_goods[0]);
        add_quantity.setText(theack_aggen(new DecimalFormat("#.00#").format( All_goods_double[0])));//MaterialSpinner
        add_date_ex.setText(All_goods[1]);
        add_date_sale.setText(All_goods[2]);
        spinner.setText(All_goods[3]);

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

        date_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_picker();
            }
        });

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
                }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

            }
        });
        purchase.show();
    }

    public void listShow_policy(){

        String[] policy=databases.get_All_policy();

        double[] policy_double =databases.get_All_policy_double();

        policy_list.clear();
        int i=0,g=0;
        for (int j=0;j<databases.get_number_policy();j++){

            policy_list.add(new policy_item_class(MessageFormat.format("{0}", policy_double[g]),policy[i],policy[i+1],policy[i+2]));
            i+=3;
            g+=1;
        }
        //////////////////////////////Add List Item//////////////////////////////////////
        purchases.ListAdupter2 ad = new purchases.ListAdupter2(policy_list);
        list_purchases.setAdapter(ad);


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
}
