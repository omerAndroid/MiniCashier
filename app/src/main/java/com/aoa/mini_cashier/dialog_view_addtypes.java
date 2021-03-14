package com.aoa.mini_cashier;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.aoa.mini_cashier.DB.Databases;

import static com.aoa.mini_cashier.add_goods_db.Text_barcode;

public class dialog_view_addtypes extends AppCompatDialogFragment {

    @SuppressLint("StaticFieldLeak")
    public static EditText  Text_q_quantity, Text_q_buy_price, Text_q_sale_price,Text_q_type;
    Button  save;
    public  String old_q_type;


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
        Text_q_type = view.findViewById(R.id.q_type);
        Text_q_quantity = view.findViewById(R.id.q_quantity);
        Text_q_buy_price = view.findViewById(R.id.q_buy_price);
        Text_q_sale_price = view.findViewById(R.id.q_sale_price);

        //////////////////////////////////////////////////////////////////////////////

        //save Button on clicked
        save.setOnClickListener(v -> {

            if (check_impot_quantity()) {
                old_q_type=Text_q_type.getEditableText().toString();
                ////// m  يتم اضافة الى جدول الكمية

                if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
                    // جلب رقم البضاعة
                    int id = databases.get_id_goods(Text_barcode.getText().toString().trim());

                    if (save.getText().toString().equals("حفظ")){
                        boolean result2 = databases.insert_quantity(Text_q_type.getEditableText().toString(), Double.parseDouble(Text_q_buy_price.getEditableText().toString()),
                                Double.parseDouble(Text_q_quantity.getEditableText().toString()), id, Double.parseDouble(Text_q_sale_price.getEditableText().toString()));

                        if (result2) {
                            dismiss();
                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                        } else{
                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                        }
                    }
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
        if (TextUtils.isEmpty(Text_q_type.getEditableText().toString())||TextUtils.isEmpty(Text_q_quantity.getEditableText().toString())
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
