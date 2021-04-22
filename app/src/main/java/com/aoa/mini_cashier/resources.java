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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.Class_Adupter.ListAdupter_quantity;
import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class resources extends AppCompatActivity {
    Dialog customer_data;
    Button add_resource;
   public static ListView list_resource;
    public Databases databases = new Databases(this);

    public static ArrayList<list_item_resource> q_list = new ArrayList<list_item_resource>();

    public static String address="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        add_resource =(Button) findViewById(R.id.add_resource);

        list_resource = findViewById(R.id.list_resource);

        listShow_qnuatitytype();

        add_resource.setOnClickListener(v -> add_customer_data ());

        list_resource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView name=view.findViewById(R.id.name_resouce);
                final TextView mobile_resource=view.findViewById(R.id.mobile_resource);
                final TextView phone_resource=view.findViewById(R.id.phone_resource);


                Toast.makeText(getApplicationContext(), name.getText().toString(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(resources.this, purchases.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("mobile_resource",mobile_resource.getText().toString());
                intent.putExtra("phone_resource",phone_resource.getText().toString());
                intent.putExtra("address","address");//address

                startActivity(intent);
            }
        });

//
//        list_resource.setOnItemClickListener((parent, view, position, id) -> {
//
//
//        });
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


        data_save.setOnClickListener(v -> {
            //save Data of customer

            if (!TextUtils.isEmpty(name.getText().toString())||!TextUtils.isEmpty(c_address.getText().toString())||
              !TextUtils.isEmpty(c_phone1.getText().toString())||!TextUtils.isEmpty(c_phone2.getText().toString())){

                address=c_address.getText().toString();

                boolean result = databases.insert_resource(name.getText().toString(),
                        Integer.parseInt(c_phone2.getText().toString()),
                        Integer.parseInt(c_phone1.getText().toString()),
                        c_address.getText().toString());

                if (result) {
                    Toast.makeText(resources.this, "ok", Toast.LENGTH_SHORT).show();
                    listShow_qnuatitytype();
                }else Toast.makeText(resources.this, "bad", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(resources.this, "0000000000", Toast.LENGTH_SHORT).show();
            customer_data.dismiss();
            customer_data.setTitle("بيانات العميل");
            c_email.setVisibility(View.VISIBLE);
            c_password.setVisibility(View.VISIBLE);
        });
        customer_data.show();
    }

    public void listShow_qnuatitytype(){

        String[] resource=databases.get_All_resource();
       // int[] resource_int=databases.get_one_resource_int("qq");

        // makeText(this, String.valueOf(g.length), Toast.LENGTH_SHORT).show();
        q_list.clear();
        int i=0;
        for (int j=0;j<databases.read_Tname_resource();j++){

            q_list.add(new list_item_resource(resource[i],resource[i+3],resource[i+2],resource[i+1]));
            i+=4;
        }
        //////////////////////////////Add List Item//////////////////////////////////////
        ListAdupter ad = new ListAdupter(q_list);
        list_resource.setAdapter(ad);

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

