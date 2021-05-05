package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aoa.mini_cashier.item_classes.report_item_class;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {

    ArrayList<report_item_class> list_item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        ListView list = findViewById(R.id.report_list);

        ListAdapter adapter = new ListAdupter(list_item);
        list.setAdapter(adapter);
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


            barcode.setText(list_item.get(i).barcode );
            name.setText(list_item.get(i).name );
            data_1.setText(String.valueOf(list_item.get(i).data_1 ));
            data_2.setText(list_item.get(i).data_2);
            data_3.setText(list_item.get(i).data_3);
            data_4.setText(list_item.get(i).data_4);


            return view;
        }
    }
}
