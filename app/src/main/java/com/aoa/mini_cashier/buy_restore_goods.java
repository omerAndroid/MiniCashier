package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class buy_restore_goods extends AppCompatActivity {

    Button list_options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_restore_goods);

        list_options = findViewById(R.id.list_options);


        ListView list = (ListView) findViewById(R.id.list_buy_restore);
        //////////////////////// Add goods in list //////////////////////////////////////////
        ArrayList<list_item_update> q_list = new ArrayList<list_item_update>();
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);
        ////////////////////////show list Options of bills/////////////////////////////////////
        list_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show menu options for bills
                PopupMenu popupMenu = new PopupMenu(buy_restore_goods.this,v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_bills_options,popupMenu.getMenu());

                /*MenuPopupHelper popupHelper = new MenuPopupHelper(MainActivity.this, (MenuBuilder) popupMenu.getMenu(),v);
                popupHelper.setForceShowIcon(true);*/
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        //print bills
                        if(id==R.id.print_bill_menu)
                        {
                            Toast.makeText(buy_restore_goods.this, "طباعة الفاتورة", Toast.LENGTH_SHORT).show();
                        }

                        //share bills
                        if(id==R.id.share_bill_menu)
                        {
                            Toast.makeText(buy_restore_goods.this, "مشاركة الفاتورة", Toast.LENGTH_SHORT).show();
                        }

                       //open PDF file
                        if(id==R.id.open_pdf_menu)
                        {
                            Toast.makeText(buy_restore_goods.this, "فتح ملف PDF", Toast.LENGTH_SHORT).show();
                        }
                        return false;

                    }
                });
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////

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

            name.setEnabled(true);
            quantity.setEnabled(true);
            buy_price.setEnabled(true);
            sale_price.setEnabled(true);
            quantity_type.setEnabled(true);
            date_ex.setEnabled(true);


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