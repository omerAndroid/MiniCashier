package com.aoa.mini_cashier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Settings extends AppCompatActivity {

    Button add_up_guantity , add_up_debart;
    private Dialog Date_Dialog;
    String dialog_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        add_up_guantity = findViewById(R.id.add_up_guantity);
        add_up_debart = findViewById(R.id.add_up_debart);

        add_up_debart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_name=add_up_debart.getText().toString();
                add_updaate_dailog();
            }
        });

        add_up_guantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_name=add_up_guantity.getText().toString();
                add_updaate_dailog();
            }
        });

    }

    public void add_updaate_dailog () {
        //Dialog Date viewer
        Date_Dialog = new Dialog(this);
        Date_Dialog.setContentView(R.layout.add_up_department);
        Date_Dialog.setTitle(dialog_name);

        //call all Buttons in dialog
        Button add_item = (Button) Date_Dialog.findViewById(R.id.add_depart_quantity);
        Button update_item = (Button) Date_Dialog.findViewById(R.id.update_depart_quantity);
        Button delete_item = (Button) Date_Dialog.findViewById(R.id.delete_depart_quantity);
        Button close_dialog = (Button) Date_Dialog.findViewById(R.id.close_depart_quantity);
        //

        //call EditText in dialog
        EditText text_edit= (EditText) Date_Dialog.findViewById(R.id.text_depart_quantity);
        //

        //call listview in dialog
        ListView list_item= (ListView) Date_Dialog.findViewById(R.id.list__depart_quantity);
        //
        ////////////////////////Buttons in click mode/////////////////////////
        add_item.setOnClickListener(v -> {
            Toast.makeText(this, "إضافة "+dialog_name, Toast.LENGTH_SHORT).show();
        });
        update_item.setOnClickListener(v -> {
            Toast.makeText(this, "تعديل "+dialog_name, Toast.LENGTH_SHORT).show();
        });
        delete_item.setOnClickListener(v -> {
            Toast.makeText(this, "حذف "+dialog_name, Toast.LENGTH_SHORT).show();
        });

        close_dialog.setOnClickListener(v -> {
            Date_Dialog.dismiss();
        });
        ////////////////////////////////////////////////////////////


        Date_Dialog.show();
    }
}
