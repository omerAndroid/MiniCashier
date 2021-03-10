package com.aoa.mini_cashier;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dialog_view_addtypes extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.types_goods, null);

        EditText Text_q_type = (EditText)view.findViewById(R.id.q_type);
        Button save_tg_btn = (Button)view.findViewById(R.id.save_tg_add);

        //save Button on clicked
        save_tg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( String.valueOf(Text_q_type.getText()).isEmpty() ) {
                    Toast.makeText(getContext(), "aasassas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view)
                .setTitle("إضافة نوع")

                .setPositiveButton("حفظ", (dialogInterface, i) -> {

                });


        return builder.create();
    }

}
