package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class add_goods_db extends AppCompatActivity {

    Databases databases=new Databases(this);
    boolean check_impot;
    ZXingScannerView scannerView;

    int date_place = 0;
    public static EditText Text_barcode,Text_name_goods,Text_quantity,Text_buy_price,Text_sale_price,Text_date_ex,Text_date_sale,Text_quantity_box,
            Text_q_type,Text_q_quantity,Text_q_buy_price,Text_q_sale_price;
    Button add_tg_btn,date_sale_btn,date_ex_btn,exeit,save;
    private Dialog Date_Dialog;
    private SimpleDateFormat date_format;
    private Calendar calendar;

    dialog_view_addtypes dva=new dialog_view_addtypes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_add_goods_db);
        //////////////////////////////////////////////////////////////////
        add_tg_btn =findViewById(R.id.add_tg_btn);
        date_sale_btn =findViewById(R.id.date_show_sale);
        date_ex_btn =findViewById(R.id.date_show_ex);
        /////////////////////////////////////////////////////////////////quantity -> dialog
        exeit =findViewById(R.id.cancel_tg_dailog);
        save =findViewById(R.id.save_tg_add);

        Text_q_type =findViewById(R.id.q_type);
        Text_q_quantity =findViewById(R.id.q_quantity);
        Text_q_buy_price =findViewById(R.id.q_buy_price);
        Text_q_sale_price =findViewById(R.id.q_sale_price);
        /////////////////////////////////////////////////////////////////googs + quantity
        Text_barcode =findViewById(R.id.add_barcode_txt);
        Text_name_goods =findViewById(R.id.add_name_goods);
        Text_quantity =findViewById(R.id.add_quantity);
        Text_quantity_box =findViewById(R.id.add_quantity_box);
        Text_buy_price =findViewById(R.id.add_buy_price);
        Text_sale_price =findViewById(R.id.add_sale_price);
        Text_date_ex =findViewById(R.id.add_date_ex);////الانتهاء
        Text_date_sale =findViewById(R.id.add_date_sale);

        //////////////////////////////////////////////////////////////////


        /////////////////////////Date Picker///////////////////////////////////
        calendar=Calendar.getInstance();
        date_format= new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        date_ex_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_place=0;
                date_picker ();
            }
        });

        date_sale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_place=1;
                date_picker ();
            }
        });

        //////////////////////////////Add List Item//////////////////////////////////////
        ListView list = (ListView) findViewById(R.id.list_quantity);
        ArrayList<list_item_qnuatitytype> q_list = new ArrayList<list_item_qnuatitytype>();
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);
        //////////////////////////////////////////////////////////////////////////////////

        add_tg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shaw quantity adder
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

            //double g=Double.parseDouble(Text_quantity_box.getEditableText().toString());

            if (check_baracod==0) {

                //Toast.makeText(this, "Good Code", Toast.LENGTH_SHORT).show();

                boolean result = databases.insert_goods(Text_barcode.getEditableText().toString(), Text_name_goods.getEditableText().toString(),
                        Double.parseDouble(Text_quantity.getEditableText().toString()),Double.parseDouble(Text_quantity_box.getEditableText().toString()),
                        Text_date_ex.getEditableText().toString(),
                        Text_date_sale.getEditableText().toString());

                if (result){

                    int id =databases.get_id_goods(Text_barcode.getEditableText().toString());// جلب رقم البضاعة

                    boolean result2 = databases.insert_quantity("حبة", Double.parseDouble(Text_buy_price.getEditableText().toString()),
                            id, Double.parseDouble(Text_sale_price.getEditableText().toString()));

                    if (result2){
                        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();//
                    }
                    else
                        Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private boolean check_impot_googs() {
        if(TextUtils.isEmpty(Text_barcode.getText().toString().trim())||TextUtils.isEmpty(Text_name_goods.getText().toString().trim())
                ||TextUtils.isEmpty(Text_quantity.getText().toString().trim())||TextUtils.isEmpty(Text_buy_price.getText().toString().trim())
                ||TextUtils.isEmpty(Text_sale_price.getText().toString().trim())||TextUtils.isEmpty(Text_date_ex.getText().toString().trim())
                ||TextUtils.isEmpty(Text_date_sale.getText().toString().trim())||TextUtils.isEmpty(Text_quantity_box.getText().toString().trim())){

            //mEmail.setError("Email is Required.");

            Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
           // return;
            check_impot =false;
        }else {
            check_impot =true;
        }
        return check_impot;
    }

    private boolean check_impot_quantity(){
        boolean check_impot;
        if(TextUtils.isEmpty(Text_q_type.getText().toString().trim())||TextUtils.isEmpty(Text_q_quantity.getText().toString().trim())
                ||TextUtils.isEmpty(Text_q_buy_price.getText().toString().trim())||TextUtils.isEmpty(Text_q_sale_price.getText().toString().trim())){

            //mEmail.setError("Email is Required.");

            Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
            // return;
            check_impot =false;
        }else {
            check_impot =true;
        }
        return check_impot;
    }

    public void save_quantity(View view){

        int id =databases.get_id_goods(Text_barcode.getEditableText().toString());// جلب رقم البضاعة

//        boolean result2 = databases.insert_quantity(Text_q_type.getEditableText().toString(), Double.parseDouble(Text_buy_price.getEditableText().toString()),
//                id, Double.parseDouble(Text_sale_price.getEditableText().toString()));
//
//        if (result2){
//            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();//
//        }
//        else
//            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
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

            TextView quantity = (TextView) view.findViewById(R.id.q_quantity_item);

            TextView buy_price = (TextView) view.findViewById(R.id.q_buy_item);

            TextView sale_price = (TextView) view.findViewById(R.id.q_sale_item);

            name.setText(list_item.get(i).name );
            quantity.setText(String.valueOf(list_item.get(i).quantity) );
            buy_price.setText(String.valueOf(list_item.get(i).buy_price));
            sale_price.setText(String.valueOf(list_item.get(i).sale_price));




            return view;
        }
    }

    public void date_picker ()
    {
        //Dialog Date viewer
        Date_Dialog = new Dialog(this);
        Date_Dialog.setContentView(R.layout.dailog_date);
        final DatePicker picker=(DatePicker) Date_Dialog.findViewById(R.id.calendar_View);
        Button date_viewer = (Button) Date_Dialog.findViewById(R.id.date_veiwer);

        picker.setMinDate(calendar.getTimeInMillis());

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.YEAR,5);
        picker.setMaxDate(calendar1.getTimeInMillis());

        date_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Date to Edit Text
                final Calendar calendar2 = Calendar.getInstance();

                calendar2.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                if(date_place == 0)
                {
                    Text_date_ex.setText(date_format.format(calendar2.getTime()));
                }
                else { Text_date_sale.setText(date_format.format(calendar2.getTime())); }
                Date_Dialog.dismiss();
            }
        });
        Date_Dialog.show();
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