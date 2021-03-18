package com.aoa.mini_cashier.Class_Adupter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.R;
import com.aoa.mini_cashier.list_item_qnuatitytype;

import java.util.ArrayList;

public class ListAdupter_quantity extends BaseAdapter {

    ArrayList<list_item_qnuatitytype> list_item;
    Context context;

    public ListAdupter_quantity(Context context, ArrayList<list_item_qnuatitytype> list_item) {
        this.context = context;
        this.list_item = list_item;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup parent) {

//        LayoutInflater inflater = getLayoutInflater();
//        @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.add_tg_item, null);

        view = LayoutInflater.from(context).inflate(R.layout.add_tg_item,parent,false);

        TextView name = (TextView) view.findViewById(R.id.q_name_item);

        TextView quantity = (TextView) view.findViewById(R.id.q_quantity_item);

        TextView buy_price = (TextView) view.findViewById(R.id.q_buy_item);

        TextView sale_price = (TextView) view.findViewById(R.id.q_sale_item);

        name.setText(list_item.get(i).getName());
        quantity.setText(list_item.get(i).getQuantity());
        buy_price.setText(list_item.get(i).getBuy_price());
        sale_price.setText(list_item.get(i).getSale_price());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Position "+i, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}