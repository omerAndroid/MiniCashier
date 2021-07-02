package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.Invoice.createpdf_2;
import com.aoa.mini_cashier.item_classes.purchases_item_class;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class bills extends AppCompatActivity {

    Dialog customer_data;
    Button add_resource;

    @SuppressLint("StaticFieldLeak")
    public ListView list_resource;
    public Databases databases = new Databases(this);
    public static ArrayList<list_item_resource> q_list = new ArrayList<>();
    ArrayList<purchases_item_class> q_list_2 = new ArrayList<>();
    AutoCompleteTextView c_name;
    String agent;
    ArrayList<String> id_bills= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        list_resource = findViewById(R.id.list_resource);
        add_resource = findViewById(R.id.add_resource);
        c_name = findViewById(R.id.searsh_resource);
        get_ALL_baracode_name_g();
        listShow_bills();
        add_resource.setOnClickListener(v -> add_customer_data_2 ());
        list_resource.setOnItemClickListener((parent, view, position, id) -> {

            //Toast.makeText(bills.this, q_list_2.get(position).getDate_expare(), Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(bills.this, purchases.class);
            intent.putExtra("id_bills",q_list_2.get(position).getDate_expare());
            intent.putExtra("name_agent",q_list_2.get(position).getBarcode());
            intent.putExtra("class","bills");//address

            startActivity(intent);

        });

        findViewById(R.id.add_bills).setOnClickListener(v -> {
            //show menu options for bills
            PopupMenu popupMenu = new PopupMenu(bills.this,v);
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
                    Toast.makeText(bills.this, "طباعة الفاتورة", Toast.LENGTH_SHORT).show();
                }

                //share bills
                if(id==R.id.share_bill_menu)
                {
                    createpdf_2 createpdf = new createpdf_2();
                    createpdf.create("آجل", agent,databases,this,"1",id_bills);
                }

                //open PDF file
                if(id==R.id.open_pdf_menu)
                {
                    createpdf_2 createpdf = new createpdf_2();
                    createpdf.create("آجل", agent,databases,this,"2",id_bills);
                }
                return false;
            });
        });
    }

    public void listShow_bills(){
        String[] resource=databases.get_All_bills("null");
        double[] All_bills_Double=databases.get_All_bills_double("null");
        q_list.clear();
        q_list = new ArrayList<>();
        q_list_2 = new ArrayList<>();
        String s;
        int i=0,g=0;
        for (int j=0;j<databases.read_The_bills();j++){
            //Toast.makeText(this, resource[i+5], Toast.LENGTH_SHORT).show();
            if (resource[i+3].equals("")){
                s="لايوجد";
            }else {
                s=resource[i+3];
            }

            q_list_2.add(new purchases_item_class(s,resource[i+4],theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[g])),
                    theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[g+1])),resource[i+2],resource[i],resource[i+1],resource[i+5]));
            i+=6;
            g+=2;
        }
        //////////////////////////////Add List Item//////////////////////////////////////
        ListAdupter_2 ad = new ListAdupter_2(q_list_2);
        list_resource.setAdapter(ad);

    }

    public void add_customer_data_2 () {

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

        //name.setText(c_name.getText());

        data_save.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(name.getText().toString())||!TextUtils.isEmpty(c_address.getText().toString())||
                    !TextUtils.isEmpty(c_phone1.getText().toString())||!TextUtils.isEmpty(c_phone2.getText().toString())||
                    !TextUtils.isEmpty(c_email.getText().toString())||!TextUtils.isEmpty(c_password.getText().toString())){

                databases.insert_agent(name.getText().toString(),c_address.getText().toString(),c_phone1.getText().toString(),
                        c_phone2.getText().toString(),c_email.getText().toString(),c_password.getText().toString());

                customer_data.dismiss();
            }
        });
        customer_data.show();
    }

    public void get_ALL_baracode_name_g() {

        String[] Allbaracod=databases.get_All_bills_agent();
        ArrayList<String> name= new ArrayList<>();

        for (String s : Allbaracod) {
            if (!name.contains(s)) {
                name.add(s);
            }
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, name);

        c_name =
                findViewById(R.id.searsh_resource);

        c_name.setAdapter(adapter1);

        c_name.setThreshold(1);//will start working from first

        c_name.setOnItemClickListener((parent, arg1, pos, id) -> {
            findViewById(R.id.add_resource).setVisibility(View.GONE);
            findViewById(R.id.add_bills).setVisibility(View.VISIBLE);

            String item = parent.getItemAtPosition(pos).toString();
             int id_agent=databases.get_id_agent(item);

            String[] All_bills=databases.get_All_bills(String.valueOf(id_agent));
            double[] All_bills_Double=databases.get_All_bills_double(String.valueOf(id_agent));
            q_list.clear();
             q_list = new ArrayList<>();
             q_list_2 = new ArrayList<>();
            int i=0,g=0;
            for (int j=0;j<databases.read_The_bills_2(id_agent);j++){

                q_list_2.add(new purchases_item_class(All_bills[i+3],All_bills[i+4],theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[g])),
                        theack_aggen(new DecimalFormat("#.00#").format(All_bills_Double[g+1])) ,All_bills[i+2],All_bills[i],All_bills[i+1],
                        All_bills[i+5]));

                id_bills.add(All_bills[i+5]);
                agent= All_bills[i+3];
                i+=6;
                g+=2;
            }
            //////////////////////////////Add List Item//////////////////////////////////////
            ListAdupter_2 ad = new ListAdupter_2(q_list_2);
            list_resource.setAdapter(ad);

            c_name.setText("");
        });

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

    class ListAdupter_2 extends BaseAdapter {
        ArrayList<purchases_item_class> list_item_2;
        ListAdupter_2(ArrayList<purchases_item_class> list_item){
            this.list_item_2 = list_item ;
        }

        @Override
        public int getCount() {
            return list_item_2.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list_item_2.get(position).name;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.purchases_item_2,null);

            TextView purchases_item_barcode =  view.findViewById(R.id.purchases_item_barcode);

            TextView purchases_item_name =view.findViewById(R.id.purchases_item_name);


            TextView purchases_item_sale =  view.findViewById(R.id.purchases_item_sale);
            TextView purchases_item_quintity =  view.findViewById(R.id.purchases_item_quintity);
            TextView purchases_item_total =  view.findViewById(R.id.purchases_item_total);
            TextView purchases_item_free_guintity =  view.findViewById(R.id.purchases_item_free_guintity);
            TextView purchases_item_date_purchase =  view.findViewById(R.id.purchases_item_date_purchase);
            TextView purchases_item_date_expare =  view.findViewById(R.id.purchases_item_date_expare);



            purchases_item_barcode.setText(list_item_2.get(i).barcode );
            purchases_item_name.setText(String.valueOf(list_item_2.get(i).name));
            purchases_item_sale.setText(String.valueOf(list_item_2.get(i).buy_price));
            purchases_item_quintity.setText(String.valueOf(list_item_2.get(i).quintity));
            purchases_item_total.setText(String.valueOf(list_item_2.get(i).total));
            purchases_item_free_guintity.setText(String.valueOf(list_item_2.get(i).free_quintity));
            purchases_item_date_purchase.setText(String.valueOf(list_item_2.get(i).date_purchase));
            purchases_item_date_expare.setText(String.valueOf(list_item_2.get(i).date_expare));

            return view;
        }
    }
}