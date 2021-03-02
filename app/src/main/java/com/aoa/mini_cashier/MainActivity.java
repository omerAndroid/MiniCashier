package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.DB.LocalBackup;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LocalBackup  localBackup = new LocalBackup(this);
        Databases databases=new Databases(this);
        String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
        localBackup.performBackup(databases, outFileName);
    }
}
