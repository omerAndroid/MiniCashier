package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aoa.mini_cashier.DB.Databases;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Databases databases=new Databases(this);
    }
}
