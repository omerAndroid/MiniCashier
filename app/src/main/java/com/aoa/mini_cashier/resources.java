package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class resources extends AppCompatActivity {
    Dialog customer_data;
    Button add_resource;
    ListView list_resource;

    public static ArrayList<list_item_resource> q_list = new ArrayList<list_item_resource>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        add_resource =(Button) findViewById(R.id.add_resource);

        list_resource = findViewById(R.id.list_resource);

        ListAdupter ad = new ListAdupter(q_list);
        list_resource.setAdapter(ad);


        add_resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_customer_data ();
            }
        });

    }

    public void add_customer_data () {

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.add_customer_dialog);
        customer_data.setTitle("بيانات المورد");
        final EditText name=(EditText) customer_data.findViewById(R.id.name_customer);
        final EditText c_address=(EditText) customer_data.findViewById(R.id.address_customer);
        final EditText c_phone1=(EditText) customer_data.findViewById(R.id.Phone_customer_1);
        final EditText c_phone2=(EditText) customer_data.findViewById(R.id.Phone_customer_2);
        final EditText c_email=(EditText) customer_data.findViewById(R.id.E_mail_customer);
        final EditText c_password=(EditText) customer_data.findViewById(R.id.password_customer);

        c_email.setVisibility(View.GONE);
        c_password.setVisibility(View.GONE);
        final Button data_save = (Button) customer_data.findViewById(R.id.save_customer_data);


        data_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save Data of customer

                customer_data.dismiss();
                customer_data.setTitle("بيانات العميل");
                c_email.setVisibility(View.VISIBLE);
                c_password.setVisibility(View.VISIBLE);
            }
        });
        customer_data.show();
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

