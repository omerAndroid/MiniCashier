package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

    public static TextView textView;
    ZXingScannerView scannerView;

    //EditText Text_,Text_,Text_,Text_,Text_,Text_;
    Button add_tg_btn;
    dialog_view_addtypes dva=new dialog_view_addtypes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_add_goods_db);

        add_tg_btn =findViewById(R.id.add_tg_btn);

        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn =findViewById(R.id.add_tg_btn);

        ListView list = (ListView) findViewById(R.id.list_quantity);
        ////////////////////////////////////////////////////////////////////////////////////
        ArrayList<list_item_qnuatitytype> q_list = new ArrayList<list_item_qnuatitytype>();
        ///////////////////////////////////////////////////////////////////////////////////
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);
        //////////////////////////////////////////////////////////////////////////////////

        add_tg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dva.show(getSupportFragmentManager(), "إضافة نوع");
            }
        });



    }

    public void red_qr(View view) {
        startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
    }

    class ListAdupter extends BaseAdapter
    {
        ArrayList<list_item_qnuatitytype> list_item =new ArrayList<list_item_qnuatitytype>();
        ListAdupter(ArrayList<list_item_qnuatitytype> list_item){
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

            TextView name = (TextView) view.findViewById(R.id.q_name_item);

            TextView quaىntity = (TextView) view.findViewById(R.id.q_quantity_item);

            TextView buy_price = (TextView) view.findViewById(R.id.q_buy_item);

            TextView sale_price = (TextView) view.findViewById(R.id.q_sale_item);

            name.setText(list_item.get(i).name );
            quaىntity.setText(String.valueOf(list_item.get(i).quantity) );
            buy_price.setText(String.valueOf(list_item.get(i).buy_price));
            sale_price.setText(String.valueOf(list_item.get(i).sale_price));




            return view;
        }
    }
}

/*

String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }


 */