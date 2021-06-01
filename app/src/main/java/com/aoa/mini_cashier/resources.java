package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;

import java.text.MessageFormat;
import java.util.ArrayList;

public class resources extends AppCompatActivity {
    Dialog customer_data;
    Button add_resource;
   @SuppressLint("StaticFieldLeak")
   public static ListView list_resource;
    public Databases databases = new Databases(this);
    public String hasOnClick;
    public static ArrayList<list_item_resource> q_list = new ArrayList<>();
    TextView  paid_pruchase,total_paid,total_pruchase;
    public static String address="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        add_resource =(Button) findViewById(R.id.add_resource);

        list_resource = findViewById(R.id.list_resource);

        paid_pruchase = findViewById(R.id.paid_pruchase);
        total_paid = findViewById(R.id.total_paid);
        total_pruchase = findViewById(R.id.total_pruchase);

        Intent data =getIntent();
        hasOnClick = data.getExtras().getString("key");

        switch (hasOnClick) {
            case "purchases_btn":
                listShow_qnuatitytype();
                show();
                break;
            case "bills":

                break;
            case "max_account_btn":

                break;
        }


        add_resource.setOnClickListener(v -> add_customer_data ());


//
//        list_resource.setOnItemClickListener((parent, view, position, id) -> {
//
//
//        });
    }

    private void show() {
        double[] total=databases.get_ALL_total_purchases(),total2;
        double v =0,vv=0,vvv=0;
        for (int i=0;i<databases.get_number_purchases();i++){
            v +=total[i];
        }
        paid_pruchase.setText(MessageFormat.format("{0}",v));

        ////////////////////////////////////
         total=databases.get_ALL_paid_purchases();
         total2=databases.get_ALL_type_policy();


        for (int i=0;i<databases.get_number_purchases();i++){
            vvv +=total[i];
        }
        for (int i=0;i<databases.get_number_policy();i++){
            vv +=total2[i];
        }

        total_paid.setText(MessageFormat.format("{0}",vvv+vv));

        total_pruchase.setText(MessageFormat.format("{0}",(vvv+vv)-v));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        show();
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

                if (Integer.parseInt(c_phone2.getText().toString())<99999999&&Integer.parseInt(c_phone1.getText().toString())<999999999){

                boolean result = databases.insert_resource(name.getText().toString(),
                        Integer.parseInt(c_phone2.getText().toString()),
                        Integer.parseInt(c_phone1.getText().toString()),
                        c_address.getText().toString());

                if (result) {
                    Toast.makeText(resources.this, "ok", Toast.LENGTH_SHORT).show();
                    listShow_qnuatitytype();
                  }else Toast.makeText(resources.this, "bad", Toast.LENGTH_SHORT).show();
                }else {
                    if (Integer.parseInt(c_phone2.getText().toString())>99999999&&Integer.parseInt(c_phone1.getText().toString())<999999999){
                        c_phone2.setError("الرقم طويل جدا.");
                    }else if (Integer.parseInt(c_phone2.getText().toString())<99999999&&Integer.parseInt(c_phone1.getText().toString())>999999999){
                        c_phone1.setError("الرقم طويل جدا.");
                    }
                }

            }
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

            TextView  accunt_bill= (TextView) view.findViewById(R.id.accunt_bill);




            name_resouce.setText(list_item.get(i).name );
            accunt_bill.setText("0");



            name_resouce.setOnClickListener(v -> {

                @SuppressLint("CutPasteId") final TextView name=view.findViewById(R.id.name_resouce);

                String[] address=databases.get_address_resource(name.getText().toString());

                Intent intent=new Intent(resources.this, purchases.class);
                intent.putExtra("name",name.getText().toString());
                //intent.putExtra("mobile_resource",mobile_resource.getText().toString());
                //intent.putExtra("phone_resource",phone_resource.getText().toString());
                intent.putExtra("address",address[0]);//address

                startActivity(intent);

            });




            return view;
        }
    }
}

