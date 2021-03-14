package com.aoa.mini_cashier.RED_QR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.add_goods_db;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    public Databases databases = new Databases(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        //setContentView(scannerView);
        /*
         *  Ask User to Open a Camera
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

            } else {
                /*
                 *  this for version > 5
                 */
                setContentView(scannerView);

            }

        } else {

            /*
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
        if (databases.check_baracod(result.getText())==0){
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            add_goods_db.Text_name_goods.setText("");
            add_goods_db.Text_quantity.setText("");
            add_goods_db.Text_quantity_box.setText("");
            add_goods_db.Text_date_ex.setText("");
            add_goods_db.Text_date_sale.setText("");

            de_Modification();
        }else {
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            Packing_for_goods(result.getText());
        }





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


    public void de_Modification(){
        add_goods_db.clear_all.setVisibility(View.GONE);
        add_goods_db.save_add_goods.setVisibility(View.VISIBLE);///visible      ظاهر
        add_goods_db.ubdate_btn.setVisibility(View.GONE);///visible      ظاهر
        add_goods_db.seve_ubdat_goods_btn.setVisibility(View.GONE);///visible      ظاه
        add_goods_db. Text_barcode.setEnabled(true);
        add_goods_db.Text_name_goods.setEnabled(true);
        add_goods_db.Text_quantity.setEnabled(true);
        add_goods_db.date_sale_btn.setEnabled(true);
        add_goods_db.date_ex_btn.setEnabled(true);
        add_goods_db.Text_quantity_box.setEnabled(true);

    }


    public void Modification() {
        add_goods_db.clear_all.setVisibility(View.VISIBLE);
        add_goods_db.save_add_goods.setVisibility(View.GONE);///visible      ظاهر
        add_goods_db.ubdate_btn.setVisibility(View.VISIBLE);///visible      ظاهر
        add_goods_db.seve_ubdat_goods_btn.setVisibility(View.GONE);///visible      ظاهر
        add_goods_db.Text_barcode.setEnabled(false);
        add_goods_db.Text_name_goods.setEnabled(false);
        add_goods_db.Text_quantity.setEnabled(false);
        add_goods_db.date_sale_btn.setEnabled(false);
        add_goods_db.date_ex_btn.setEnabled(false);
        add_goods_db.Text_quantity_box.setEnabled(false);
    }

    private void Packing_for_goods(String item) {
        Modification();

        String[] All_goods=databases.get_All_goods_for_barcod(item);
        add_goods_db.Text_name_goods.setText(All_goods[1]);
        add_goods_db.Text_quantity.setText(All_goods[2]);
        add_goods_db.Text_quantity_box.setText(All_goods[0]);
        add_goods_db.Text_date_ex.setText(All_goods[3]);
        add_goods_db.Text_date_sale.setText(All_goods[4]);

    }
}