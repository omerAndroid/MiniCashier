package com.aoa.mini_cashier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoa.mini_cashier.DB.Databases;
import com.aoa.mini_cashier.item_classes.Settings_item;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;


public class Settings extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public Databases databases = new Databases(this);
    public static ArrayList<Settings_item> Settings_list = new ArrayList<>();
    Button add_up_guantity , add_up_debart,add_market_Phone;
    private Dialog Date_Dialog;
    String dialog_name="",name_new,name_old;
    String[] Settings;
    private static final int PICK_IMAGE_REQUEST=100;
    private Uri imageFilePath;
    private Bitmap imageTOstore;
    private ImageView objectImageView;
    GoogleSignInClient mGoogleSignInClient;
    EditText market_name,market_address,market_Email_password,quintity_max,debts_max;
    TextView text_gmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(v -> signIn());
        add_up_guantity = findViewById(R.id.add_up_guantity);
        add_up_debart = findViewById(R.id.add_up_debart);
        add_market_Phone = findViewById(R.id.market_phone);
        objectImageView = findViewById(R.id.market_photo);
        quintity_max = findViewById(R.id.quintity_max);
        debts_max = findViewById(R.id.debts_max);
        market_name= findViewById(R.id.market_name);
        market_address= findViewById(R.id.market_address);
        text_gmail= findViewById(R.id.text_gmail);

        market_Email_password= findViewById(R.id.market_Email_password);

        add_up_debart.setOnClickListener(v -> {
            dialog_name=add_up_debart.getText().toString();
            add_updaate_dailog();
            listShow_policy("أقسام الأصناف");
        });

        add_up_guantity.setOnClickListener(v -> {
            dialog_name=add_up_guantity.getText().toString();
            add_updaate_dailog();
            listShow_policy("كميات الأصناف");
        });

        add_market_Phone.setOnClickListener(v -> {
            dialog_name=add_market_Phone.getText().toString();
            add_updaate_dailog();
            listShow_policy("رقم المحل");
        });

        findViewById(R.id.save_Market_data).setOnClickListener(v -> {
            try {
                if (!market_name.getText().toString().isEmpty() &&! market_address.getText().toString().isEmpty()
                        &&!text_gmail.getText().toString().isEmpty() &&! market_Email_password.getText().toString().isEmpty()
                        && objectImageView.getDrawable()!=null && imageTOstore!=null){
                    market_Email_password.setError(null);
                    if (market_Email_password.getText().toString().length()<=9){

                        if ((databases.read_save_Market())>=1) {
                            databases.store_update_Market(market_name.getText().toString(),market_address.getText().toString(),text_gmail.getText().toString(),
                                    Integer.parseInt(market_Email_password.getText().toString()),imageTOstore);
                        }else {
                            databases.store_save_Market(market_name.getText().toString(),market_address.getText().toString(),text_gmail.getText().toString(),
                                    Integer.parseInt(market_Email_password.getText().toString()),imageTOstore);
                        }
                    }else {market_Email_password.setError("الرقم طويل جدا.");}
                }
                else {
                    Toast.makeText(getApplication(), "أكمل البيانات", Toast.LENGTH_SHORT).show();
                }
                //market_Email_password.setError(null);
            }catch (Exception e){
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if ((databases.read_save_Market())>=1) {
            Show_info();
        }

        sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
        String tecack_1=sharedPreferences.getString("key_1"," ");
        String tecack_2=sharedPreferences.getString("key_2"," ");
        quintity_max.setText(tecack_1);
        debts_max.setText(tecack_2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_1",quintity_max.getText().toString());
        editor.putString("key_2",debts_max.getText().toString());
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_1",quintity_max.getText().toString());
        editor.putString("key_2",debts_max.getText().toString());
        editor.apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
        String tecack_1=sharedPreferences.getString("key_1"," ");
        String tecack_2=sharedPreferences.getString("key_2"," ");
        quintity_max.setText(tecack_1);
        debts_max.setText(tecack_2);
    }

    private void Show_info() {
        String[] all_text=databases.get_ALL_store_save_Market();
        Bitmap[] all_Bitmap=databases.get_ALLq_store_Blob_Market();
        market_name.setText(all_text[0]);
        market_address.setText(all_text[1]);
        text_gmail.setText(all_text[2]);
        market_Email_password.setText(all_text[3]);
        objectImageView.setImageBitmap(all_Bitmap[0]);
    }

    int RC_SIGN_IN=10;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void listShow_policy(String who){

        int size=0;

        switch (who) {
            case "كميات الأصناف":
                Settings = databases.get_ALLq_qnuatity_type();
                size = databases.read_qnuatity_type();
                break;
            case "أقسام الأصناف":
                Settings = databases.get_ALL_department();
                size = databases.return_lenght_department();

                break;
            case "رقم المحل":
                Settings = databases.get_ALLq_store_number();
                size = databases.read_store_number();
                break;
        }

        Settings_list.clear();
        int i=0;
        for (int j=0;j<size;j++){

            Settings_list.add(new Settings_item(Settings[i]));
            i+=1;

        }
        //////////////////////////////Add List Item//////////////////////////////////////

        ListView list_item= (ListView) Date_Dialog.findViewById(R.id.list__depart_quantity);
        ListAdupter ad = new ListAdupter(Settings_list);
        list_item.setAdapter(ad);


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
        //EditText text_edit= (EditText) Date_Dialog.findViewById(R.id.text_depart_quantity);
        //

        //call listview in dialog
        //ListView list_item= (ListView) Date_Dialog.findViewById(R.id.list__depart_quantity);
        //
        ////////////////////////Buttons in click mode/////////////////////////

        add_item.setOnClickListener(v -> Add(dialog_name));

        update_item.setOnClickListener(v -> {
            EditText text_edit= (EditText) Date_Dialog.findViewById(R.id.text_depart_quantity);
            if (text_edit.getText().toString().length()>0) {
                get_dileg(text_edit.getText().toString());
            }
        });
        delete_item.setOnClickListener(v -> {
            Toast.makeText(this, "حذف "+dialog_name, Toast.LENGTH_SHORT).show();
        });

        close_dialog.setOnClickListener(v -> Date_Dialog.dismiss());
        ////////////////////////////////////////////////////////////


        Date_Dialog.show();
    }

    public void Add(String who){
        EditText text_edit= Date_Dialog.findViewById(R.id.text_depart_quantity);
        switch (who) {
            case "كميات الأصناف":
               if (text_edit.getText().toString().length()>0){
                   boolean b = databases.insert_new_quantity_type(text_edit.getText().toString());
                   if (b){
                       Toast.makeText(this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                       listShow_policy(who);
                   }else {

                       Toast.makeText(this, "موجود مسبقا", Toast.LENGTH_SHORT).show();
                   }
               }
                break;
            case "أقسام الأصناف":
                if (text_edit.getText().toString().length()>0){
                    boolean b = databases.insert_new_department(text_edit.getText().toString());
                    if (b){
                        Toast.makeText(this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                        listShow_policy(who);
                    }else {
                        Toast.makeText(this, "موجود مسبقا", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case "رقم المحل":
                if (text_edit.getText().toString().length()>0){
                    boolean b = databases.insert_new_store_number(text_edit.getText().toString());
                    if (b){
                        Toast.makeText(this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                        listShow_policy(who);
                    }else {
                        Toast.makeText(this, "موجود مسبقا", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void get_dileg(String s) {

        Dialog customer_data;

        //Dialog Customer Data viewer
        customer_data = new Dialog(this);
        customer_data.setContentView(R.layout.agent_dialog);
        customer_data.setTitle(dialog_name);
        final EditText add_extra_quantity_2 =customer_data.findViewById(R.id.add_extra_quantity_2);//1
        final EditText add_quantity_2 =customer_data.findViewById(R.id.add_quantity_2);//2
        final Button save_quantity_2 =customer_data.findViewById(R.id.save_quantity_2);

        add_quantity_2.setHint("الاسم الجديد");
        add_quantity_2.setInputType(1);
        add_extra_quantity_2.setHint("الاسم القديم");
        add_extra_quantity_2.setInputType(1);
        add_extra_quantity_2.setText(s);

        save_quantity_2.setOnClickListener(v -> {
            //save Data of customer
            EditText text_edit= (EditText) Date_Dialog.findViewById(R.id.text_depart_quantity);
            if (add_extra_quantity_2.getText().toString().length()<1){
                add_extra_quantity_2.setText("0");
            }
            name_old=add_extra_quantity_2.getText().toString();
            if (add_quantity_2.getText().toString().length()<1){
                add_quantity_2.setText("0");
            }
            name_new=add_quantity_2.getText().toString();
            customer_data.dismiss();
            boolean b;
            if (name_old.length()>0&&name_new.length()>0) {

                switch (dialog_name) {
                    case "كميات الأصناف":
                         b = databases.get_update_qnuatity_type(name_old,name_new);
                        if (b){
                            Toast.makeText(this, "تم التعديل", Toast.LENGTH_SHORT).show();
                            listShow_policy(dialog_name);
                        }else {
                            Toast.makeText(this, "غير موجود ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "أقسام الأصناف":
                         b = databases.get_update_department(name_old,name_new);
                        if (b){
                            Toast.makeText(this, "تم التعديل", Toast.LENGTH_SHORT).show();
                            listShow_policy(dialog_name);
                        }else {
                            Toast.makeText(this, "غير موجود ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "رقم المحل":
                        b = databases.get_update_store_number(name_old,name_new);
                        if (b){
                            Toast.makeText(this, "تم التعديل", Toast.LENGTH_SHORT).show();
                            listShow_policy(dialog_name);
                        }else {
                            Toast.makeText(this, "غير موجود ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                text_edit.setText("");
            }

        });
        customer_data.show();
    }

    public void chooseImage(View view) {

        try {
            Intent objectIntent=new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK&&data!=null&&data.getData()!=null){

                imageFilePath=data.getData();
                imageTOstore= MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
                objectImageView.setImageBitmap(imageTOstore);

            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("********************************************************");
            System.out.println( e.getMessage());
            System.out.println("********************************************************");
        }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            assert account != null;
            Toast.makeText(this, "Name : "+account.getDisplayName(), Toast.LENGTH_SHORT).show();
            TextView  text_gmail= findViewById(R.id.text_gmail);
            text_gmail.setText(account.getEmail());
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    public void signOut(View view) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        revokeAccess();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        TextView  text_gmail= findViewById(R.id.text_gmail);
                        text_gmail.setText("");
                    }
                });
    }

    class ListAdupter extends BaseAdapter {
        ArrayList<Settings_item> list_item;
        ListAdupter(ArrayList<Settings_item> list_item){
            this.list_item = list_item ;
        }

        @Override
        public int getCount() {
            return list_item.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list_item.get(position).name;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View view =inflater.inflate(R.layout.settings_item,null);

            TextView item_name = (TextView) view.findViewById(R.id.item_name);

            item_name.setText(String.valueOf(list_item.get(i).name));

            item_name.setOnClickListener((View.OnClickListener) v -> {
                EditText text_edit= (EditText) Date_Dialog.findViewById(R.id.text_depart_quantity);
                text_edit.setText(item_name.getText().toString());
            });
            return view;
        }
    }
}
