package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.DB.LocalBackup;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button au_menu,buy_btn,purchases_btn, options,bills,max_account_btn,max_quintity_btn,reports_btn;
    //public Databases databases = new Databases(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        LocalBackup localBackup = new LocalBackup(this);
        Databases databases=new Databases(this);
        String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
        localBackup.performBackup(databases, outFileName);


        //////////////////////n        حفظ لمرة واحده
        sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
        String tecack_1=sharedPreferences.getString("key","");

        if (!tecack_1.equals("true")){
            seve_quantity_type_and_department_first();
        }

        bills = (Button) findViewById(R.id.bills);
        options = (Button) findViewById(R.id.options);
        au_menu = (Button) findViewById(R.id.add_update_menu);
        buy_btn = (Button) findViewById(R.id.buy_btn);
        purchases_btn = (Button) findViewById(R.id.purchases_btn);
        max_quintity_btn = (Button) findViewById(R.id.max_quintity_btn);
        max_account_btn = (Button) findViewById(R.id.max_account_btn);
        reports_btn = (Button) findViewById(R.id.reports_btn);

        options.setOnClickListener(v -> startActivity(new Intent(this,Settings.class)));

        buy_btn.setOnClickListener(v -> startActivity(new Intent(this,buy_restore_goods.class)));

        purchases_btn.setOnClickListener(v -> startActivity(new Intent(this,resources.class).
                putExtra("key","purchases_btn")));////m مشتريات

        bills.setOnClickListener(v -> startActivity(new Intent(this,resources.class).
                putExtra("key","bills")));///////n  فواتير

        au_menu.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.add_update,popupMenu.getMenu());

            /*MenuPopupHelper popupHelper = new MenuPopupHelper(MainActivity.this, (MenuBuilder) popupMenu.getMenu(),v);
            popupHelper.setForceShowIcon(true);*/
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if(id==R.id.add_goods_menu)
                    {
                        Intent intent = new Intent(MainActivity.this ,add_goods_db.class);
                        startActivity(intent);
                    }

                    if(id==R.id.update_goods_menu)
                    {
                        startActivity(new Intent(MainActivity.this,update_goods_db.class).
                                putExtra("key","update_goods_menu"));
                    }
                    return false;

                }
            });

        });

        max_account_btn.setOnClickListener(view -> startActivity(new Intent(this,resources.class).
                putExtra("key","max_account_btn")));////n

        max_quintity_btn.setOnClickListener(view -> startActivity(new Intent(this,update_goods_db.class).
                putExtra("key","max_quintity_btn")));

        reports_btn.setOnClickListener(view -> startActivity(new Intent(this,Reports.class)));

    }


    ///////n       حفظ الكميات الرئيسية لاول مره فقط
    private void seve_quantity_type_and_department_first() {
        Databases databases = new Databases(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key","true");
        editor.apply();
        ////////////   quantity_type
        Toast.makeText(this, "key true", Toast.LENGTH_SHORT).show();
        databases.insert_new_quantity_type("كرتون");
        databases.insert_new_quantity_type("درزن");
        databases.insert_new_quantity_type("حبة");

        //////  department
        databases.insert_new_department("عام");

        databases.insert_new_money_box(0);/// id->1
    }
}
