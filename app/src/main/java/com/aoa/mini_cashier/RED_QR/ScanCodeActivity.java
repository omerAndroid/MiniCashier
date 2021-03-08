package com.aoa.mini_cashier.RED_QR;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.aoa.mini_cashier.add_goods_db;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        //setContentView(scannerView);
        /**
         *  Ask User to Open a Camera
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

            } else {
                /**
                 *  this for version > 5
                 */
                setContentView(scannerView);

            }

        } else {

            /**
             *  this for version < 5
             */
            setContentView(scannerView);


        }
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setTitle("Back");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void handleResult(Result result) {


        add_goods_db.Text_barcode.setText(result.getText());
        //vewes.button.setVisibility(View.VISIBLE);
        onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();

        scannerView.stopCamera();

    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}