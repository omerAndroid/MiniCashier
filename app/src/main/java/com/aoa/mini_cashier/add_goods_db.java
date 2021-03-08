package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

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
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

    Databases databases=new Databases(this);
    boolean check_impot;
    ZXingScannerView scannerView;

    public static EditText Text_barcode,Text_name_goods,Text_quantity,Text_buy_price,Text_sale_price,Text_date_ex;
    Button add_tg_btn;
    dialog_view_addtypes dva=new dialog_view_addtypes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_add_goods_db);

        add_tg_btn =findViewById(R.id.add_tg_btn);

        Text_barcode =findViewById(R.id.add_barcode_txt);
        Text_name_goods =findViewById(R.id.add_name_goods);
        Text_quantity =findViewById(R.id.add_quantity);
        Text_buy_price =findViewById(R.id.add_buy_price);
        Text_sale_price =findViewById(R.id.add_sale_price);
        Text_date_ex =findViewById(R.id.add_date_ex);

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

    public void seve_goods(View view) {
        if(check_impot_googs()){
            int check_baracod=databases.check_baracod(Text_barcode.getText().toString().trim());

            if (check_baracod==0) {
                Toast.makeText(this, "Good Code", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean check_impot_googs() {
        if(TextUtils.isEmpty(Text_barcode.getText().toString().trim())||TextUtils.isEmpty(Text_name_goods.getText().toString().trim())
                ||TextUtils.isEmpty(Text_quantity.getText().toString().trim())||TextUtils.isEmpty(Text_buy_price.getText().toString().trim())
                ||TextUtils.isEmpty(Text_sale_price.getText().toString().trim())||TextUtils.isEmpty(Text_date_ex.getText().toString().trim())){
            //mEmail.setError("Email is Required.");
            Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
           // return;
            check_impot =false;
        }else {
            check_impot =true;
        }
        return check_impot;
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