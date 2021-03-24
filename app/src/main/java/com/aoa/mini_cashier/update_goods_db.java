package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class update_goods_db extends AppCompatActivity {

    public Databases databases = new Databases(this);

    public static Button searsh_goods_btn;

    public static AutoCompleteTextView ALL_baracode_name_g;

    ArrayList<list_item_update> q_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goods_db);

        searsh_goods_btn =findViewById(R.id.searsh_goods_btn);

        ////////////////////////Add List Item//////////////////////////////////////////
        ListView listView =findViewById(R.id.list_goods_db);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView barcode =view.findViewById(R.id.barcode_view);
                Intent intent =new Intent(update_goods_db.this,add_goods_db.class);
                intent.putExtra("key",1);
                intent.putExtra("barcode",barcode.getText().toString());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder =new AlertDialog.Builder(update_goods_db.this);
                builder.setMessage("هل تريد حذف المنتج");
                builder.setTitle("حذف منتج");
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView barcode =view.findViewById(R.id.barcode_view);
                        if(databases.get_delete_goods(barcode.getText().toString()))
                        {
                            makeText(update_goods_db.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            makeText(update_goods_db.this, "No Way", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return false;
            }
        });
        //////////////////////////////////////////////////////////////////////////////////
        get_ALL_baracode_name_g();

        get_goods();


        searsh_goods_btn.setOnClickListener(v -> {
            Intent intent=new Intent(this,ScanCodeActivity.class);
            intent.putExtra("page","update_goods_db");
            startActivity(intent);
            //startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
        });

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
            View view =inflater.inflate(R.layout.update_goods_item,null);

            TextView barcode = (TextView) view.findViewById(R.id.barcode_view);

            TextView name = (TextView) view.findViewById(R.id.goods_view);

            TextView quantity = (TextView) view.findViewById(R.id.quantity_veiw);

            TextView date_ex = (TextView) view.findViewById(R.id.date_ex_veiw);

            TextView buy_date = (TextView) view.findViewById(R.id.buy_date_veiw);





            barcode.setText(list_item.get(i).barcode );
            name.setText(list_item.get(i).name );
            quantity.setText(String.valueOf(list_item.get(i).quantity ));
            buy_date.setText(list_item.get(i).date_buy);
            date_ex.setText(list_item.get(i).date_ex);



            return view;
        }
    }

    public void get_goods()
    {
        try {
            ListView list = findViewById(R.id.list_goods_db);
            ArrayList<list_item_update> list_item = new ArrayList<list_item_update>();
            String[] arrayList = databases.get_All_goods();
            for (int i = 0; i < arrayList.length; i++) {
                list_item.add(new list_item_update(arrayList[i], arrayList[i + 1], arrayList[i + 2], arrayList[i + 3], arrayList[i + 4]));
                i += 5;
            }
            ListAdapter adapter = new ListAdupter(list_item);
            list.setAdapter(adapter);
        }catch (Exception e){}

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

        ALL_baracode_name_g = (AutoCompleteTextView)
                findViewById(R.id.searsh_goods_txt);

        ALL_baracode_name_g.setAdapter(adapter1);

        ALL_baracode_name_g.setThreshold(1);//will start working from first

        ALL_baracode_name_g.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات

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

            //Packing_for_goods(item,who);

        });
    }

    private void Packing_for_goods(String item, String who) {

        if (who.equals("Allbaracod")){

            int id = databases.get_id_goods(item);
            int a=databases.read_Tname_q_type(id);
            Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();
            String[] g=databases.get_ALLq_qnuatitytype(id);
            makeText(this, String.valueOf(g.length), Toast.LENGTH_SHORT).show();

            int i=0;
            for (int j=0;j<a;j++){

                makeText(this, g[0], Toast.LENGTH_SHORT).show();
                makeText(this, g[1], Toast.LENGTH_SHORT).show();
                makeText(this, g[2], Toast.LENGTH_SHORT).show();
                makeText(this, g[3], Toast.LENGTH_SHORT).show();

                //q_list.add(new list_item_update(g[0],g[1],g[2],g[3]));
                i+=3;
            }

            ListView list = (ListView) findViewById(R.id.list_goods_db);
            ListAdupter ad =new ListAdupter(q_list);
            list.setAdapter(ad);

        }else {
            Toast.makeText(this, "Allname_g", Toast.LENGTH_SHORT).show();
        }
    }
}