package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.aoa.mini_cashier.item_classes.purchases_item_class;

import java.text.MessageFormat;
import java.util.ArrayList;

public class resources extends AppCompatActivity {
    Dialog customer_data;
    Button add_resource;
    SharedPreferences sharedPreferences;

    @SuppressLint("StaticFieldLeak")
   public  ListView list_resource;
    public Databases databases = new Databases(this);
    public String hasOnClick;
    public static ArrayList<list_item_resource> q_list = new ArrayList<>();
    TextView  paid_pruchase,total_paid,total_pruchase;
    public static String address="";
    TextView name_resouce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        add_resource = findViewById(R.id.add_resource);

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
                add_resource.setOnClickListener(v -> add_customer_data ());
                break;

            case "max_account_btn":
                findViewById(R.id.linearLayout10).setVisibility(View.GONE);
                findViewById(R.id.add_resource).setVisibility(View.GONE);
                findViewById(R.id.linearLayout7).setVisibility(View.GONE);
                sharedPreferences= getSharedPreferences("key",0);
                String tecack_1=sharedPreferences.getString("key_2","0");///check_agent

                  String[] resource=databases.get_All_bills();////n    اسم العميل

                ArrayList<String> name_prod= new ArrayList<>();
                int z=0;
                for (int i=0;i<databases.read_Tname_bills();i++){
                    if (!name_prod.contains(resource[i])){

                        int id_agent=databases.check_agent(resource[i]);////n   id_agent
                        double id_agent_total=databases.read_The_bills_total(id_agent);////n   id_agent_total
                        double a1=databases.get_policy_amount_agent(id_agent,"صرف");
                        double a2=databases.get_policy_amount_agent(id_agent,"قبض");

                    name_prod.add(resource[i]);
                    name_prod.add(theack_aggen(MessageFormat.format("{0}", ((id_agent_total+a1)-a2))));
                    z++;
                    }
                }

                q_list.clear();
                int i=0;
                for (int j=0;j<z;j++){
                    if(Double.parseDouble(name_prod.get(i + 1))>=Double.parseDouble(tecack_1)){
                    q_list.add(new list_item_resource(name_prod.get(i), name_prod.get(i + 1),"",""));
                    }
                    i+=2;
                }

                ListAdupter ad = new ListAdupter(q_list);
                list_resource.setAdapter(ad);
                break;
        }


        //add_resource.setOnClickListener(v -> add_customer_data ());

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
        listShow_qnuatitytype();
    }

    public void add_customer_data () {

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.add_customer_dialog);
        customer_data.setTitle("بيانات المورد");
        final EditText name= customer_data.findViewById(R.id.name_customer);
        final EditText c_address= customer_data.findViewById(R.id.address_customer);
        final EditText c_phone1= customer_data.findViewById(R.id.Phone_customer_1);
        final EditText c_phone2= customer_data.findViewById(R.id.Phone_customer_2);
        final EditText c_email= customer_data.findViewById(R.id.E_mail_customer);
        final EditText c_password= customer_data.findViewById(R.id.password_customer);

        c_email.setVisibility(View.GONE);
        c_password.setVisibility(View.GONE);
        final Button data_save =  customer_data.findViewById(R.id.save_customer_data);


        data_save.setOnClickListener(v -> {
            //save Data of customer

            if (!TextUtils.isEmpty(name.getText().toString())||!TextUtils.isEmpty(c_address.getText().toString())||
              !TextUtils.isEmpty(c_phone1.getText().toString())||!TextUtils.isEmpty(c_phone2.getText().toString())){

                address=c_address.getText().toString();

                if (c_phone2.getText().toString().length()<=9&&(c_phone1.getText().toString().length())<=9){

                boolean result = databases.insert_resource(name.getText().toString(),
                        Integer.parseInt(c_phone2.getText().toString()),
                        Integer.parseInt(c_phone1.getText().toString()),
                        c_address.getText().toString());

                if (result) {
                    Toast.makeText(resources.this, "ok", Toast.LENGTH_SHORT).show();
                    listShow_qnuatitytype();
                  }else Toast.makeText(resources.this, "bad", Toast.LENGTH_SHORT).show();
                }else {
                    if (c_phone2.getText().toString().length()>9&&c_phone1.getText().toString().length()<=9){
                        c_phone2.setError("الرقم طويل جدا.");
                    }else if (c_phone2.getText().toString().length()<=9&&c_phone1.getText().toString().length()>9){
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

             name_resouce =  view.findViewById(R.id.name_resouce);

            TextView  accunt_bill=  view.findViewById(R.id.accunt_bill);


            if ("max_account_btn".equals(hasOnClick)) {
                name_resouce.setText(list_item.get(i).name );
                accunt_bill.setText(list_item.get(i).phone);
            }else {
                name_resouce.setText(list_item.get(i).name );
                accunt_bill.setText(MessageFormat.format("{0}",
                        (
                                (databases.get_purchases_total(databases.get_id_resource(list_item.get(i).name))+
                                databases.get_policy_amount(databases.get_id_resource(list_item.get(i).name),"قبض")
                                )
                        )-databases.get_policy_amount(databases.get_id_resource(list_item.get(i).name),"صرف")
                        ));


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

            }

            return view;
        }
    }

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
}

