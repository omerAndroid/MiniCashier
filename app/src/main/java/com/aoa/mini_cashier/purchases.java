package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.aoa.mini_cashier.item_classes.policy_item_class;
import com.aoa.mini_cashier.item_classes.purchases_item_class;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class purchases extends AppCompatActivity {

    public Databases databases = new Databases(this);
    Dialog purchase,Date_Dialog;
    ListView list_purchases;
    Button add_purchases,add_policy;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    String date_viewe= "asdf";
    TextView phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt;

    public static ArrayList<purchases_item_class> q_list = new ArrayList<purchases_item_class>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        list_purchases= (ListView) findViewById(R.id.list_purchases);
        add_policy =(Button) findViewById(R.id.add_policy);
        add_purchases =(Button) findViewById(R.id.add_purchases);

        ListAdupter ad = new ListAdupter(q_list);
        list_purchases.setAdapter(ad);

         phone_resource_txt=findViewById(R.id.phone_resource_txt);
         mobile_resource_txt=findViewById(R.id.mobile_resource_txt);
         address_resource=findViewById(R.id.address_resource);
         name_resource_txt=findViewById(R.id.name_resource_txt);

        /////////////////////////Date Picker///////////////////////////////////
        calendar = Calendar.getInstance();
        date_format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        add_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_policy_data();
            }
        });

        add_purchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_purchases_data();
            }
        });

        /////Intent
        Intent data =getIntent();///phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt;data.getExtras().getString("page");
        //phone_resource_txt.setText(data.getExtras().getString("phone_resource"));
        //mobile_resource_txt.setText(data.getExtras().getString("mobile_resource"));
        address_resource.setText(data.getExtras().getString("address"));
        name_resource_txt.setText(data.getExtras().getString("name"));



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

        final Button add_barcode = (Button) purchase.findViewById(R.id.add_barcode);


        add_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save Data of customer

            }
        });
        purchase.show();
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

        catch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //قبض
                if (!TextUtils.isEmpty(resource_name.getText().toString())||!TextUtils.isEmpty(amount_policy.getText().toString())||
                        !TextUtils.isEmpty(note_txt.getText().toString())){

                    int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
                    boolean result = databases.insert_policy(
                            Double.parseDouble(amount_policy.getText().toString()),
                            date_paid.getText().toString(),
                            note_txt.getText().toString(),
                            "قبض",id_resource,0);

                    if (result) {
                        Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

                }

            }
        });
        pure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //صرف
                if (!TextUtils.isEmpty(resource_name.getText().toString())||!TextUtils.isEmpty(amount_policy.getText().toString())||
                        !TextUtils.isEmpty(note_txt.getText().toString())){

                    int id_resource=databases.get_id_resource(name_resource_txt.getText().toString());
                    boolean result = databases.insert_policy(
                            Double.parseDouble(amount_policy.getText().toString()),
                            date_paid.getText().toString(),
                            note_txt.getText().toString(),
                            "صرف",id_resource,0);

                    if (result) {
                        Toast.makeText(purchases.this, "ok", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(purchases.this, "no no no ", Toast.LENGTH_SHORT).show();

                }
            }
        });
        purchase.show();
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
