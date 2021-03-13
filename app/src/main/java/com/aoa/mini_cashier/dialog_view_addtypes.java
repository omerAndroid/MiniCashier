package com.aoa.mini_cashier;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.aoa.mini_cashier.DB.Databases;

import static android.content.Context.IPSEC_SERVICE;
import static com.aoa.mini_cashier.R.drawable.ic_baseline_delete_24;
import static com.aoa.mini_cashier.add_goods_db.Text_barcode;

public class dialog_view_addtypes extends AppCompatDialogFragment {

    public static EditText  Text_q_quantity, Text_q_buy_price, Text_q_sale_price;
    Button exeit, save;
    public  String old_q_type;
    public Databases databases = new Databases(getActivity());
    public AutoCompleteTextView AutoCompleteTextView ;

    public AutoCompleteTextView Text_q_type ;

    boolean check_impot;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Databases databases = new Databases(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.types_goods, null);
        /////////////////////////////////////////////////////////////////quantity -> dialog
        exeit = view.findViewById(R.id.cancel_tg_dailog);
        save = view.findViewById(R.id.save_tg_add);
        Text_q_type = view.findViewById(R.id.q_type);
        Text_q_quantity = view.findViewById(R.id.q_quantity);
        Text_q_buy_price = view.findViewById(R.id.q_buy_price);
        Text_q_sale_price = view.findViewById(R.id.q_sale_price);

        AutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.q_type);
        //////////////////////////////////////////////////////////////////////////////


        if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
            int id = databases.get_id_goods(Text_barcode.getText().toString().trim());
           /// get_ALL_q_type(id);
        }


        //save Button on clicked
        save.setOnClickListener(v -> {

            if (check_impot_quantity()) {
                old_q_type=Text_q_type.getEditableText().toString();
                ////// m  يتم اضافة الى جدول الكمية

                if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
                    // جلب رقم البضاعة
                    int id = databases.get_id_goods(Text_barcode.getText().toString().trim());//  public static void
                    Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
                    if (save.getText().toString().equals("حفظ")){
                        boolean result2 = databases.insert_quantity(Text_q_type.getEditableText().toString(), Double.parseDouble(Text_q_buy_price.getEditableText().toString()),
                                Double.parseDouble(Text_q_quantity.getEditableText().toString()), id, Double.parseDouble(Text_q_sale_price.getEditableText().toString()));

                        if (result2) {
                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
                            EditText_quantity_gone();
                            ///get_ALL_q_type();
                        } else{
                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (save.getText().toString().equals("تعديل")){
                        EditText_quantity_visible();
                    }
                    else if (save.getText().toString().equals("حفظ التعديل")){

                        if (check_impot_quantity()){

                            boolean result2 = databases.get_seve_ubdate_quantity(Text_q_type.getEditableText().toString(), Double.parseDouble(Text_q_buy_price.getEditableText().toString()),
                                    Double.parseDouble(Text_q_quantity.getEditableText().toString()), id, Double.parseDouble(Text_q_sale_price.getEditableText().toString()),old_q_type);

                            if (result2) {
                                Toast.makeText(getActivity(), "OK ubdat quantity", Toast.LENGTH_SHORT).show();
                                EditText_quantity_gone();
                                clear_inpot();
                               // get_ALL_q_type();
                            } else{
                                Toast.makeText(getActivity(), "No ubdat quantity", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

        });

        exeit.setOnClickListener(v -> {
            if (!exeit.getText().toString().equals("إغلاق")){
                exeit.setText("إغلاق");
                EditText_quantity_visible();
                save.setText("حفظ");
                exeit.setBackgroundResource(0);

                Text_q_type.setText("");
                Text_q_quantity.setText("");
                Text_q_buy_price.setText("");
                Text_q_sale_price.setText("");
            }
        });

        builder.setView(view)
                .setTitle("إضافة نوع");
        /*
                .setPositiveButton("حفظ", (dialogInterface, i) -> {

                });*/


        return builder.create();
    }


    public void EditText_quantity_gone(){
        Text_q_type.setEnabled(false);
        Text_q_quantity.setEnabled(false);
        Text_q_buy_price.setEnabled(false);
        Text_q_sale_price.setEnabled(false);
        save.setText("تعديل");
    }

    public void EditText_quantity_visible(){
        Text_q_type.setEnabled(true);
        Text_q_quantity.setEnabled(true);
        Text_q_buy_price.setEnabled(true);
        Text_q_sale_price.setEnabled(true);
        save.setText("حفظ التعديل");
    }

    public void clear_inpot(){
        exeit.setText(null);
        exeit.setBackgroundResource(ic_baseline_delete_24);
    }

    private boolean check_impot_quantity() {
        if (TextUtils.isEmpty(Text_q_type.getEditableText().toString())||TextUtils.isEmpty(Text_q_quantity.getEditableText().toString())
                ||TextUtils.isEmpty(Text_q_buy_price.getEditableText().toString())||TextUtils.isEmpty(Text_q_sale_price.getEditableText().toString())) {

            //mEmail.setError("Email is Required.");

            Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
            // return;
            check_impot = false;
        } else {
            check_impot = true;
        }
        return check_impot;
    }


   public void get_ALL_q_type(int idd){
       String[] Allq_type=databases.get_ALLq_type(idd);
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
               android.R.layout.simple_list_item_1, Allq_type);

       AutoCompleteTextView.setAdapter(adapter);

       AutoCompleteTextView.setOnItemClickListener((parent, arg1, pos, id) -> {
           String item = parent.getItemAtPosition(pos).toString();
           Toast.makeText(getActivity(),"Selected Item is: \t " + item, Toast.LENGTH_LONG).show();

           //////b تعبئة المدخلات بعد اختيار الباركود الموجود من قاعدة البيانات
           //Packing_for_goods(item);

       });
    }

}
