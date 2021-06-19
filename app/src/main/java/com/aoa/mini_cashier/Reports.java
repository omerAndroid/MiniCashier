package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.item_classes.report_item_class;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {

    ArrayList<report_item_class> list_item = new ArrayList<>();
    public Databases databases = new Databases(this);
    AutoCompleteTextView Text_add_name_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        get_ALL_baracode_name_g();

        listShow_products_bills();

    }

    public void get_ALL_baracode_name_g() {

        String[] Allbaracod=databases.get_ALLbaracod();

        String[] Allname_g=databases.get_ALLname_g();



        String[] All=new String[Allbaracod.length+Allname_g.length];

        ////n   تستخدم لنسخ محتوى مصفوفة و وضعه في مصفوفة أخرى
        System.arraycopy(Allbaracod, 0, All, 0, Allbaracod.length);
        System.arraycopy(Allname_g, 0, All, Allbaracod.length, Allname_g.length);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, All);

        Text_add_name_item =
                findViewById(R.id.searsh_text);

        Text_add_name_item.setAdapter(adapter1);

        Text_add_name_item.setThreshold(1);//will start working from first

        Text_add_name_item.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            // Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات

            tcack_camera(item);
            Text_add_name_item.setText("");
        });


    }

    public void tcack_camera(String item){

        String[] Allbaracod=databases.get_ALLbaracod();

        String[] Allname_g=databases.get_ALLname_g();
        String who="";
        for(String val :Allbaracod){
            if (val.equals(item)){
                Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
                who="Allbaracod";
            }
        }

        for(String val :Allname_g){
            if (val.equals(item)){
                Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
                who="Allname_g";
            }
        }

        Packing_for_products_bills(item,who);
    }

    private void Packing_for_products_bills(String item, String who) {

        list_item = new ArrayList<>();


        String[] g=databases.get_one_goods(item,who);///////n     الاسم    g[1]


        String[] products_bills=databases.get_All_products_bills(g[1]);
        if (databases.get_number_products_bills(g[1])>0) {
            int i = 0;
            for (int j = 0; j < databases.get_number_products_bills(g[1]); j++) {

                list_item.add(new report_item_class(products_bills[i], products_bills[i + 1], products_bills[i + 2],
                        products_bills[i + 3], products_bills[i + 4], products_bills[i + 5],
                        ""+Double.parseDouble(products_bills[i + 2])*Double.parseDouble(products_bills[i + 3])+""));
                i += 6;
            }


            ListView list = findViewById(R.id.report_list);
            ListAdupter ad = new ListAdupter(list_item);
            list.setAdapter(ad);
        }
    }

    public void listShow_products_bills(){
System.out.println("hh"+5+"");
        String[] products_bills=databases.get_All_products_bills("null");
        if (databases.get_number_products_bills("null")>0) {
            int i = 0;
            for (int j = 0; j < databases.get_number_products_bills("null"); j++) {

                list_item.add(new report_item_class(products_bills[i], products_bills[i + 1], products_bills[i + 2],
                        products_bills[i + 3], products_bills[i + 4], products_bills[i + 5],
                        ""+Double.parseDouble(products_bills[i + 2])*Double.parseDouble(products_bills[i + 3])+""));
                i += 6;
            }


            ListView list = findViewById(R.id.report_list);
            ListAdupter ad = new ListAdupter(list_item);
            list.setAdapter(ad);
        }
    }

    class ListAdupter extends BaseAdapter {
        ArrayList<report_item_class> list_item;
        ListAdupter(ArrayList<report_item_class> list_item){
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.report_item,null);

            TextView barcode =  view.findViewById(R.id.report_barcode);

            TextView name =view.findViewById(R.id.report_name);

            TextView data_1 = view.findViewById(R.id.report_data_1);

            TextView data_2 = view.findViewById(R.id.report_data_2);

            TextView data_3 =  view.findViewById(R.id.report_data_3);

            TextView data_4 =  view.findViewById(R.id.report_data_4);

            TextView data_5 =  view.findViewById(R.id.report_data_5);


            barcode.setText(list_item.get(i).barcode );
            name.setText(list_item.get(i).name );
            data_1.setText(String.valueOf(list_item.get(i).data_1 ));
            data_2.setText(list_item.get(i).data_2);
            data_3.setText(list_item.get(i).data_3);
            data_4.setText(list_item.get(i).data_4);
            data_5.setText(list_item.get(i).data_5);


            return view;
        }
    }
}
