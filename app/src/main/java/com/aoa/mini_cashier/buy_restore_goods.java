package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.aoa.mini_cashier.RED_QR.ScanCodeActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class buy_restore_goods extends AppCompatActivity {

    public Databases databases = new Databases(this);
    Dialog customer_data,edit_item;
    Button list_options,save_btn,add_item_list;
    EditText name_sender,discount_bill_items,total_price;
    public static boolean  check_impot;
    SwitchMaterial buy_or_restore,money_or_debt;
    ArrayList<list_buy_restore> list_item= new ArrayList<>();
    ArrayList<String> item_Array= new ArrayList<>();
    TextView goods_quanitity;
    //
    ArrayList<String> name_prod= new ArrayList<>();
    ArrayList<String> purchase_price= new ArrayList<>();
    ArrayList<String> selling_price= new ArrayList<>();
    ArrayList<String> quantity= new ArrayList<>();
    ArrayList<String> quantity_type= new ArrayList<>();//c_name

    boolean isCheck_number=true;
    AutoCompleteTextView c_name;
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

        ListView list =  findViewById(R.id.list_buy_restore);
        //////////////////////// Add goods in list //////////////////////////////////////////
        ArrayList<list_buy_restore> q_list = new ArrayList<>();
        ListAdupter ad =new ListAdupter(q_list);
        list.setAdapter(ad);

        ///////////////////////////////////////////////////////////////////////////////////////
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView goods_name = view.findViewById(R.id.goods_name);
            TextView goods_q_type =view.findViewById(R.id.goods_q_type);
             goods_quanitity = view.findViewById(R.id.goods_quanitity);
            TextView goods_buy = view.findViewById(R.id.goods_buy);
            TextView goods_sale = view.findViewById(R.id.goods_sale);

            item_Array.clear();

            Toast.makeText(buy_restore_goods.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

            item_Array.add(goods_name.getText().toString());
            item_Array.add(goods_q_type.getText().toString());
            item_Array.add(goods_quanitity.getText().toString());
            item_Array.add(goods_buy.getText().toString());
            item_Array.add(goods_sale.getText().toString());

            edit_item_dialog();
        });
        ////////////////////////show list Options of bills/////////////////////////////////////
        list_options.setOnClickListener(v -> {
            //show menu options for bills
            PopupMenu popupMenu = new PopupMenu(buy_restore_goods.this,v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_bills_options,popupMenu.getMenu());

            /*MenuPopupHelper popupHelper = new MenuPopupHelper(MainActivity.this, (MenuBuilder) popupMenu.getMenu(),v);
            popupHelper.setForceShowIcon(true);*/
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
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
            });
        });
        ///////////////////////////////////////////////////////////////////////////////////

        buy_or_restore.setOnClickListener(v -> {
            if(buy_or_restore.isChecked())
            {
                buy_or_restore.setText("استرجاع");
            }
            else {
                buy_or_restore.setText("بيع");
            }

        });

        money_or_debt.setOnClickListener(v -> {
            if(money_or_debt.isChecked())
            {
                money_or_debt.setText("آجل");
            }
            else {
                money_or_debt.setText("نقد");
            }
        });

        save_btn.setOnClickListener(v -> {



                if (money_or_debt.getText().toString().equals("نقد") && buy_or_restore.getText().toString().equals("بيع")) {
                    if (check_impot_save_btn()) {
//                        Toast.makeText(buy_restore_goods.this, "ok", Toast.LENGTH_SHORT).show();
                        print();
                        if (name_prod.size() > 0) {
                            insert_bills("نقد", 0, null, Double.parseDouble(total_price.getText().toString()));
                            insert_products_bills();
                            quantity_stored9("بيع", "null");

                        }
                    }
                } else if (money_or_debt.getText().toString().equals("آجل") && buy_or_restore.getText().toString().equals("بيع")) {
                    if (check_impot_save_btn() && !TextUtils.isEmpty(c_name.getText().toString()) && !
                            TextUtils.isEmpty(name_sender.getText().toString()) && name_prod.size() > 0) {
                        if (databases.check_agent(c_name.getText().toString()) > 0) {
                            insert_bills("آجل", databases.check_agent(c_name.getText().toString()), name_sender.getText().toString(), 0);
                            insert_products_bills();
                            quantity_stored9("بيع", "آجل");
                        } else {
                            add_customer_data();
                        }
                    }
                    //Toast.makeText(buy_restore_goods.this, c_name.getText().toString()+name_sender.getText().toString(), Toast.LENGTH_SHORT).show();
                    //add_customer_data();
                } else if (money_or_debt.getText().toString().equals("نقد") && buy_or_restore.getText().toString().equals("استرجاع")) {
                    if (name_prod.size() > 0) {
                        quantity_stored9("استرجاع", "null");
                    }
                }


        });

        findViewById(R.id.bracode_reader).setOnClickListener(v -> {
            Intent intent=new Intent(buy_restore_goods.this, ScanCodeActivity.class);
            intent.putExtra("page","purchases");
            //startActivity(intent);
            startActivityForResult(intent,4);
        });
    }

    private void quantity_stored9(String text,String s) {
        double money = databases.get_money_box();
        Object[] arr = new Object[name_prod.size()];

        if (text.equals("بيع")){

            for(int i=0; i<arr.length; i++) {

                databases.get_update_quantity_stored(name_prod.get(i),
                 databases.get_quantity_stored(name_prod.get(i))-(1*get_quantity_q(quantity_type.get(i),name_prod.get(i))),///////n    العدد المحدد
                        1*get_quantity_q(quantity_type.get(i),name_prod.get(i)));///////n    العدد المحدد

            }
            if (!s.equals("آجل")){
                databases.get_insert_money_box(money+Double.parseDouble(total_price.getText().toString()));
            }

        }else {
            for(int i=0; i<arr.length; i++) {

                databases.get_update_quantity_stored(name_prod.get(i),
                databases.get_quantity_stored(name_prod.get(i))+(1*get_quantity_q(quantity_type.get(i),name_prod.get(i))),///////n    العدد المحدد
                        get_quantity_q(quantity_type.get(i),name_prod.get(i))-1);///////n    العدد المحدد
            }
            databases.get_insert_money_box(money-Double.parseDouble(total_price.getText().toString()));
        }
    }

    private void insert_bills(String paid_type,int id_agent,String recipient,double paid) {
        Calendar calendar1=Calendar.getInstance(Locale.getDefault());
        calendar1.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
        String timeAdded=""+ DateFormat.format("dd/MM/yyyy",calendar1);

        databases.insert_bills(timeAdded,paid_type,
                Double.parseDouble(total_price.getText().toString())-Double.parseDouble(discount_bill_items.getText().toString()),
                paid-Double.parseDouble(discount_bill_items.getText().toString()),
                Double.parseDouble(discount_bill_items.getText().toString()),
                id_agent,recipient);
    }

    private void insert_products_bills() {
        Object[] arr = new Object[name_prod.size()];
        int a=databases.read_bills();
        for(int i=0; i<arr.length; i++) {

            databases.insert_products_bills(name_prod.get(i),Double.parseDouble(purchase_price.get(i)),Double.parseDouble(selling_price.get(i)),
                    Double.parseDouble(quantity.get(i)),
                    a,quantity_type.get(i));

        }
    }

    private boolean check_impot_save_btn() {


            if ( TextUtils.isEmpty(discount_bill_items.getText().toString().trim())&&name_prod.size() > 0) {

                //mEmail.setError("Email is Required.");

                Toast.makeText(this, "أكمل البيانات", Toast.LENGTH_SHORT).show();
                // return;
                check_impot = false;
            }else {
                check_impot = true;
            }


        return check_impot;
    }

    public void get_ALL_baracode_name_g() {

        String[] Allbaracod=databases.get_ALLbaracod();

        String[] Allname_g=databases.get_ALLname_g();

        String[] ALLagent=databases.get_ALLagent();

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

        ///n    العميل
        ArrayAdapter<String> adapter_agent = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ALLagent);

        c_name = findViewById(R.id.customer_name);
        c_name.setAdapter(adapter_agent);
        c_name.setThreshold(1);

        c_name.setOnItemClickListener((parent, arg1, pos, id) -> {
            String item = parent.getItemAtPosition(pos).toString();
            // Toast.makeText(getApplication(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

            //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات

            c_name.setText(item);

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

        int x=0;
        double y=1;
        for(int i=0; i<arr.length; i++) {
            System.out.println(" = [ 1 ]الاسم" +name_prod.get(i));

            double[] ss=databases.get_quantity_q(databases.get_one_goods(name_prod.get(i)));
            String[] name_q_t=databases.get_ALLq_qnuatity(databases.get_one_goods(name_prod.get(i)));
            for(int s=0; s<databases.read_Thname_quantity_q(databases.get_one_goods(name_prod.get(i))); s++) {
                if (x==1){
                    y *=ss[s];
                }
                System.out.println("quantity_q : "+ss[s]);
                System.out.println("name_q_t : "+name_q_t[s]);
                if (name_q_t[s].equals(quantity_type.get(i))){
                    x=1;
                }

            }
            System.out.println("00000000000000000000000000000000000000000000000000000");
            System.out.println("omer  : "+y);
            System.out.println("00000000000000000000000000000000000000000000000000000");
            System.out.println(" = [ 2 ] سعر البيع" +purchase_price.get(i));
            System.out.println(" = [ 3 ]سعر الشراء" +selling_price.get(i));
            System.out.println(" = [ 4 ]الكمية" +quantity.get(i));
            System.out.println(" = [ 5 ]نوع الكمية" +quantity_type.get(i));



            System.out.println("00000000000000000000000000000000000000000000000000000");

            System.out.println( "quantity_stored : "+databases.get_quantity_stored(name_prod.get(i)));
            System.out.println("00000000000000000000000000000000000000000000000000000");
        }


    }

    public double get_quantity_q(String quantity_type ,String name){

        double[] ss=databases.get_quantity_q(databases.get_one_goods(name));
        String[] name_q_t=databases.get_ALLq_qnuatity(databases.get_one_goods(name));
        int x=0;
        double y=1;

        for(int s=0; s<databases.read_Thname_quantity_q(databases.get_one_goods(name)); s++) {
            if (x==1){
                y *=ss[s];
            }
            if (name_q_t[s].equals(quantity_type)){
                x=1;
            }
        }
       return y;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String retern,key;
        int intent_key = 4;
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

            TextView name =  view.findViewById(R.id.goods_name);

            TextView quantity_type =  view.findViewById(R.id.goods_q_type);


             goods_quanitity =  view.findViewById(R.id.goods_quanitity);

            TextView buy_price =  view.findViewById(R.id.goods_buy);

            TextView sale_price =  view.findViewById(R.id.goods_sale);///n   البيع


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
        final EditText name= customer_data.findViewById(R.id.name_customer);
        final EditText c_address= customer_data.findViewById(R.id.address_customer);
        final EditText c_phone1= customer_data.findViewById(R.id.Phone_customer_1);
        final EditText c_phone2= customer_data.findViewById(R.id.Phone_customer_2);
        final EditText c_email= customer_data.findViewById(R.id.E_mail_customer);
        final EditText c_password= customer_data.findViewById(R.id.password_customer);

        final Button data_save =  customer_data.findViewById(R.id.save_customer_data);

        name.setText(c_name.getText());

        data_save.setOnClickListener(v -> {
            //save Data of customer

            databases.insert_agent(name.getText().toString(),c_address.getText().toString(),c_phone1.getText().toString(),
                    c_phone2.getText().toString(),c_email.getText().toString(),c_password.getText().toString());

            customer_data.dismiss();
        });
        customer_data.show();
    }

    public void edit_item_dialog () {

        //Dialog Customer Data viewer
        edit_item = new Dialog(this);
        edit_item.setContentView(R.layout.item_idet_dialog);
        edit_item.setTitle("بيانات المنتج");
       // final EditText barcode_item_buy=(EditText) edit_item.findViewById(R.id.barcode_item_buy);
        final EditText name_item_buy= edit_item.findViewById(R.id.name_item_buy);
        final EditText quantity_item_buy= edit_item.findViewById(R.id.quantity_item_buy);
        final EditText buy_item_buy= edit_item.findViewById(R.id.buy_item_buy);
        final EditText sale_item_buy= edit_item.findViewById(R.id.sale_item_buy);

        final Spinner quantitytype_item_buy= edit_item.findViewById(R.id.quantitytype_item_buy);

       // final Button delete_item_buy=(Button) edit_item.findViewById(R.id.delete_item_buy);
        final Button save_item_buy =  edit_item.findViewById(R.id.save_item_buy);

        ////////////////////////////////////////////////////////////////
        String[] x=new String[1];
        x[0]=item_Array.get(1);
        ArrayAdapter<String> Array= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,x);
        ///////////////////////////////////////

        name_item_buy.setText(item_Array.get(0));
        quantitytype_item_buy.setAdapter(Array);
        quantity_item_buy.setText(item_Array.get(2));
        buy_item_buy.setText(item_Array.get(3));
        sale_item_buy.setText(item_Array.get(4));
        //quantitytype_item_buy.setText(item_Array.get(1));


        save_item_buy.setOnClickListener(v -> {
            //save edit item buy

            edit_item.dismiss();
        });
        edit_item.show();
    }

}
