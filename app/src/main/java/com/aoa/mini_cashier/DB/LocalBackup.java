package com.aoa.mini_cashier.DB;

import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.aoa.mini_cashier.MainActivity;
import com.aoa.mini_cashier.R;
import com.aoa.mini_cashier.Settings;

import java.io.File;

public class LocalBackup {

    private final Settings activity;

    public LocalBackup(Settings activity) {
        this.activity = activity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public  void performBackup(final Databases db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Backup Name");
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", (dialog, which) -> {
                String m_Text = input.getText().toString();
                String out = outFileName + m_Text + ".db";
                db.backup(out);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else
            Toast.makeText(activity, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
    }

    //ask to the user what backup to restore
    public void performRestore(final Databases db) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            db.importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }
}
