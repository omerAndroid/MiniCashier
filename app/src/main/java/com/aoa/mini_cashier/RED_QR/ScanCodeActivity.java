package com.aoa.mini_cashier.RED_QR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.add_goods_db;
import com.aoa.mini_cashier.update_goods_db;
import com.google.zxing.Result;


import java.text.MessageFormat;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    public Databases databases = new Databases(this);


   public String hasOnClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);

        Intent data =getIntent();
        hasOnClick = data.getExtras().getString("page");

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
             *\  this for version < 5
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


        Toast.makeText(this, hasOnClick, Toast.LENGTH_SHORT).show();
        /////////v     لتحقق من الذي قام بالاستدعاء
        if (hasOnClick.equals("update_goods_db")){

            Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
            update_goods_db.ALL_baracode_name_g.setText(result.getText());

            if (databases.check_baracod(result.getText()) > 0) {
                Intent intent=getIntent();
                intent.putExtra("key","true");
                intent.putExtra("valu",result.getText());
                setResult(RESULT_OK,intent);
            }

        }else if (hasOnClick.equals("add_goods_db")){

            add_goods_db.Text_barcode.setText(result.getText());

            if (databases.check_baracod(result.getText()) == 0) {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                add_goods_db.Text_name_goods.setText("");
                add_goods_db.Text_quantity.setText("");
                add_goods_db.Text_date_ex.setText("");
                add_goods_db.Text_date_sale.setText("");
                add_goods_db.spinner.setText("");
                add_goods_db.spinner.setText("الاقسام");

                de_Modification();
            } else {
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                Packing_for_goods(result.getText());


                Intent intent=getIntent();
                intent.putExtra("page","true");
                setResult(RESULT_OK,intent);

            }

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
        add_goods_db.Text_extra_quantity.setEnabled(true);
        add_goods_db.spinner.setEnabled(true);
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
        add_goods_db.Text_extra_quantity.setEnabled(false);
        add_goods_db.spinner.setEnabled(false);
    }

    private void Packing_for_goods(String item) {
        Modification();

        String[] All_goods=databases.get_All_goods_for_barcod(item);
        double[] All_goods_double=databases.get_All_goods_for_barcod_Double(item);
        add_goods_db.Text_name_goods.setText(All_goods[0]);
        add_goods_db.Text_quantity.setText(theack_aggen(MessageFormat.format("{0}", All_goods_double[0])));
        add_goods_db.Text_date_ex.setText(All_goods[1]);
        add_goods_db.Text_date_sale.setText(All_goods[2]);
        add_goods_db.spinner.setText(All_goods[3]);

        add_goods_db.department_item=All_goods[3];
    }
    /////////////////n     خوارزمية تساعد لعملية عرض وادخال الارقام
    public String theack_aggen(String s){
        StringBuilder ss= new StringBuilder();
        for (int i = 0; i<= s.length()-1; i++){
            if (String.valueOf(s.charAt(i)).equals("٠")){
                ss.append("0");
            }else if(String.valueOf(s.charAt(i)).equals("٩")){
                ss.append("9");
            }else if(String.valueOf(s.charAt(i)).equals("١")){
                ss.append("1");
            }else if(String.valueOf(s.charAt(i)).equals("٢")){
                ss.append("2");
            }else if(String.valueOf(s.charAt(i)).equals("٣")){
                ss.append("3");
            }else if(String.valueOf(s.charAt(i)).equals("٤")){
                ss.append("4");
            }else if(String.valueOf(s.charAt(i)).equals("٥")){
                ss.append("5");
            }else if(String.valueOf(s.charAt(i)).equals("٦")){///١٢٣٤٥٦٧٨٩٫٠٠٠
                ss.append("6");
            }else if(String.valueOf(s.charAt(i)).equals("٧")){
                ss.append("7");
            }else if(String.valueOf(s.charAt(i)).equals("٨")){
                ss.append("8");
            }else if(String.valueOf(s.charAt(i)).equals("٫")){
                ss.append(".");
            }
        }

        return ss.toString();
    }
}