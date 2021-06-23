package com.aoa.mini_cashier;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.aoa.mini_cashier.DB.Databases;


import java.util.Objects;

import static com.aoa.mini_cashier.add_goods_db.Text_barcode;
import static com.aoa.mini_cashier.add_goods_db.Text_quantity;

public class dialog_view_addtypes_old extends AppCompatDialogFragment {
//
//    @SuppressLint("StaticFieldLeak")
//    public static EditText  Text_q_type, Text_q_buy_price, Text_q_sale_price,Text_q_type_2,
//            Text_q_quantity_2,Text_q_buy_price_2,Text_q_sale_price_2,Text_q_type_3,Text_q_quantity_3,
//            Text_q_buy_price_3,Text_q_sale_price_3,Text_q_type_4,Text_q_quantity_4,Text_q_buy_price_4,Text_q_sale_price_4;
//    public  String name_type,quantity,buy_price,sale_price;
//    RadioGroup radioGroup;
//    public  boolean check_work=false,check_insert_Data=false,isChecked_1=false,isChecked_2=false,isChecked_3=false,isChecked_4=false;
//    Button  save,del;
//    int id_quantity;
//    public  String old_q_type,add_q_type;
//
//    AlertDialog.Builder builder;
//    boolean check_impot;
//
//    RadioButton radioButton;
//
//    int sum_q;
//    SharedPreferences sharedPreferences;
//
//    int size_impout;
//
//


//
//    float quantity_stored=1;
//
//    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        try {
//
//        builder = new AlertDialog.Builder(getActivity());
//        Databases databases = new Databases(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.types_goods, null);
//        /////////////////////////////////////////////////////////////////quantity -> dialog
//        save = view.findViewById(R.id.save_tg_add);
//        del = view.findViewById(R.id.delete_tg_add);
//
//         //primary_type=view.findViewById(R.id.primary_type);
//            Text_q_type=view.findViewById(R.id.q_type);
//            Text_q_buy_price=view.findViewById(R.id.q_buy_price);
//            Text_q_sale_price=view.findViewById(R.id.q_sale_price);
//
//       //  primary_type_2=view.findViewById(R.id.primary_type_2);
//            Text_q_type_2=view.findViewById(R.id.q_type_2);
//            Text_q_quantity_2=view.findViewById(R.id.q_quantity_2);
//            Text_q_buy_price_2=view.findViewById(R.id.q_buy_price_2);
//            Text_q_sale_price_2=view.findViewById(R.id.q_sale_price_2);
//
//        // primary_type_3=view.findViewById(R.id.primary_type_3);
//            Text_q_type_3=view.findViewById(R.id.q_type_3);
//            Text_q_quantity_3=view.findViewById(R.id.q_quantity_3);
//            Text_q_buy_price_3=view.findViewById(R.id.q_buy_price_3);
//            Text_q_sale_price_3=view.findViewById(R.id.q_sale_price_3);
//
//       //  primary_type_4=view.findViewById(R.id.primary_type_4);
//            Text_q_type_4=view.findViewById(R.id.q_type_4);
//            Text_q_quantity_4=view.findViewById(R.id.q_quantity_4);
//            Text_q_buy_price_4=view.findViewById(R.id.q_buy_price_4);
//            Text_q_sale_price_4=view.findViewById(R.id.q_sale_price_4);
//
//        //////////////////////////n   عملية الضغط على الرايديو بوتن  ////////////////////////////////////////////////////
//            radioGroup=view.findViewById(R.id.radioGroup);
//            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//                radioButton=view.findViewById(checkedId);
//                switch (radioButton.getId()){
//                    case R.id.primary_type:{
//                        isChecked_1=true;
//                    }
//                    break;
//                    case R.id.primary_type_2:{
//                        isChecked_2=true;
//                    }
//                    break;
//                    case R.id.primary_type_3:{
//                        isChecked_3=true;
//                    }
//                    break;
//                    case R.id.primary_type_4:{
//                        isChecked_4=true;
//                    }
//                    break;
//                }
//            });
//
//        /////////////////////////////////n حفظ الكميات لاول مره فقط /////////////////////////////////////////////
//            sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
//
//                                String tecack=sharedPreferences.getString("key","");
//
//                                if (!tecack.equals("true")){
//                                    seve_q_first();
//                                }
//
//
//        /*databases.insert_quantity_type();
//
//        ArrayList<String> add_spinner = databases.Show_quantity_type();
//        add_q_type = add_spinner.get(0);
//        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,add_spinner);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Text_q_type.setAdapter(adapter);*/
/////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//        if(check_work)
//        {
//            del.setVisibility(View.VISIBLE);
//            add_q_type = name_type;
//           // Text_q_quantity.setText(quantity);
//            Text_q_buy_price.setText(buy_price);
//            Text_q_sale_price.setText(sale_price);
//            save.setText("تعديل");
//            id_quantity=databases.get_id_quantity(name_type,databases.get_id_goods(Text_barcode.getText().toString().trim()));
//        }
//        else {
//            save.setText("حفظ");
//            del.setVisibility(View.GONE);
//
//        }
//
//
//
//        ///////////////////////////////////////////////////////////////////////////////////////
//        /*Text_q_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                add_q_type= add_spinner.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });*/
//        //////////////////////////////////////////////////////////////////////////////
//
//        //save Button on clicked
//        save.setOnClickListener(v -> {
//
//            if (check_impot_quantity()) {
//
//                //old_q_type=add_q_type;
//
//                ////// m  يتم اضافة الى جدول الكمية
//                if (!Text_barcode.getText().toString().trim().isEmpty()&&databases.check_baracod(Text_barcode.getText().toString().trim())>0) {
//                    // جلب رقم البضاعة
//                    //int id = databases.get_id_goods(Text_barcode.getText().toString().trim());
//
//                    if (save.getText().toString().equals("حفظ")){
//
//                        if (!isChecked_1&&!isChecked_2&&!isChecked_3&&!isChecked_4){
//                            Toast.makeText(getActivity(), "حدد احد الخيارات التي بالاسفل", Toast.LENGTH_SHORT).show();
//                        }else {
//                            boolean result =insert_Data();
//                            if (result) {
//                                /////////m     يقوم بادخال الكمية المخزونة
//                                databases.insert_fast_to_quantity_stored_in_goods(quantity_stored,Text_barcode.getText().toString().trim());
//                                /////////m     يقوم لارسال القيمة الى العرض
//                                Text_quantity.setText(Float.toString(quantity_stored));
//
//                                dismiss();
//                                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
//                            } else{
//                                Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                    else
//                    {
////                        boolean result2 = databases.get_seve_ubdate_quantity(id_quantity,add_q_type, Double.parseDouble(Text_q_buy_price.getText().toString().trim()),
////                                Double.parseDouble(Text_q_quantity.getText().toString().trim()), id, Double.parseDouble(Text_q_sale_price.getText().toString().trim()));
//
//                        if (true) {
//                            dismiss();
//                            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
//                            ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
//                        } else{
//                            Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }//
//            }
//
//        });
//
//        del.setOnClickListener(v -> {
//            boolean result2 = databases.get_delete_quantity(id_quantity);
//            if (result2) {
//                dismiss();
//                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();//
//                ////Toast.makeText(getActivity(), String.valueOf(Double.parseDouble(Integer.)), Toast.LENGTH_SHORT).show();//
//            } else{
//                Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//
//
//            builder.setView(view)
//                    .setTitle("إضافة نوع");
//        /*
//                .setPositiveButton("حفظ", (dialogInterface, i) -> {
//
//                });*/
//
//
//        }catch (Exception e){
//            //////
//        }
//        return builder.create();
//    }
//
//
//    public boolean insert_Data(){
//        Databases databases = new Databases(getActivity());
//        int id_g = databases.get_id_goods(Text_barcode.getText().toString().trim());
//        int id_q,number_type=0;
//        check_insert_Data=false;
//        if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())){
//            number_type=1;
//            id_q=databases.get_id_quantity_type(Text_q_type.getEditableText().toString());
//
//            check_insert_Data= databases.insert_quantity(number_type,1,isChecked_1,Float.parseFloat(Text_q_buy_price.getText().toString()),
//                    Float.parseFloat(Text_q_sale_price.getText().toString()),id_q,id_g);
//
//            quantity_stored *=1;
//        }
//        if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
//            number_type=2;
//            id_q=databases.get_id_quantity_type(Text_q_type_2.getEditableText().toString());
//
//            check_insert_Data=databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_2.getText().toString()),isChecked_2,
//                    Float.parseFloat(Text_q_buy_price_2.getText().toString()), Float.parseFloat(Text_q_sale_price_2.getText().toString()),id_q,id_g);
//
//            quantity_stored *=Float.parseFloat(Text_q_quantity_2.getText().toString()+"f");
//        }
//        if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
//            number_type=3;
//            id_q=databases.get_id_quantity_type(Text_q_type_3.getEditableText().toString());
//            check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_3.getText().toString()),isChecked_3,
//                    Float.parseFloat(Text_q_buy_price_3.getText().toString()), Float.parseFloat(Text_q_sale_price_3.getText().toString()),id_q,id_g);
//
//            quantity_stored *=Float.parseFloat(Text_q_quantity_3.getText().toString()+"f");
//        }
//        if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
//            number_type=4;
//            id_q=databases.get_id_quantity_type(Text_q_type_4.getEditableText().toString());
//            check_insert_Data= databases.insert_quantity(number_type,Integer.parseInt(Text_q_quantity_4.getText().toString()),isChecked_4,
//                    Float.parseFloat(Text_q_buy_price_4.getText().toString()), Float.parseFloat(Text_q_sale_price_4.getText().toString()),id_q,id_g);
//
//            quantity_stored *=Float.parseFloat(Text_q_quantity_4.getText().toString()+"f");
//        }
//
//        return check_insert_Data;
//
//    }
//     ///////n       حفظ الكميات الرئيسية لاول مره فقط
//    private void seve_q_first() {
//        Databases databases = new Databases(getActivity());
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("key","true");
//        editor.apply();
//
//        Toast.makeText(getActivity(), "key true", Toast.LENGTH_SHORT).show();
//        databases.insert_new_quantity_type("كرتون");
//        databases.insert_new_quantity_type("درزن");
//        databases.insert_new_quantity_type("حبة");
//
//    }
//
//    private boolean check_impot_quantity() {
//        size_impout=0;
//        sum_q=0;
//        if (!TextUtils.isEmpty(Text_q_type.getEditableText().toString())) {
//
//            if (TextUtils.isEmpty(Text_q_buy_price.getEditableText().toString())||TextUtils.isEmpty(Text_q_sale_price.getEditableText().toString())){
//                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
//                check_impot = false;
//            }else {
//
//                sum_q+=1;
//                if (!checked_quantity_type(Text_q_type.getText().toString().trim())){
//                    check_impot = AlertDialog_show(Text_q_type.getEditableText().toString());
//                }else {check_impot = true;}
//            }
//        }
//        if (!TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())){
//            if (TextUtils.isEmpty(Text_q_quantity_2.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_2.getEditableText().toString())||
//                    TextUtils.isEmpty(Text_q_sale_price_2.getEditableText().toString())){
//
//                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
//                check_impot = false;
//            }else {
//
//                sum_q+=1;
//                if (!checked_quantity_type(Text_q_type_2.getText().toString().trim())){
//                    check_impot = AlertDialog_show(Text_q_type_2.getEditableText().toString());
//                }else {check_impot = true;}
//            }
//        }
//        if (!TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())){
//            if (TextUtils.isEmpty(Text_q_quantity_3.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_3.getEditableText().toString())||
//                    TextUtils.isEmpty(Text_q_sale_price_3.getEditableText().toString())){
//
//                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
//                check_impot = false;
//            }else {
//
//                sum_q+=1;
//                if (!checked_quantity_type(Text_q_type_3.getText().toString().trim())){
//                    check_impot = AlertDialog_show(Text_q_type_3.getEditableText().toString());
//                }else {check_impot = true;}
//            }
//        }
//        if (!TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())){
//            if (TextUtils.isEmpty(Text_q_quantity_4.getEditableText().toString())||TextUtils.isEmpty(Text_q_buy_price_4.getEditableText().toString())||
//                    TextUtils.isEmpty(Text_q_sale_price_4.getEditableText().toString())){
//
//                Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
//                check_impot = false;
//            }else {
//
//                sum_q+=1;
//                if (!checked_quantity_type(Text_q_type_4.getText().toString().trim())){
//                    check_impot = AlertDialog_show(Text_q_type_4.getEditableText().toString());
//                }else {check_impot = true;}
//            }
//        }
//
//        else if(TextUtils.isEmpty(Text_q_type.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_2.getEditableText().toString())&&
//                TextUtils.isEmpty(Text_q_type_3.getEditableText().toString())&&TextUtils.isEmpty(Text_q_type_4.getEditableText().toString())) {
//            Toast.makeText(getActivity(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
//            check_impot = false;
//        }
//
//        return check_impot;
//    }
//
//    ////////////////////n       اشعار تنبية اذا لم يكن نوع الكمية مخزون من قبل ويريد اضافتة
//    boolean b=false;
//    public boolean AlertDialog_show(String new_type){
//        b=false;
//        Databases databases = new Databases(getActivity());
//        AlertDialog.Builder bu = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//        bu.setMessage("هل تريد حفظ اسم الكمية الجديدة"+new_type)
//                .setTitle(R.string.eree)
//                .setPositiveButton("نعم", (dialog, id) -> {
//                    databases.insert_new_quantity_type(new_type);
//                    b=true;
//                })
//                .setNegativeButton("لا", (dialog, id) ->{
//                    b=false;
//                }).show();
//        return b;
//    }
//    //////////////////n         عملية التاكد بان اسم الكمية مسجل من قبل
//    public boolean checked_quantity_type(String s){
//        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//        int number;
//        boolean cheack;
//
//        Databases databases = new Databases(getActivity());
//
//        number=databases.get_check_quantity_type(s);
//
//        cheack= number > 0;
//
//        return cheack;
//    }
//

}
