package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class buy_restore_goods extends AppCompatActivity {

    Dialog customer_data;
    Button list_options,save_btn;
    EditText c_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_restore_goods);

        list_options = findViewById(R.id.list_options);
        save_btn = findViewById(R.id.save_bills);
        c_name = findViewById(R.id.customer_name);

        ListView list = (ListView) findViewById(R.id.list_buy_restore);
        //////////////////////// Add goods in list //////////////////////////////////////////
        ArrayList<list_buy_restore> q_list = new ArrayList<list_buy_restore>();
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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_customer_data();
            }
        });
    }
    class ListAdupter extends BaseAdapter
    {
        ArrayList<list_buy_restore> list_item =new ArrayList<list_buy_restore>();
        ListAdupter(ArrayList<list_buy_restore> list_item){
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
            View view =inflater.inflate(R.layout.buy_restore_item,null);

            TextView name = (TextView) view.findViewById(R.id.goods_name);

            TextView quantity_type = (TextView) view.findViewById(R.id.goods_q_type);


            TextView goods_quanitity = (TextView) view.findViewById(R.id.goods_quanitity);

            TextView buy_price = (TextView) view.findViewById(R.id.goods_buy);

            TextView sale_price = (TextView) view.findViewById(R.id.goods_sale);



            name.setText(list_item.get(i).name );
            quantity_type.setText(String.valueOf(list_item.get(i).quantity_type));
            goods_quanitity.setText(String.valueOf(list_item.get(i).goods_quanitity));



            return view;
        }
    }

    public void add_customer_data ()
    {

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.add_customer_dialog);
        customer_data.setTitle("بيانات العميل");
        final EditText name=(EditText) customer_data.findViewById(R.id.name_customer);
        final EditText c_address=(EditText) customer_data.findViewById(R.id.address_customer);
        final EditText c_phone1=(EditText) customer_data.findViewById(R.id.Phone_customer_1);
        final EditText c_phone2=(EditText) customer_data.findViewById(R.id.Phone_customer_2);
        final EditText c_email=(EditText) customer_data.findViewById(R.id.E_mail_customer);
        final EditText c_password=(EditText) customer_data.findViewById(R.id.password_customer);

        final Button data_save = (Button) customer_data.findViewById(R.id.save_customer_data);

        name.setText(c_name.getText());


        data_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save Data of customer


                customer_data.dismiss();
            }
        });
        customer_data.show();
    }

}
