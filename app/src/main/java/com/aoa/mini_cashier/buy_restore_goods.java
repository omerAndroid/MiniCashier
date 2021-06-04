package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class buy_restore_goods extends AppCompatActivity {

    public Databases databases = new Databases(this);
    Dialog customer_data,edit_item;
    Button list_options,save_btn,add_item_list;
    EditText c_name,name_sender,discount_bill_items,total_price;
    public static boolean  check_impot;
    SwitchMaterial buy_or_restore,money_or_debt;
    ArrayList<list_buy_restore> list_item= new ArrayList<>();
    ArrayList<String> item_Array= new ArrayList<String>();

    ArrayList<String> name_prod= new ArrayList<>();
    ArrayList<String> purchase_price= new ArrayList<>();
    ArrayList<String> selling_price= new ArrayList<>();
    ArrayList<String> quantity= new ArrayList<>();
    ArrayList<String> quantity_type= new ArrayList<>();

    double aDouble=0;

    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView Text_add_name_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_restore_goods);

        list_options = findViewById(R.id.list_options);
        save_btn = findViewById(R.id.save_bills);
        add_item_list = findViewById(R.id.add_item_list);

        c_name = findViewById(R.id.customer_name);////n  اسم العميل
        Text_add_name_item= findViewById(R.id.add_name_item);/////baracod
        name_sender = findViewById(R.id.name_sender);//////n     اسم المستلم
        discount_bill_items = findViewById(R.id.discount_bill_items);//////n      الخصم
        total_price = findViewById(R.id.total_price);//////n      المجموع

        buy_or_restore = findViewById(R.id.buy_or_restore);
        money_or_debt = findViewById(R.id.cash_or_debt);


        /////////////////////////////////
        get_ALL_baracode_name_g();
        discount_bill_items.setText("0");



        ListView list = (ListView) findViewById(R.id.list_buy_restore);
        //////////////////////// Add goods in list //////////////////////////////////////////
        ArrayList<list_buy_restore> q_list = new ArrayList<list_buy_restore>();
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);

        ///////////////////////////////////////////////////////////////////////////////////////
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView goods_name = (TextView)view.findViewById(R.id.goods_name);
                TextView goods_q_type = (TextView)view.findViewById(R.id.goods_q_type);
                TextView goods_quanitity = (TextView)view.findViewById(R.id.goods_quanitity);
                TextView goods_buy = (TextView)view.findViewById(R.id.goods_buy);
                TextView goods_sale = (TextView)view.findViewById(R.id.goods_sale);

                item_Array.clear();

                item_Array.add(goods_name.getText().toString());
                item_Array.add(goods_q_type.getText().toString());
                item_Array.add(goods_quanitity.getText().toString());
                item_Array.add(goods_buy.getText().toString());
                item_Array.add(goods_sale.getText().toString());

                edit_item_dialog();
            }
        });
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

        buy_or_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buy_or_restore.isChecked())
                {
                    buy_or_restore.setText("استرجاع");
                }
                else {
                    buy_or_restore.setText("بيع");
                }

            }
        });

        money_or_debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money_or_debt.isChecked())
                {
                    money_or_debt.setText("آجل");
                }
                else {
                    money_or_debt.setText("نقد");
                }

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1=Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
                String timeAdded=""+ DateFormat.format("dd/MM/yyyy",calendar1);

                if (money_or_debt.getText().toString().equals("نقد")&&buy_or_restore.getText().toString().equals("بيع")){
                    if (check_impot_save_btn()){
                        Toast.makeText(buy_restore_goods.this, "ok", Toast.LENGTH_SHORT).show();
                        print ();
                        databases.insert_bills(timeAdded,"نقد",Double.parseDouble(total_price.getText().toString()),
                                Double.parseDouble(total_price.getText().toString()),Double.parseDouble(discount_bill_items.getText().toString()));
                    }
                }else {
                    add_customer_data();
                }
            }
        });
    }

    private boolean check_impot_save_btn() {

        if (money_or_debt.getText().toString().equals("نقد")){//TextUtils.isEmpty(Text_add_name_item.getText().toString().trim()) ||

            if ( TextUtils.isEmpty(discount_bill_items.getText().toString().trim())) {

                //mEmail.setError("Email is Required.");

                Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
                // return;
                check_impot = false;
            }else {
                check_impot = true;
            }
        }

        return check_impot;
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

        Text_add_name_item =
                findViewById(R.id.add_name_item);

        Text_add_name_item.setAdapter(adapter1);

        Text_add_name_item.setThreshold(1);//will start working from first

        Text_add_name_item.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
           // Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات

            tcack_camera(item);
            Text_add_name_item.setText("");
        });
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

    private void Packing_for_goods(String item, String who) {

        int id_goods;
        if (who.equals("Allname_g")){
            id_goods = databases.get_one_goods(item);


        }else {
             id_goods = databases.get_id_goods(item);
        }

        String[] g=databases.get_one_goods(item,who);///////n     الاسم
        String[] q=databases.get_one_quantity(id_goods);///////n    نوع الكمية
        double[] q_Double=databases.get_one_quantity_double(id_goods);///////n    نوع الكمية

       // list_item = new ArrayList<>();
        for (int j=0;j<1;j++){

            list_item.add(new list_buy_restore(g[1],q[0],"1",theack_aggen(new DecimalFormat("#.00#").format( q_Double[1])),
                    theack_aggen(new DecimalFormat("#.00#").format( q_Double[0]))));
        }

        ///n      عملية اضافة المنتجات في المصفوفات
        name_prod.add(g[1]);
        purchase_price.add(theack_aggen(new DecimalFormat("#.00#").format( q_Double[1])));
        selling_price.add(theack_aggen(new DecimalFormat("#.00#").format( q_Double[0])));
        quantity.add("1");
        quantity_type.add(q[0]);



        aDouble +=Double.parseDouble(theack_aggen(new DecimalFormat("#.00#").format( q_Double[0])));
        total_price.setText(theack_aggen(new DecimalFormat("#.00#").format( aDouble)));


        ListView list =  findViewById(R.id.list_buy_restore);
        buy_restore_goods.ListAdupter ad =new buy_restore_goods.ListAdupter(list_item);
        list.setAdapter(ad);
    }

    public void print (){

        Object[] arr = new Object[name_prod.size()];

        for(int i=0; i<arr.length; i++) {
            System.out.println(" = [ 1 ]الاسم" +name_prod.get(i));
            System.out.println(" = [ 2 ] سعر البيع" +purchase_price.get(i));
            System.out.println(" = [ 3 ]سعر الشراء" +selling_price.get(i));
            System.out.println(" = [ 4 ]الكمية" +quantity.get(i));
            System.out.println(" = [ 5 ]نوع الكمية" +quantity_type.get(i));
            System.out.println("00000000000000000000000000000000000000000000000000000");
        }





    }

    /////////////////n     خوارزمية تساعد لعملية عرض وادخال الارقام

    public String theack_aggen(@NonNull String s){
        StringBuilder ss= new StringBuilder();

        boolean b=false;
        for (int i = 0; i<= s.length()-1; i++){
            if (String.valueOf(s.charAt(i)).equals("٠")){
                ss.append("0");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٩")){
                ss.append("9");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("١")){
                ss.append("1");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٢")){
                ss.append("2");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٣")){
                ss.append("3");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٤")){
                ss.append("4");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٥")){
                ss.append("5");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                ss.append("6");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٧")){
                ss.append("7");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٨")){
                ss.append("8");
                b=true;
            }else if(String.valueOf(s.charAt(i)).equals("٫")){
                ss.append(".");
                b=true;
            }
        }

        if (b){
            return ss.toString();
        }else {
            return s;
        }

    }

    class ListAdupter extends BaseAdapter {
        ArrayList<list_buy_restore> list_item;
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.buy_restore_item,null);

            TextView name = (TextView) view.findViewById(R.id.goods_name);

            TextView quantity_type = (TextView) view.findViewById(R.id.goods_q_type);


            TextView goods_quanitity = (TextView) view.findViewById(R.id.goods_quanitity);

            TextView buy_price = (TextView) view.findViewById(R.id.goods_buy);

            TextView sale_price = (TextView) view.findViewById(R.id.goods_sale);///n   البيع


            name.setText(list_item.get(i).name );
            quantity_type.setText(String.valueOf(list_item.get(i).quantity_type));
            goods_quanitity.setText(String.valueOf(list_item.get(i).goods_quanitity));
            sale_price.setText(String.valueOf(list_item.get(i).sale_price));
            buy_price.setText(String.valueOf(list_item.get(i).buy_price));
            return view;
        }
    }

    public void add_customer_data () {

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

    public void edit_item_dialog () {

        //Dialog Customer Data viewer
        edit_item = new Dialog(this);
        edit_item.setContentView(R.layout.item_idet_dialog);
        edit_item.setTitle("بيانات المنتج");
        final EditText barcode_item_buy=(EditText) edit_item.findViewById(R.id.barcode_item_buy);
        final EditText name_item_buy=(EditText) edit_item.findViewById(R.id.name_item_buy);
        final EditText quantity_item_buy=(EditText) edit_item.findViewById(R.id.quantity_item_buy);
        final EditText buy_item_buy=(EditText) edit_item.findViewById(R.id.buy_item_buy);
        final EditText sale_item_buy=(EditText) edit_item.findViewById(R.id.sale_item_buy);

        final Spinner quantitytype_item_buy=(Spinner) edit_item.findViewById(R.id.quantitytype_item_buy);

        final Button delete_item_buy=(Button) edit_item.findViewById(R.id.delete_item_buy);
        final Button save_item_buy = (Button) edit_item.findViewById(R.id.save_item_buy);

        ////////////////////////////////////////////////////////////////
        name_item_buy.setText(item_Array.get(0));
        quantity_item_buy.setText(item_Array.get(2));
        buy_item_buy.setText(item_Array.get(3));
        sale_item_buy.setText(item_Array.get(4));
        //quantitytype_item_buy.setText(item_Array.get(1));


        save_item_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save edit item buy


                edit_item.dismiss();
            }
        });
        edit_item.show();
    }

}
