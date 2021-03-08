package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.DB.LocalBackup;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button au_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        LocalBackup  localBackup = new LocalBackup(this);
//        Databases databases=new Databases(this);
//        String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
//        localBackup.performBackup(databases, outFileName);


        au_menu = (Button) findViewById(R.id.add_update_menu);

        au_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            Intent intent = new Intent(MainActivity.this ,update_goods_db.class);
                            startActivity(intent);
                        }
                        return false;

                    }
                });

            }
        });

    }
}
