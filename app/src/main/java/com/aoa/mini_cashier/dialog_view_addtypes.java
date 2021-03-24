package com.aoa.mini_cashier;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.aoa.mini_cashier.DB.Databases;

import java.util.ArrayList;

import static com.aoa.mini_cashier.add_goods_db.Text_barcode;

public class dialog_view_addtypes extends AppCompatDialogFragment {

    @SuppressLint("StaticFieldLeak")
    public static EditText  Text_q_quantity, Text_q_buy_price, Text_q_sale_price,Text_q_type;
    public  String name_type,quantity,buy_price,sale_price;
    public  boolean check_work=false;
    Button  save,del;
    int id_quantity;
    public  String old_q_type,add_q_type;


    boolean check_impot;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Databases databases = new Databases(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.types_goods, null);
        /////////////////////////////////////////////////////////////////quantity -> dialog
        save = view.findViewById(R.id.save_tg_add);
        del = view.findViewById(R.id.delete_tg_add);

        Text_q_type = view.findViewById(R.id.q_type);
        //Text_q_quantity = view.findViewById(R.id.q_quantity);
        Text_q_buy_price = view.findViewById(R.id.q_buy_price);
        Text_q_sale_price = view.findViewById(R.id.q_sale_price);
        //////////////////////////////////////////////////////////////////////////////


        /*databases.insert_quantity_type();

        ArrayList<String> add_spinner = databases.Show_quantity_type();
        add_q_type = add_spinner.get(0);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,add_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Text_q_type.setAdapter(adapter);*/
///////////////////////////////////////////////////////////////////////////////////////////////

        if(check_work)
        {
            del.setVisibility(View.VISIBLE);
            add_q_type = name_type;
            Text_q_quantity.setText(quantity);
            Text_q_buy_price.setText(buy_price);
            Text_q_sale_price.setText(sale_price);
            save.setText("تعديل");
            id_quantity=databases.get_id_quantity(name_type,databases.get_id_goods(Text_barcode.getText().toString().trim()));
        }
        else {
            save.setText("حفظ");
            del.setVisibility(View.GONE);

        }

        ///////////////////////////////////////////////////////////////////////////////////////
        /*Text_q_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add_q_type= add_spinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        //////////////////////////////////////////////////////////////////////////////

        //save Button on clicked
        save.setOnClickListener(v -> {

            if (check_impot_quantity()) {

                old_q_type=add_q_type;
                ////// m  يتم اضافة الى جدول الكمية

                if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
                    // جلب رقم البضاعة
                    int id = databases.get_id_goods(Text_barcode.getText().toString().trim());

                    if (save.getText().toString().equals("حفظ")){//Double.parseDouble(Text_q_quantity.getText().toString().trim())
                        boolean result2 = databases.insert_quantity(add_q_type, Double.parseDouble(Text_q_buy_price.getText().toString().trim()),
                                Double.parseDouble(Text_q_quantity.getText().toString().trim()), id, Double.parseDouble(Text_q_sale_price.getText().toString().trim()));

                        if (result2) {
                            dismiss();
                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                            ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
                        } else{
                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        boolean result2 = databases.get_seve_ubdate_quantity(id_quantity,add_q_type, Double.parseDouble(Text_q_buy_price.getText().toString().trim()),
                                Double.parseDouble(Text_q_quantity.getText().toString().trim()), id, Double.parseDouble(Text_q_sale_price.getText().toString().trim()));

                        if (result2) {
                            dismiss();
                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                            ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
                        } else{
                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result2 = databases.get_delete_quantity(id_quantity);
                if (result2) {
                    dismiss();
                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                    ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
                } else{
                    Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setView(view)
                .setTitle("إضافة نوع");
        /*
                .setPositiveButton("حفظ", (dialogInterface, i) -> {

                });*/


        return builder.create();
    }

    private boolean check_impot_quantity() {
        if (TextUtils.isEmpty(add_q_type)||TextUtils.isEmpty(Text_q_quantity.getEditableText().toString())
                ||TextUtils.isEmpty(Text_q_buy_price.getEditableText().toString())||TextUtils.isEmpty(Text_q_sale_price.getEditableText().toString())) {

            Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
            // return;
            check_impot = false;
        } else {
            check_impot = true;
        }
        return check_impot;
    }
}
