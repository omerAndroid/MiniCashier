package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class purchases extends AppCompatActivity {


    ListView list_purchases;
    Button add_purchases,add_policy;

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

        add_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add policy
            }
        });

        add_purchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add purchases
            }
        });

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
