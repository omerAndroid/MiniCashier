package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class add_goods_db extends AppCompatActivity {

    Button add_tg_btn;
    dialog_view_addtypes dva=new dialog_view_addtypes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_db);

        add_tg_btn =findViewById(R.id.add_tg_btn);
        add_tg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dva.show(getSupportFragmentManager(), "إضافة نوع");
            }
        });

    }
}