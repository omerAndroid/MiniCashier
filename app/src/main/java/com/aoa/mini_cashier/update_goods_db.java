package com.aoa.mini_cashier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;

import java.util.ArrayList;


public class update_goods_db extends AppCompatActivity {

    public Databases databases = new Databases(this);

    @SuppressLint("StaticFieldLeak")
    public static Button searsh_goods_btn;

    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView ALL_baracode_name_g;
    ArrayList<list_item_update> q_list;
    ArrayList<list_item_update> list_item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goods_db);

        searsh_goods_btn =findViewById(R.id.searsh_goods_btn);

        ////////////////////////Add List Item//////////////////////////////////////////
        ListView listView =findViewById(R.id.list_goods_db);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder =new AlertDialog.Builder(update_goods_db.this);/////////////////////////n        يوجد خطا هنا تاكد لا تنسسسسسس
            builder.setMessage("أختر العملية التي تريدها");
            builder.setTitle("حذف أو تعديل المنتج ");
            builder.setPositiveButton("حذف", (dialog, which) -> {

                int idd = databases.get_id_goods(ALL_baracode_name_g.getText().toString());
                String s=String.valueOf(idd);
                databases.get_delete_goods(ALL_baracode_name_g.getText().toString());
                databases.get_delete_quantity(s);
                get_ALL_baracode_name_g();
                list_item.clear();
                q_list.clear();
                
            });
            builder.setNegativeButton("تعديل", (dialog, which) -> {
                //////////////////b يتم فتح كلاس لاضافة لكي يقوم بعملية التعديل
                Intent intent =new Intent(update_goods_db.this,add_goods_db.class);
                intent.putExtra("key",1);
                intent.putExtra("barcode",ALL_baracode_name_g.getText().toString());
                startActivity(intent);
            }).show();
            return false;
        });
        //////////////////////////////////////////////////////////////////////////////////
        get_ALL_baracode_name_g();

        get_goods();

        searsh_goods_btn.setOnClickListener(v -> {
            Intent intent=new Intent(this,ScanCodeActivity.class);
            intent.putExtra("page","update_goods_db");
            startActivityForResult(intent,1);
            //startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
        });

    }

    class ListAdupter extends BaseAdapter
    {
        ArrayList<list_item_update> list_item;
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.update_goods_item,null);

            TextView barcode =  view.findViewById(R.id.barcode_view);

            TextView name =view.findViewById(R.id.goods_view);

            TextView quantity = view.findViewById(R.id.quantity_veiw);

            TextView date_ex = view.findViewById(R.id.date_ex_veiw);

            TextView buy_date =  view.findViewById(R.id.buy_date_veiw);

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
            int a=databases.read_Tname();

            String[] arrayList = databases.get_All_goods();

            int i=0;
            for (int j = 0; j < a; j++) {
                list_item.add(new list_item_update(arrayList[i], arrayList[i + 1], arrayList[i + 2], arrayList[i + 3], arrayList[i + 4]));
                i += 5;
            }
            ListAdapter adapter = new ListAdupter(list_item);
            list.setAdapter(adapter);
        }catch (Exception ignored){

        }

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

        ALL_baracode_name_g =
                findViewById(R.id.searsh_goods_txt);

        ALL_baracode_name_g.setAdapter(adapter1);

        ALL_baracode_name_g.setThreshold(1);//will start working from first

        ALL_baracode_name_g.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات

            tcack_camera(item);

        });
    }

    private void Packing_for_goods(String item, String who) {

            String[] g=databases.get_one_goods(item,who);
            list_item = new ArrayList<>();
            q_list = new ArrayList<>();
            for (int j=0;j<1;j++){

                q_list.add(new list_item_update(g[0],g[1],g[2],g[3],g[4]));
            }

            ListView list =  findViewById(R.id.list_goods_db);
            ListAdupter ad =new ListAdupter(q_list);
            list.setAdapter(ad);
    }

    public void tcack_camera(String item){

        String[] Allbaracod=databases.get_ALLbaracod();

        String[] Allname_g=databases.get_ALLname_g();
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

        Packing_for_goods(item,who);
    }

    ///////n      يقوم بارسال تاكيد بانة تمت القراء باستخدام الكمايراء لكي يتم مل الليستة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String retern,key;
        int intent_key = 1;
        if (requestCode== intent_key){
            if (resultCode==RESULT_OK){

                assert data != null;
                key=data.getStringExtra("key");
                retern=data.getStringExtra("valu");

                if (key.equals("true")){
                    tcack_camera(retern);
                }
            }
        }
    }


}