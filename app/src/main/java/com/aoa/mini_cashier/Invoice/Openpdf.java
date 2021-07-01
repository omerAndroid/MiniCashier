package com.aoa.mini_cashier.Invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.aoa.mini_cashier.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Openpdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openpdf);

        Intent data =getIntent();
        String PATH = data.getExtras().getString("key");

        File file=new File(PATH);
        PDFView pdfView;
        pdfView= findViewById(R.id.pdfView);
        pdfView.fromFile(file).load();
    }
}