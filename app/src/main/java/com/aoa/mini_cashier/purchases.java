package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class purchases extends AppCompatActivity {


    Dialog purchase,Date_Dialog;
    ListView list_purchases;
    Button add_purchases,add_policy;
    private SimpleDateFormat date_format;
    private Calendar calendar;
    String date_viewe= "asdf";
    TextView phone_resource_txt,mobile_resource_txt,address_resource,name_resource_txt;

    public static ArrayList<list_item_resource> q_list = new ArrayList<list_item_resource>();


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
        phone_resource_txt.setText(data.getExtras().getString("phone_resource"));
        mobile_resource_txt.setText(data.getExtras().getString("mobile_resource"));
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
        purchase.setContentView(R.layout.activity_add_goods_db);
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
        final TextView resource_name=(TextView) purchase.findViewById(R.id.resource_name);
        final EditText amount_policy=(EditText) purchase.findViewById(R.id.amount_policy);
        final Button date_paid=(Button) purchase.findViewById(R.id.date_paid);
        final MultiAutoCompleteTextView note_txt=(MultiAutoCompleteTextView) purchase.findViewById(R.id.note_txt);


        final Button catch_btn=(Button) purchase.findViewById(R.id.catch_btn);
        final Button pure_btn = (Button) purchase.findViewById(R.id.pure_btn);

        Date date =new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
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

            }
        });
        pure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //صرف

            }
        });
        purchase.show();
    }


    class ListAdupter extends BaseAdapter {
        ArrayList<list_item_resource> list_item;
        ListAdupter(ArrayList<list_item_resource> list_item){
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.resource_list_item,null);

            TextView name_resouce = (TextView) view.findViewById(R.id.name_resouce);

            TextView phone_resouce = (TextView) view.findViewById(R.id.phone_resource);


            TextView mobile_resouce = (TextView) view.findViewById(R.id.mobile_resource);



            name_resouce.setText(list_item.get(i).name );
            phone_resouce.setText(String.valueOf(list_item.get(i).phone));
            mobile_resouce.setText(String.valueOf(list_item.get(i).mobile));
            return view;
        }
    }
}
