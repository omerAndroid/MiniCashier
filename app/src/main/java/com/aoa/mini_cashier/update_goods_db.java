package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class update_goods_db extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goods_db);

        ListView list = (ListView) findViewById(R.id.list_goods_db);
        ////////////////////////Add List Item//////////////////////////////////////////
        ArrayList<list_item_update> q_list = new ArrayList<list_item_update>();
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);
        //////////////////////////////////////////////////////////////////////////////////


    }

    class ListAdupter extends BaseAdapter
    {
        ArrayList<list_item_update> list_item =new ArrayList<list_item_update>();
        ListAdupter(ArrayList<list_item_update> list_item){
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
            View view =inflater.inflate(R.layout.add_tg_item,null);

            EditText name = (EditText) view.findViewById(R.id.goods_view);

            EditText quantity = (EditText) view.findViewById(R.id.quantity_veiw);

            EditText quantity_type = (EditText) view.findViewById(R.id.quantity_type_view);

            EditText date_ex = (EditText) view.findViewById(R.id.date_ex_veiw);

            EditText buy_price = (EditText) view.findViewById(R.id.buy_veiw);

            EditText sale_price = (EditText) view.findViewById(R.id.sale_veiw);

            name.setEnabled(false);
            quantity.setEnabled(false);
            buy_price.setEnabled(false);
            sale_price.setEnabled(false);
            quantity_type.setEnabled(false);
            date_ex.setEnabled(false);


            name.setText(list_item.get(i).name );
            quantity.setText(String.valueOf(list_item.get(i).quantity ));
            buy_price.setText(String.valueOf(list_item.get(i).buy_price));
            sale_price.setText(String.valueOf(list_item.get(i).sale_price));
            quantity_type.setText(String.valueOf(list_item.get(i).quantity_type));
            date_ex.setText(String.valueOf(list_item.get(i).date_ex));



            return view;
        }
    }
}